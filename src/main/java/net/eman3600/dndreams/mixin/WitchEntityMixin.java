package net.eman3600.dndreams.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMixin extends RaiderEntity implements RangedAttackMob {

    protected WitchEntityMixin(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "modifyAppliedDamage", constant = @Constant(floatValue = 0.15f))
    private float dndreams$modifyAppliedDamage$normalizeMagicDamage(float constant) {
        return 1f;
    }
}
