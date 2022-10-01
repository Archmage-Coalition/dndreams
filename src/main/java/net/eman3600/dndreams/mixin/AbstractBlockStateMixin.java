package net.eman3600.dndreams.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin extends State<Block, BlockState> {
    protected AbstractBlockStateMixin(Block owner, ImmutableMap<Property<?>, Comparable<?>> entries, MapCodec<BlockState> codec) {
        super(owner, entries, codec);
    }

    @Inject(method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void dndreams$getOutlineShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if(context instanceof EntityShapeContext) {
            EntityShapeContext esc = (EntityShapeContext)context;
            if(esc.getEntity() != null) {
                Entity entity = esc.getEntity();
                if (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModStatusEffects.RESTRICTED)) {
                    cir.setReturnValue(VoxelShapes.empty());
                }
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", cancellable = true)
    private void dndreams$getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape blockShape = cir.getReturnValue();
        if(!blockShape.isEmpty() && context instanceof EntityShapeContext) {
            EntityShapeContext esc = (EntityShapeContext)context;
            if(esc.getEntity() != null) {
                Entity entity = esc.getEntity();
                if (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModStatusEffects.INSUBSTANTIAL) && !world.getBlockState(pos).isIn(ModTags.SUBSTANTIAL) && !entity.getType().isIn(ModTags.SUBSTANTIAL_ENTITIES)) {
                    cir.setReturnValue(VoxelShapes.empty());
                }
            }
        }
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void dndreams$onEntityCollision(World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModStatusEffects.INSUBSTANTIAL) && !world.getBlockState(pos).isIn(ModTags.SUBSTANTIAL)) {
            ci.cancel();
        }
    }
}
