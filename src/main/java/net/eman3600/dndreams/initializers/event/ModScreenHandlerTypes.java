package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.screens.AttunementScreenHandler;
import net.eman3600.dndreams.screens.RefineryScreenHandler;
import net.eman3600.dndreams.screens.WeavingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlerTypes {
    public static final ScreenHandlerType<WeavingScreenHandler> WEAVING = register("weaving", WeavingScreenHandler::new);
    public static final ScreenHandlerType<AttunementScreenHandler> ATTUNEMENT = register("attunement", AttunementScreenHandler::new);
    public static final ScreenHandlerType<RefineryScreenHandler> REFINERY = register("refinery",RefineryScreenHandler::new);

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registry.SCREEN_HANDLER, new Identifier(Initializer.MODID, id), new ScreenHandlerType<>(factory));
    }

    public static void registerTypes() {
        System.out.println("Registering screen handler types for " + Initializer.MODID);
    }
}
