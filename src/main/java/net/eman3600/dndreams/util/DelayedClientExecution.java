package net.eman3600.dndreams.util;

import net.minecraft.client.MinecraftClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class DelayedClientExecution {

    private final BiConsumer<MinecraftClient, DelayedClientExecution> function;
    private boolean hasRun = false;
    private final Queue<Float> floats = new LinkedList<>();
    private final Queue<Double> doubles = new LinkedList<>();
    private final Queue<Integer> ints = new LinkedList<>();
    private final Queue<Boolean> bools = new LinkedList<>();

    public DelayedClientExecution(BiConsumer<MinecraftClient, DelayedClientExecution> function) {
        this.function = function;
    }

    public DelayedClientExecution(PacketByteBuf packet, BiConsumer<MinecraftClient, DelayedClientExecution> function, BiConsumer<PacketByteBuf, DelayedClientExecution> setup) {
        this.function = function;
        setup.accept(packet, this);
    }

    public void run() {
        if (hasRun) throw new IllegalStateException("Tried to execute delayed execution multiple times!");
        function.accept(MinecraftClient.getInstance(), this);

        hasRun = true;
    }

    public void pushFloat(float f) {
        floats.add(f);
    }
    public void pushDouble(double d) {
        doubles.add(d);
    }
    public void pushInt(int i) {
        ints.add(i);
    }
    public void pushBool(boolean b) {
        bools.add(b);
    }

    public float popFloat() {
        return floats.remove();
    }
    public double popDouble() {
        return doubles.remove();
    }
    public int popInt() {
        return ints.remove();
    }
    public boolean popBool() {
        return bools.remove();
    }
}
