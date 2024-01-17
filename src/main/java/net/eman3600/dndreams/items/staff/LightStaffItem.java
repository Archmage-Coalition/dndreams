package net.eman3600.dndreams.items.staff;

import net.eman3600.dndreams.entities.projectiles.GlowBoltEntity;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightStaffItem extends TooltipItem implements ManaCostItem {
    public LightStaffItem(Settings settings) {
        super(settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (canAffordMana(user, stack)) {

            spendMana(user, stack);

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.7f, 1.3f);

            user.getItemCooldownManager().set(this, 10);
            if (!user.isCreative()) stack.damage(1, user, p -> p.sendToolBreakStatus(hand));

            if (!world.isClient) {
                GlowBoltEntity bolt = new GlowBoltEntity(user, world);
                bolt.setVelocity(user, Math.max(user.getPitch() - 7.5f, -90f), user.getYaw(), 0.0f, 1.4f, 0.3f);
                world.spawnEntity(bolt);

                Criteria.SHOT_CROSSBOW.trigger((ServerPlayerEntity) user, stack);
            }

            return TypedActionResult.success(stack, world.isClient());
        }

        return super.use(world, user, hand);
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
    public int getEnchantability() {
        return 15;
    }
}
