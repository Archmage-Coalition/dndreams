package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.DeepslateCoreBlock;
import net.eman3600.dndreams.blocks.portal.GenericPortalAreaHelper;
import net.eman3600.dndreams.blocks.portal.GenericPortalBlock;
import net.eman3600.dndreams.initializers.ModBlockEntities;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DeepslateCoreBlockEntity extends BlockEntity {
    private int width = 3;
    private int height = 3;
    private int ticks = 0;
    private boolean forming = false;


    public DeepslateCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DEEPSLATE_CORE_ENTITY, pos, state);
    }

    public boolean isForming() {
        return forming;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("width", width);
        nbt.putInt("height", height);
        nbt.putInt("ticks", ticks);
        nbt.putBoolean("forming", forming);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        width = nbt.getInt("width");
        height = nbt.getInt("height");
        ticks = nbt.getInt("ticks");
        forming = nbt.getBoolean("forming");
    }

    public void init(GenericPortalAreaHelper areaHelper) {
        width = areaHelper.width;
        height = areaHelper.height;
    }

    public BlockPos getCenterPortal() {
        Direction.Axis axis = getCachedState().get(DeepslateCoreBlock.AXIS);

        return new BlockPos(pos.getX() - (axis == Direction.Axis.X ? (width / 2) + 1 : 0),
                pos.getY() + (height/2),
                pos.getZ() + (axis == Direction.Axis.Z ? (width / 2) + 1 : 0));
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, DeepslateCoreBlockEntity entity) {
        entity.tick(world);
    }

    private void tick(World world) {
        if (!(world.getBlockState(getCenterPortal()).getBlock() instanceof GenericPortalBlock)) {
            deactivate(world);
            return;
        }

        ticks++;

        if (!forming && ticks > 60 && world instanceof ServerWorld serverWorld) {
            BlockPos center = getCenterPortal();
            world.breakBlock(center, true);

            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                PacketByteBuf packet = PacketByteBufs.create();

                packet.writeDouble(center.getX()); packet.writeDouble(center.getY()); packet.writeDouble(center.getZ());
                packet.writeBoolean(forming);
                packet.writeBoolean(false);

                ServerPlayNetworking.send(player, ModMessages.ANCIENT_PORTAL_SOUND_ID, packet);
            }

            deactivate(world);
        }
    }

    private void deactivate(World world) {
        world.setBlockState(pos, ModBlocks.CHARGED_DEEPSLATE.getDefaultState());
    }
}
