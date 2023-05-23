package net.eman3600.dndreams.entities.mobs;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;

public interface SanityEntity {

    String WOVEN_KEY = "Woven";
    String CORPOREAL_KEY = "Corporeal";

    boolean isWoven();
    boolean isCorporeal();

    boolean canView(PlayerEntity player);

    /**
     * Calculates the opacity that the specified player should see the tormentor at.
     * @param player The player seeing the tormentor.
     * @return The opacity, between 0.0 and 1.0. This is used in an alpha channel for transparency.
     */
    float renderedOpacity(PlayerEntity player);
    /**
     * Calculates the clarity (brightness) that the specified player should see the tormentor at.
     * @param player The player seeing the tormentor.
     * @return The clarity, between 0.0 and 1.0. This is used in RGB channels for multiplying color.
     */
    float renderedClarity(PlayerEntity player);

    default TormentComponent getTorment(PlayerEntity player) {
        return EntityComponents.TORMENT.get(player);
    }
    default float getSanity(PlayerEntity player) {
        return getTorment(player).getAttunedSanity();
    }
}
