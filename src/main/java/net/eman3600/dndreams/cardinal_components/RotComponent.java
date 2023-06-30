package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class RotComponent implements AutoSyncedComponent, ServerTickingComponent {

    private final LivingEntity entity;
    private int rot = 0;
    private int rotTicks = 0;
    private float trueHp = 20.0f;
    private boolean dirty = false;
    public static final UUID rotId = new UUID(0xade4f29f0a684049L, 0xbcb4bb8d300edac8L);

    public RotComponent(LivingEntity entity) {
        this.entity = entity;
    }


    @Override
    public void serverTick() {

        if (rot > 0) {

            if (entity.getAbsorptionAmount() > 0) {
                int g = rot;
                rot = 0;
                inflictRot(g);
            } else if (rotTicks > 0) {
                rotTicks--;
                markDirty();
            } else if (shouldCleanseRot()) {
                rotTicks = 10;
                rot--;
                updateRot();
            }
        } else if (rotTicks > 0) {

            rotTicks = 0;
            markDirty();
        }

        if (trueHp != entity.getMaxHealth()) {

            trueHp = entity.getMaxHealth();
            markDirty();
        }

        if (dirty) {
            dirty = false;
            EntityComponents.ROT.sync(entity);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        rot = tag.getInt("rot");
        rotTicks = tag.getInt("rot_ticks");
        trueHp = tag.getFloat("true_hp");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {

        tag.putInt("rot", rot);
        tag.putInt("rot_ticks", rotTicks);
        trueHp = entity.getMaxHealth();
        tag.putFloat("true_hp", trueHp);
    }

    public void inflictRot(int value) {

        if (isImmune()) return;

        rotTicks = 60;
        if (entity.getAbsorptionAmount() > 0) {
            float remainder = value - entity.getAbsorptionAmount();

            if (remainder >= 1) {
                entity.setAbsorptionAmount(0f);
                rot += remainder;
                updateRot();
            } else {
                entity.damage(DamageSourceAccess.ROT, value);
                updateRot();
            }
            return;
        }

        rot += value;
        updateRot();
    }

    public void healRot(int value) {

        rot = Math.max(0, rot - value);
        updateRot();
    }

    public int getRot() {
        return rot;
    }

    public float getTrueHp() {
        return trueHp;
    }

    public void setRot(int value) {
        if (rot < value) rotTicks = 60;
        rot = value;
        updateRot();
    }

    public boolean isImmune() {
        return entity.isUndead() || entity.getType().isIn(ModTags.GLOOM_ENTITIES);
    }

    public boolean shouldCleanseRot() {
        if (entity instanceof PlayerEntity player && EntityComponents.TORMENT.get(player).getSanityDamage() > 3) return false;
        return entity.world.getLightLevel(entity.getBlockPos(), entity.world.getAmbientDarkness()) >= 12;
    }

    public void updateRot() {
        EntityAttributeInstance instance = entity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);

        if (instance != null) {

            instance.removeModifier(rotId);
            if (rot >= (int)instance.getValue()) rot = (int)instance.getValue() - 1;

            float extra = entity.getHealth() - ((float)instance.getValue() - rot);

            if (rot > 0) {
                instance.addPersistentModifier(new EntityAttributeModifier(rotId, "rot", -rot, EntityAttributeModifier.Operation.ADDITION));
            }
            if (extra > 0 && entity.isAlive()) {

                entity.damage(DamageSourceAccess.ROT, 0);
            }
        }

        markDirty();
    }

    public void markDirty() {
        dirty = true;
    }
}
