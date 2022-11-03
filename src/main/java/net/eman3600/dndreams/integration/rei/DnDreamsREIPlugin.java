package net.eman3600.dndreams.integration.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.registry.ReloadStage;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.integration.rei.categories.*;
import net.eman3600.dndreams.integration.rei.display.*;
import net.eman3600.dndreams.items.consumable.MutandisItem;
import net.eman3600.dndreams.recipe.SmokestackRecipe;
import net.eman3600.dndreams.recipe.TransmutationRecipe;
import net.eman3600.dndreams.recipe.WeavingShapedRecipe;
import net.eman3600.dndreams.recipe.WeavingShapelessRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public class DnDreamsREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<WeavingShapelessDisplay> WEAVING_SHAPELESS = CategoryIdentifier.of(new Identifier(Initializer.MODID, "weaving_shapeless"));
    public static final CategoryIdentifier<WeavingShapedDisplay> WEAVING_SHAPED = CategoryIdentifier.of(new Identifier(Initializer.MODID, "weaving_shaped"));
    public static final CategoryIdentifier<TransmutationDisplay> TRANSMUTATION = CategoryIdentifier.of(new Identifier(Initializer.MODID, "transmutation"));
    public static final CategoryIdentifier<SmokestackDisplay> SMOKESTACK = CategoryIdentifier.of(new Identifier(Initializer.MODID, "smokestack"));
    public static final CategoryIdentifier<MutandisDisplay> MUTANDIS = CategoryIdentifier.of(new Identifier(Initializer.MODID, "mutandis"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new WeavingShapedCategory());
        registry.addWorkstations(WEAVING_SHAPED, WeavingShapedCategory.ICON);
        registry.add(new WeavingShapelessCategory());
        registry.addWorkstations(WEAVING_SHAPELESS, WeavingShapelessCategory.ICON);
        registry.add(new TransmutationCategory());
        registry.addWorkstations(TRANSMUTATION, TransmutationCategory.ICON);
        registry.add(new SmokestackCategory());
        registry.addWorkstations(SMOKESTACK, SmokestackCategory.ICON);
        registry.addWorkstations(SMOKESTACK, EntryStacks.of(Blocks.SMOKER));
        registry.add(new MutandisCategory());
        registry.addWorkstations(MUTANDIS, MutandisCategory.ICON);



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

        for (Block in: MutandisItem.mutables) {
            for (Block out: MutandisItem.mutables) {
                if (in != out) {
                    registry.add(new MutandisDisplay(EntryStacks.of(in), EntryStacks.of(out)));
                }
            }
        }



        //registry.endReload();
    }
}
