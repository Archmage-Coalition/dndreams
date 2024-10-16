package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.UUID;

public class RotComponent implements AutoSyncedComponent, ServerTickingComponent {

    private final LivingEntity entity;
    private int rot = 0;
    private float rotTicks = 0;
    private int slowTicks = 0;
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
                healRot(MathHelper.ceil(entity.getAbsorptionAmount()));
                entity.setAbsorptionAmount(0);
                if (entity.hasStatusEffect(StatusEffects.ABSORPTION)) entity.removeStatusEffect(StatusEffects.ABSORPTION);
            } else if (rotTicks > 0) {
                float cleansing = getPassiveCleansing();
                if (cleansing > 0) {
                    rotTicks -= cleansing;
                    markDirty();
                } else if (rotTicks < 10) {
                    rotTicks = 10;
                    markDirty();
                }
            } else if (shouldCleanseRot()) {
                rotTicks = 10f;
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
        rotTicks = tag.getFloat("rot_ticks");
        trueHp = tag.getFloat("true_hp");

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

        tag.putInt("rot", rot);
        tag.putFloat("rot_ticks", rotTicks);
        trueHp = entity.getMaxHealth();
        tag.putFloat("true_hp", trueHp);
    }

    public void inflictRot(int value) {

        if (isImmune()) return;

        EntityComponents.TORMENT.maybeGet(entity).ifPresent(torment -> torment.lowerSanity(value * 1.5f));

        rotTicks = 60;
        if (entity.getAbsorptionAmount() > 0) {
            float remainder = value - entity.getAbsorptionAmount();

            entity.setAbsorptionAmount(0f);
            if (entity.hasStatusEffect(StatusEffects.ABSORPTION)) entity.removeStatusEffect(StatusEffects.ABSORPTION);
            if (remainder >= 1) {
                rot += remainder;
            }

            updateRot();
            return;
        }

        rot += value;
        updateRot();
    }

    public void healRot(int value) {

        if (rot > 0) {
            rot = Math.max(0, rot - value);
            updateRot();
        }
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
        return entity.isUndead() || entity.getType().isIn(ModTags.ROT_IMMUNE_ENTITIES);
    }

    public boolean shouldCleanseRot() {
        return getPassiveCleansing() > 0;
    }

    public float getPassiveCleansing() {

        if (rotTicks >= 11) return 1;
        if (entity.hasStatusEffect(StatusEffects.DARKNESS) || entity.hasStatusEffect(ModStatusEffects.LOOMING)) return 0;
        if (entity.world.getRegistryKey() == ModDimensions.GATEWAY_DIMENSION_KEY) return 0;
        if (entity instanceof PlayerEntity player && (EntityComponents.TORMENT.get(player).getNightmareHaze() > 0.1f || EntityComponents.TORMENT.get(player).isInStorm())) return 0;
        if (getBloodMoonComponent().isBloodMoon()) return 0;
        if (entity.world instanceof ServerWorld server && TormentComponent.shouldShroud(server, entity.getBlockPos())) return 0;
        //if (entity.world.getRegistryKey() == World.NETHER) return 0.05f;
        if (entity.world.getRegistryKey() == World.END && entity.world.getScoreboard() != null) return WorldComponents.BOSS_STATE.get(entity.world.getScoreboard()).dragonSlain() ? 1 : 0;
        if (entity.world.getRegistryKey() == ModDimensions.HAVEN_DIMENSION_KEY) return 0.05f;
        if (entity.world.getLightLevel(LightType.SKY, entity.getBlockPos()) >= 12) return entity.world.isDay() ? 1 : 0.2f;

        return 0.01f;
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

    private BloodMoonComponent getBloodMoonComponent() {
        return WorldComponents.BLOOD_MOON.get(entity.world);
    }

    public static boolean hasRot(Entity entity) {
        return EntityComponents.ROT.isProvidedBy(entity) && EntityComponents.ROT.get(entity).getRot() > 0;
    }

    public int getSlowTicks() {
        return slowTicks;
    }

    public void setSlowTicks(int slowTicks) {
        this.slowTicks = slowTicks;
        markDirty();
    }
}
