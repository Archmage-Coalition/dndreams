package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.BloodMoonWorld;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ServerWorld.class)
public abstract class BloodMoonMixin implements BloodMoonWorld {
    private int moonChance = 0;
    private boolean isBloodMoon = false;
    private int pastPhase = 0;
    private Random lunarRandom = new Random();

    @Override
    public boolean isBloodMoon() {
        return isNight() && isBloodMoon;
    }

    @Override
    public int getBloodMoonChance() {
        return moonChance;
    }

    @Inject(method = "tickTime", at = @At("HEAD"))
    protected void injectTickTime() {
        if (pastPhase != getMoonPhase()) {
            pastPhase = getMoonPhase();

            if (isBloodMoon) {
                moonChance = 0;
                isBloodMoon = false;
            } else if (lunarRandom.nextInt(100) < moonChance) {
                isBloodMoon = true;
            } else {
                moonChance += 5;
            }
        }
    }
}
