package net.eman3600.dndreams.items.misc_tool;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class ChargebackItem extends TooltipItem {

    private static final int COOLDOWN = 18;

    public ChargebackItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        InfusionComponent component = EntityComponents.INFUSION.get(user);
        
        if (!world.isClient()) {
            component.startParry();

            if (!user.isCreative())
                user.getItemCooldownManager().set(this, COOLDOWN);
        }

        return TypedActionResult.success(stack);
    }
}
