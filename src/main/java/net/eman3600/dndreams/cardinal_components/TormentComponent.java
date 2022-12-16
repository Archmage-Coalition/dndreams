package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.TormentComponentI;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.util.Function2;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TormentComponent implements TormentComponentI, AutoSyncedComponent, ServerTickingComponent {
    private float maxSanity = 100;
    private float sanity = 100;
    public static final float THREAD_VALUE = 3.5f;
    private int dragonFlashTicks = 0;
    private int sanityDamageTicks = 0;

    private static final List<Function<PlayerEntity, Float>> INSANITY_PREDICATES = new ArrayList<>();
    private static final Map<Function<LivingEntity, Boolean>, InsanityRangePair> MOBS_TO_INSANITY = new HashMap<>();

    private boolean shielded = false;

    private final PlayerEntity player;

    public static final float MAX_SANITY = 100f;
    public static final float MIN_SANITY = 0f;
    public static final float MIN_MAX_SANITY = 30f;
    private static final int SANITY_DAMAGE = 15;

    public TormentComponent(PlayerEntity playerIn) {
        player = playerIn;
    }

    @Override
    public float getSanity() {
        return sanity;
    }

    @Override
    public float getAttunedSanity() {
        return isAttuned() ? MAX_SANITY : getSanity();
    }

    @Override
    public float getMaxSanity() {
        return maxSanity;
    }

    private boolean sanityInflexible() {
        return false/*sanityDisabled
                && player.getWorld().getDimensionKey() != ModDimensions.DREAM_TYPE_KEY*/;
    }

    @Override
    public boolean isShielded() {
        return shielded;
    }

    public void shield(boolean shielded) {
        this.shielded = shielded;
        EntityComponents.TORMENT.sync(player);
    }

    @Override
    public void setSanity(float value) {
        sanity = value;
        normalize();
    }

    @Override
    public void setMaxSanity(float value) {
        maxSanity = value;
        normalize();
    }

    @Override
    public void lowerSanity(float value) {
        sanity -= value;
        normalize();
    }

    @Override
    public void lowerMaxSanity(float value) {
        maxSanity -= value;

        if (value > 0) sanityDamageTicks = SANITY_DAMAGE;

        normalize();
    }

    @Override
    public float getSanityDamage() {
        return MathHelper.clamp((float)sanityDamageTicks/SANITY_DAMAGE, 0, 1);
    }

    @Override
    public boolean shearSanity(float value, boolean yield) {
        boolean bl = false;
        while (value >= THREAD_VALUE) {
            ItemStack stack;
            if (getMaxSanity() - THREAD_VALUE >= MIN_MAX_SANITY && player.world.getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY) {
                bl = true;
                lowerMaxSanity(THREAD_VALUE);
                stack = ModItems.SANITY_THREAD.getDefaultStack();
            } else if (getSanity() - THREAD_VALUE >= MIN_SANITY) {
                bl = true;
                lowerSanity(THREAD_VALUE);
                stack = player.world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY ? ModItems.DREAM_POWDER.getDefaultStack() : ModItems.SCULK_POWDER.getDefaultStack();
            } else {
                break;
            }

            if (yield) {
                Vec3d vec = player.getPos().add(0, 1.5, 0);

                ItemEntity item = new ItemEntity(player.world, vec.x, vec.y, vec.z, stack);
                item.addVelocity(0, 0.15D, 0);
                item.setPickupDelay(20);

                player.world.spawnEntity(item);
            }

            value -= THREAD_VALUE;
        }


        normalize();
        return bl;
    }

    @Override
    public void lowerPerSecond(float value) {
        lowerSanity(value/20);
    }

    @Override
    public void lowerPerMinute(float value) {
        lowerSanity(value/1200);
    }

    private void normalize() {
        if (sanityInflexible()) {
            maxSanity = MAX_SANITY;
            sanity = getMaxSanity();
        } else {
            maxSanity = MathHelper.clamp(maxSanity, MIN_MAX_SANITY, MAX_SANITY);
            sanity = MathHelper.clamp(sanity, MIN_SANITY, getMaxSanity());
        }
        EntityComponents.TORMENT.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        sanity = tag.getFloat("sanity");
        maxSanity = tag.getFloat("max_sanity");
        dragonFlashTicks = tag.getInt("dragon_flash_ticks");
        shielded = tag.getBoolean("shielded");
        sanityDamageTicks = tag.getInt("sanity_damage_ticks");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("sanity", sanity);
        tag.putFloat("max_sanity", maxSanity);
        tag.putInt("dragon_flash_ticks", dragonFlashTicks);
        tag.putBoolean("shielded", shielded);
        tag.putInt("sanity_damage_ticks", sanityDamageTicks);
    }

    @Override
    public void serverTick() {
        float j = 0f;

        for (Function<PlayerEntity, Float> predicate: INSANITY_PREDICATES) {
            j += predicate.apply(player);
        }

        for (Function<LivingEntity, Boolean> predicate: MOBS_TO_INSANITY.keySet()) {
            InsanityRangePair pair = MOBS_TO_INSANITY.get(predicate);

            for (LivingEntity entity: player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(1.0 * pair.range, 0.5 * pair.range, 1.0 * pair.range))) {
                if (!predicate.apply(entity)) continue;

                float d = Math.max(0f, 1f - entity.distanceTo(player)/pair.range);
                j += d * pair.insanity;
            }
        }

        if (j != 0) {
            lowerPerMinute(j);
        }

        float attunedSanity = getAttunedSanity();
        if (sanityDamageTicks > 0 && attunedSanity > 25) {
            sanityDamageTicks--;
            EntityComponents.TORMENT.sync(player);
        } else if (sanityDamageTicks < SANITY_DAMAGE && attunedSanity <= 25) {
            sanityDamageTicks++;
            EntityComponents.TORMENT.sync(player);
        }
    }

    @Override
    public boolean shouldRender() {
        return player.getWorld().getDimensionKey() != ModDimensions.GATEWAY_TYPE_KEY;
    }

    @Override
    public boolean shouldOffsetRender() {
        return EntityComponents.MANA.get(player).shouldRender();
    }

    @Override
    public boolean isAttuned() {
        try {
            return (WorldComponents.BOSS_STATE.get(player.world.getScoreboard()).dragonSlain() && (player.world.getRegistryKey() == World.END)) || player.hasStatusEffect(ModStatusEffects.BRAINFREEZE);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static void registerInsanityMob(Function<LivingEntity, Boolean> predicate, float insanity, float range) {
        MOBS_TO_INSANITY.put(predicate, new InsanityRangePair(insanity, range));
    }

    /**
     * Registers a predicate that removes a certain amount of sanity per minute based on conditions.
     * @param predicate The function used to determine the amount of sanity lowered.
     */
    public static void registerPredicate(Function<PlayerEntity, Float> predicate) {
        INSANITY_PREDICATES.add(predicate);
    }

    public static void registerPredicate(Function2<PlayerEntity, TormentComponent, Float> predicate) {
        registerPredicate((player) -> predicate.apply(player, EntityComponents.TORMENT.get(player)));
    }

    private static class InsanityRangePair {
        public final float insanity;
        public final float range;

        private InsanityRangePair(float insanity, float range) {
            this.insanity = insanity;
            this.range = range;
        }
    }
}
