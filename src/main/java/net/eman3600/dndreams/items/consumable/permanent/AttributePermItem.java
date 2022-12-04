package net.eman3600.dndreams.items.consumable.permanent;

import net.eman3600.dndreams.cardinal_components.PermItemComponent;
import net.eman3600.dndreams.cardinal_components.StatBoonComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AttributePermItem extends Item {
    private final EntityAttribute attribute;
    private final double modifier;

    public AttributePermItem(Settings settings, int maxUses, EntityAttribute attribute, double modifier) {
        super(settings);

        PermItemComponent.pairDefault(this, maxUses);

        this.attribute = attribute;
        this.modifier = modifier;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        PermItemComponent perms = EntityComponents.PERM_ITEM.get(user);

        if (perms.canUse(this) && world.getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY) {
            StatBoonComponent boons = EntityComponents.STAT_BOON.get(user);

            ItemStack stack = user.getStackInHand(hand);

            perms.decrement(this);
            Identifier id;
            if ((id = Registry.ATTRIBUTE.getId(attribute)) != null) {
                boons.increase(id, modifier);
            }

            if (!user.isCreative()) stack.decrement(1);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (world instanceof ClientWorldAccess access) {
            EntityComponents.PERM_ITEM.maybeGet(access.getPlayer()).ifPresent(perms -> {
                if (perms.canUse(this)) {
                    tooltip.add(Text.translatable(getTranslationKey(stack) + ".tooltip").formatted(Formatting.DARK_AQUA));
                } else {
                    tooltip.add(Text.translatable(getTranslationKey(stack) + ".tooltip.spent").formatted(Formatting.DARK_RED));
                }
            });
        }
    }
}
