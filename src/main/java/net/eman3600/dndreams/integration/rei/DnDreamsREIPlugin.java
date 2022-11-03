package net.eman3600.dndreams.integration.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.registry.ReloadStage;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.integration.rei.categories.SmokestackCategory;
import net.eman3600.dndreams.integration.rei.categories.TransmutationCategory;
import net.eman3600.dndreams.integration.rei.categories.WeavingShapedCategory;
import net.eman3600.dndreams.integration.rei.categories.WeavingShapelessCategory;
import net.eman3600.dndreams.integration.rei.display.SmokestackDisplay;
import net.eman3600.dndreams.integration.rei.display.TransmutationDisplay;
import net.eman3600.dndreams.integration.rei.display.WeavingShapedDisplay;
import net.eman3600.dndreams.integration.rei.display.WeavingShapelessDisplay;
import net.eman3600.dndreams.recipe.SmokestackRecipe;
import net.eman3600.dndreams.recipe.TransmutationRecipe;
import net.eman3600.dndreams.recipe.WeavingShapedRecipe;
import net.eman3600.dndreams.recipe.WeavingShapelessRecipe;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

public class DnDreamsREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<WeavingShapelessDisplay> WEAVING_SHAPELESS = CategoryIdentifier.of(new Identifier(Initializer.MODID, "weaving_shapeless"));
    public static final CategoryIdentifier<WeavingShapedDisplay> WEAVING_SHAPED = CategoryIdentifier.of(new Identifier(Initializer.MODID, "weaving_shaped"));
    public static final CategoryIdentifier<TransmutationDisplay> TRANSMUTATION = CategoryIdentifier.of(new Identifier(Initializer.MODID, "transmutation"));
    public static final CategoryIdentifier<SmokestackDisplay> SMOKESTACK = CategoryIdentifier.of(new Identifier(Initializer.MODID, "smokestack"));

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



        //registry.endReload(ReloadStage.END);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(WeavingShapedRecipe.class, WeavingShapedDisplay::new);
        registry.registerFiller(WeavingShapelessRecipe.class, WeavingShapelessDisplay::new);
        registry.registerFiller(TransmutationRecipe.class, TransmutationDisplay::new);
        registry.registerFiller(SmokestackRecipe.class, SmokestackDisplay::new);



        //registry.endReload();
    }
}
