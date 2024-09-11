package coffee.j4n.westonia.utils.statics.enums;

/**
 * Enum representing different inventory sizes.
 */
public enum InventorySize {
    DROPPER(9),
    HOPPER(5),
    CHEST_3ROW(27),
    CHEST_4ROW(36),
    CHEST_5ROW(45),
    CHEST_6ROW(54);

    private final int size;

    InventorySize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}