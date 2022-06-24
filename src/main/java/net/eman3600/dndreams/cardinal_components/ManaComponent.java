package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.ManaComponentI;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class ManaComponent implements ManaComponentI, AutoSyncedComponent {
    private int mana = 50;
    private int maxMana = 50;
    private int infusion = 0;
    private int regenTime = 0;
    private PlayerEntity player;

    private final int MAX_XP_BONUS = 50;
    private final int BASE_RATE = 8;
    private final int REGEN_REQUIRE = 60;
    private final int MIN_REGEN = -120;


    public ManaComponent(PlayerEntity playerIn) {
        player = playerIn;
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public int getBaseManaMax() {
        return maxMana;
    }

    @Override
    public int getInfusion() {
        return infusion;
    }

    @Override
    public void setInfusion(int change) {
        infusion = change;
    }

    @Override
    public void tick() {
        if (mana < getManaMax()) {
            regenerate();
        } else if (regenTime != 0) {
            regenTime = 0;
        }
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
        return BASE_RATE;
    }

    @Override
    public int getManaMax() {
        return getBaseManaMax() + getXPBonus();
    }

    @Override
    public int getXPBonus() {
        return Math.min(player.experienceLevel/2, MAX_XP_BONUS);
    }

    @Override
    public boolean useMana(int cost) {
        if (cost > mana) {
            return false;
        } else {
            mana -= cost;
            EntityComponents.MANA.sync(player);
            return true;
        }
    }

    public void chargeMana(int charge) {
        mana += charge;
        if (mana > getManaMax()) {
            mana = getManaMax();
        }
        EntityComponents.MANA.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        mana = tag.getInt("mana");
        maxMana = tag.getInt("max_mana");
        infusion = tag.getInt("infusion");
        regenTime = tag.getInt("regen_time");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("mana", mana);
        tag.putInt("max_mana", maxMana);
        tag.putInt("infusion", infusion);
        tag.putInt("regen_time", regenTime);
    }
}
