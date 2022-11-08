package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.TormentComponentI;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class TormentComponent implements TormentComponentI, AutoSyncedComponent, ServerTickingComponent {
    private float torment = 0;
    private int dragonFlashTicks = 0;
    private boolean tormentRush = false;

    private final PlayerEntity player;

    public static final float MAX_TORMENT = 100;

    public TormentComponent(PlayerEntity playerIn) {
        player = playerIn;
    }

    @Override
    public float getTorment() {
        return torment;
    }

    @Override
    public void setTorment(float value) {
        torment = value;
        normalize();
    }

    @Override
    public void addTorment(float value) {
        torment += value;
        normalize();
    }

    @Override
    public void addPerSecond(float value) {
        addTorment(value/20);
    }

    @Override
    public void addPerMinute(float value) {
        addTorment(value/1200);
    }

    private void normalize() {
        if (player.hasStatusEffect(ModStatusEffects.SPIRIT_WARD) || (!WorldComponents.BOSS_STATE.get(player.getWorld().getScoreboard()).dragonSlain()
                && !player.getWorld().isClient()
                && player.getWorld().getDimensionKey() != ModDimensions.DREAM_TYPE_KEY)) {
            torment = 0;
        } else {
            torment = MathHelper.clamp(torment, 0, MAX_TORMENT);
        }
        EntityComponents.TORMENT.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        torment = tag.getFloat("torment");
        dragonFlashTicks = tag.getInt("dragon_flash_ticks");
        tormentRush = tag.getBoolean("torment_rush");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("torment", torment);
        tag.putInt("dragon_flash_ticks", dragonFlashTicks);
        tag.putBoolean("torment_rush", tormentRush);
    }

    @Override
    public void serverTick() {
        if (!terrorized() && torment > 0) {
            addPerMinute(-7.5f);
        }

        if (torment >= 90 && !tormentRush) {
            tormentRush = true;

            player.sendMessage(Text.translatable("message.dndreams.torment_rush"), true);
        } else if (tormentRush && torment < 10) {
            tormentRush = false;
        }
    }

    @Override
    public boolean terrorized() {
        return player.hasStatusEffect(ModStatusEffects.LOOMING) || player.world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY;
    }

    @Override
    public boolean isRushed() {
        return tormentRush;
    }
}
