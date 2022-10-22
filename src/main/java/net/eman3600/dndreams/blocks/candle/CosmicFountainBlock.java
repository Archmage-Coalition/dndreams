package net.eman3600.dndreams.blocks.candle;

import net.eman3600.dndreams.blocks.entities.CosmicFountainBlockEntity;
import net.eman3600.dndreams.blocks.entities.CosmicFountainPoleBlockEntity;
import net.eman3600.dndreams.initializers.ModBlockEntities;
import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CosmicFountainBlock extends BlockWithEntity {
    public static final BooleanProperty FUNCTIONAL = BooleanProperty.of("functional");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;


    public CosmicFountainBlock(Settings settings) {
        super(settings);
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
            if (stack.getItem() == ModItems.LIQUID_SOUL && entity.addPower(500)) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                return ActionResult.SUCCESS;
            }

            if (!world.isClient) {
                player.sendMessage(Text.translatable("block.dndreams.cosmic_fountain.power", entity.getPower(), CosmicFountainBlockEntity.MAX_POWER), true);
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
        return world.isClient ? null : checkType(type, ModBlockEntities.COSMIC_FOUNTAIN_ENTITY, CosmicFountainBlockEntity::tick);
    }
}
