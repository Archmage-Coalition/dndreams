package net.eman3600.dndreams.cardinal_components;

import dev.emi.trinkets.api.TrinketsApi;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.TormentComponentI;
import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.util.Function2;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Function;

public class TormentComponent implements TormentComponentI, AutoSyncedComponent, ServerTickingComponent {
    private float maxSanity = 100;
    private float sanity = 100;
    public static final float THREAD_VALUE = 3.5f;
    public static final int MAX_SHROUD = 60;
    public static final int MAX_HAUNT = 15;
    private int dragonFlashTicks = 0;
    private int sanityDamageTicks = 0;
    private int shroud = 0;
    private int haunt = 0;
    private boolean dirty = false;
    private int gloom = 0;
    private int gloomTicks = 0;
    private float trueHp = 20.0f;
    public static final UUID gloomId = new UUID(0xade4f29f0a684049L, 0xbcb4bb8d300edac8L);

    private static final List<Function<PlayerEntity, Float>> INSANITY_PREDICATES = new ArrayList<>();
    private static final Map<Function<LivingEntity, Boolean>, InsanityRangePair> MOBS_TO_INSANITY = new HashMap<>();

    private final PlayerEntity player;

    public static final float MAX_SANITY = 100f;
    public static final float MIN_SANITY = 0f;
    public static final float LOWEST_MAX_SANITY = 30f;
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
        return isAttuned() ? MAX_SANITY : isAwakened() ? 0 : getSanity();
    }

    public float getTrueMaxSanity() {
        return maxSanity;
    }

    @Override
    public float getMaxSanity() {
        return MathHelper.clamp(maxSanity, 0, MAX_SANITY);
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
    public void inflictGloom(int value) {

        gloomTicks = 60;
        if (player.getAbsorptionAmount() > 0) {
            float remainder = value - player.getAbsorptionAmount();

            if (remainder >= 1) {
                player.setAbsorptionAmount(0f);
                gloom += remainder;
                updateGloom();
            } else {
                player.damage(DamageSourceAccess.GLOOM, value);
            }
            return;
        }

        gloom += value;
        updateGloom();
    }

    @Override
    public int getGloom() {
        return gloom;
    }

    @Override
    public float getTrueHp() {
        return trueHp;
    }

    @Override
    public void setGloom(int value) {
        if (gloom < value) gloomTicks = 60;
        gloom = value;
        updateGloom();
    }

    public boolean shouldHealGloom() {
        return shroud <= 0;
    }

    @Override
    public void updateGloom() {
        EntityAttributeInstance instance = player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);

        if (instance != null) {

            instance.removeModifier(gloomId);

            float extra = player.getHealth() - ((float)instance.getValue() - gloom);

            if (gloom > 0) {

                instance.addPersistentModifier(new EntityAttributeModifier(gloomId, "gloom", -gloom, EntityAttributeModifier.Operation.ADDITION));
            }
            if (extra > 0) {

                player.timeUntilRegen = 0;
                player.damage(DamageSourceAccess.GLOOM, extra);
            }
        }

        markDirty();
    }


    @Override
    public void lowerSanity(float value) {
        if (player.hasStatusEffect(ModStatusEffects.BRAINFREEZE)) return;

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

    public int getMaxTormentors() {
        float effective = getAttunedSanity();

        return effective <= 5 ? 24 : effective <= 50 ? (int)(12f - (effective/5f)) : 0;
    }

    @Override
    public void damageSanity(int increment) {
        sanityDamageTicks = MathHelper.clamp(sanityDamageTicks + increment, 0, SANITY_DAMAGE);
    }

    @Override
    public boolean shearSanity(float value, boolean yield) {
        boolean bl = false;
        while (value >= THREAD_VALUE) {
            ItemStack stack;
            if (getTrueMaxSanity() - THREAD_VALUE >= LOWEST_MAX_SANITY && player.world.getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY) {
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
        maxSanity = MathHelper.clamp(maxSanity, LOWEST_MAX_SANITY, MAX_SANITY);
        sanity = MathHelper.clamp(sanity, MIN_SANITY, getMaxSanity());
        markDirty();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        sanity = tag.getFloat("sanity");
        maxSanity = tag.getFloat("max_sanity");
        dragonFlashTicks = tag.getInt("dragon_flash_ticks");
        sanityDamageTicks = tag.getInt("sanity_damage_ticks");
        shroud = tag.getInt("shroud");
        haunt = tag.getInt("haunt");
        gloom = tag.getInt("gloom");
        gloomTicks = tag.getInt("gloom_ticks");
        trueHp = tag.getFloat("true_hp");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("sanity", sanity);
        tag.putFloat("max_sanity", maxSanity);
        tag.putInt("dragon_flash_ticks", dragonFlashTicks);
        tag.putInt("sanity_damage_ticks", sanityDamageTicks);
        tag.putInt("shroud", shroud);
        tag.putInt("haunt", haunt);
        tag.putInt("gloom", gloom);
        tag.putInt("gloom_ticks", gloomTicks);
        trueHp = player.getMaxHealth();
        tag.putFloat("true_hp", trueHp);
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

        if (sanityDamageTicks > 0) {
            sanityDamageTicks--;
            markDirty();
        }


        boolean shouldShroud = shouldShroud();
        if (shroud < MAX_SHROUD && shouldShroud) {
            shroud++;
            markDirty();
        } else if (shroud > 0 && !shouldShroud) {
            shroud--;
            markDirty();
        }

        if (gloom > 0) {

            if (player.getAbsorptionAmount() > 0) {
                int g = gloom;
                gloom = 0;
                inflictGloom(g);
            } else if (gloomTicks > 0) {
                gloomTicks--;
                markDirty();
            } else if (shouldHealGloom()) {
                gloomTicks = 10;
                gloom--;
                updateGloom();
            }
        } else if (gloomTicks > 0) {

            gloomTicks = 0;
            markDirty();
        }

        if (trueHp != player.getMaxHealth()) {

            trueHp = player.getMaxHealth();
            markDirty();
        }

        if (player.hasStatusEffect(ModStatusEffects.HAUNTED) && haunt < MAX_HAUNT) {
            haunt = Math.min(haunt + 5, MAX_HAUNT);
            markDirty();
        } else if (haunt > 0 && !player.hasStatusEffect(ModStatusEffects.HAUNTED)) {
            haunt--;
            markDirty();
        }

        if (dirty) {
            EntityComponents.TORMENT.sync(player);
            dirty = false;
        }
    }

    @Override
    public boolean shouldOffsetRender() {
        return EntityComponents.MANA.get(player).shouldRender();
    }

    @Override
    public boolean isAttuned() {
        try {
            return (WorldComponents.BOSS_STATE.get(player.world.getScoreboard()).dragonSlain() && (player.world.getRegistryKey() == World.END)) || player.hasStatusEffect(ModStatusEffects.SPIRIT_WARD);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean isAwakened() {
        try {
            return player.hasStatusEffect(ModStatusEffects.THIRD_EYE) || TrinketsApi.getTrinketComponent(player).get().isEquipped(ModItems.TRUTH_GLASSES);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public int getShroud() {
        return shroud;
    }

    @Override
    public int getHaunt() {
        return haunt;
    }

    @Override
    public boolean canAfford(float cost) {
        return sanity >= cost/2;
    }

    @Override
    public void spendSanity(float cost) {
        sanity -= cost;

        if (sanity < -cost/2) {
            StatusEffectInstance effect = player.getStatusEffect(ModStatusEffects.LOOMING);

            int duration = 40 * MathHelper.ceil(-sanity);
            if (effect != null) {
                duration += effect.getDuration();
            }

            inflictGloom(MathHelper.ceil(-sanity));

            player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.LOOMING, duration, 0));
        }

        normalize();
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

    private boolean shouldShroud() {

        if (player.hasStatusEffect(ModStatusEffects.LOOMING)) return true;

        if (player.world instanceof ServerWorld serverWorld) {
            return shouldShroud(serverWorld, player.getBlockPos());
        }

        return false;
    }

    public static boolean shouldShroud(ServerWorld world, BlockPos pos) {
        BlockPos posStructure = world.locateStructure(ModTags.ENSHROUDED, pos, 100, false);
        BlockPos posBiome;
        try {
            posBiome = world.locateBiome(biome -> biome.isIn(ModTags.SHROUDED), pos, 30, 20, 20).getFirst();
        } catch (NullPointerException e) {
            posBiome = null;
        }

        return (posStructure != null && posStructure.isWithinDistance(pos, 80)) || (posBiome != null && posBiome.isWithinDistance(pos, 30));
    }

    public static boolean canSpawnTormentor(ServerWorld world, BlockPos pos) {
        int allowed = 0;
        int current = 0;
        Box searchRegion = Box.from(Vec3d.of(pos)).expand(120, 70, 120);

        for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, searchRegion)) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            allowed += torment.getMaxTormentors();
        }

        current += world.getNonSpectatingEntities(TormentorEntity.class, searchRegion).size();

        return current < allowed;
    }

    private static class InsanityRangePair {
        public final float insanity;
        public final float range;

        private InsanityRangePair(float insanity, float range) {
            this.insanity = insanity;
            this.range = range;
        }
    }

    public void markDirty() {
        dirty = true;
    }
}
