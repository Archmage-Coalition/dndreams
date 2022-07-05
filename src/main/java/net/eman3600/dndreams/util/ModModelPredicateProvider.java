package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.ModItems;
import net.eman3600.dndreams.items.mindstring_bow.MindstringBow;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    public static void registerModModels() {
        registerBow(ModItems.MINDSTRING_BOW);
        registerBow(ModItems.LIGHTSTRING_BOW);
    }

    private static void registerBow(Item bow) {
        if (bow instanceof MindstringBow)
            registerBow((MindstringBow) bow);
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
}
