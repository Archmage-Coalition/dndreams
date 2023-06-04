package net.eman3600.dndreams.entities.ai;

import software.bernie.geckolib3.core.builder.ILoopType;

public interface AnimatedGoal {

    String getAnimation();
    boolean doAnimate();
    ILoopType.EDefaultLoopTypes getLoopType();
}
