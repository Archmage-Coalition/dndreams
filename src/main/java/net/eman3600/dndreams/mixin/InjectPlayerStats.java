package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.ImplementPlayerStats;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class InjectPlayerStats implements ImplementPlayerStats {
    // Mixes all Nbt Data relating to Mana and Torment

    private NbtCompound mana;

    private NbtCompound torment;

    private NbtCompound initiated;

    private final float ABSOLUTE_MANA_MAX = 200.0f;

    public final float DEFAULT_MANA_MAX = 20.0f;
    public final float MIN_TORMENT = 0f;
    public final float MAX_TORMENT = 20f;
    public final boolean DEFAULT_INITIATED = false;

    public final String MAGIC_VALUE = "value";
    public final String MAGIC_MAX = "max";

    public final String MANA_IDENTIFIER = "DnDreamsMana";
    public final String TORMENT_IDENTIFIER = "DnDreamsTorment";
    public final String INITIATED_IDENTIFIER = "DnDreamsInitiated";


    @Override
    public NbtCompound getMana() {
        if (mana == null) {
            mana = new NbtCompound();
            mana.putFloat(MAGIC_VALUE,DEFAULT_MANA_MAX);
            mana.putFloat(MAGIC_MAX,DEFAULT_MANA_MAX);
        }

        if (mana.getFloat(MAGIC_MAX) > ABSOLUTE_MANA_MAX) {
            mana.putFloat(MAGIC_MAX,ABSOLUTE_MANA_MAX);
        }

        if (mana.getFloat(MAGIC_VALUE) > mana.getFloat(MAGIC_MAX)) {
            mana.putFloat(MAGIC_VALUE,mana.getFloat(MAGIC_MAX));
        }

        return mana;
    }

    @Override
    public NbtCompound getTorment() {
        if (torment == null) {
            torment = new NbtCompound();
            torment.putFloat(MAGIC_VALUE,MIN_TORMENT);
        }

        if (torment.getFloat(MAGIC_VALUE) > MAX_TORMENT) {
            torment.putFloat(MAGIC_VALUE,MAX_TORMENT);
        }

        return torment;
    }

    @Override
    public NbtCompound getInitiated() {
        if (initiated == null) {
            initiated = new NbtCompound();
            initiated.putBoolean(MAGIC_VALUE,DEFAULT_INITIATED);
        }
        return initiated;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if (mana != null) {
            nbt.put(MANA_IDENTIFIER, mana);
        }
        if (torment != null) {
            nbt.put(TORMENT_IDENTIFIER, torment);
        }
        if (initiated != null) {
            nbt.put(INITIATED_IDENTIFIER, initiated);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(MANA_IDENTIFIER)) {
            mana = nbt.getCompound(MANA_IDENTIFIER);
        }
        if (nbt.contains(TORMENT_IDENTIFIER)) {
            torment = nbt.getCompound(TORMENT_IDENTIFIER);
        }
        if (nbt.contains(INITIATED_IDENTIFIER)) {
            initiated = nbt.getCompound(INITIATED_IDENTIFIER);
        }
    }
}
