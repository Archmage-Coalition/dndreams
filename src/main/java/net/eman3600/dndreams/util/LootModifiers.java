package net.eman3600.dndreams.util;

import net.eman3600.dndreams.Initializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class LootModifiers {
    /*private static final Identifier SCULK_SENSOR_ID
            = new Identifier("minecraft", "blocks/sculk_sensor");
    private static final Identifier SCULK_CATALYST_ID
            = new Identifier("minecraft", "blocks/sculk_catalyst");
    private static final Identifier SCULK_SHRIEKER_ID
            = new Identifier("minecraft", "blocks/sculk_shrieker");
    private static final Identifier GRASS_ID
            = new Identifier("minecraft", "blocks/grass");
    private static final Identifier SEAGRASS_ID
            = new Identifier("minecraft", "blocks/seagrass");
    private static final Identifier TALL_SEAGRASS_ID
            = new Identifier("minecraft", "blocks/tall_seagrass");
    private static final Identifier WITHER_ID
            = new Identifier("minecraft", "entities/wither");
    private static final Identifier BASTION_TREASURE_ID
            = new Identifier("minecraft", "chests/bastion_treasure");*/

    private static Identifier buildInjectionRoute(Identifier id) {
        return new Identifier(MODID, "injections/" + id.getNamespace() + "/" + id.getPath());
    }

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, manager, id, supplier, setter) -> {
            LootTable table = manager.getTable(buildInjectionRoute(id));
            if (table != LootTable.EMPTY) {
                LootPool[] pools = table.pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            /*if(SCULK_SENSOR_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(MODID, "injections/sculk_sensor")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            if(SCULK_CATALYST_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(MODID, "injections/sculk_catalyst")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            if(SCULK_SHRIEKER_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(MODID, "injections/sculk_shrieker")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            if(GRASS_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(MODID, "injections/grass")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            if(SEAGRASS_ID.equals(id) || TALL_SEAGRASS_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(MODID, "injections/seagrass")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }

            if(WITHER_ID.equals(id)) {
                LootPool[] pools = manager.getTable(new Identifier(MODID, "injections/wither")).pools;

                if (pools != null)
                    supplyPools(supplier, pools);
            }*/

        });
    }

    private static void supplyPools(LootTable.Builder supplier, LootPool[] pools) {
        for (LootPool pool: pools) {
            supplier.pool(pool);
        }
    }
}
