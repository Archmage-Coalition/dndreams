package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.ManaComponentI;
import net.eman3600.dndreams.infusions.Infusion;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModAttributes;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class ManaComponent implements ManaComponentI, AutoSyncedComponent {
    private int mana = 0;
    private Infusion infusion = Infusion.NONE;
    private int regenTime = 0;
    private final PlayerEntity player;

    private final int MAX_XP_BONUS = 50;
    private final int REGEN_REQUIRE = 60;
    private final int MIN_REGEN = -120;


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
    public Infusion getInfusion() {
        return infusion;
    }

    @Override
    public void setInfusion(Infusion change) {
        infusion = change;
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

        return Math.max(getBaseManaMax() + getXPBonus() + infusion.manaBonus() + manaFactors, 1);
    }

    @Override
    public int getXPBonus() {
        return Math.min(player.experienceLevel/2, MAX_XP_BONUS);
    }

    @Override
    public void useMana(int cost) {
        if (player.hasStatusEffect(ModStatusEffects.LIFEMANA)) {
            player.damage(DamageSource.MAGIC, (float)cost/2);
            return;
        }
        mana = Math.max(0, mana - cost);
        regenTime = Math.min(regenTime, -getRegenRequirement());
    }

    @Override
    public void setMana(int value) {
        mana = value;
    }

    public void chargeMana(int charge) {
        mana += charge;
        if (mana > getManaMax() && !player.hasStatusEffect(ModStatusEffects.VOID_FLOW)) {
            mana = getManaMax();
        }
        if (player.hasStatusEffect(ModStatusEffects.VOID_FLOW) && mana > getManaMax() && player.canTakeDamage()) {
            player.damage(DamageSource.MAGIC, 0.3f * ((float)mana - getManaMax()));
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        mana = tag.getInt("mana");
        infusion = Infusion.getFromNum(tag.getInt("infusion"));
        regenTime = tag.getInt("regen_time");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("mana", mana);
        tag.putInt("infusion", infusion.getNum());
        tag.putInt("regen_time", regenTime);
    }
}
