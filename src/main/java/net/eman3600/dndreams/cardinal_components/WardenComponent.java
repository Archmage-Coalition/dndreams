package net.eman3600.dndreams.cardinal_components;

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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WardenComponent implements WardenComponentI {

    private final LivingEntity entity;
    private int spawnTicks = -1;
    private int dangerTicks = 0;
    private BlockPos shriekerPos = new BlockPos(0, 0, 0);
    private Identifier shriekerDimension = new Identifier("overworld");
    private boolean dirty = false;

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
                spawnTicks = dangerTicks > 0 ? 80 : 300;
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
        shriekerDimension = Identifier.tryParse(tag.getString("shrieker_dimension"));

        NbtList posList = tag.getList("shrieker_pos", NbtCompound.INT_TYPE);
        shriekerPos = new BlockPos(posList.getInt(0),posList.getInt(1),posList.getInt(2));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {

        tag.putInt("spawn_ticks", spawnTicks);
        tag.putInt("danger_ticks", dangerTicks);

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
