package xcykrix.harderendportal.properties;

import net.minecraft.item.Item;
import net.minecraft.util.StringIdentifiable;

public enum EyeState implements StringIdentifiable {
    EMPTY,
    CURSED_EYE,
    CHILLED_EYE,
    CRACKED_EYE,
    LOST_EYE,
    MAGICAL_EYE,
    WARDED_EYE,
    ELDRITCH_EYE,
    WITHERED_EYE,
    ROTTEN_EYE,
    ALEMBIC_EYE,
    ENCHANTED_EYE,
    GOLDEN_EYE;

    @Override
    public String asString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        switch (this) {
            case EMPTY:
            default:
                return "empty";
            case CURSED_EYE:
                return "cursed_eye";
            case CHILLED_EYE:
                return "chilled_eye";
            case CRACKED_EYE:
                return "cracked_eye";
            case LOST_EYE:
                return "lost_eye";
            case MAGICAL_EYE:
                return "magical_eye";
            case WARDED_EYE:
                return "warded_eye";
            case ELDRITCH_EYE:
                return "eldrich_eye";
            case WITHERED_EYE:
                return "withered_eye";
            case ROTTEN_EYE:
                return "rotten_eye";
            case ALEMBIC_EYE:
                return "alembic_eye";
            case ENCHANTED_EYE:
                return "enchanted_eye";
            case GOLDEN_EYE:
                return "golden_eye";
        }
    }

    public static EyeState getProperty(Item eye) {
        for (EyeState property : EyeState.values()) {
            if (property.asString().equals(eye.toString())) {
                return property;
            }
        }

        return EMPTY;
    }
}
