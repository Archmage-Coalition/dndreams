package net.eman3600.dndreams.blocks.energy;

import net.eman3600.dndreams.blocks.entities.AbstractPowerReceiver;
import net.eman3600.dndreams.blocks.entities.CosmicFountainBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CosmicFountainBlock extends BlockWithEntity {
    public static final BooleanProperty FUNCTIONAL = BooleanProperty.of("functional");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public static final int SEARCH_RANGE = 16;
    public static final int GIVE_RANGE = 24;
    public static final int SPLIT_RANGE = 49;

    public static final List<BlockPos> SEARCH_OFFSETS = BlockPos.stream(-SEARCH_RANGE, -SEARCH_RANGE, -SEARCH_RANGE, SEARCH_RANGE, SEARCH_RANGE, SEARCH_RANGE).map(BlockPos::toImmutable).toList();
    public static final List<BlockPos> SPLIT_OFFSETS = BlockPos.stream(-SPLIT_RANGE, -SPLIT_RANGE, -SPLIT_RANGE, SPLIT_RANGE, SPLIT_RANGE, SPLIT_RANGE).map(BlockPos::toImmutable).toList();
    public static final List<BlockPos> PORTAL_OFFSETS = BlockPos.stream(-3, -1, -3, 3, 1, 3).map(BlockPos::toImmutable).toList();
    public static final List<BlockPos> COSMIC_AUGMENT_OFFSETS = BlockPos.stream(-3, 0, -3, 3, 0, 3)
            .filter(p -> {
                if (Math.abs(p.getX()) == 2 && Math.abs(p.getZ()) == 2) return true;
                if ((Math.abs(p.getX()) == 3 && Math.abs(p.getZ()) <= 1) || (Math.abs(p.getZ()) == 3 && Math.abs(p.getX()) <= 1)) return true;

                return false;
            })
            .map(BlockPos::toImmutable).toList();
    public static final List<BlockPos> GIVE_OFFSETS = BlockPos.stream(-GIVE_RANGE, -GIVE_RANGE, -GIVE_RANGE, GIVE_RANGE, GIVE_RANGE, GIVE_RANGE).map(BlockPos::toImmutable).toList();


    public CosmicFountainBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(FUNCTIONAL, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CosmicFountainBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        try {
            CosmicFountainBlockEntity entity = (CosmicFountainBlockEntity) world.getBlockEntity(pos);

            ItemStack stack = player.getStackInHand(hand);
            assert entity != null;
            if (state.get(FUNCTIONAL) && stack.getItem() == ModItems.LIQUID_SOUL && entity.addPower(500)) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                return ActionResult.SUCCESS;
            }

            if (!world.isClient) {
                if (state.get(FUNCTIONAL)) {
                    player.sendMessage(Text.translatable("block.dndreams.cosmic_fountain.power", entity.getPower(), entity.getMaxPower(), entity.getRate()), true);
                } else if (!WorldComponents.BOSS_STATE.get(world.getScoreboard()).dragonSlain()) {
                    player.sendMessage(Text.translatable("block.dndreams.cosmic_fountain.sealed"), true);
                } else if (entity.isValid(world, false)) {
                    player.sendMessage(Text.translatable("block.dndreams.cosmic_fountain.no_power"), true);
                } else {
                    player.sendMessage(Text.translatable("block.dndreams.cosmic_fountain.invalid"), true);
                }
            }

            return ActionResult.SUCCESS;
        } catch (ClassCastException | NullPointerException e) {
            return ActionResult.PASS;
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(FUNCTIONAL, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FUNCTIONAL, FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, ModBlockEntities.COSMIC_FOUNTAIN_ENTITY, CosmicFountainBlockEntity::tickClient) : checkType(type, ModBlockEntities.COSMIC_FOUNTAIN_ENTITY, CosmicFountainBlockEntity::tick);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        if (state.get(FUNCTIONAL) && world.getBlockEntity(pos) instanceof CosmicFountainBlockEntity entity) {
            BlockPos portal = pos.down(entity.getLength());

            for (BlockPos blockPos: SEARCH_OFFSETS) {
                if (random.nextInt(48) != 0 || !isSoulPower(world, portal, blockPos)) continue;
                displayEnchantParticle(world, pos, blockPos.down(entity.getLength()), ModParticles.SOUL_ENERGY);
            }

            for (BlockPos blockPos: COSMIC_AUGMENT_OFFSETS) {
                if (random.nextInt(6) != 0 || !isCosmicAugment(world, portal.up(), blockPos)) continue;
                displayEnchantParticle(world, pos, blockPos.down(entity.getLength() - 1), ModParticles.COSMIC_ENERGY);
            }
        }
    }

    public static boolean isSoulPower(World world, BlockPos pos, BlockPos offset) {
        return world.getBlockState(pos.add(offset)).isIn(ModTags.SOUL_POWER);
    }

    public static boolean isCosmicPortal(World world, BlockPos pos, BlockPos offset) {
        return world.getBlockState(pos.add(offset)).isOf(ModBlocks.COSMIC_PORTAL);
    }

    public static boolean isCosmicAugment(World world, BlockPos pos, BlockPos offset) {
        return world.getBlockState(pos.add(offset)).isIn(ModTags.COSMIC_AUGMENTS);
    }

    public static AbstractPowerReceiver findPowerReceiver(World world, BlockPos pos, BlockPos offset) {
        return world.getBlockEntity(pos.add(offset)) instanceof AbstractPowerReceiver entity ? entity : null;
    }

    public void displayEnchantParticle(World world, BlockPos pos, BlockPos blockPos, ParticleEffect type) {
        if (world instanceof ServerWorld server) {
            displayEnchantParticle(server, pos, blockPos, type, 1);
            return;
        }

        Random random = world.random;

        world.addParticle(type, (double)pos.getX() + 0.5, (double)pos.getY() + 2.0, (double)pos.getZ() + 0.5, (double)((float)blockPos.getX() + random.nextFloat()) - 0.5, (float)blockPos.getY() - random.nextFloat() - 1.0f, (double)((float)blockPos.getZ() + random.nextFloat()) - 0.5);
    }

    public void displayEnchantParticle(ServerWorld world, BlockPos pos, BlockPos blockPos, ParticleEffect type, int count) {
        Random random = world.random;

        world.spawnParticles(type, (double)pos.getX() + 0.5, (double)pos.getY() + 2.0, (double)pos.getZ() + 0.5, count, (double)((float)blockPos.getX() + random.nextFloat()) - 0.5, (float)blockPos.getY() - random.nextFloat() - 1.0f, (double)((float)blockPos.getZ() + random.nextFloat()) - 0.5, 1f);
    }

    public void displayEnchantParticle(ServerWorld world, BlockPos pos, LivingEntity target, ParticleEffect type, int count) {
        displayEnchantParticle(world, target.getBlockPos(), pos.subtract(target.getBlockPos()), type, count);
    }
}
