package net.eman3600.dndreams.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> {
    public MinecraftServerMixin(String string) {
        super(string);
    }

    /*@ModifyVariable(method = "createWorlds", name = "list", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/Registry;get(Lnet/minecraft/util/registry/RegistryKey;)Ljava/lang/Object;"))
    private List dndreams$createWorlds$addSpawns(List value) {
        ArrayList<Spawner> tempList = new ArrayList<>();

        for (Object item: value) {
            if (value instanceof Spawner spawner) {
                tempList.add(spawner);
            }
        }

        tempList.add(new TormentorSpawner());

        return ImmutableList.copyOf(tempList);
    }*/
}
