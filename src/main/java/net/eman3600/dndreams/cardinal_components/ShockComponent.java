package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.ShockComponentI;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.CreeperEntityAccess;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import javax.annotation.Nonnegative;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ShockComponent implements ShockComponentI {

    private static final List<Function<LivingEntity, Boolean>> chargePredicates = new ArrayList<>();

    private float shock = 0f;
    private boolean cushioned = false;

    private final LivingEntity entity;
    private boolean dirty;

    public ShockComponent(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void chargeShock(@Nonnegative float amount) {
        shock += amount;
        markDirty();

        if (entity instanceof CreeperEntityAccess access) {
            entity.getDataTracker().set(access.getChargedTracker(), true);
        }
    }

    @Override
    public boolean hasShock() {
        return shock > 0f;
    }

    @Override
    public float dischargeShock() {
        float i = shock;
        shock = 0;

        if (entity instanceof CreeperEntityAccess access) {
            entity.getDataTracker().set(access.getChargedTracker(), false);
        }

        markDirty();
        return i;
    }

    @Override
    public float dischargeShock(@Nonnegative float max) {
        return Math.min(dischargeShock(), max);
    }

    public boolean canStoreShock() {
        for (Function<LivingEntity, Boolean> predicate: chargePredicates) {
            if (predicate.apply(entity)) return true;
        }
        return false;
    }


    @Override
    public void serverTick() {

        if (hasShock() && !canStoreShock()) {
            entity.timeUntilRegen = 0;
            entity.damage(DamageSourceAccess.SHOCK, dischargeShock());
        }

        if (dirty) {
            EntityComponents.SHOCK.sync(entity);
            dirty = false;
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        shock = tag.getFloat("shock");
        cushioned = tag.getBoolean("cushioned");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("shock", shock);
        tag.putBoolean("cushioned", cushioned);
    }

    @Override
    public boolean isCushioned()
    {
        return cushioned;
    }

    @Override
    public void setCushioned(boolean cushioned)
    {
        this.cushioned = cushioned;
        markDirty();
    }

    public static void registerChargePredicate(Function<LivingEntity, Boolean> predicate) {
        chargePredicates.add(predicate);
    }
}
