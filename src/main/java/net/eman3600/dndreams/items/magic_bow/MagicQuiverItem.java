package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class MagicQuiverItem extends TooltipItem {

    private static final StatusEffectInstance[] RANDOM_EFFECTS = new StatusEffectInstance[] {
            new StatusEffectInstance(StatusEffects.POISON, 200),
            new StatusEffectInstance(StatusEffects.WEAKNESS, 240),
            new StatusEffectInstance(StatusEffects.WITHER, 140),
            new StatusEffectInstance(StatusEffects.SLOWNESS, 240),
            new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200),
            new StatusEffectInstance(StatusEffects.LEVITATION, 140),
            new StatusEffectInstance(StatusEffects.BLINDNESS, 200),
            new StatusEffectInstance(ModStatusEffects.MORTAL, 240),
            new StatusEffectInstance(ModStatusEffects.VOID_FLOW, 140),
            new StatusEffectInstance(ModStatusEffects.SUPPRESSED, 200),
            new StatusEffectInstance(ModStatusEffects.IMPENDING),
            new StatusEffectInstance(ModStatusEffects.IMMOLATION, 1, 1),
            new StatusEffectInstance(ModStatusEffects.HEARTBLEED, 140)
    };
    private static final float TIP_CHANCE = .7f;

    public MagicQuiverItem(Settings settings) {
        super(settings);

        this.withTooltip("tooltip.dndreams.magic_quiver", 1);
    }

    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        ArrowEntity arrowEntity = new ArrowEntity(world, shooter);

        ItemStack modelArrow;

        if (world.random.nextFloat() < TIP_CHANCE) {
            modelArrow = new ItemStack(Items.TIPPED_ARROW);
            int effect = world.random.nextInt(RANDOM_EFFECTS.length);
            StatusEffectInstance instance = new StatusEffectInstance(RANDOM_EFFECTS[effect]);
            DefaultedList<StatusEffectInstance> effectsList = DefaultedList.ofSize(1, instance);

            PotionUtil.setCustomPotionEffects(modelArrow, effectsList);

        } else {
            modelArrow = Items.ARROW.getDefaultStack();
        }
        arrowEntity.initFromStack(modelArrow);
        return arrowEntity;
    }

    public boolean canAfford(PlayerEntity user, ItemStack stack) {
        return true;
    }

    public void spendCost(PlayerEntity user, ItemStack stack) {

    }
}
