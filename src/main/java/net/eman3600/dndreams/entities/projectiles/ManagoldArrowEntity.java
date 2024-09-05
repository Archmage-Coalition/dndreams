package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.networking.packet_s2c.ManagoldFlashPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ManagoldArrowEntity extends PersistentProjectileEntity {
    private static final double BLAST_RANGE = 5d;

    public ManagoldArrowEntity(EntityType<? extends ManagoldArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public ManagoldArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.MANAGOLD_ARROW, owner, world);
    }

    public ManagoldArrowEntity(World world, double x, double y, double z) {
        super(ModEntities.MANAGOLD_ARROW, x, y, z, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.MANAGOLD_ARROW);
    }

    private void burst() {
        Vec3d origin = getPos();
        Box box = Box.of(origin, BLAST_RANGE, BLAST_RANGE, BLAST_RANGE);

        int i = MathHelper.ceil(MathHelper.clamp(this.getVelocity().length() * getDamage() * .5, 0.0, 2.147483647E9));

        for (Entity target : world.getOtherEntities(this, box)) {

            target.timeUntilRegen = 0;
            target.damage(DamageSource.magic(this, getOwner()), i);
        }

        ManagoldFlashPacket.send((ServerWorld) world, origin);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        double d = getDamage();
        setDamage(d * .5);
        super.onEntityHit(entityHitResult);
        setDamage(d);

        if (!world.isClient)
            burst();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (!world.isClient)
            burst();
        kill();
    }
}
