/**
 * Represents an item in the game.
 */
abstract class Item implements Cloneable {
    protected int value;
    final protected String name;
    protected Room currRoom;
    protected boolean doesExits;

    protected boolean inPlayersBag;

    /**
     * constructor function
     * @param name: name of item
     */
    public Item(String name, int value) {
        this.name = name;
        this.doesExits = true;
        this.value = value;
        this.inPlayersBag=false;
    }
    protected abstract void useItem(Player player);

    /**
     * Method to get the value of the item.
     * @return The value of the item.
     */
    public int getValue() {
        return value;
    }
    /**
     * Sets the existence status of the item.
     * @param doesExist True if the item exists, false otherwise.
     */
    public void setDoesExits(boolean doesExist) {
        this.doesExits = doesExist;
    }
    /**
     * return items name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the current room where the item is located.
     * @param currRoom The current room where the item is located.
     */
    public void setCurrRoom(Room currRoom) {
        this.currRoom = currRoom;
    }

    /**
     * Method to get the current room where the item is located.
     * @return The current room where the item is located.
     */
    public Room getCurrRoom() {
        return this.currRoom;
    }
    /**
     * Method to disassemble the item.
     * Removes the item from the player's bag or the current room.
     * @param player The player performing the action.
     */
    public void disassembleItem(Player player) {
        Bag playerBag = player.getPlayerBag();
        Room currRoom = player.getCurrRoom();
        String nameOfPlayer = player.getName();
        Item temp = findItem(player);
        if(player.playerBag.equals(this)){
            player.setPlayerBag(null);
            System.out.println(nameOfPlayer + " disassembled " + this.getName() + ".");
            return;
        }
        for (int i = 0; i < playerBag.getMaxSize(); i++) {
            if (playerBag.getItemInBag(i) != null && playerBag.getItemInBag(i).equals(this)) {
                playerBag.setItemNull(i);
                System.out.println(nameOfPlayer + " disassembled " + temp.getName() + ".");
                player.getPlayerBag().setSize(-1);
                this.setDoesExits(false);
                return;
            }
        }
        for (int j = 0; j < currRoom.getMaxNumberOfItems(); j++) {
            if (currRoom.getItemsInRoom(j) != null && currRoom.getItemsInRoom(j).equals(this)) {
                currRoom.setItemNull(j);
                System.out.println(nameOfPlayer + " disassembled " + temp.getName() + ".");
                player.getCurrRoom().setNumberOfItems(-1);
                this.setDoesExits(false);
                return;
            }
        }
        System.out.println(nameOfPlayer + " could not disassemble " + this.getName() + ".");
    }
    /**
     * Method to disassemble the item without any output.
     * Removes the item from the player's bag or the current room.
     * @param player The player performing the action.
     */
    public void disassembleItemWithoutOutput(Player player) {
        Bag playerBag = player.getPlayerBag();
        Room currRoom = player.getCurrRoom();

        for (int i = 0; i < playerBag.getMaxSize(); i++) {
            if (playerBag.getItemInBag(i) != null && playerBag.getItemInBag(i).equals(this)) {
                playerBag.setItemNull(i);
                player.getPlayerBag().setSize(-1);
                this.setDoesExits(false);
                return;
            }
        }
        for (int j = 0; j < currRoom.getMaxNumberOfItems(); j++) {
            if (currRoom.getItemsInRoom(j) != null && currRoom.getItemsInRoom(j).equals(this)) {
                currRoom.setItemNull(j);
                player.getCurrRoom().setNumberOfItems(-1);
                this.setDoesExits(false);
                return;
            }
        }
    }
    /**
     * Method to find the item in the player's bag or the current room.
     * @param player The player performing the action.
     * @return The found item or null if not found.
     */
    public Item findItem(Player player) {
        for (int i = 0; i < player.getCurrRoom().getMaxNumberOfItems(); i++) {
            if (player.getCurrRoom().items[i] != null && player.getCurrRoom().items[i].equals(this)) {
                return player.getCurrRoom().items[i];
            }
        }
        for (int i = 0; i < player.playerBag.maxSize; i++) {
            if (player.playerBag.bag[i] != null && player.playerBag.bag[i].equals(this)) {
                return player.playerBag.bag[i];

            }
        }
        return null;
    }
    /**
     * Method to find the item in a specific room.
     * @param room The room to search for the item.
     * @return The found item or null if not found.
     */
    public Item findItemInRoom(Room room) {
        for (int i = 0; i < room.getMaxNumberOfItems(); i++) {
            if (room.items[i] != null && room.items[i].equals(this)) {
                return room.items[i];
            }
        }
        return null;
    }
    /**
     * Creates and returns a deep copy of this Item object.
     *
     * @return A deep copy of this Item object.
     */
    @Override
    public Item clone() {
        try {
            Item clone = (Item) super.clone();
            clone.currRoom = this.currRoom != null ? this.currRoom.clone(): null;
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
