package net.eman3600.dndreams.blocks.energy;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.items.misc_tool.WaystoneItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SoulCandleBlock extends RitualCandleBlock {
    public SoulCandleBlock(ParticleEffect particle, Settings settings) {
        super(particle, settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SoulCandleBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.SOUL_CANDLE_ENTITY, SoulCandleBlockEntity::tick);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity e = world.getBlockEntity(pos);

            if (e instanceof SoulCandleBlockEntity entity) {
                if (entity.isCasting() || entity.isSustained()) entity.deactivate(false);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof SoulCandleBlockEntity entity) {
            if (state.get(LIT)) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.getItem() instanceof WaystoneItem && stack.getOrCreateNbt().contains("bound_pos")) {

                    stack.damage(1, player, (p) -> {
                        p.sendToolBreakStatus(hand);
                    });

                    entity.setBoundPos(WaystoneItem.readBoundPos(stack.getOrCreateNbt()));

                    return ActionResult.SUCCESS;
                }

                entity.deactivate(true);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
