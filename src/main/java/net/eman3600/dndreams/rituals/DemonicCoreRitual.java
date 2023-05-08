package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.rituals.setup.Ritual;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DemonicCoreRitual extends Ritual {
    public DemonicCoreRitual() {
        super(500, new Ring(Ring.INNER_RING, CandleTuning.NETHER));
    }

    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        List<PassiveEntity> passives = world.getNonSpectatingEntities(PassiveEntity.class, new Box(-5, -1, -5, 5, 1, 5).offset(pos));
        for (PassiveEntity passive: passives) {
            if (passive.getHealth() <= 16f) {
                passive.damage(DamageSource.MAGIC, 30);
                ItemStack stack = new ItemStack(ModItems.DEMONIC_CORE_CHARGED);
                Vec3d vec = Vec3d.ofCenter(pos);

                ItemEntity entity = new ItemEntity(world, vec.x, vec.y + 1, vec.z, stack);

                world.spawnEntity(entity);
                return true;
            }
        }

        return false;
    }
}
