package net.eman3600.dndreams.mixin_interfaces;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;

public interface HudAccess {
    void setDragonFlash(int ticks);

    enum CustomHeartType {

        NO_CHANGE(0),
        AFFLICTION(1),
        MORTAL(2),
        MORTAL_CONTAINER(2, true),
        REJUVENATION(3),
        ROT(4);

        private final int id;
        private final boolean container;

        CustomHeartType(int id, boolean container) {
            this.id = id;
            this.container = container;
        }

        CustomHeartType(int id) {
            this(id, false);
        }

        public static CustomHeartType fromPlayerState(PlayerEntity player, InGameHud.HeartType type) {
            if (player == null) return NO_CHANGE;

            if (type == InGameHud.HeartType.CONTAINER) return player.hasStatusEffect(ModStatusEffects.MORTAL) ? MORTAL_CONTAINER : NO_CHANGE;
            if (type == InGameHud.HeartType.ABSORBING) return NO_CHANGE;

            return player.hasStatusEffect(ModStatusEffects.MORTAL) ? MORTAL : player.hasStatusEffect(ModStatusEffects.REJUVENATION) ? REJUVENATION : NO_CHANGE;
        }

        public int getU() {
            return id * 9;
        }

        public int getV(boolean half, boolean blinking, boolean hardcore) {
            if (container) {
                return blinking ? 81 : 72;
            }
            else {
                int v = 0;

                if (half) v += 9;
                if (blinking) v += 18;
                if (hardcore) v += 36;

                return v;
            }
        }
    }
}
