package net.eman3600.dndreams.blocks.energy;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BonfireBlock extends BlockWithEntity {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    public static final BooleanProperty SOUL = BooleanProperty.of("soul");
    public static final BooleanProperty STRONG = BooleanProperty.of("strong");
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public BonfireBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SOUL, false).with(FACING, Direction.NORTH).with(STRONG, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BonfireBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.getBlockEntity(pos) instanceof BonfireBlockEntity entity) {
            int slot = entity.nextAvailableSlot();

            if (stack.getItem() instanceof ShovelItem) {
                if (!world.isClient()) {
                    world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, pos, 0);
                }
                stack.damage(1, player, p -> p.sendToolBreakStatus(hand));

                BlockState newState = state.get(SOUL) ? Blocks.SOUL_CAMPFIRE.getDefaultState().with(CampfireBlock.LIT, false).with(CampfireBlock.FACING, state.get(FACING)) : Blocks.CAMPFIRE.getDefaultState().with(CampfireBlock.LIT, false).with(CampfireBlock.FACING, state.get(FACING));

                world.setBlockState(pos, newState, Block.NOTIFY_ALL);

                return ActionResult.SUCCESS;

            } else if (stack.isIn(ModTags.RITUAL_FOCI) && entity.getRitualFocus().isEmpty()) {

                if (!world.isClient) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    entity.setRitualFocus(copy);
                }

                if (!player.isCreative()) {
                    stack.decrement(1);
                }

                return ActionResult.SUCCESS;
            } else if (!stack.isIn(ModTags.RITUAL_FOCI) && !stack.isEmpty() && slot != -1) {

                if (!world.isClient) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    entity.setRitualItem(copy, slot);
                }

                if (!player.isCreative()) {
                    stack.decrement(1);
                }

                return ActionResult.SUCCESS;
            } else if (stack.isEmpty()) {

                if (!world.isClient) entity.removeItems();
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, ModBlockEntities.BONFIRE_ENTITY, BonfireBlockEntity::tickClient) : checkType(type, ModBlockEntities.BONFIRE_ENTITY, BonfireBlockEntity::tick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SOUL, FACING, STRONG);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            world.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);

        if (!entity.isFireImmune() && entity instanceof LivingEntity e && !EnchantmentHelper.hasFrostWalker(e)) {
            Optional<TrinketComponent> trinketOptional = TrinketsApi.getTrinketComponent((LivingEntity) entity);
            if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.FLAME_CAPE)) {
                return;
            }
            e.damage(DamageSourceAccess.CURSED_FLAME, 2);
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BonfireBlockEntity entity) {
            ItemScatterer.spawn(world, pos, (entity).getRitualItems());
            ItemScatterer.spawn(world, pos, DefaultedList.ofSize(1, entity.getRitualFocus()));
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
}
