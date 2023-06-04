package net.eman3600.dndreams.entities.ai;

import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.minecraft.item.Item;
import software.bernie.geckolib3.core.builder.ILoopType;

public class TormentorRangedAttackGoal extends MagicBowAttackGoal<TormentorEntity> implements AnimatedGoal {

    public TormentorRangedAttackGoal(TormentorEntity actor, double speed, int attackInterval, float range, Item bowItem) {
        super(actor, speed, attackInterval, range, bowItem);
    }

    @Override
    public String getAnimation() {
        return "bow";
    }

    @Override
    public boolean doAnimate() {
        return true;
    }

    @Override
    public ILoopType.EDefaultLoopTypes getLoopType() {
        return ILoopType.EDefaultLoopTypes.LOOP;
    }
}
