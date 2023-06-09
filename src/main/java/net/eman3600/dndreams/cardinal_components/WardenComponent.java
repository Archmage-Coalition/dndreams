package net.eman3600.dndreams.cardinal_components;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.eman3600.dndreams.cardinal_components.interfaces.WardenComponentI;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WardenComponent implements WardenComponentI {

    public static int SPAWN_DELAY = 600;

    private final LivingEntity entity;
    private int spawnTicks = -1;
    private int dangerTicks = 0;
    private int warningDelay = 0;
    private BlockPos shriekerPos = new BlockPos(0, 0, 0);
    private Identifier shriekerDimension = new Identifier("overworld");
    private boolean dirty = false;
    private static final Int2ObjectMap<SoundEvent> WARNING_SOUNDS = Util.make(new Int2ObjectOpenHashMap<>(), warningSounds -> {
        warningSounds.put(2, SoundEvents.ENTITY_WARDEN_NEARBY_CLOSER);
        warningSounds.put(1, SoundEvents.ENTITY_WARDEN_NEARBY_CLOSEST);
        warningSounds.put(0, SoundEvents.ENTITY_WARDEN_NEARBY_CLOSEST);
    });

    public WardenComponent(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public int getSpawnTicks() {
        return spawnTicks;
    }

    @Override
    public int getDangerTicks() {
        return dangerTicks;
    }

    @Override
    public void alertShrieker(World world, BlockPos pos) {

        if (canSummon(world, pos)) {

            if (spawnTicks < 0) {
                spawnTicks = dangerTicks > 0 ? 120 : SPAWN_DELAY;
                warningDelay = 90;
            }
            shriekerPos = new BlockPos(pos);
            shriekerDimension = world.getRegistryKey().getValue();

            markDirty();
        }
    }

    private boolean canSummon(World world, BlockPos pos) {

        return world.getNonSpectatingEntities(WardenEntity.class, Box.from(Vec3d.of(pos)).expand(128, 64, 128)).size() < 1;
    }

    @Override
    public void neutralizeSpawn() {

        spawnTicks = -1;
        markDirty();
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public BlockPos getShriekerPos() {
        return shriekerPos;
    }

    @Override
    public Identifier getShriekerDimension() {
        return shriekerDimension;
    }

    private void spawnWarden() {

        if (shriekerDimension.equals(entity.world.getRegistryKey().getValue()) && entity.world instanceof ServerWorld server) {

            LargeEntitySpawnHelper.trySpawnAt(EntityType.WARDEN, SpawnReason.TRIGGERED, server, shriekerPos, 20, 5, 6, LargeEntitySpawnHelper.Requirements.WARDEN);
        }
    }

    @Override
    public void serverTick() {

        if (spawnTicks > 0) {
            spawnTicks--;
            warningDelay--;

            if (warningDelay <= 0) {

                World world = entity.getWorld();
                int t = (int)(spawnTicks * 3f / SPAWN_DELAY);
                warningDelay = world.random.nextBetween(40, 70) + (50 * t);

                SoundEvent soundEvent = WARNING_SOUNDS.get(t);
                if (soundEvent != null) {
                    BlockPos blockPos = shriekerPos;
                    int i = blockPos.getX() + MathHelper.nextBetween(world.random, -10 * t, 10 * t);
                    int j = blockPos.getY() + MathHelper.nextBetween(world.random, -10 * t, 10 * t);
                    int k = blockPos.getZ() + MathHelper.nextBetween(world.random, -10 * t, 10 * t);
                    world.playSound(null, i, j, k, soundEvent, SoundCategory.HOSTILE, 5.0f, 1.0f);
                }
            }

            if (spawnTicks == 0) {
                spawnWarden();
                dangerTicks = 18000;
                spawnTicks = -1;
            }

            markDirty();
        } else if (dangerTicks > 0) {

            dangerTicks--;
            markDirty();
        }

        if (dirty) {
            dirty = false;
            EntityComponents.WARDEN.sync(entity);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

        spawnTicks = tag.getInt("spawn_ticks");
        dangerTicks = tag.getInt("danger_ticks");
        warningDelay = tag.getInt("warning_delay");
        shriekerDimension = Identifier.tryParse(tag.getString("shrieker_dimension"));

        NbtList posList = tag.getList("shrieker_pos", NbtCompound.INT_TYPE);
        shriekerPos = new BlockPos(posList.getInt(0),posList.getInt(1),posList.getInt(2));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {

        tag.putInt("spawn_ticks", spawnTicks);
        tag.putInt("danger_ticks", dangerTicks);
        tag.putInt("warning_delay", warningDelay);

        tag.put("shrieker_pos", this.toNbtList(shriekerPos.getX(), shriekerPos.getY(), shriekerPos.getZ()));
        tag.putString("shrieker_dimension", shriekerDimension.toString());
    }

    private NbtList toNbtList(int... values) {
        NbtList nbtList = new NbtList();

        for (int d : values) {
            nbtList.add(NbtInt.of(d));
        }

        return nbtList;
    }
}
