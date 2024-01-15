package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.FallingBlockEntityAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity implements FallingBlockEntityAccess {

    @Shadow private BlockState block;

    @Shadow public abstract BlockState getBlockState();

    public FallingBlockEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void setBlockState(BlockState block) {
        this.block = block;
    }
}
