package net.eman3600.dndreams.blocks.energy;

import net.eman3600.dndreams.blocks.entities.RefinedCauldronBlockEntity;
import net.eman3600.dndreams.blocks.properties.BrewType;
import net.eman3600.dndreams.blocks.properties.CauldronState;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RefinedCauldronBlock extends BlockWithEntity {
    private static final VoxelShape CAULDRON_SHAPE = Block.createCuboidShape(1, 0.0, 1, 15, 11.0, 15);

    public RefinedCauldronBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return CAULDRON_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RefinedCauldronBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof RefinedCauldronBlockEntity entity) {
            ItemStack stack = player.getStackInHand(hand);

            if (entity.getBrewType() == BrewType.WATER && entity.getLevel() < 3) {

                if (stack.isOf(Items.POTION) && stack.hasNbt() && stack.getNbt().contains("Potion") && stack.getNbt().getString("Potion").equals("minecraft:water")) {
                    entity.fill(1);

                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, Items.GLASS_BOTTLE.getDefaultStack()));

                    return ActionResult.SUCCESS;
                } else if (stack.isOf(Items.WATER_BUCKET)) {
                    entity.fill(3);

                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, Items.BUCKET.getDefaultStack()));

                    return ActionResult.SUCCESS;
                }
            } else if (entity.getBrewType() == BrewType.CRAFT) {
                if (world instanceof ServerWorld serverWorld) {
                    ItemStack result = entity.take(stack, serverWorld);

                    if (!result.isEmpty()) {
                        player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, result));
                    }
                }

                return ActionResult.SUCCESS;
            } else if (entity.getBrewType() == BrewType.BREW && entity.getCauldronState() == CauldronState.IDLE && entity.isBrewDone()) {
                if (!world.isClient) {
                    entity.beginStir();
                }

                return ActionResult.SUCCESS;
            } else if (entity.getBrewType() == BrewType.BREW && entity.getCauldronState() == CauldronState.FINISHED) {
                if (world instanceof ServerWorld serverWorld) {
                    ItemStack result = entity.take(stack, serverWorld);

                    if (!result.isEmpty()) {
                        player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, result));
                    }
                }

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, ModBlockEntities.REFINED_CAULDRON_ENTITY, RefinedCauldronBlockEntity::tickClient) :
                checkType(type, ModBlockEntities.REFINED_CAULDRON_ENTITY, RefinedCauldronBlockEntity::tick);
    }
}
