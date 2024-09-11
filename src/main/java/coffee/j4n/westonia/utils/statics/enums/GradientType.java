package coffee.j4n.westonia.utils.statics.enums;

/**
 * Represents the type of a gradient.
 * Used to determine which gradient should be applied to a message.
 */
public enum GradientType {
    NONE(""), // no gradient
    PREFIX("#D49A19:#D0B26E"), // gold
    DEFAULT("#AAAAAA:#888888"), // gray
    STATIC("#5555FF:#3333CC"), // blue
    PLAYER("#FFE259:#FFA751"), // orange
    NAME("#84CEEB:#5B86E5"), // light blue
    INTEGER("#00AAAA:#008888"), // teal/cyan
    FLOAT("#FFFF55:#CCCC33"), // yellow
    SUCCESS("#55FF55:#33CC33"), // light green
    WARN("#FFB347:#FF7043"), // orange
    ERROR("#CB2D3E:#EF473A"); // red


    private final String gradient;

    GradientType(String gradient) {
        this.gradient = gradient;
    }

    public String getGradient() {
        return gradient;
    }

}
