package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.fluids.FlowingSpiritFluid;
import net.eman3600.dndreams.fluids.block.FlowingSpiritBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModFluids {
    public static FlowableFluid FLOWING_FLOWING_SPIRIT = register("flowing_flowing_spirit", new FlowingSpiritFluid.Flowing());
    public static FlowableFluid STILL_FLOWING_SPIRIT = register("flowing_spirit", new FlowingSpiritFluid.Still());

    public static final TagKey<Fluid> FLOWING_SPIRIT_TAG = registerTag("flowing_spirit");

    public static Block FLOWING_SPIRIT_BLOCK = ModBlocks.registerBlock("flowing_spirit", new FlowingSpiritBlock(STILL_FLOWING_SPIRIT, FabricBlockSettings.copy(Blocks.WATER).luminance(state -> 7)));
    public static Item FLOWING_SPIRIT_BUCKET = ModItems.registerItem("flowing_spirit_bucket", new BucketItem(STILL_FLOWING_SPIRIT, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).recipeRemainder(Items.BUCKET)));

    private static <T extends Fluid> T register(String id, T value) {
        return Registry.register(Registry.FLUID, new Identifier(Initializer.MODID, id), value);
    }

    private static TagKey<Fluid> registerTag(String id) {
        return TagKey.of(Registry.FLUID_KEY, new Identifier(Initializer.MODID, id));
    }

    public static void registerFluids() {
        System.out.println("Registering fluids for " + Initializer.MODID);
    }
}
