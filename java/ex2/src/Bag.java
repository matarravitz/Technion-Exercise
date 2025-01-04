/**
 * Represents a bag that can hold items for a player.
 */
public class Bag {
    private int size; // Current size of the bag
    private int maxSize; // Maximum size of the bag
    private Item[] bag; // Array to store items in the bag

    /**
     * Constructs a new bag with selected maximum size.
     * @param maxSize The maximum size of the bag.
     */
    public Bag(int maxSize) {
        this.maxSize = maxSize;
        this.bag = new Item[maxSize];
        this.size = 0;
    }

    /**
     * Gets the maximum size of the bag.
     * @return The maximum size of the bag.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Gets the current size of the bag.
     * @return The current size of the bag.
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the item at the wanted index in the bag.
     * @param i The index of the item to retrieve.
     * @return The item at the specified index.
     */
    public Item getItemInBag(int i) {
        return bag[i];
    }

    /**
     * Sets the item at the wanted index in the bag to null.
     * @param i The index of the item to set to null.
     */
    public void setItemNull(int i){
        this.bag[i] = null;
    }

    /**
     * Sets the item at the wanted index in the bag.
     * @param item The item to set.
     * @param i The index where the item should be set.
     */
    public void setItem(Item item, int i){
        this.bag[i] = item;
    }

    /**
     * Increases the size of the bag by the wanted amount.
     * @param i The amount by which to increase the size of the bag.
     */
    public void addToBagSize(int i) {
        this.size = this.size + i;
    }
}
