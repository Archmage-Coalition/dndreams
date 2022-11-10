package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.cardinal_components.DreamingComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManifestBrewItem extends Item {
    public ManifestBrewItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            player.playSound(SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1f, 1f);

            DreamingComponent component = EntityComponents.DREAMING.get(player);

            if (!component.isCongealed() && !world.isClient) {
                component.congeal();

                player.sendMessage(Text.translatable("item.dndreams.manifest_brew.use"), true);

                if (!player.isCreative()) {
                    player.setStackInHand(user.getActiveHand(), ItemUsage.exchangeStack(stack, player, Items.GLASS_BOTTLE.getDefaultStack()));
                }
            }
        }

        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!EntityComponents.DREAMING.get(user).isCongealed()) return ItemUsage.consumeHeldItem(world, user, hand);

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        try {
            if (world instanceof ClientWorldAccess access) {
                DreamingComponent component = EntityComponents.DREAMING.get(access.getClient().player);

                if (component.isCongealed()) {
                    tooltip.add(Text.translatable("item.dndreams.manifest_brew.tooltip_after"));
                    tooltip.add(Text.translatable("item.dndreams.manifest_brew.tooltip_after2"));
                } else if (access.getClient().player.getInventory().contains(stack)) {
                    tooltip.add(Text.translatable("item.dndreams.manifest_brew.tooltip_before"));
                    tooltip.add(Text.translatable("item.dndreams.manifest_brew.tooltip_before2"));
                } else {
                    tooltip.add(Text.literal("§3§kThe end is never the end"));
                    tooltip.add(Text.literal("§3§kEnd the suffering"));
                }
            }
        } catch (NullPointerException ignored) {}
    }
}
