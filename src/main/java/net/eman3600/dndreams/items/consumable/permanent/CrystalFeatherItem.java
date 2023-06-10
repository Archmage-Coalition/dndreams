package net.eman3600.dndreams.items.consumable.permanent;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrystalFeatherItem extends Item {

    public CrystalFeatherItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);

        InfusionComponent infusion = EntityComponents.INFUSION.get(user);
        if (!infusion.hasDodge()) {
            infusion.setHasDodge(true);

            user.playSound(SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1f, 1f);

            stack.decrement(1);

            return TypedActionResult.success(stack, world.isClient);
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (world instanceof ClientWorldAccess access && access.getPlayer() != null) {

            InfusionComponent infusion = EntityComponents.INFUSION.get(access.getPlayer());

            tooltip.add(Text.translatable(getTranslationKey() + (infusion.hasDodge() ? ".tooltip.used" : ".tooltip")));
        }
    }
}
