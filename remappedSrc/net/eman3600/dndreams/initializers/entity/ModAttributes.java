package net.eman3600.dndreams.initializers.entity;

import net.eman3600.dndreams.Initializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModAttributes {
    public static final EntityAttribute PLAYER_MAX_MANA = register("player.max_mana",
            (new ClampedEntityAttribute("attribute.dndreams.name.player.max_mana", 25.0D, 1.0D, 1024.0D)).setTracked(true));
    public static final EntityAttribute PLAYER_MANA_REGEN = register("player.mana_regen",
            (new ClampedEntityAttribute("attribute.dndreams.name.player.mana_regen", 8.0D, 0.0D, 1024.0D)).setTracked(true));

    private static EntityAttribute register(String id, EntityAttribute attribute) {
        return Registry.register(Registry.ATTRIBUTE, new Identifier(Initializer.MODID, id), attribute);
    }

    public static void registerAttributes() {
        System.out.println("Registering entity attributes for " + Initializer.MODID);
    }
}
