package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.interfaces.GatewayComponentI;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.Optional;

public class GatewayComponent implements GatewayComponentI {
    private PlayerEntity player;
    private boolean challenge = false;
    private int event = -1;
    private Vec3d returnPos;

    private boolean foughtPhantomLord = false;

    public GatewayComponent(PlayerEntity player) {
        this.player = player;
        returnPos = player.getPos();
    }

    @Override
    public int getEvent() {
        return event;
    }

    @Override
    public Vec3d getReturnPos() {
        return returnPos;
    }

    @Override
    public boolean isChallenge() {
        return challenge;
    }

    @Override
    public ServerWorld getExitDimension(boolean success) {
        DimensionPair dims = null;

        if (challenge) {
            dims = new DimensionPair(World.END, World.END);
        } else if (event == 0) {
            dims = new DimensionPair(World.OVERWORLD, World.NETHER);
        } else if (event == 1) {
            dims = new DimensionPair(ModDimensions.DREAM_DIMENSION_KEY, World.OVERWORLD);
        }

        try {
            return ((ServerPlayerEntity)player).server.getWorld(dims.getDim(success));
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

    public ServerWorld getGatewayDimension() {
        try {
            return ((ServerPlayerEntity)player).server.getWorld(ModDimensions.GATEWAY_DIMENSION_KEY);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

    public Vec3d getGatewayPosition() {
        if (event == 0) {
            return new Vec3d(4.5, 1, 5.5);
        }

        return new Vec3d(0, 0, 0);
    }

    public BlockPos getStructureStart() {
        if (event == 0) {
            return new BlockPos(1, -12, -7);
        }

        return new BlockPos(0, 0, 0);
    }

    public Identifier getStructure() {
        if (event == 0) {
            return new Identifier(Initializer.MODID, "gateway/nether_start");
        }

        return null;
    }

    public void enterGateway(int event, boolean challenge) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        this.event = event;
        this.challenge = challenge;

        returnPos = player.getPos();

        EntityComponents.GATEWAY.sync(player);

        try {
            FabricDimensions.teleport(player, getGatewayDimension(), new TeleportTarget(getGatewayPosition(), Vec3d.ZERO, -90, 0));
        } catch (NullPointerException e) {
            return;
        }

        // Structure Generation
        for (ServerPlayerEntity otherPlayer : getGatewayDimension().getPlayers()) {
            if (player == otherPlayer) {
                continue;
            }

            if (EntityComponents.GATEWAY.get(otherPlayer).getEvent() == event) {
                return;
            }
        }

        loadStructure(getGatewayDimension(), getStructureStart(), getStructure());
    }

    private boolean loadStructure(ServerWorld world, BlockPos pos, Identifier templateName) {
        StructureTemplateManager structureTemplateManager = world.getStructureTemplateManager();

        Optional optional;
        try {
            optional = structureTemplateManager.getTemplate(templateName);
        } catch (InvalidIdentifierException var6) {
            return false;
        }

        return optional.isPresent() && this.placeStructure(world, (StructureTemplate) optional.get(), pos);
    }

    private boolean placeStructure(ServerWorld world, StructureTemplate template, BlockPos blockPos) {
        StructurePlacementData structurePlacementData = (new StructurePlacementData()).setMirror(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(true);

        template.place(world, blockPos, blockPos, structurePlacementData, StructureBlockBlockEntity.createRandom(0), 2);
        return true;
    }


    @Override
    public boolean hasFoughtPhantomLord() {
        EntityComponents.GATEWAY.sync(player);

        try {
            return foughtPhantomLord || WorldComponents.BOSS_STATE.get(((ServerWorld)player.getWorld()).getScoreboard()).dragonSlain();
        } catch (ClassCastException | NullPointerException e) {
            return foughtPhantomLord;
        }
    }

    public void exitGateway(boolean success) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        if (!success) {
            FabricDimensions.teleport(player, getExitDimension(false), new TeleportTarget(returnPos, Vec3d.ZERO, player.getYaw(), player.getPitch()));
        } else {
            FabricDimensions.teleport(player, getExitDimension(true), new TeleportTarget(returnPos, Vec3d.ZERO, player.getYaw(), player.getPitch()));
        }
    }


    @Override
    public void serverTick() {

    }

    @Override
    public void setFoughtPhantomLord(boolean foughtPhantomLord) {
        this.foughtPhantomLord = foughtPhantomLord;

        EntityComponents.GATEWAY.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtList posList = tag.getList("return_pos", 6);

        challenge = tag.getBoolean("challenge");
        event = tag.getInt("event");
        returnPos = new Vec3d(posList.getDouble(0),posList.getDouble(1),posList.getDouble(2));
        foughtPhantomLord = tag.getBoolean("fought_phantom_lord");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("challenge", challenge);
        tag.putInt("event", event);
        tag.put("return_pos", toNbtList(returnPos.getX(),returnPos.getY(),returnPos.getZ()));
        tag.putBoolean("fought_phantom_lord", foughtPhantomLord);
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




        private class DimensionPair {
            private RegistryKey<World> dimEnter;
            private RegistryKey<World> dimExit;

            private DimensionPair(RegistryKey<World> dimEnter, RegistryKey<World> dimExit) {
                this.dimEnter = dimEnter;
                this.dimExit = dimExit;
            }

            private RegistryKey<World> getDim(boolean exit) {
                return exit ? dimExit : dimEnter;
            }
        }
}
