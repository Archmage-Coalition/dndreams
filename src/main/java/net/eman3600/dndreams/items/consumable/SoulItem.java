package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.blocks.DeepslateCoreBlock;
import net.eman3600.dndreams.blocks.entities.DeepslateCoreBlockEntity;
import net.eman3600.dndreams.blocks.portal.GenericPortalAreaHelper;
import net.eman3600.dndreams.blocks.portal.GenericPortalBlock;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.items.TooltipItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulItem extends TooltipItem {
    public SoulItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(pos);
        PlayerEntity player = context.getPlayer();

        BlockPos offset = pos.offset(context.getSide());

        Direction.Axis axis;

        if (state.getBlock() == ModBlocks.CHARGED_DEEPSLATE && context.getWorld() instanceof ServerWorld world && !partOfPortal(world, offset) && (axis = getAxis(world, pos)) != null) {
            GenericPortalAreaHelper helper = new GenericPortalAreaHelper(world, offset, axis, ((GenericPortalBlock)ModBlocks.WEAK_PORTAL).frameBlock, (GenericPortalBlock)ModBlocks.WEAK_PORTAL);

            if (helper.width > 1 && helper.height > 1) {
                if (player != null && !player.isCreative()) stack.decrement(1);
                helper.createPortal();

                world.setBlockState(helper.getFrameCorner(), ModBlocks.DEEPSLATE_CORE.getDefaultState().with(DeepslateCoreBlock.AXIS, axis));

                assert world.getBlockEntity(helper.getFrameCorner()) instanceof DeepslateCoreBlockEntity;
                DeepslateCoreBlockEntity entity = (DeepslateCoreBlockEntity) world.getBlockEntity(helper.getFrameCorner());

                entity.init(helper);
                BlockPos center = entity.getCenterPortal();

                for (ServerPlayerEntity player2 : world.getPlayers()) {
                    PacketByteBuf packet = PacketByteBufs.create();

                    packet.writeDouble(center.getX()); packet.writeDouble(center.getY()); packet.writeDouble(center.getZ());
                    packet.writeBoolean(entity.isForming());
                    packet.writeBoolean(true);

                    ServerPlayNetworking.send(player2, ModMessages.ANCIENT_PORTAL_SOUND_ID, packet);
                }

                return ActionResult.SUCCESS;
            }
        } else if (state.isOf(Blocks.REINFORCED_DEEPSLATE)) {

            if (player instanceof ServerPlayerEntity player2) {
                player2.sendMessage(Text.translatable("item.dndreams.soul.sealed"), true);
            }

            return ActionResult.SUCCESS;
        } else if (context.getWorld().getBlockState(pos).getBlock() == ModBlocks.WORLD_FOUNTAIN && player != null) {
            if (player instanceof ServerPlayerEntity player2) {
                EntityComponents.GATEWAY.get(player2).enterGateway(0, true);
            }
            if (!player.isCreative()) stack.decrement(1);
            return ActionResult.SUCCESS;
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
