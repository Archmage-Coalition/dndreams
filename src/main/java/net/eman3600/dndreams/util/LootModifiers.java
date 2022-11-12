package net.eman3600.dndreams.util;

import net.eman3600.dndreams.Initializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

public class LootModifiers {
    private static final Identifier SCULK_SENSOR_ID
            = new Identifier("minecraft", "blocks/sculk_sensor");
    private static final Identifier SCULK_CATALYST_ID
            = new Identifier("minecraft", "blocks/sculk_catalyst");
    private static final Identifier SCULK_SHRIEKER_ID
            = new Identifier("minecraft", "blocks/sculk_shrieker");
    private static final Identifier GRASS_ID
            = new Identifier("minecraft", "blocks/grass");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, manager, id, supplier, setter) -> {

            /*
            * SCULK SENSOR
            * */
            if(SCULK_SENSOR_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(Initializer.MODID, "injections/sculk_sensor")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            /*
             * SCULK CATALYST
             * */
            if(SCULK_CATALYST_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(Initializer.MODID, "injections/sculk_catalyst")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            /*
             * SCULK SHRIEKER
             * */
            if(SCULK_SHRIEKER_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(Initializer.MODID, "injections/sculk_shrieker")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            if(GRASS_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(Initializer.MODID, "injections/grass")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

        });
    }

    private static void supplyPools(LootTable.Builder supplier, LootPool[] pools) {
        for (LootPool pool: pools) {
            supplier.pool(pool);
        }
    }
}
