package net.eman3600.dndreams.util;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class LootModifiers {

    private static Identifier buildInjectionRoute(Identifier id) {
        return new Identifier(MODID, "injections/" + id.getNamespace() + "/" + id.getPath());
    }

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, manager, id, supplier, setter) -> {
            LootTable table = manager.getTable(buildInjectionRoute(id));
            if (table != LootTable.EMPTY) {
                LootPool[] pools = table.pools;

                if (pools != null) {
                    for (LootPool pool : pools) {
                        supplier.pool(pool);
                    }
                }
            }

        });
    }
}
