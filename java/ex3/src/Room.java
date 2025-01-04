
/**
 * Represents a room in the game environment.
 */
public class Room {

    protected boolean useKeyStatus;
    protected Key usedKey;
    protected static final int MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM = 2;
    protected static final int NUMBER_OF_ENTRIES_IN_ROOM = 4;

    protected final String name;
    protected Room[] roomsConnectedToEntries;
    protected Item[] items;
    protected Puzzle puzzle;
    protected int numberOfItems;
    protected boolean doesExits;
    protected boolean unlockedWithKey;

    /**
     * Constructs a room with the given name.
     *
     * @param name The name of the room.
     */
    public Room(String name) {
        this.name = name;
        this.roomsConnectedToEntries = new Room[NUMBER_OF_ENTRIES_IN_ROOM];
        this.items = new Item[MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM];
        this.puzzle = new Puzzle();
        this.numberOfItems = 0;
        this.doesExits = true;
        this.unlockedWithKey = false;
        this.useKeyStatus = false;
    }

    /**
     * Sets the status of whether a key was used in the current room.
     *
     * @param useKeyStatus True if a key is used, false otherwise.
     */
    public void setUseKeyStatus(boolean useKeyStatus) {
        this.useKeyStatus = useKeyStatus;
    }

    /**
     * Checks whether a key was used in the current room.
     *
     * @return True if a key is used, false otherwise.
     */
    public boolean isUseKeyStatus() {
        return useKeyStatus;
    }

    /**
     * Return the key that was used in the current room.
     *
     * @return The key that was used.
     */
    public Key getUsedKey() {
        return usedKey;
    }

    /**
     * Sets the key that was used in the current room.
     *
     * @param usedKey The key that was used.
     */
    public void setUsedKey(Key usedKey) {
        this.usedKey = usedKey;
    }

    /**
     * Checks whether a key is used in the current room.
     *
     * @return True if a key is used, false otherwise.
     */
    public boolean useKeyStatus() {
        return useKeyStatus;
    }

    /**
     * return the state of the puzzle in the room
     *
     * @return: The state of the puzzle in the room
     */
    public Room getRoomsConnectedToEntries(int i) {
        return roomsConnectedToEntries[i];
    }

    /**
     * return the item at a specific index in the room.
     *
     * @param i The index of the item.
     * @return The item at the specified index.
     */
    public Item getItemsInRoom(int i) {
        return items[i];
    }

    /**
     * Sets the item at a specific index in the room to null.
     *
     * @param i The index of the item to set to null.
     */
    public void setItemNull(int i) {
        items[i] = null;
    }

    /**
     * Sets the items in the room to the given list of items.
     *
     * @param listOfItems The list of items to set in the room.
     */
    public void setItemsInRoom(Item[] listOfItems) {
        this.items = listOfItems;
    }

    /**
     * Checks if the room exists.
     *
     * @return true if the room exists, false otherwise.
     */
    public boolean getDoesExits() {
        return doesExits;
    }

    /**
     * Sets the existence status of the room.
     *
     * @param bool True if the room exists, false otherwise.
     */
    public void setDoesExits(Boolean bool) {
        this.doesExits = bool;
    }

    /**
     * Gets the number of items in the room.
     *
     * @return The number of items in the room.
     */
    public int getNumberOfItems() {
        return numberOfItems;
    }

    /**
     * Sets the number of items in the room.
     *
     * @param i The number of items to set.
     */
    public void setNumberOfItems(int i) {
        this.numberOfItems = this.numberOfItems + i;
    }

    /**
     * Checks whether the puzzle associated with the room is solved.
     *
     * @return true if the puzzle is solved, false otherwise.
     */
    public boolean getPuzzleState() {
        return puzzle.getPuzzleState();
    }

    /**
     * setting the puzzle in the room
     *
     * @param bool:the puzzle state
     */
    public void setPuzzleState(Boolean bool) {
        this.puzzle.setPuzzleState(bool);
    }

