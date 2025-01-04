/**
 * Represents a large bag item in the game.
 * Extends the Bag class.
 */
public class LargeBag extends Bag implements Cloneable{

    /**
     * Constructs a new LargeBag with the specified name, maximum size, and value.
     * If the value is less than 5, sets the maximum size to 5.
     *
     * @param name The name of the large bag.
     * @param maxSize The maximum size of the bag.
     * @param value The value of the large bag.
     */
    public LargeBag(String name, int maxSize, int value) {
        super(name, maxSize, value);
        if (value < 5) {
            this.maxSize = 5;
        }
    }

    /**
     * Equals method to compare two LargeBag objects.
     * LargeBags are considered equal if they have the same value, total items value, and maximum size.
     *
     * @param other The object to compare with.
     * @return True if the LargeBags are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LargeBag)) {
            return false;
        }
        LargeBag largeBag = (LargeBag) other;
        return  this.value == largeBag.value &&
                this.getItemsValue() == largeBag.getItemsValue() &&
                this.maxSize == largeBag.maxSize;
    }

    /**
     * Hashcode method to generate a hashcode for the LargeBag object.
     * Incorporates the value, total items value, and maximum size for hashcode calculation.
     *
     * @return The hashcode of the LargeBag object.
     */
    @Override
    public int hashCode() {
        return  17 * this.getItemsValue() +
                19 * this.value + 23 * this.maxSize + 1;
    }
    /**
     * Creates and returns a deep copy of this LargeBag object.
     *
     * @return A deep copy of this LargeBag object.
     */
    public LargeBag clone() {
        return (LargeBag) super.clone();
    }
}
