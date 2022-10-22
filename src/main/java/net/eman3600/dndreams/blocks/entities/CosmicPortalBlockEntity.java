package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.initializers.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CosmicPortalBlockEntity extends BlockEntity {
    public CosmicPortalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COSMIC_PORTAL_ENTITY, pos, state);
    }

    public boolean shouldDrawSide(Direction direction) {
        return direction.getAxis() == Direction.Axis.Y;
    }
}
