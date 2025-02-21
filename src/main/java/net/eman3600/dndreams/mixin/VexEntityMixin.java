package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.entities.mobs.ParryableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VexEntity.class)
public abstract class VexEntityMixin extends HostileEntity implements ParryableEntity {
    @Shadow public abstract boolean isCharging();

    protected VexEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canParry() {
        return isCharging();
    }

    @Override
    public float parryInterruptDamage() {
        return (float)getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }
}
