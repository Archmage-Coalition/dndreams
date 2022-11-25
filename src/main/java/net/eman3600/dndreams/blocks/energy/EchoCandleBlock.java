package net.eman3600.dndreams.blocks.energy;

import net.eman3600.dndreams.blocks.entities.EchoCandleBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.items.charge.TuningItem;
import net.eman3600.dndreams.rituals.setup.Ritual.CandleTuning;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EchoCandleBlock extends RitualCandleBlock {
    public static final EnumProperty<CandleTuning> TUNING = EnumProperty.of("tuning", CandleTuning.class);

    public EchoCandleBlock(ParticleEffect particle, Settings settings) {
        super(particle, settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EchoCandleBlockEntity(pos, state);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(TUNING, CandleTuning.NONE);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(TUNING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.ECHO_CANDLE_ENTITY, EchoCandleBlockEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() instanceof TuningItem item && state.get(TUNING) != item.getTuning() && !state.get(Properties.LIT)) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(TUNING, item.getTuning()));

                stack.damage(1, player, (p) -> {
                    p.sendToolBreakStatus(hand);
                });
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}
