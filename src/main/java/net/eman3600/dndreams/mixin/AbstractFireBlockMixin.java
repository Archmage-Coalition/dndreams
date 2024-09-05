package net.eman3600.dndreams.mixin;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.mixin_interfaces.AreaHelperAccess;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.AreaHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AbstractFireBlock.class)
public abstract class AbstractFireBlockMixin extends Block {
    @Shadow @Final private float damage;

    public AbstractFireBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void dndreams$onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity) {
            Optional<TrinketComponent> trinketOptional = TrinketsApi.getTrinketComponent((LivingEntity) entity);
            if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.FLAME_CAPE)) {
                ci.cancel();
                return;
            }
        }

        if (state.isIn(ModTags.CURSE_FIRES)) {

            entity.damage(DamageSourceAccess.CURSED_FLAME, this.damage);
            ci.cancel();
        }
    }

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/AreaHelper;createPortal()V"))
    private void dndreams$onBlockAdded$sabotagePortal(AreaHelper instance) {

        if (instance instanceof AreaHelperAccess access) {
            access.createUnchargedPortal();
            return;
        }

        instance.createPortal();
    }
}
