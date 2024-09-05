package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.blocks.portal.GenericPortalBlock;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.mixin_interfaces.AreaHelperAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.AreaHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AreaHelper.class)
public abstract class AreaHelperMixin implements AreaHelperAccess {

    @Shadow @Final private WorldAccess world;

    @Shadow private @Nullable BlockPos lowerCorner;

    @Shadow private int height;

    @Shadow @Final private Direction.Axis axis;

    @Shadow @Final private Direction negativeDir;

    @Shadow @Final private int width;

    @Override
    public void createUnchargedPortal() {
        BlockState blockState = ModBlocks.UNCHARGED_NETHER_PORTAL.getDefaultState().with(GenericPortalBlock.AXIS, this.axis);
        BlockPos.iterate(this.lowerCorner, this.lowerCorner.offset(Direction.UP, this.height - 1).offset(this.negativeDir, this.width - 1)).forEach(blockPos -> this.world.setBlockState(blockPos, blockState, Block.NOTIFY_LISTENERS | Block.FORCE_STATE));
    }
}
