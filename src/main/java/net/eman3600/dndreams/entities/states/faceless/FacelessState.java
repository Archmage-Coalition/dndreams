package net.eman3600.dndreams.entities.states.faceless;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.entities.states.MobState;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public interface FacelessState extends MobState<FacelessEntity> {

    boolean canView(PlayerEntity player);
    boolean shouldVanish();

    float renderedOpacity(PlayerEntity player);
    float renderedClarity(PlayerEntity player);

    @Nullable
    public String getNextState();

    default void onSeen() {}

    default float getSanity() {
        PlayerEntity player = getMob().getVictim();

        return player == null ? 100 : getMob().getSanity(player);
    }
}
