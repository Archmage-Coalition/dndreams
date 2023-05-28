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
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

import java.util.List;

public class DnDreamsREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<WeavingShapelessDisplay> WEAVING_SHAPELESS = CategoryIdentifier.of(new Identifier(Initializer.MODID, "weaving_shapeless"));
    public static final CategoryIdentifier<WeavingShapedDisplay> WEAVING_SHAPED = CategoryIdentifier.of(new Identifier(Initializer.MODID, "weaving_shaped"));
    public static final CategoryIdentifier<TransmutationDisplay> TRANSMUTATION = CategoryIdentifier.of(new Identifier(Initializer.MODID, "transmutation"));
    public static final CategoryIdentifier<SmokestackDisplay> SMOKESTACK = CategoryIdentifier.of(new Identifier(Initializer.MODID, "smokestack"));
    public static final CategoryIdentifier<MutandisDisplay> MUTANDIS = CategoryIdentifier.of(new Identifier(Initializer.MODID, "mutandis"));
    public static final CategoryIdentifier<RefineryDisplay> REFINERY = CategoryIdentifier.of(new Identifier(Initializer.MODID, "refinery"));
    public static final CategoryIdentifier<CauldronDisplay> CAULDRON = CategoryIdentifier.of(new Identifier(Initializer.MODID, "cauldron"));
    public static final CategoryIdentifier<RitualDisplay> RITUAL = CategoryIdentifier.of(new Identifier(Initializer.MODID, "ritual"));
    public static final CategoryIdentifier<ApothecaryDisplay> APOTHECARY = CategoryIdentifier.of(new Identifier(Initializer.MODID, "apothecary"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new SmokestackCategory());
        registry.addWorkstations(SMOKESTACK, SmokestackCategory.ICON);
        registry.addWorkstations(SMOKESTACK, EntryStacks.of(Blocks.SMOKER));
        registry.add(new RefineryCategory());
        registry.addWorkstations(REFINERY, RefineryCategory.ICON);
        registry.add(new CauldronCategory());
        registry.addWorkstations(CAULDRON, CauldronCategory.ICON);
        registry.add(new ApothecaryCategory());
        registry.addWorkstations(APOTHECARY, ApothecaryCategory.ICON);
        registry.add(new RitualCategory());
        registry.addWorkstations(RITUAL, RitualCategory.ICON);
        registry.addWorkstations(RITUAL, EntryStacks.of(ModBlocks.ECHO_CANDLE));

        registry.add(new MutandisCategory());
        registry.addWorkstations(MUTANDIS, MutandisCategory.ICON);
        registry.addWorkstations(MUTANDIS, EntryStacks.of(ModItems.MUTANDIS_EXTREMIS));
        registry.addWorkstations(MUTANDIS, EntryStacks.of(ModItems.MUTANDIS_ONEIROS));

        registry.add(new WeavingShapedCategory());
        registry.addWorkstations(WEAVING_SHAPED, WeavingShapedCategory.ICON);
        registry.add(new WeavingShapelessCategory());
        registry.addWorkstations(WEAVING_SHAPELESS, WeavingShapelessCategory.ICON);
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
        registry.registerFiller(WeavingShapedRecipe.class, WeavingShapedDisplay::new);
        registry.registerFiller(WeavingShapelessRecipe.class, WeavingShapelessDisplay::new);
        registry.registerFiller(TransmutationRecipe.class, TransmutationDisplay::new);
        registry.registerFiller(SmokestackRecipe.class, SmokestackDisplay::new);
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



        //registry.endReload();
    }
}
