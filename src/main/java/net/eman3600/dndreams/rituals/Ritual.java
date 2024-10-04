package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.eman3600.dndreams.recipes.RitualRecipe;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;

public abstract class Ritual {
    private final RegistryEntry.Reference<Ritual> entry;


    public Ritual() {
        this.entry = RitualRegistry.REGISTRY.createEntry(this);
    }

    public RegistryEntry.Reference<Ritual> reference() {
        return entry;
    }

    public static Ritual ofID(Identifier id) {
        return RitualRegistry.REGISTRY.get(id);
    }

    public String getTranslationKey() {
        return Util.createTranslationKey("ritual", RitualRegistry.REGISTRY.getId(this));
    }

    public String toString() {
        return RitualRegistry.REGISTRY.getId(this).getPath();
    }


    /**
     * Called when the ritual is cast by the Bonfire.
     * @return Whether the casting succeeded and should consume the materials.
     * */
    public abstract boolean onCast(ServerWorld world, BlockPos pos, BonfireBlockEntity blockEntity, RitualRecipe recipe);

    /**
     * Called after a ritual fails to get the error message.
     * @return The translation key for the error message.
     */
    public String getErrorTranslation(ServerWorld world, BlockPos pos, BonfireBlockEntity blockEntity, RitualRecipe recipe) {
        return "ritual.dndreams.error.improper";
    }
}
