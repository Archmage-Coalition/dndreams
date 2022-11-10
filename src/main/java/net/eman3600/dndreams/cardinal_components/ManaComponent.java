package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.ManaComponentI;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class ManaComponent implements ManaComponentI, AutoSyncedComponent {
    private final int MAX_XP_BONUS = 50;
    private final int REGEN_REQUIRE = 60;
    private final int RENDER_LINGER = 60;
    private final int MIN_REGEN = -120;

    private int mana = 0;
    private int regenTime = 0;
    private int renderTime = RENDER_LINGER;
    private final PlayerEntity player;

    public ManaComponent(PlayerEntity playerIn) {
        player = playerIn;
    }

    @Override
    public int getMana() {
        if (player.hasStatusEffect(ModStatusEffects.LIFEMANA)) {
            return (int)(player.getHealth() * 2);
        }
        return mana;
    }

    @Override
    public int getBaseManaMax() {
        return (int)player.getAttributeValue(ModAttributes.PLAYER_MAX_MANA);
    }

    @Override
    public void serverTick() {
        if (player.hasStatusEffect(ModStatusEffects.LIFEMANA)) {
            return;
        }

        if (mana < getManaMax() || player.hasStatusEffect(ModStatusEffects.VOID_FLOW)) {
            regenerate();
        } else if (regenTime != 0) {
            regenTime = 0;
        }

        if (mana < getManaMax()) {
            renderTime = RENDER_LINGER;
        } else if (renderTime > 0) {
            renderTime--;
        }

        if (mana > getManaMax() && !player.hasStatusEffect(ModStatusEffects.VOID_FLOW)) {
            mana = getManaMax();
        }

        EntityComponents.MANA.sync(player);
    }

    private void regenerate() {
        regenTime += getRegenRate();
        if (regenTime >= getRegenRequirement()) {
            chargeMana(1);
            regenTime -= getRegenRequirement();
        }
    }

    private int getRegenRequirement() {
        return REGEN_REQUIRE;
    }

    @Override
    public int getRegenRate() {
        float regenRate = (float)player.getAttributeValue(ModAttributes.PLAYER_MANA_REGEN);

        if (player.isCreative()) {
            return REGEN_REQUIRE - regenTime;
        }
        else if (player.hasStatusEffect(ModStatusEffects.SUPPRESSED)) {
            return 0;
        }

        if (player.hasStatusEffect(ModStatusEffects.DREAMY)) {
            regenRate *= 1.85f * Math.pow(1.3f, player.getStatusEffect(ModStatusEffects.DREAMY).getAmplifier());
        }
        if (player.getWorld().getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            regenRate *= 3f;
        }

        return (int)regenRate;
    }

    @Override
    public int getManaMax() {
        int manaFactors = 0;

        if (player.hasStatusEffect(ModStatusEffects.LIFEMANA)) {
            return (int)(player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH) * 2);
        }

        if (player.hasStatusEffect(ModStatusEffects.MEMORY)) {
            manaFactors += 15 * (player.getStatusEffect(ModStatusEffects.MEMORY).getAmplifier() + 1);
        }

        return Math.max(getBaseManaMax() + getXPBonus() + manaFactors, 1);
    }

    @Override
    public int getXPBonus() {
        return Math.min(player.experienceLevel/2, MAX_XP_BONUS);
    }

    public boolean shouldRender() {
        return renderTime > 0;
    }

    public void reRender() {
        renderTime = RENDER_LINGER;
    }

    @Override
    public void useMana(int cost) {
        if (player.hasStatusEffect(ModStatusEffects.LIFEMANA)) {
            player.timeUntilRegen = 1;
            player.damage(DamageSource.MAGIC, (float)cost/2);
            return;
        }
        mana = Math.max(0, mana - cost);
        regenTime = Math.min(regenTime, -getRegenRequirement());
        reRender();
    }

    @Override
    public void setMana(int value) {
        mana = value;
        reRender();
    }

    public void chargeMana(int charge) {
        mana += charge;
        if (mana > getManaMax() && !player.hasStatusEffect(ModStatusEffects.VOID_FLOW)) {
            mana = getManaMax();
        }
        if (player.hasStatusEffect(ModStatusEffects.VOID_FLOW) && mana > getManaMax() && player.canTakeDamage()) {
            player.damage(DamageSource.MAGIC, 0.3f * ((float)mana - getManaMax()));
        }
        reRender();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        mana = tag.getInt("mana");
        regenTime = tag.getInt("regen_time");
        renderTime = tag.getInt("render_time");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("mana", mana);
        tag.putInt("regen_time", regenTime);
        tag.putInt("render_time", renderTime);
    }
}
