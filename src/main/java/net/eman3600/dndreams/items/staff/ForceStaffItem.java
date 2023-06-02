package net.eman3600.dndreams.items.staff;

import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForceStaffItem extends TooltipItem implements ManaCostItem, MagicDamageItem {
    public static final double DISTANCE = 12;
    public static final double SQ = DISTANCE * DISTANCE;

    public ForceStaffItem(Settings settings) {
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
                Vec3d pos = user.getCameraPosVec(0);
                Vec3d angle = user.getRotationVec(1.0f).multiply(DISTANCE);
                Vec3d extent = pos.add(angle.x, angle.y, angle.z);
                Box box = user.getBoundingBox().stretch(angle).expand(1.0, 1.0, 1.0);

                EntityHitResult entityHitResult = ProjectileUtil.raycast(user, pos, extent, box, entity -> !entity.isSpectator() && entity.canHit(), SQ);

                if (entityHitResult != null && entityHitResult.getEntity() instanceof LivingEntity target) {
                    target.timeUntilRegen = 0;
                    target.damage(DamageSource.magic(user, user), getMagicDamage(stack));
                }

            }

            return TypedActionResult.success(stack, world.isClient());
        }

        return super.use(world, user, hand);
    }

    @Override
    public int getBaseManaCost() {
        return 5;
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
