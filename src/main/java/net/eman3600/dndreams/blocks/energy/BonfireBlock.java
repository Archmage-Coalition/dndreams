package net.eman3600.dndreams.blocks.energy;

import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

public class BonfireBlock extends BlockWithEntity {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    public static final BooleanProperty LIT = Properties.LIT;
    public static final BooleanProperty STRONG = BooleanProperty.of("strong");
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public BonfireBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false).with(FACING, Direction.NORTH).with(STRONG, true));
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

        if (!state.get(LIT) && player.getHealth() > 10f && world.getBlockEntity(pos) instanceof BonfireBlockEntity entity) {

            if (!world.isClient) {
                player.damage(BloodlustItem.CRIMSON_SACRIFICE, 10f);
                entity.setPower(0);
                world.setBlockState(pos, state.with(LIT, true), Block.NOTIFY_ALL);
            }

            link(state, world, pos, player, entity);
            return ActionResult.SUCCESS;
        } else if (state.get(LIT) && world.getBlockEntity(pos) instanceof BonfireBlockEntity entity) {
            if (stack.getItem() instanceof ShovelItem) {
                if (!world.isClient()) {
                    world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, pos, 0);
                }
                stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
                entity.setPower(0);
                world.setBlockState(pos, state.with(LIT, false), Block.NOTIFY_ALL);

            } else {
                link(state, world, pos, player, entity);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private void link(BlockState state, World world, BlockPos pos, PlayerEntity player, BonfireBlockEntity entity) {
        if (canRespawn(world)) {
            ServerPlayerEntity serverPlayerEntity;
            if (!(world.isClient || (serverPlayerEntity = (ServerPlayerEntity) player).getSpawnPointDimension() == world.getRegistryKey() && pos.equals(serverPlayerEntity.getSpawnPointPosition()))) {

                serverPlayerEntity.setSpawnPoint(world.getRegistryKey(), pos, 0.0f, false, true);
            }
        }

        if (!world.isClient) {
            world.playSound(null, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1.0f, 1.0f);

            EntityComponents.INFUSION.maybeGet(player).ifPresent(component -> component.setLinkTicks(InfusionComponent.LINK_LENGTH));
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? (state.get(LIT) ? checkType(type, ModBlockEntities.BONFIRE_ENTITY, BonfireBlockEntity::tickClient) : null) : checkType(type, ModBlockEntities.BONFIRE_ENTITY, BonfireBlockEntity::tick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING, STRONG);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) {
            return;
        }
        if (random.nextInt(10) == 0) {
            world.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
        }
        if (random.nextInt(5) == 0) {
            for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                world.addParticle(ParticleTypes.LAVA, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, random.nextFloat() / 2.0f, 5.0E-5, random.nextFloat() / 2.0f);
            }
        }
    }

    public static boolean canRespawn(World world) {
        return world.getDimension().respawnAnchorWorks() || world.getDimension().bedWorks();
    }
}
