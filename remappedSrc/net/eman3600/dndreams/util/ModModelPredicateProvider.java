package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.items.mindstring_bow.MindstringBow;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    public static void registerModModels() {
        registerBow(ModItems.MINDSTRING_BOW);
        registerBow(ModItems.LIGHTSTRING_BOW);

        registerBloodlustItem(ModItems.CORRUPT_SWORD);
        registerBloodlustItem(ModItems.CORRUPT_PICKAXE);
        registerBloodlustItem(ModItems.CORRUPT_AXE);
        registerBloodlustItem(ModItems.CORRUPT_SHOVEL);
        registerBloodlustItem(ModItems.CORRUPT_HOE);
    }

    private static void registerBow(Item bow) {
        if (bow instanceof MindstringBow mindstringBow)
            registerBow(mindstringBow);
    }

    private static void registerBow(MindstringBow bow) {
        ModelPredicateProviderRegistry.register(bow, new Identifier("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0f;
                    }
                    if (entity.getActiveItem() != stack) {
                        return 0.0f;
                    }
                    return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / bow.pullProgressDivisor();
                });

        ModelPredicateProviderRegistry.register(bow, new Identifier("pulling"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem()
                        && entity.getActiveItem() == stack ? 1.0f : 0.0f);
    }



    private static void registerBloodlustItem(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("bloodlust"),
                (stack, world, entity, seed) -> {
                    if (item instanceof BloodlustItem lustItem && entity != null) {
                        return lustItem.hasBloodlust(entity) ? 1f : 0f;
                    }
                    return 0f;
                });
    }
}
