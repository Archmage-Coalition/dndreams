package net.eman3600.dndreams.items.managold;

import net.eman3600.dndreams.entities.projectiles.ManagoldArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ManagoldArrowItem
        extends ArrowItem {
    public ManagoldArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new ManagoldArrowEntity(world, shooter);
    }
}
