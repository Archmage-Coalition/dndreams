package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.ReviveComponentI;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class ReviveComponent implements ReviveComponentI {

    /**
     * If the player can use revives
     */
    public static final float REGEN = 5f;

    private boolean enabled = false;
    private float vitality = 0;
    private int revivesUsed = 0;
    private int cooldown = 0;

    private boolean dirty = false;
    private final PlayerEntity player;

    public ReviveComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        markDirty();
    }

    @Override
    public int maxRevives() {
        return enabled ? Math.max((int)player.getAttributeValue(ModAttributes.PLAYER_REVIVAL), 0) : 0;
    }
    @Override
    public boolean canRevive() {
        return enabled && cooldown <= 0 && !player.hasStatusEffect(ModStatusEffects.MORTAL) && revivesUsed < maxRevives();
    }
    @Override
    public int remainingRevives() {
        return maxRevives() - revivesUsed;
    }
    @Override
    public boolean shouldDisplay() {
        return !player.hasStatusEffect(ModStatusEffects.MORTAL) && maxRevives() > 0;
    }
    @Override
    public boolean shouldOffsetRender() {
        return EntityComponents.MANA.get(player).shouldRender();
    }

    @Override
    public void revive() {
        revivesUsed++;
        cooldown = 1200;
        markDirty();
    }

    @Override
    public void addVitality(float amount) {
        vitality = MathHelper.clamp(vitality + amount, 0, enabled ? 100 : 0);
        markDirty();
    }
    @Override
    public void deathReset() {
        vitality = 0f;
        cooldown = 0;
        revivesUsed = 0;

        markDirty();
    }
    @Override
    public float getVitality() {
        return vitality;
    }
    @Override
    public float getDisplayedVitality() {
        return revivesUsed > 0 ? vitality : 100f;
    }
    @Override
    public boolean needsMoreVitality() {
        return enabled && revivesUsed > 0 && vitality < 100f;
    }

    @Override
    public boolean onCooldown() {
        return cooldown > 0;
    }

    private void markDirty() {
        dirty = true;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        enabled = tag.getBoolean("enabled");
        vitality = tag.getFloat("vitality");
        revivesUsed = tag.getInt("revives_used");
        cooldown = tag.getInt("cooldown");
    }
    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("enabled", enabled);
        tag.putFloat("vitality", vitality);
        tag.putInt("revives_used", revivesUsed);
        tag.putInt("cooldown", cooldown);
    }



    @Override
    public void serverTick() {
        if (cooldown > 0) {
            cooldown--;
            markDirty();
        }

        if (revivesUsed > 0) {
            addVitality(REGEN/1200);
            markDirty();
        }

        if (vitality >= 100 && revivesUsed > 0) {
            revivesUsed--;
            vitality = 0;
            markDirty();
        }

        if (vitality > 0 && revivesUsed <= 0) {
            vitality = 0;
            markDirty();
        }

        if (dirty) {
            EntityComponents.REVIVE.sync(player);
            dirty = false;
        }
    }
}
