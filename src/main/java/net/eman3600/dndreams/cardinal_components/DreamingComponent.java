package net.eman3600.dndreams.cardinal_components;

import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.cardinal_components.interfaces.DreamingComponentI;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DreamingComponent implements DreamingComponentI {
    private PlayerEntity player;

    private boolean dreaming = false;
    private Vec3d returnPos;
    private PlayerInventory storedInv;

    public DreamingComponent(PlayerEntity player) {
        this.player = player;
        returnPos = player.getPos();
        storedInv = new PlayerInventory(player);
    }

    private TormentComponent torment() {
        return EntityComponents.TORMENT.get(player);
    }

    @Override
    public void changeDimension(boolean toDream) {
        dreaming = toDream;
        PlayerInventory currInv = new PlayerInventory(player);
        transferInventories(currInv, player.getInventory());
        transferInventories(player.getInventory(), storedInv);
        transferInventories(storedInv, currInv);
        currInv.clear();

        if (toDream) {
            returnPos = player.getPos();
            player.setHealth((float)player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 4), player);
            torment().setTorment(0);
        } else {
            player.setPosition(returnPos);
            player.setHealth((float)player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 4), player);
            torment().setTorment(0);
        }
    }

    @Override
    public boolean isDreaming() {
        return dreaming;
    }

    @Override
    public PlayerInventory storedInv() {
        return storedInv;
    }

    @Override
    public Vec3d returnPos() {
        return returnPos;
    }

    @Override
    public void serverTick() {
        if (dreaming && getDimension() != ModDimensions.DREAM_TYPE_KEY) {
            changeDimension(false);
        } else if (!dreaming && getDimension() == ModDimensions.DREAM_TYPE_KEY) {
            changeDimension(true);
        } else if (dreaming) {
            torment().addPerMinute(10f);
        }

        EntityComponents.DREAMING.sync(player);
    }

    private RegistryKey<DimensionType> getDimension() {
        return player.getWorld().getDimensionKey();
    }

    private void transferInventories(PlayerInventory into, PlayerInventory outOf) {
        into.clear();
        for (int i = 0; i < outOf.size(); i++) {
            into.setStack(i, outOf.getStack(i));
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtList inventoryList = tag.getList("stored_inv", 10);
        NbtList posList = tag.getList("return_pos", 6);


        storedInv.readNbt(inventoryList);
        returnPos = new Vec3d(posList.getDouble(0),posList.getDouble(1),posList.getDouble(2));
        dreaming = tag.getBoolean("dreaming");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.put("stored_inv", storedInv.writeNbt(new NbtList()));
        tag.put("return_pos", this.toNbtList(returnPos.getX(),returnPos.getY(),returnPos.getZ()));
        tag.putBoolean("dreaming", dreaming);
    }

    private NbtList toNbtList(double... values) {
        NbtList nbtList = new NbtList();
        double[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            double d = var3[var5];
            nbtList.add(NbtDouble.of(d));
        }

        return nbtList;
    }
}
