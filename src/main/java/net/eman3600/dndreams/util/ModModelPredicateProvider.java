package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.items.magic_bow.BloodyCarbineItem;
import net.eman3600.dndreams.items.magic_bow.MagicBowItem;
import net.eman3600.dndreams.items.magic_bow.MagicCrossbowItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    public static void registerModModels() {
        registerBow(ModItems.MANASTRING_BOW);
        registerBow(ModItems.MINDSTRING_BOW);
        registerBow(ModItems.BLOODY_CARBINE);

        registerBloodlustItem(ModItems.BLOODFLAME_VESSEL);

        registerRestoredItem(ModItems.CROWNED_EDGE);
        registerRestoredItem(ModItems.TRUE_CROWNED_EDGE);

        registerItem(new Identifier("bound"),
                (stack, world, entity, seed) -> (stack.hasNbt() && stack.getNbt().contains("bound")) ? 1f : 0f,
                ModItems.TAGLOCK);

        registerItem(new Identifier("form"),
                (stack, world, entity, seed) -> stack.hasNbt() ? (float)stack.getNbt().getInt("Form") * 0.1f : 0f,
                ModItems.ATLAS);

        registerItem(new Identifier("broken"),
                (stack, world, entity, seed) -> ElytraItem.isUsable(stack) ? 0.0f : 1.0f,
                ModItems.EVERGALE);
    }


    private static void registerItem(Identifier model, UnclampedModelPredicateProvider provider, Item... items) {
        for (Item item: items) {
            ModelPredicateProviderRegistry.register(item, model, provider);
        }
    }

    private static void registerBow(Item bow) {
        if (bow instanceof MagicBowItem bowItem)
            registerMagicBow(bowItem);
        else if (bow instanceof MagicCrossbowItem crossbowItem)
            registerMagicCrossbow(crossbowItem);
    }

    private static void registerMagicBow(MagicBowItem bow) {
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

    private static void registerMagicCrossbow(MagicCrossbowItem crossbow) {
        ModelPredicateProviderRegistry.register(crossbow, new Identifier("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0f;
                    }
                    if (entity.getActiveItem() != stack) {
                        return 0.0f;
                    }
                    return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / crossbow.pullTime(stack);
                });

        ModelPredicateProviderRegistry.register(crossbow, new Identifier("pulling"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem()
                        && entity.getActiveItem() == stack ? 1.0f : 0.0f);

        ModelPredicateProviderRegistry.register(crossbow, new Identifier("charged"),
                (stack, world, entity, seed) -> MagicCrossbowItem.isCharged(stack) ? 1 : 0);
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

    private static void registerRestoredItem(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("restored"),
                ((stack, world, entity, seed) -> {
                    NbtCompound nbt = stack.getNbt();
                    if (nbt != null && nbt.contains("restored") && nbt.getBoolean("restored")) {
                        return 1f;
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