    /**
     * return the name of the room.
     *
     * @return The name of the room.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the maximum number of items allowed in the room.
     *
     * @return The maximum number of items allowed in the room.
     */
    public int getMaxNumberOfItems() {
        return MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM;
    }

    /**
     * Connects the current room to another room in a specified direction.
     *
     * @param room      The room to connect to.
     * @param direction The direction in which to connect the rooms (NORTH, EAST, SOUTH, WEST).
     * @return true if the rooms are successfully connected, false otherwise.
     */
    public boolean connectRooms(Room room, Direction direction) {
        int directionIndex;
        switch (direction) {
            case NORTH:
                directionIndex = 0;
                break;
            case EAST:
                directionIndex = 1;
                break;
            case SOUTH:
                directionIndex = 2;
                break;
            default:
                directionIndex = 3;
                break;
        }
        if ((this.roomsConnectedToEntries[directionIndex] != null &&
                this.roomsConnectedToEntries[directionIndex].doesExits)
                || (room.roomsConnectedToEntries[(directionIndex + 2) % 4] != null &&
                room.roomsConnectedToEntries[(directionIndex + 2) % 4].doesExits)) {
            return false;
        }

        this.roomsConnectedToEntries[directionIndex] = room;
        room.roomsConnectedToEntries[(directionIndex + 2) % 4] = this;

        return true;
    }

    /**
     * checking whether there is empty from items, place in the room
     *
     * @return: is there empty from items, place in the room
     */
    public boolean roomGotSpace() {
        return numberOfItems < MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM;
    }

    /**
     * Adds an item to the room.
     *
     * @param item The item to add to the room.
     */
    public void addItem(Item item) {
        for (int i = 0; i < MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM; i++) {
            if (items[i] == null) {
                items[i] = item;
                this.numberOfItems++;
                break;
            }
        }
    }

    /**
     * return the total value of items in the room.
     *
     * @return The total value of items in the room.
     */
    public int getItemsValue() {
        int count = 0;
        for (int i = 0; i < getMaxNumberOfItems(); i++) {
            if (items[i] != null) {
                count = count + items[i].getValue();
            }
        }
        return count;
    }

    /**
     * Compare rooms based on their name, total value of items, puzzle state, and key usage status.
     *
     * @param other The object to compare with.
     * @return True if the rooms are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Room)) {
            return false;
        }
        Room room = (Room) other;
        return this.getName().equals(room.getName()) &&
                this.getItemsValue() == room.getItemsValue() &&
                this.getPuzzleState() == room.getPuzzleState() &&
                this.useKeyStatus() == room.useKeyStatus();
    }

    /**
     * Calculates the hash code based on the room's name, total value of items, puzzle state, and key usage status.
     *
     * @return The hash code of the Room object.
     */
    @Override
    public int hashCode() {
        return 13 * this.getName().hashCode() + 17 * this.getItemsValue() +
                (getPuzzleState() ? 23 : 29) + (useKeyStatus() ? 31 : 37);
    }

    /**
     * Disconnects all connections of the room to other rooms.
     */
    public void unConnect() {
        for (int i = 0; i < NUMBER_OF_ENTRIES_IN_ROOM; i++) {
            if (this.roomsConnectedToEntries[i] != null) {
                this.roomsConnectedToEntries[i].unConnectFromOne(this);
            }
        }
        this.roomsConnectedToEntries = new Room[NUMBER_OF_ENTRIES_IN_ROOM];
    }

    /**
     * Disconnects the connection of the room from a specific room.
     *
     * @param room The room from which to disconnect.
     */
    public void unConnectFromOne(Room room) {
        for (int i = 0; i < NUMBER_OF_ENTRIES_IN_ROOM; i++) {
            if (this.roomsConnectedToEntries[i] != null && this.roomsConnectedToEntries[i].equals(room)) {
                this.roomsConnectedToEntries[i] = null;
            }
        }
    }
}