package net.eman3600.dndreams.mixin_interfaces;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;

import javax.annotation.Nullable;
import java.util.Optional;

public interface PlayerEntityAccess {
    @Nullable
    GameMode getGameMode();

    static boolean hasAerialMovement(PlayerEntity player) {

        Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);

        if (optional.isPresent()) {
            TrinketComponent trinket = optional.get();


            if (trinket.isEquipped(ModItems.FLEETFOOT_BAND) || trinket.isEquipped(ModItems.DRAGONFOOT_BAND) || trinket.isEquipped(ModItems.SKYSTEP_SOCKS)) return true;
        }

        return player.hasStatusEffect(ModStatusEffects.GRACE);
    }

    static boolean hasAerialMovement(LivingEntity entity) {
        return entity instanceof PlayerEntity player && hasAerialMovement(player);
    }

    static int getExperienceOf(int level) {
        if (level >= 32) {
            return (int)(4.5 * level * level - 162.5 * level + 2220);
        }
        if (level >= 17) {
            return (int)(2.5 * level * level - 40.5 + 360);
        }
        return level * level + 6 * level;
    }

    static int getExperienceBetween(int level, int excludedLevel) {

        return Math.max(0, getExperienceOf(level) - getExperienceOf(excludedLevel));
    }


}
