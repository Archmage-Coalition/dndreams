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

    private static final int MAX_XP_BONUS = 50;


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

    public static int getManaMax(Entity entity) {
        return EntityComponents.MANA.get(entity).getBaseManaMax() + getXPBonus(entity);
    }

    public static int getXPBonus(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return Math.min(((PlayerEntity)entity).experienceLevel / 2, MAX_XP_BONUS);
        }
        return 0;
    }

    @Override
    public boolean useMana(int cost) {
        if (cost > mana) {
            return false;
        } else {
            mana -= cost;
            return true;
        }
    }

    public static boolean chargeMana(Entity entity, int charge) {
        ManaComponent component = EntityComponents.MANA.get(entity);
        component.mana += charge;
        if (component.mana > getManaMax(entity)) {
            component.mana = getManaMax(entity);
            return false;
        }
        return true;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        mana = tag.getInt("mana");
        maxMana = tag.getInt("max_mana");
        infusion = tag.getInt("infusion");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("mana", mana);
        tag.putInt("max_mana", maxMana);
        tag.putInt("infusion", infusion);
    }
}
