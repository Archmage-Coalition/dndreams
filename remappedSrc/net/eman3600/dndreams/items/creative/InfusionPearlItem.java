package net.eman3600.dndreams.items.creative;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModInfusions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class InfusionPearlItem extends Item {
    public InfusionPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        InfusionComponent component = EntityComponents.INFUSION.get(user);

        if (component.getInfusion().world == world.getRegistryKey()) {
            component.setInfusion(ModInfusions.NONE);
        } else {
            if (Infusion.WORLDS_TO_INFUSIONS.containsKey(world.getRegistryKey())) {
                component.setInfusion(Infusion.WORLDS_TO_INFUSIONS.get(world.getRegistryKey()));
            } else {
                return TypedActionResult.pass(user.getStackInHand(hand));
            }
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
