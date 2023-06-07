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


            if (trinket.isEquipped(ModItems.FLEETFOOT_BAND)) return true;
        }

        return player.hasStatusEffect(ModStatusEffects.GRACE);
    }

    static boolean hasAerialMovement(LivingEntity entity) {
        return entity instanceof PlayerEntity player && hasAerialMovement(player);
    }
}
