package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.StatBoonComponentI;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StatBoonComponent implements StatBoonComponentI {
    private final PlayerEntity player;
    private final Map<Identifier, Double> attributes = new HashMap<>();
    public static final UUID uuid = new UUID(0x628c2a812ca80f25L, 0xc0b0234fd066d371L);
    /**
     * The variable hp is changed only when reading nbt. It is NOT an accurate representation of the player's hp at any time other
     * than when the player rejoins the game.
     */
    public float hp;

    public StatBoonComponent(PlayerEntity player) {
        this.player = player;
        hp = player.getHealth();
    }


    @Override
    public void increase(@Nullable Identifier id, double amount) {
        double curr = 0d;

        if (attributes.containsKey(id)) {
            curr = attributes.get(id);
        }

        set(id, amount + curr);
    }

    @Override
    public void set(@Nullable Identifier id, double amount) {
        attributes.put(id, amount);

        reloadAttributes();
    }

    @Override
    public void remove(@Nullable Identifier id) {
        EntityAttribute attribute = Registry.ATTRIBUTE.get(id);
        main: if (attribute != null) {
            EntityAttributeInstance instance = player.getAttributes().getCustomInstance(attribute);
            if (instance == null) break main;

            instance.removeModifier(uuid);
        }

        attributes.remove(id);
    }

    @Override
    public void reloadAttributes() {
        AttributeContainer container = player.getAttributes();

        for (Identifier id: attributes.keySet()) {
            EntityAttribute attribute = Registry.ATTRIBUTE.get(id);
            if (attribute == null) continue;

            EntityAttributeInstance instance = container.getCustomInstance(attribute);
            if (instance == null) continue;
            instance.removeModifier(uuid);
        }

        Map<EntityAttribute, EntityAttributeModifier> map = getAttributes();
        for (EntityAttribute attribute: map.keySet()) {
            EntityAttributeInstance instance = container.getCustomInstance(attribute);

            instance.addTemporaryModifier(map.get(attribute));
        }
    }

    @Override
    public Map<EntityAttribute, EntityAttributeModifier> getAttributes() {
        Map<EntityAttribute, EntityAttributeModifier> map = new HashMap<>();

        for (Identifier id: attributes.keySet()) {
            EntityAttribute attribute = Registry.ATTRIBUTE.get(id);
            if (attribute == null) continue;

            EntityAttributeModifier modifier = getAttribute(attribute);
            if (modifier != null) map.put(attribute, modifier);
        }

        return map;
    }

    @Override
    @Nullable
    public EntityAttributeModifier getAttribute(EntityAttribute attribute) {
        Identifier id = Registry.ATTRIBUTE.getId(attribute);

        if (attribute == null || id == null) return null;

        if (attributes.containsKey(id)) {
            return new EntityAttributeModifier(uuid, "boon", attributes.get(id), EntityAttributeModifier.Operation.ADDITION);
        } else {
            return new EntityAttributeModifier(uuid, "boon", 0, EntityAttributeModifier.Operation.ADDITION);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (String key: tag.getKeys()) {
            if (key.equals("hp")) continue;
            Identifier id = Identifier.tryParse(key);

            if (id != null) {
                attributes.put(id, tag.getDouble(key));
            }
        }

        hp = tag.getFloat("hp");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        for (Identifier attribute: attributes.keySet()) {
            String s = attribute.toString();

            tag.putDouble(s, attributes.get(attribute));
        }

        tag.putFloat("hp", player.getHealth());
    }
}
