package net.eman3600.dndreams.entities.states.faceless;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.entities.states.MobState;
import net.minecraft.entity.player.PlayerEntity;

public interface FacelessState extends MobState<FacelessEntity> {

    boolean canView(PlayerEntity player);
    boolean shouldVanish();

    float renderedOpacity(PlayerEntity player);
    float renderedClarity(PlayerEntity player);
}
