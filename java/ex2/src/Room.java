/**
 * Represents a room in the game environment.
 */
public class Room {

    private static final int MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM = 2;
    private static final int NUMBER_OF_ENTRIES_IN_ROOM = 4;

    private final String name;
    private Room[] roomsConnectedToEntries;
    private Item[] items;
    private Puzzle puzzle;
    private int numberOfItems;
    private boolean doesExits;

    /**
     * Constructs a room with the given name.
     * @param name The name of the room.
     */
    public Room(String name) {
        this.name = name;
        this.roomsConnectedToEntries = new Room[NUMBER_OF_ENTRIES_IN_ROOM];
        this.items = new Item[MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM];
        this.puzzle = new Puzzle();
        this.numberOfItems = 0;
        this.doesExits = true;
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
     * Retrieves the item at a specific index in the room.
     * @param i The index of the item.
     * @return The item at the specified index.
     */
    public Item getItemsInRoom(int i) {
        return items[i];
    }

    /**
     * Sets the item at a specific index in the room to null.
     * @param i The index of the item to set to null.
     */
    public void setItemNull(int i) {
        items[i] = null;
    }

    /**
     * Sets the items in the room to the given list of items.
     * @param listOfItems The list of items to set in the room.
     */
    public void setItemsInRoom(Item[] listOfItems) {
        this.items = listOfItems;
    }

    /**
     * Checks if the room exists.
     * @return true if the room exists, false otherwise.
     */
    public boolean getDoesExits() {
        return doesExits;
    }

    /**
     * Sets the existence status of the room.
     * @param bool True if the room exists, false otherwise.
     */
    public void setDoesExits(Boolean bool) {
        this.doesExits = bool;
    }

    /**
     * Gets the number of items in the room.
     * @return The number of items in the room.
     */
    public int getNumberOfItems() {
        return numberOfItems;
    }

    /**
     * Sets the number of items in the room.
     * @param i The number of items to set.
     */
    public void setNumberOfItems(int i) {
        this.numberOfItems = this.numberOfItems + i;
    }

    /**
     * Checks whether the puzzle associated with the room is solved.
     * @return true if the puzzle is solved, false otherwise.
     */
    public boolean getPuzzleState() {
        return puzzle.getPuzzleState();
    }

    /**
     * setting the puzzle in the room
     *
     * @param bool:the puzzle state
     * */
    public void setPuzzleState(Boolean bool) {
        this.puzzle.setPuzzleState(bool);
    }

    /**
     * Retrieves the name of the room.
     * @return The name of the room.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the maximum number of items allowed in the room.
     * @return The maximum number of items allowed in the room.
     */
    public int getMaxNumberOfItems() {
        return MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM;
    }

    /**
     * Connects the current room to another room in a specified direction.
     * @param room The room to connect to.
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
     * @return: is there empty from items, place in the room
     */
    public boolean roomGotSpace() {
        return numberOfItems < MAXIMAL_NUMBER_OF_ITEMS_IN_ROOM;
    }

    /**
     * Adds an item to the room.
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
}
