package net.eman3600.dndreams.blocks.portal;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class GenericPortalAreaHelper {
    private final AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK;
    private final WorldAccess world;
    private final Block frameBlock;
    private final GenericPortalBlock portalBlock;
    public final Direction.Axis axis;
    private final Direction negativeDir;
    private int foundPortalBlocks;
    @Nullable
    private BlockPos lowerCorner;
    public final int height;
    public final int width;

    public GenericPortalAreaHelper(WorldAccess world, BlockPos pos, Direction.Axis axis, Block frameBlock, GenericPortalBlock portalBlock) {
        this.frameBlock = frameBlock;
        this.IS_VALID_FRAME_BLOCK = (state, w, p) -> state.isOf(frameBlock);
        this.portalBlock = portalBlock;

        this.world = world;
        this.axis = axis;
        this.negativeDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.lowerCorner = this.getLowerCorner(pos);
        if (this.lowerCorner == null) {
            this.lowerCorner = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.getWidth();
            if (this.width > 0) {
                this.height = this.getHeight();
            } else {
                height = 0;
            }
        }
    }

    @Nullable
    public BlockPos getFrameCorner() {
        if (axis == Direction.Axis.X)
            return lowerCorner.offset(axis, 1).down();
        else
            return lowerCorner.offset(axis, -1).down();
    }

    @Nullable
    private BlockPos getLowerCorner(BlockPos pos) {
        int i = Math.max(this.world.getBottomY(), pos.getY() - 21);
        while (pos.getY() > i && validStateInsidePortal(this.world.getBlockState(pos.down()))) {
            pos = pos.down();
        }
        Direction direction = this.negativeDir.getOpposite();
        int j = this.getWidth(pos, direction) - 1;
        if (j < 0) {
            return null;
        }
        return pos.offset(direction, j);
    }

    private boolean validStateInsidePortal(BlockState state) {
        return state.isAir() || state.getBlock() instanceof GenericPortalBlock || state.isOf(Blocks.SCULK_VEIN);
    }

    private int getWidth(BlockPos pos, Direction direction) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = 0; i <= 21; ++i) {
            mutable.set(pos).move(direction, i);
            BlockState blockState = this.world.getBlockState(mutable);
            if (!validStateInsidePortal(blockState)) {
                if (!IS_VALID_FRAME_BLOCK.test(blockState, this.world, mutable)) break;
                return i;
            }
            BlockState blockState2 = this.world.getBlockState(mutable.move(Direction.DOWN));
            if (!IS_VALID_FRAME_BLOCK.test(blockState2, this.world, mutable)) break;
        }
        return 0;
    }

    private int getWidth() {
        int i = this.getWidth(this.lowerCorner, this.negativeDir);
        if (i < 2 || i > 21) {
            return 0;
        }
        return i;
    }

    private int getHeight() {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = this.countPortalBlocks(mutable);
        if (i < 3 || i > 21 || !this.isValidPortal(mutable, i)) {
            return 0;
        }
        return i;
    }

    private int countPortalBlocks(BlockPos.Mutable mutable) {
        for (int i = 0; i < 21; ++i) {
            mutable.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, -1);
            if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(mutable), this.world, mutable)) {
                return i;
            }
            mutable.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, this.width);
            if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(mutable), this.world, mutable)) {
                return i;
            }
            for (int j = 0; j < this.width; ++j) {
                mutable.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, j);
                BlockState blockState = this.world.getBlockState(mutable);
                if (!validStateInsidePortal(blockState)) {
                    return i;
                }
                if (!blockState.isOf(portalBlock)) continue;
                ++this.foundPortalBlocks;
            }
        }
        return 21;
    }

    private boolean isValidPortal(BlockPos.Mutable mutable, int i) {
        for (int j = 0; j < this.width; ++j) {
            BlockPos.Mutable mutable2 = mutable.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, j);
            if (IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(mutable2), this.world, mutable2)) continue;
            return false;
        }
        return true;
    }

    public boolean wasAlreadyValid() {
        return this.isValid() && this.foundPortalBlocks == this.width * this.height;
    }

    public boolean isValid() {
        return this.lowerCorner != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortal() {
        createPortalOf(portalBlock.getDefaultState().with(GenericPortalBlock.AXIS, this.axis));
    }

    public void createPortalOf(BlockState blockState) {
        assert this.lowerCorner != null;
        BlockPos.iterate(this.lowerCorner, this.lowerCorner.offset(Direction.UP, this.height - 1).offset(this.negativeDir, this.width - 1)).forEach(blockPos -> this.world.setBlockState(blockPos, blockState, Block.NOTIFY_LISTENERS | Block.FORCE_STATE));
    }
}
