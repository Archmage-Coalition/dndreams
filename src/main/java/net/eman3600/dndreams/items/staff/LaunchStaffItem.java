package net.eman3600.dndreams.items.staff;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.eman3600.dndreams.mob_effects.ModStatusEffect;
import net.eman3600.dndreams.networking.packet_s2c.MotionUpdatePacket;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaunchStaffItem extends TooltipItem implements ManaCostItem {

    public LaunchStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getBaseManaCost() {
        return 3;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(getTooltipMana(stack));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (canAffordMana(user, stack)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);

        int charge = Math.min((getMaxUseTime(stack) - remainingUseTicks)/10, 4);

        if (charge > 0 && user instanceof PlayerEntity player) {

            ManaComponent component = EntityComponents.MANA.get(player);
            int mana = component.getMana();
            int cost = getManaCost(stack);
            charge = Math.min(charge, mana/cost);

            if (!world.isClient) {
                Vec3d vec = user.getRotationVecClient();
                vec = vec.normalize().multiply(charge * .7);
                player.setVelocity(vec);
                player.fallDistance = 0;
                MotionUpdatePacket.send((ServerPlayerEntity) player);
                component.useMana(charge * cost);
                player.getItemCooldownManager().set(this, user.hasStatusEffect(ModStatusEffects.GRACE) ? 60 : 16);
                if (!player.isCreative()) stack.damage(1, user, p -> p.sendToolBreakStatus(player.getActiveHand()));
            }
        }
    }

    @Override
    public boolean allowThrifty() {
        return false;
    }
}
