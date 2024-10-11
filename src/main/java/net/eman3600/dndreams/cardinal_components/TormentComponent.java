package net.eman3600.dndreams.cardinal_components;

import dev.emi.trinkets.api.TrinketsApi;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.TormentComponentI;
import net.eman3600.dndreams.entities.misc.ShadeRiftEntity;
import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.eman3600.dndreams.events.torment.FacelessEvent;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.event.ModCriterion;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.items.AtlasItem;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.util.Function2;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class TormentComponent implements TormentComponentI, AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    private static final int MAX_TENSION = 50;
    private float maxSanity = 100;
    private float sanity = 100;
    public static final float THREAD_VALUE = 3.5f;
    public static final int MAX_SHROUD = 60;
    public static final int MAX_HAUNT = 15;
    private int dragonFlashTicks = 0;
    /**
     * Haze refers to the nightmare haze, which causes a purple vignette to appear and prevents the passive healing of affliction.
     */
    private int hazeTicks = 0;
    /**
     * Shroud is the darkness of the Deep Dark.
     */
    private int shroud = 0;
    private int haunt = 0;
    private boolean truthActive = false;
    private boolean inStorm = false;
    private boolean dirty = false;

    private float staticFrame = 0;
    private static final float STATIC_FRAME_RATE = 0.5f;
    public static final int STATIC_FRAMES = 8;
    /**
     * When set to true, the next tick will see the nightmare haze increase instead of decrease. Should be set to true every tick when the haze needs to be maintained.
     */
    private boolean nightmareHaze = false;
    private int fearDrowning = 0;
    private int facelessCooldown = 20;
    private int tension = 0;
    @Nullable private Entity facelessEntity = null;
    @Nullable private UUID facelessID = null;

    private static final List<Function<PlayerEntity, Float>> INSANITY_PREDICATES = new ArrayList<>();
    private static final Map<Function<LivingEntity, Boolean>, InsanityRangePair> MOBS_TO_INSANITY = new HashMap<>();

    private static final List<FacelessEvent> FACELESS_EVENTS = new ArrayList<>();

    private final PlayerEntity player;

    public static final float MAX_SANITY = 100f;
    public static final float MIN_SANITY = 0f;
    public static final float LOWEST_MAX_SANITY = 30f;
    private static final int SANITY_DAMAGE = 15;

    public TormentComponent(PlayerEntity playerIn) {
        player = playerIn;

        if (player.world != null && player.world.getLevelProperties() != null && player.world.getLevelProperties().isHardcore()) setMaxSanity(82.5f);
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
    public void lowerSanity(float value) {
        if (player.hasStatusEffect(ModStatusEffects.BRAINFREEZE) || truthActive) return;

        sanity -= value;
        normalize();
    }

    @Override
    public void lowerMaxSanity(float value) {
        maxSanity -= value;

        if (value > 0) hazeTicks = SANITY_DAMAGE;

        normalize();
    }

    @Override
    public float getNightmareHaze() {
        return MathHelper.clamp((float) hazeTicks /SANITY_DAMAGE, 0, 1);
    }

    public int getMaxTormentors() {
        float effective = getAttunedSanity();

        return inStorm ? 20 : effective < 5 ? 10 : effective < 25 ? 6 : effective < 45 ? 2 : 0;
    }

    @Override
    public void inflictHaze(int increment) {
        hazeTicks = MathHelper.clamp(hazeTicks + increment, 0, SANITY_DAMAGE);
        markDirty();
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
        sanity = inStorm ? 0f : MathHelper.clamp(sanity, MIN_SANITY, getMaxSanity());
        markDirty();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        sanity = tag.getFloat("sanity");
        maxSanity = tag.getFloat("max_sanity");
        dragonFlashTicks = tag.getInt("dragon_flash_ticks");
        hazeTicks = tag.getInt("haze_ticks");
        shroud = tag.getInt("shroud");
        haunt = tag.getInt("haunt");
        facelessCooldown = tag.getInt("faceless_cooldown");
        tension = tag.getInt("tension");
        truthActive = tag.getBoolean("truth_active");
        inStorm = tag.getBoolean("in_storm");
        fearDrowning = tag.getInt("fear_drowning");
        if (tag.containsUuid("faceless")) {
            this.facelessID = tag.getUuid("faceless");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("sanity", sanity);
        tag.putFloat("max_sanity", maxSanity);
        tag.putInt("dragon_flash_ticks", dragonFlashTicks);
        tag.putInt("haze_ticks", hazeTicks);
        tag.putInt("shroud", shroud);
        tag.putInt("haunt", haunt);
        tag.putInt("faceless_cooldown", facelessCooldown);
        tag.putInt("tension", tension);
        tag.putBoolean("truth_active", truthActive);
        tag.putBoolean("in_storm", inStorm);
        tag.putInt("fear_drowning", fearDrowning);
        tag.putFloat("prevalence", getFacelessPrevalence());
        if (this.facelessID != null) {
            tag.putUuid("faceless", this.facelessID);
        }
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

        if (truthActive && nightmareHaze) {
            nightmareHaze = false;
            markDirty();
        }

        if (hazeTicks > 0 && !nightmareHaze) {
            hazeTicks--;
            markDirty();
        } else if (nightmareHaze) {
            nightmareHaze = false;
            inflictHaze(1);
        }

        if (truthActive && !player.getInventory().containsAny(stack -> stack.getItem() instanceof AtlasItem item && item.isActive(stack))) {
            setTruthActive(false);
        }

        if (inStorm) {
            RegistryKey<World> dim = player.world.getRegistryKey();
            DarkStormComponent storm = WorldComponents.DARK_STORM.get(player.world.getScoreboard());

            if ((dim != ModDimensions.DREAM_DIMENSION_KEY && dim != ModDimensions.HAVEN_DIMENSION_KEY) || isAttuned() || truthActive || storm.windStrength() < 0.9f) {
                setInStorm(false);
            }
        }

        boolean shouldShroud = shouldShroud();
        if (shroud < MAX_SHROUD && shouldShroud) {
            shroud++;
            markDirty();
        } else if (shroud > 0 && !shouldShroud) {
            shroud--;
            markDirty();
        }

        if (player.hasStatusEffect(ModStatusEffects.HAUNTED) && haunt < MAX_HAUNT) {
            haunt = Math.min(haunt + 5, MAX_HAUNT);
            markDirty();
        } else if (haunt > 0 && !player.hasStatusEffect(ModStatusEffects.HAUNTED)) {
            haunt--;
            markDirty();
        }

        Entity faceless = getFacelessEntity();

        if (faceless == null) {
            boolean bl = FacelessEntity.daylightAt(player.world, player.getBlockPos());

            if (bl && tension > 0) {
                tension = 0;
                markDirty();
            }
            if (facelessCooldown > 0) {
                facelessCooldown--;
                markDirty();
            }
        }

        if (fearDrowning > 0) {
            fearDrowning--;
            markDirty();
        }

        if (dirty) {
            EntityComponents.TORMENT.sync(player);
            ModCriterion.INSANITY.trigger((ServerPlayerEntity) player);
            dirty = false;
        }
    }

    @Override
    public void clientTick() {
        if (inStorm) {
            staticFrame = (staticFrame + STATIC_FRAME_RATE) % STATIC_FRAMES;
        }
    }

    public int getStaticFrame() {
        return (int) staticFrame;
    }

    @Override
    public boolean shouldOffsetRender() {
        return EntityComponents.MANA.get(player).shouldRender();
    }

    @Override
    public boolean isAttuned() {
        try {
            return !truthActive && (WorldComponents.BOSS_STATE.get(player.world.getScoreboard()).dragonSlain() && (player.world.getRegistryKey() == World.END)) || player.hasStatusEffect(ModStatusEffects.SPIRIT_WARD) || player.isCreative();
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean isAwakened() {
        try {
            return truthActive || player.hasStatusEffect(ModStatusEffects.THIRD_EYE) || TrinketsApi.getTrinketComponent(player).get().isEquipped(ModItems.TRUTH_GLASSES);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isInStorm() {
        return inStorm;
    }

    public void setInStorm(boolean inStorm) {
        this.inStorm = inStorm;
        markDirty();
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

    public boolean canFullyAfford(float cost) {
        return sanity >= cost;
    }

    @Override
    public void spendSanity(float cost) {
        sanity -= cost;

        if (sanity < -cost/2) {

            player.timeUntilRegen = 5;
            player.damage(DamageSourceAccess.INSANITY, MathHelper.ceil(-sanity));
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

        if (player.world instanceof ServerWorld serverWorld) {
            return inStorm || player.hasStatusEffect(ModStatusEffects.LOOMING) || shouldShroud(serverWorld, player.getBlockPos());
        }

        return false;
    }

    public static boolean shouldShroud(ServerWorld world, BlockPos pos) {
        //if (WorldComponents.BLOOD_MOON.get(world).isBloodMoon()) return true;
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

    public static boolean canSpawnShade(ServerWorld world, BlockPos pos) {
        Box searchRegion = Box.from(Vec3d.of(pos)).expand(30, 30, 30);

        return world.getNonSpectatingEntities(ShadeRiftEntity.class, searchRegion).isEmpty();
    }

    public static boolean canSpawnSanityMob(ServerWorld world, BlockPos pos, Class<Entity> type, Function<TormentComponent, Integer> maxAllowed) {
        int allowed = 0;
        int current = 0;
        Box searchRegion = Box.from(Vec3d.of(pos)).expand(120, 70, 120);

        for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, searchRegion)) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            allowed += maxAllowed.apply(torment);
        }

        current += world.getNonSpectatingEntities(type, searchRegion).size();

        return current < allowed;
    }

    public int getFacelessCooldown() {
        return facelessCooldown;
    }

    public void setFacelessCooldown(int facelessCooldown) {
        this.facelessCooldown = facelessCooldown;
        markDirty();
    }

    @Override
    public int getTension() {
        return tension;
    }

    @Override
    public int getEffectiveTension() {
        return player.hasStatusEffect(ModStatusEffects.LOOMING) ? MAX_TENSION : tension;
    }

    @Override
    public void addTension(int tension) {
        this.tension = MathHelper.clamp(tension + this.tension, 0, MAX_TENSION);
        normalize();
    }

    @Override
    public float getFacelessPrevalence() {
        float sanityScore = (90 - getAttunedSanity())/90;
        float t = getEffectiveTension() * 0.002f * (1 + sanityScore * 2);

        sanityScore *= sanityScore/3;

        return MathHelper.clamp(sanityScore + t, 0.002f, 0.6f);
    }

    @Nullable
    public Entity getFacelessEntity() {
        if (facelessEntity != null && !facelessEntity.isRemoved()) return facelessEntity;
        if (facelessID != null && player.world instanceof ServerWorld world) {
            this.facelessEntity = world.getEntity(facelessID);
            return facelessEntity;
        }
        return null;
    }

    public void setFacelessEntity(@Nullable Entity entity) {
        if (entity != null) {
            this.facelessID = entity.getUuid();
            this.facelessEntity = entity;
            markDirty();
        }
    }

    public boolean isTruthActive() {
        return truthActive;
    }

    public void setTruthActive(boolean truthActive) {
        this.truthActive = truthActive;
        markDirty();
    }

    public boolean isFearDrowning() {
        return fearDrowning > 0;
    }

    public void setFearDrowning() {
        this.fearDrowning = 4;
        markDirty();
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

    public void markHaze() {
        nightmareHaze = true;
    }

    private List<FacelessEvent> getValidEvents() {
        List<FacelessEvent> validEvents = new ArrayList<>();
        float sanity = getAttunedSanity();

        for (FacelessEvent event : FACELESS_EVENTS) {
            if (event.canOccur(sanity)) {
                validEvents.add(event);
            }
        }

        return validEvents;
    }

    public int triggerFacelessEvent() {

        if (player.world.isClient) return 0;

        List<FacelessEvent> validEvents = getValidEvents();

        if (validEvents.size() == 0) return 0;

        FacelessEvent event = validEvents.get(player.world.random.nextInt(validEvents.size()));

        return event.attempt(player, (ServerWorld) player.world, this);
    }

    static {
        FACELESS_EVENTS.add(FacelessEvent.OBSERVATION);
        FACELESS_EVENTS.add(FacelessEvent.ENGAGEMENT);
    }
}
