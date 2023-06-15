package net.eman3600.dndreams.mixin_interfaces;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;

public interface HudAccess {
    void setDragonFlash(int ticks);

    enum CustomHeartType {

        NO_CHANGE(0),
        AFFLICTION(1);

        private final int id;

        CustomHeartType(int id) {
            this.id = id;
        }

        public static CustomHeartType fromPlayerState(PlayerEntity player, InGameHud.HeartType type) {
            if (player == null) return NO_CHANGE;

            if (type == InGameHud.HeartType.CONTAINER) return NO_CHANGE;
            if (type == InGameHud.HeartType.ABSORBING) return NO_CHANGE;

            return player.hasStatusEffect(ModStatusEffects.AFFLICTION) ? AFFLICTION : NO_CHANGE;
        }

        public int getU() {
            return id * 9;
        }

        public int getV(boolean half, boolean blinking, boolean hardcore) {
            int v = 0;

            if (half) v += 9;
            if (blinking) v += 18;
            if (hardcore) v += 36;

            return v;
        }
    }
}
