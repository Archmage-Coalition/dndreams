package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.blocks.DeepslateCoreBlock;
import net.eman3600.dndreams.blocks.entities.DeepslateCoreBlockEntity;
import net.eman3600.dndreams.blocks.portal.GenericPortalAreaHelper;
import net.eman3600.dndreams.blocks.portal.GenericPortalBlock;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class LiquidSoulItem extends Item {
    public LiquidSoulItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(pos);

        BlockPos offset = pos.offset(context.getSide());

        Direction.Axis axis;

        if (state.getBlock() == ModBlocks.CHARGED_DEEPSLATE && context.getWorld() instanceof ServerWorld world && !partOfPortal(world, offset) && (axis = getAxis(world, pos)) != null) {
            GenericPortalAreaHelper helper = new GenericPortalAreaHelper(world, offset, axis, ((GenericPortalBlock)ModBlocks.WEAK_PORTAL).frameBlock, (GenericPortalBlock)ModBlocks.WEAK_PORTAL);

            if (helper.width > 1 && helper.height > 1) {
                context.getPlayer().setStackInHand(context.getHand(), ItemUsage.exchangeStack(stack, context.getPlayer(), new ItemStack(Items.GLASS_BOTTLE)));
                helper.createPortal();

                world.setBlockState(helper.getFrameCorner(), ModBlocks.DEEPSLATE_CORE.getDefaultState().with(DeepslateCoreBlock.AXIS, axis));

                assert world.getBlockEntity(helper.getFrameCorner()) instanceof DeepslateCoreBlockEntity;
                DeepslateCoreBlockEntity entity = (DeepslateCoreBlockEntity) world.getBlockEntity(helper.getFrameCorner());

                entity.init(helper);
                BlockPos center = entity.getCenterPortal();

                for (ServerPlayerEntity player : world.getPlayers()) {
                    PacketByteBuf packet = PacketByteBufs.create();

                    packet.writeDouble(center.getX()); packet.writeDouble(center.getY()); packet.writeDouble(center.getZ());
                    packet.writeBoolean(entity.isForming());
                    packet.writeBoolean(true);

                    ServerPlayNetworking.send(player, ModMessages.ANCIENT_PORTAL_SOUND_ID, packet);
                }

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    private Direction.Axis getAxis(World world, BlockPos pos) {
        if (world.getBlockState(pos.north()).isOf(ModBlocks.CHARGED_DEEPSLATE) || world.getBlockState(pos.south()).isOf(ModBlocks.CHARGED_DEEPSLATE)) {
            return Direction.Axis.Z;
        } else if (world.getBlockState(pos.east()).isOf(ModBlocks.CHARGED_DEEPSLATE) || world.getBlockState(pos.west()).isOf(ModBlocks.CHARGED_DEEPSLATE)) {
            return Direction.Axis.X;
        }

        return null;
    }

    private boolean partOfPortal(World world, BlockPos pos) {
        for (BlockPos test : new BlockPos[]{pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()}) {
            if (world.getBlockState(test).getBlock() instanceof GenericPortalBlock) {
                return true;
            }
        }
        return false;
    }
}
