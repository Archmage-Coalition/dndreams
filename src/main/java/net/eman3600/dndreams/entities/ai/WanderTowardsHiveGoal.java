package net.eman3600.dndreams.entities.ai;

import net.eman3600.dndreams.entities.mobs.ShamblerEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WanderTowardsHiveGoal extends WanderAroundGoal {

    private final ShamblerEntity shambler;

    public WanderTowardsHiveGoal(ShamblerEntity mob, double speed) {
        super(mob, speed);
        shambler = mob;
    }

    @Nullable
    @Override
    protected Vec3d getWanderTarget() {

        List<ShamblerEntity> possibilities = getNearbyShamblers(shambler.hiveGoal.getHiveSize());

        if (possibilities.size() > 0) {
            ShamblerEntity entity = possibilities.get(shambler.world.random.nextInt(possibilities.size()));

            Vec3d result = NoPenaltyTargeting.find(entity, 4, 4);

            if (result != null) return result;
        }

        return super.getWanderTarget();
    }

    public List<ShamblerEntity> getNearbyShamblers(int partners) {

        return shambler.world.getEntitiesByClass(ShamblerEntity.class, Box.from(Vec3d.of(shambler.getBlockPos())).expand(getGroupRange(partners)), entity -> entity.isAlive() && entity != this.shambler);
    }

    private int getGroupRange(int partners) {
        return (36 - Math.min(4 * partners, 26));
    }
}
