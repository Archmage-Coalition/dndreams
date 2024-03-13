package net.eman3600.dndreams.entities.projectiles;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ManagoldArrowEntity extends PersistentProjectileEntity {

    public ManagoldArrowEntity(EntityType<? extends ManagoldArrowEntity> entityType, World world) {
        super((EntityType<? extends PersistentProjectileEntity>)entityType, world);
    }

    public ManagoldArrowEntity(World world, LivingEntity owner) {
        super(EntityType.ARROW, owner, world);
    }

    public ManagoldArrowEntity(World world, double x, double y, double z) {
        super(EntityType.ARROW, x, y, z, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.MANAGOLD_ARROW);
    }
}
