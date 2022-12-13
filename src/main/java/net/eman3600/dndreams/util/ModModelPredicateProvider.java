package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.items.magic_bow.MagicBow;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    public static void registerModModels() {
        registerBow(ModItems.MANASTRING_BOW);
        registerBow(ModItems.MINDSTRING_BOW);
        registerBow(ModItems.LIGHTSTRING_BOW);

        registerBloodlustItem(ModItems.CORRUPT_SWORD);
        registerBloodlustItem(ModItems.CORRUPT_PICKAXE);
        registerBloodlustItem(ModItems.CORRUPT_AXE);
        registerBloodlustItem(ModItems.CORRUPT_SHOVEL);
        registerBloodlustItem(ModItems.CORRUPT_HOE);

        registerShockedItem(ModItems.TESLA_SABER);

        registerItem(new Identifier("bound"),
                (stack, world, entity, seed) -> (stack.getOrCreateNbt().contains("bound")) ? 1f : 0f,
                ModItems.TAGLOCK);
    }


    private static void registerItem(Identifier model, UnclampedModelPredicateProvider provider, Item... items) {
        for (Item item: items) {
            ModelPredicateProviderRegistry.register(item, model, provider);
        }
    }

    private static void registerBow(Item bow) {
        if (bow instanceof MagicBow bowItem)
            registerBow(bowItem);
    }

    private static void registerBow(MagicBow bow) {
        ModelPredicateProviderRegistry.register(bow, new Identifier("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0f;
                    }
                    if (entity.getActiveItem() != stack) {
                        return 0.0f;
                    }
                    return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / bow.pullTime();
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

    private static void registerShockedItem(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("shocked"),
                ((stack, world, entity, seed) -> {
                    if (entity != null && EntityComponents.SHOCK.isProvidedBy(entity)) {
                        return EntityComponents.SHOCK.get(entity).hasShock() ? 1f : 0f;
                    }
                    return 0f;
                }));
    }

    private static void registerChargedItem(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("charge"),
                ((stack, world, entity, seed) -> {
                    NbtCompound nbt = stack.getNbt();
                    return (nbt != null && nbt.contains("charge")) ? nbt.getInt("charge") : 0f;
                }));
    }
}
