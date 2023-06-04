package net.eman3600.dndreams.entities.ai;

import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TormentorTacticsGoal extends Goal {

    private final TormentorEntity tormentor;
    private int cooldown = 60;
    private final int bias;
    private boolean unlocked = true;

    public TormentorTacticsGoal(TormentorEntity tormentor) {
        this.tormentor = tormentor;

        bias = tormentor.world.random.nextBetween(-2, 2);
    }

    @Override
    public boolean canStart() {
        return tormentor.getTarget() != null;
    }

    public List<TormentorEntity> getNearbyTormentors() {

        return tormentor.world.getEntitiesByClass(TormentorEntity.class, Box.from(Vec3d.of(tormentor.getBlockPos())).expand(30), entity -> entity.isCorporeal() && entity.isAlive());
    }

    @Override
    public void tick() {
        cooldown--;

        if (cooldown <= 0 && tormentor.getTarget() != null) {

            updateTactics();
        }
    }

    public void updateTactics() {

        cooldown = 60 + tormentor.world.getRandom().nextInt(100);

        double distance = tormentor.squaredDistanceTo(tormentor.getTarget());
        int brawlers = 0;
        int rangers = 0;

        List<TormentorEntity> partners = getNearbyTormentors();

        if (!unlocked) {
            if (partners.size() < 3) unlocked = true;
            else return;
        }

        for (TormentorEntity entity: partners) {
            if (entity.isRanged()) {
                rangers++;
            } else {
                brawlers++;
            }
        }
        int lean = brawlers - rangers;

        if (distance >= 25) {
            lean += distance / 50;
        } else {
            lean -= 2;
        }
        lean += bias;

        tormentor.setRanged(lean >= 2 && brawlers > 0);

        if (partners.size() >= 3) {
            unlocked = false;
        }
    }
}
