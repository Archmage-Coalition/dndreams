package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.BiPredicate;

public abstract class MagicBowItem extends BowItem {
    public static final BiPredicate<ItemStack, PlayerEntity> QUIVERS = (stack, player) -> stack.isIn(ModTags.QUIVERS) && stack.getItem() instanceof MagicQuiverItem item && item.canAfford(player, stack);

    public MagicBowItem(Settings settings) {
        super(settings);
    }

    public BiPredicate<ItemStack, PlayerEntity> getQuivers() {
        return QUIVERS;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            boolean bl = playerEntity.getAbilities().creativeMode;
            if (canAfford(playerEntity, stack) || bl) {

                int i = this.getMaxUseTime(stack) - remainingUseTicks;
                float f = getBowPullProgress(i);
                if (!((double)f < 0.1D) && !world.isClient) {

                    if (!bl) {
                        payAmmo(playerEntity, stack);
                    }

                    ItemStack quiverStack = getQuiver(playerEntity, stack);

                    MagicQuiverItem quiverItem = (MagicQuiverItem)(quiverStack.getItem() instanceof MagicQuiverItem ? quiverStack.getItem() : null);
                    PersistentProjectileEntity persistentProjectileEntity = quiverItem == null ? createDefaultArrow(world, stack, playerEntity) : quiverItem.createArrow(world, quiverStack, playerEntity);
                    persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, f * 3.0F, 1.0F);
                    if (f == 1.0F) {
                        persistentProjectileEntity.setCritical(true);
                    }

                    int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                    if (j > 0) {
                        persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double)j * 0.5D + 0.5D);
                    }

                    int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                    if (k > 0) {
                        persistentProjectileEntity.setPunch(k);
                    }

                    if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                        persistentProjectileEntity.setOnFireFor(100);
                    }

                    if (!bl && quiverItem != null) quiverItem.spendCost(playerEntity, quiverStack);

                    stack.damage(1, playerEntity, (p) -> {
                        p.sendToolBreakStatus(playerEntity.getActiveHand());
                    });
                    persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;

                    world.spawnEntity(persistentProjectileEntity);

                    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = canAfford(user, itemStack);
        if (!user.getAbilities().creativeMode && !bl) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    public abstract int pullTime();

    protected abstract boolean canAfford(PlayerEntity player, ItemStack stack);
    protected abstract void payAmmo(PlayerEntity player, ItemStack stack);

    public final float getBowPullProgress(int useTicks) {
        float f = (float)useTicks / pullTime();
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public abstract PersistentProjectileEntity createDefaultArrow(World world, ItemStack stack, LivingEntity shooter);

    public static ItemStack getQuiver(PlayerEntity player, ItemStack stack) {
        if (!(stack.getItem() instanceof MagicBowItem bow)) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = findQuiver(player, bow.getQuivers());
        if (!itemStack.isEmpty()) {
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack findQuiver(PlayerEntity entity, BiPredicate<ItemStack, PlayerEntity> predicate) {
        if (predicate.test(entity.getStackInHand(Hand.OFF_HAND), entity)) {
            return entity.getStackInHand(Hand.OFF_HAND);
        }
        if (predicate.test(entity.getStackInHand(Hand.MAIN_HAND), entity)) {
            return entity.getStackInHand(Hand.MAIN_HAND);
        }
        for (int i = 0; i < entity.getInventory().size(); ++i) {
            ItemStack stack = entity.getInventory().getStack(i);
            if (!predicate.test(stack, entity)) continue;
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
