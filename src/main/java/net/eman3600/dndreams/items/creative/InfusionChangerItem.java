package net.eman3600.dndreams.items.creative;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.infusions.Infusion;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class InfusionChangerItem extends Item {
    public InfusionChangerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user != null) {
            InfusionComponent infusion = EntityComponents.INFUSION.get(user);
            int i = infusion.getInfusion().getNum() + 1;
            infusion.setInfusion(Infusion.getFromNum(i));
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
