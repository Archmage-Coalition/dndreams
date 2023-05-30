package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.CosmicFountainBlock;
import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.cardinal_components.ReviveComponent;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static net.eman3600.dndreams.blocks.energy.BonfireBlock.LIT;
import static net.eman3600.dndreams.blocks.energy.BonfireBlock.STRONG;

public class BonfireBlockEntity extends BlockEntity implements AbstractPowerReceiver {
    public static int THRESHOLD = 250;
    private int power = 0;
    private List<PlayerEntity> linkedPlayers = new ArrayList<>();

    public static double GIVE_RANGE = 6.5d;

    private static final Box eRange = new Box(-GIVE_RANGE,-GIVE_RANGE,-GIVE_RANGE,GIVE_RANGE,GIVE_RANGE,GIVE_RANGE);


    public BonfireBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BONFIRE_ENTITY, pos, state);
    }






    public static void tick(World world, BlockPos blockPos, BlockState blockState, BonfireBlockEntity entity) {
        try {
            ServerWorld server = (ServerWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    private void tick(ServerWorld world) {
        boolean strong = getCachedState().get(STRONG);

        if (!getCachedState().get(LIT) && power > 0) {
            setPower(0);
        }
        if ((!strong && canAfford(THRESHOLD)) || (strong && power <= 0)) {
            world.setBlockState(pos, getCachedState().with(STRONG, !strong), Block.NOTIFY_ALL);
            strong = !strong;
            markDirty();
        }

        if (world.getTime() % 5 == 0) {
            linkedPlayers.clear();
            for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, boxRange())) {
                InfusionComponent infusion = EntityComponents.INFUSION.get(player);
                if (!(player instanceof ServerPlayerEntity serverPlayer) || !(serverPlayer.getSpawnPointDimension() == world.getRegistryKey() && pos.equals(serverPlayer.getSpawnPointPosition()) && infusion.linkedToBonfire())) continue;

                linkedPlayers.add(player);
                infusion.setLinkTicks(InfusionComponent.LINK_LENGTH);

                ManaComponent mana = EntityComponents.MANA.get(player);
                ReviveComponent revive = EntityComponents.REVIVE.get(player);

                if (strong && (player.getHealth() < player.getMaxHealth() || mana.getMana() < mana.getManaMax() || player.getHungerManager().isNotFull() || player.getHungerManager().getSaturationLevel() < player.getHungerManager().getFoodLevel() || revive.needsMoreVitality()) && usePower(30)) {
                    revive.addVitality(5f);
                    if (!player.hasStatusEffect(ModStatusEffects.VOID_FLOW)) mana.chargeMana(10);
                    player.heal(4f);
                    player.getHungerManager().add(1, 1f);

                    ((CosmicFountainBlock) ModBlocks.COSMIC_FOUNTAIN).displayEnchantParticle(world, pos, player, ModParticles.COSMIC_ENERGY, 3);
                }
            }
        }

        if (linkedPlayers.isEmpty() && !usePower(25)) setPower(0);
    }



    @Environment(EnvType.CLIENT)
    public static void tickClient(World world, BlockPos blockPos, BlockState blockState, BonfireBlockEntity entity) {
        try {
            ClientWorld client = (ClientWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tickClient(client);
        } catch (ClassCastException ignored) {}
    }

    @Environment(EnvType.CLIENT)
    private void tickClient(ClientWorld world) {
        int i;
        Random random = world.random;
        if (random.nextFloat() < (getCachedState().get(STRONG) ? 0.11f: 0.06f)) {
            for (i = 0; i < random.nextInt(2) + 2; ++i) {
                CampfireBlock.spawnSmokeParticle(world, pos, true, false);
            }
        }
    }




    public Box boxRange() {
        return eRange.offset(Vec3d.ofCenter(pos));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("power", power);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        power = nbt.getInt("power");
    }

    @Override
    public boolean addPower(int amount) {
        if (power < getMaxPower()) {
            setPower(power + amount);
            return true;
        }
        return false;
    }

    @Override
    public void setPower(int amount) {
        power = MathHelper.clamp(amount, 0, getMaxPower());

        markDirty();
        world.updateNeighborsAlways(pos, getCachedState().getBlock());
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getMaxPower() {
        return THRESHOLD;
    }

    @Override
    public boolean usePower(int amount) {
        if (canAfford(amount)) {
            setPower(power - amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean needsPower() {
        return power < getMaxPower() && getCachedState().get(LIT) && !linkedPlayers.isEmpty();
    }

    @Override
    public int powerRequest() {
        return 10;
    }
}
