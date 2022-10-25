package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.mixin_interfaces.ChunkRendererRegionAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(ChunkRendererRegion.class)
public abstract class ChunkRendererRegionMixin implements BlockRenderView, ChunkRendererRegionAccess {
    @Shadow @Final protected World world;

    @Override
    public World getWorld() {
        return world;
    }
}
