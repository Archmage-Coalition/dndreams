package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.BloodMoonComponentI;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.WorldAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

public class BloodMoonComponent implements BloodMoonComponentI, AutoSyncedComponent {
    private int chance = 0;
    private long knownDay = -1;
    private boolean damnedNight = false;
    private boolean notifiedClients = false;

    private final World world;

    private static final int CUMULATIVE_CHANCE = 3;
    private static final int HARD_CUMULATIVE_CHANCE = 5;

    public BloodMoonComponent(World world) {
        this.world = world;
        if (world != null && world.getLevelProperties() != null && world.getLevelProperties().isHardcore()) chance = 100;
    }

    private RegistryKey<DimensionType> getDimensionKey() {
        return world.getDimensionKey();
    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public long getKnownDay() {
        return knownDay;
    }

    @Override
    public void setDamnedNight(boolean damnedNight) {
        this.damnedNight = damnedNight;
    }

    @Override
    public boolean damnedNight() {
        if (getDimensionKey() == DimensionTypes.OVERWORLD) {
            return damnedNight;
        }
        return getDimensionKey() == ModDimensions.DREAM_TYPE_KEY;
    }

    @Override
    public void serverTick() {
        long day = world.getTimeOfDay() / 24000;
        if (knownDay != day) {
            knownDay = day;
            if (damnedNight) {
                chance = 0;
                damnedNight = false;
            } else if (world.random.nextInt(100) < chance) {
                damnedNight = true;
            } else {
                chance += (world.getScoreboard() != null && WorldComponents.BOSS_STATE.get(world.getScoreboard()).dragonSlain()) ? HARD_CUMULATIVE_CHANCE : CUMULATIVE_CHANCE;
            }
        }
        if (notifiedClients != isBloodMoon() && world.getRegistryKey() == World.OVERWORLD) {
            notifiedClients = isBloodMoon();
            if (notifiedClients) ((ServerWorld)world).getServer().getPlayerManager().getPlayerList().forEach(player -> player.sendMessageToClient(Text.translatable("message.dndreams.blood_moon"), false));
            System.out.println("The current time is " + world.getTimeOfDay());
        }
        WorldComponents.BLOOD_MOON.sync(world);
    }

    public boolean isBloodMoon() {
        return damnedNight() && ((WorldAccess)world).isTrulyNight();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        chance = tag.getInt("chance");
        knownDay = tag.getLong("known_day");
        damnedNight = tag.getBoolean("damned_night");
        notifiedClients = tag.getBoolean("notified_clients");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("chance", chance);
        tag.putLong("known_day", knownDay);
        tag.putBoolean("damned_night", damnedNight);
        tag.putBoolean("notified_clients", notifiedClients);
    }
}
