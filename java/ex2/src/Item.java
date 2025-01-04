/**
 * Represents an item in the game.
 */
public class Item {
    final private String name;
    private boolean doesExits;
    /**
     * return items name
     * @return: item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the existence status of the item.
     * @param doesExist True if the item exists, false otherwise.
     */
    public void setDoesExits(boolean doesExist) {
        this.doesExits = doesExist;
    }
    /**
     * constructor function
     * @param name: name of item
     */
    public Item(String name) {
        this.name = name;
        this.doesExits = true;
    }

}
