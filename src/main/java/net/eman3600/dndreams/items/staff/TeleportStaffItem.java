package net.eman3600.dndreams.items.staff;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class TeleportStaffItem extends TooltipItem implements ManaCostItem {
    public TeleportStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        HitResult target = user.raycast(16, 0, false);

        if (canAffordMana(user, stack) && user.squaredDistanceTo(target.getPos()) <= 225) {

            spendMana(user, stack);

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.7f, 1.3f);

            user.getItemCooldownManager().set(this, 10);
            if (!user.isCreative()) stack.damage(1, user, p -> p.sendToolBreakStatus(hand));

            if (!world.isClient && target instanceof BlockHitResult blockTarget) {
                BlockPos pos = blockTarget.getBlockPos().offset(blockTarget.getSide(), blockTarget.getSide() == Direction.DOWN ? 2 : 1);
                Vec3d vec = new Vec3d(pos.getX() + .5, pos.getY(), pos.getZ() + .5);

                user.teleport(vec.x, vec.y, vec.z);

                world.playSound(null, vec.x, vec.y, vec.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.7f, 1.3f);

                Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(user);

                if (optional.isPresent() && optional.get().isEquipped(ModItems.DISSOCIATION_CHARM) && !user.hasStatusEffect(ModStatusEffects.DISCORDANT)) {
                    user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.DISCORDANT, 80));
                } else {
                    user.damage(DamageSource.FALL, 3.0f);
                }
            }

            return TypedActionResult.success(stack, world.isClient());
        }

        return super.use(world, user, hand);
    }

    @Override
    public int getBaseManaCost() {
        return 8;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(getTooltipMana(stack));
    }
}
