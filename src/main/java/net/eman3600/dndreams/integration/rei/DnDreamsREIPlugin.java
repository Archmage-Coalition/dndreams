package net.eman3600.dndreams.integration.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.basics.ModFluids;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.integration.rei.categories.*;
import net.eman3600.dndreams.integration.rei.display.*;
import net.eman3600.dndreams.items.consumable.MutandisExtremisItem;
import net.eman3600.dndreams.items.consumable.MutandisItem;
import net.eman3600.dndreams.items.consumable.MutandisOneirosItem;
import net.eman3600.dndreams.recipes.*;
import net.eman3600.dndreams.screens.slot.AttunementBurnSlot;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import java.util.List;

public class DnDreamsREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<AttunementDisplay> ATTUNEMENT = CategoryIdentifier.of(new Identifier(Initializer.MODID, "attunement"));
    public static final CategoryIdentifier<TransmutationDisplay> TRANSMUTATION = CategoryIdentifier.of(new Identifier(Initializer.MODID, "transmutation"));
    public static final CategoryIdentifier<MutandisDisplay> MUTANDIS = CategoryIdentifier.of(new Identifier(Initializer.MODID, "mutandis"));
    public static final CategoryIdentifier<WeavingDisplay> WEAVING = CategoryIdentifier.of(new Identifier(Initializer.MODID, "weaving"));
    public static final CategoryIdentifier<RefineryDisplay> REFINERY = CategoryIdentifier.of(new Identifier(Initializer.MODID, "refinery"));
    public static final CategoryIdentifier<CauldronDisplay> CAULDRON = CategoryIdentifier.of(new Identifier(Initializer.MODID, "cauldron"));
    public static final CategoryIdentifier<RitualDisplay> RITUAL = CategoryIdentifier.of(new Identifier(Initializer.MODID, "ritual"));
    public static final CategoryIdentifier<ApothecaryDisplay> APOTHECARY = CategoryIdentifier.of(new Identifier(Initializer.MODID, "apothecary"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new RefineryCategory());
        registry.addWorkstations(REFINERY, RefineryCategory.ICON);
        registry.add(new WeavingCategory());
        registry.addWorkstations(WEAVING, WeavingCategory.ICON);
        registry.add(new CauldronCategory());
        registry.addWorkstations(CAULDRON, CauldronCategory.ICON);
        registry.add(new ApothecaryCategory());
        registry.addWorkstations(APOTHECARY, ApothecaryCategory.ICON);
        registry.add(new RitualCategory());
        registry.addWorkstations(RITUAL, RitualCategory.ICON);

        registry.add(new AttunementCategory());
        registry.addWorkstations(ATTUNEMENT, AttunementCategory.ICON);

        registry.add(new MutandisCategory());
        registry.addWorkstations(MUTANDIS, MutandisCategory.ICON);
        registry.addWorkstations(MUTANDIS, EntryStacks.of(ModItems.MUTANDIS_EXTREMIS));
        registry.addWorkstations(MUTANDIS, EntryStacks.of(ModItems.MUTANDIS_ONEIROS));

        registry.add(new TransmutationCategory());
        registry.addWorkstations(TRANSMUTATION, EntryStacks.of(ModFluids.STILL_FLOWING_SPIRIT));



        //registry.endReload(ReloadStage.END);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(MUTANDIS, MutandisDisplay.serializer());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(TransmutationRecipe.class, TransmutationDisplay::new);
        registry.registerFiller(WeavingRecipe.class, WeavingDisplay::new);
        registry.registerFiller(RefineryRecipe.class, RefineryDisplay::new);
        registry.registerFiller(CauldronRecipe.class, CauldronDisplay::new);
        registry.registerFiller(RitualRecipe.class, RitualDisplay::new);
        registry.registerFiller(ApothecaryRecipe.class, ApothecaryDisplay::new);

        for (List<Block> blocks: MutandisItem.mutables.values()) {
            for (Block out: blocks) {
                registry.add(new MutandisDisplay(EntryStacks.of(blocks.get(0)), EntryStacks.of(out)));
            }
        }
        for (Block out: MutandisExtremisItem.extremeMutables) {
            registry.add(new MutandisDisplay(EntryStacks.of(ModItems.MUTANDIS_EXTREMIS), EntryStacks.of(out)));
        }
        for (Block out: MutandisOneirosItem.fullMutables) {
            registry.add(new MutandisDisplay(EntryStacks.of(ModItems.MUTANDIS_ONEIROS), EntryStacks.of(out)));
        }

        for (ItemConvertible item : AttunementBurnSlot.ITEM_TO_ENERGY.keySet()) {
            registry.add(new AttunementDisplay(EntryStacks.of(item), AttunementBurnSlot.ITEM_TO_ENERGY.getOrDefault(item, 0)));
        }



        //registry.endReload();
    }
}
