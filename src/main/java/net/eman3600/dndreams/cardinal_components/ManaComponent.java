package net.eman3600.dndreams.cardinal_components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.eman3600.dndreams.cardinal_components.interfaces.ManaComponentI;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.items.tormite.TormiteArmorItem;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.mob_effects.ModStatusEffect;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class ManaComponent implements ManaComponentI, AutoSyncedComponent {
    private static final int MAX_XP_BONUS = 25;
    private static final int REGEN_REQUIRE = 60;
    public static final int MANA_FRAMES = 324;

    private int mana = 0;
    private int regenTime = 0;
    private final PlayerEntity player;

    private float manaFrame = 0;

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

        /*if (player.hasStatusEffect(ModStatusEffects.DREAMY)) {
            regenRate *= 1.85f * Math.pow(1.3f, player.getStatusEffect(ModStatusEffects.DREAMY).getAmplifier());
        }*/
        if (player.getWorld().getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            regenRate *= 3f;
        }

        if (TormiteArmorItem.wornPieces(player) > 0) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            regenRate *= 1f + (TormiteArmorItem.wornPieces(player) * (1f - torment.getSanity()/100) * .375f);
        }

        return (int)regenRate;
    }

    @Override
    public int getManaMax() {
        if (player.hasStatusEffect(ModStatusEffects.LIFEMANA)) {
            return (int)(player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH) * 2);
        }

        return Math.max(getBaseManaMax() + getXPBonus(), 0);
    }

    @Override
    public int getXPBonus() {
        if (getBaseManaMax() <= 0) {
            return 0;
        }
        return Math.min(player.experienceLevel/2, MAX_XP_BONUS);
    }

    @Override
    public boolean canAfford(int cost) {
        return player.isCreative() || getMana() >= cost;
    }

    @Override
    public boolean shouldRender() {
        return getManaMax() > 0 && !player.hasStatusEffect(ModStatusEffects.LIFEMANA);
    }

    @Override
    public void useMana(int cost) {
        if (player.isCreative()) {
            return;
        }

        if (player.hasStatusEffect(ModStatusEffects.LIFEMANA)) {
            player.timeUntilRegen = 1;
            player.damage(DamageSourceAccess.AFFLICTION, (float)cost/2);
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
        if (player.hasStatusEffect(ModStatusEffects.VOID_FLOW) && mana > getManaMax()) {
            player.timeUntilRegen = 1;
            player.damage(ModStatusEffect.COSMIC_OVERLOAD, 0.3f * ((float)mana - getManaMax()));
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        mana = tag.getInt("mana");
        regenTime = tag.getInt("regen_time");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("mana", mana);
        tag.putInt("regen_time", regenTime);
    }

    private float framesPerTick() {

        return Math.min(getRegenRate() / 16f, 3f);
    }

    @Override
    public int getManaFrame() {
        return (int)manaFrame;
    }

    @Override
    public void clientTick() {

        manaFrame += framesPerTick();
        manaFrame %= MANA_FRAMES;
    }
}
