package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.recipes.RitualRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DreadMoonRitual extends Ritual {

    @Override
    public String getErrorTranslation(ServerWorld world, BlockPos pos, BonfireBlockEntity blockEntity, RitualRecipe recipe) {
        if (world.getRegistryKey() == World.OVERWORLD) {
            return world.isNight() ? "ritual.dndreams.error.dread_moon" : "ritual.dndreams.error.damned_night";
        }

        return "ritual.dndreams.error.not_overworld";
    }

    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, BonfireBlockEntity blockEntity, RitualRecipe recipe) {

        if (world.getRegistryKey() != World.OVERWORLD) return false;

        BloodMoonComponent component = WorldComponents.BLOOD_MOON.get(world);
        if (component.damnedNight()) return false;

        component.setDamnedNight(true);
        component.resetChance();

        return true;
    }
}
