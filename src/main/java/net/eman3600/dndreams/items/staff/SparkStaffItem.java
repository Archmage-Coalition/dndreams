package net.eman3600.dndreams.items.staff;

import net.eman3600.dndreams.entities.projectiles.SparkBoltEntity;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SparkStaffItem extends TooltipItem implements ManaCostItem, MagicDamageItem {

    public SparkStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (canAffordMana(user, stack)) {
            spendMana(user, stack);

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.7f, 1.3f);

            user.getItemCooldownManager().set(this, 7);
            if (!user.isCreative()) stack.damage(1, user, p -> p.sendToolBreakStatus(hand));

            if (!world.isClient) {
                SparkBoltEntity bolt = new SparkBoltEntity(user, world);
                bolt.setVelocity(user, Math.max(user.getPitch() - 7.5f, -90f), user.getYaw(), 0.0f, 1.2f, 0.3f);
                bolt.setDamage(getMagicDamage(stack));
                world.spawnEntity(bolt);
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
    public float getBaseMagicDamage() {
        return 4f;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(getTooltipMana(stack));
        tooltip.add(getTooltipMagicDamage(stack));
    }

    @Override
    public int getEnchantability() {
        return 15;
    }
}
