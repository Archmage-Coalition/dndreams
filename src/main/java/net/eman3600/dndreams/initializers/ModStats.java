package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStats {
    public static Identifier ENTER_DREAM = registerStat("enter_dream");

    private static Identifier registerStat(String name) {
        Identifier stat = Registry.register(Registry.CUSTOM_STAT, name, new Identifier(Initializer.MODID, name));
        Stats.CUSTOM.getOrCreateStat(stat, StatFormatter.DEFAULT);
        return stat;
    }

    public static void registerStats() {
        System.out.println("Registering stats for " + Initializer.MODID);
    }
}
