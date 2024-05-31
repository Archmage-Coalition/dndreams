package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class MagicQuiverItem extends TooltipItem {

    public MagicQuiverItem(Settings settings) {
        super(settings);

        this.withTooltip("tooltip.dndreams.magic_quiver", 1);
    }

    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        ArrowEntity arrowEntity = new ArrowEntity(world, shooter);
        arrowEntity.initFromStack(Items.ARROW.getDefaultStack());
        arrowEntity.setPierceLevel((byte) 1);
        return arrowEntity;
    }

    public boolean canAfford(PlayerEntity user, ItemStack stack) {
        return true;
    }

    public void spendCost(PlayerEntity user, ItemStack stack) {

    }
}
