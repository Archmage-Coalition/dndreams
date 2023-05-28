package net.eman3600.dndreams.initializers.basics;

import net.eman3600.dndreams.staves.prongs.AmethystStaffProng;
import net.eman3600.dndreams.staves.setup.*;
import net.minecraft.item.Items;

public class ModStaves {

    public static final StaffCore DIAMOND = register("diamond", new StaffCore(Items.DIAMOND, 1561, 3, 7, 3f, 1.2f));

    public static final StaffHandle MANAGOLD = register("managold", new StaffHandle(ModItems.MANAGOLD_INGOT, 0, 1f));

    public static final StaffProng AMETHYST = register("amethyst", new AmethystStaffProng());



    public static void registerStaves() {}

    private static StaffCore register(String id, StaffCore core) {
        return StaffCoreRegistry.register(id, core);
    }

    private static StaffHandle register(String id, StaffHandle handle) {
        return StaffHandleRegistry.register(id, handle);
    }

    private static StaffProng register(String id, StaffProng prong) {
        return StaffProngRegistry.register(id, prong);
    }
}
