package net.eman3600.dndreams.mixin.server;

import com.google.common.collect.ImmutableList;
import net.eman3600.dndreams.entities.spawners.TormentorSpawner;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.spawner.Spawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> {
    public MinecraftServerMixin(String string) {
        super(string);
    }

    @ModifyVariable(method = "createWorlds", name = "list", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/Registry;get(Lnet/minecraft/util/registry/RegistryKey;)Ljava/lang/Object;"))
    private List dndreams$createWorlds$addSpawns(List value) {
        ArrayList<Spawner> tempList = new ArrayList<>();

        for (Object item: value) {
            if (value instanceof Spawner spawner) {
                tempList.add(spawner);
            }
        }

        tempList.add(new TormentorSpawner());

        return ImmutableList.copyOf(tempList);
    }
}
