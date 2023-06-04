package net.eman3600.dndreams.entities.ai;

import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class TormentorMeleeAttackGoal extends MeleeAttackGoal {
    private final TormentorEntity tormentor;
    private int ticks;

    public TormentorMeleeAttackGoal(TormentorEntity tormentor, double speed, boolean pauseWhenMobIdle) {
        super(tormentor, speed, pauseWhenMobIdle);
        this.tormentor = tormentor;
    }

    @Override
    public void start() {
        super.start();
        this.ticks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.tormentor.setAttacking(false);
    }

    @Override
    public void tick() {
        super.tick();
        ++this.ticks;
        this.tormentor.setAttacking(this.ticks >= 5 && this.getCooldown() < this.getMaxCooldown() / 2);
    }
}