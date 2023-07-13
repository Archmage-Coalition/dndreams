package net.eman3600.dndreams.events;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyInputHandler {

    public static final String KEY_DODGE = "key.dndreams.dodge";

    public static KeyBinding dodgeKey;
    private static boolean holdingDodgeKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dodgeKey.isPressed() && client.player != null) {

                if (!holdingDodgeKey) {
                    holdingDodgeKey = true;
                    EntityComponents.INFUSION.get(client.player).tryDodgeClient();
                }
            } else if (holdingDodgeKey) {
                holdingDodgeKey = false;
            }
        });
    }

    public static void registerBindings() {

        dodgeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_DODGE, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, KeyBinding.MOVEMENT_CATEGORY));
    }
}
