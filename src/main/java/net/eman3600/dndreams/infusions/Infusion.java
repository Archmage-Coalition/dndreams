package net.eman3600.dndreams.infusions;

import net.eman3600.dndreams.Initializer;
import net.minecraft.util.Identifier;

public enum Infusion {
    NONE(0, "none", 0),
    OVERWORLD(1, "overworld", 25),
    NETHER(2, "nether", 35),
    END(3, "end", 40),
    SPIRIT(4, "spirit", 50);

    private final int num;
    private final Identifier id;
    private final int mana_bonus;

    Infusion(int num, String name, int mana_bonus) {
        this.num = num;
        this.id = new Identifier(Initializer.MODID, name);
        this.mana_bonus = mana_bonus;
    }

    public int getNum() {
        return num;
    }

    public Identifier getId() {
        return id;
    }

    public int manaBonus() {
        return mana_bonus;
    }

    public static Infusion getFromNum(int find) {
        if (find == NONE.num) {
            return NONE;
        } else if (find == OVERWORLD.num) {
            return OVERWORLD;
        } else if (find == NETHER.num) {
            return NETHER;
        } else if (find == END.num) {
            return END;
        } else if (find == SPIRIT.num) {
            return SPIRIT;
        }
        throw new EnumConstantNotPresentException(Infusion.class, "Invalid infusion ID given");
    }
}
