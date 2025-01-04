
/**
 * Manages the game, including players, rooms, items.
 */
public class GameManager {
    private static final int MAXIMAL_NUMBER_OF_ROOMS = 5;
    private Room[] roomsInTheGame = new Room[MAXIMAL_NUMBER_OF_ROOMS];
    private Player gamePlayer;
    private int numberOfRooms;

    /**
     * Constructs a GameManager object.
     */
    public GameManager() {
        this.gamePlayer = null;
        this.numberOfRooms = 0;
    }

    /**
     * Adds a player to the game.
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        if (this.gamePlayer != null) {
            System.out.println("Could not add " + player.getName() + " to the game.");
        } else {
            this.gamePlayer = player;
            System.out.println(player.getName() + " was added to the game.");

        }
    }
    /**
     * Adds a room to the game.
     * @param room The room to add.
     */
    public void addRoom(Room room) {
        if (this.numberOfRooms == MAXIMAL_NUMBER_OF_ROOMS) {
            System.out.println("Could not add " + room.getName() + " to the game.");
        } else {
            for (int i = 0; i < 5; i++) {
                if (roomsInTheGame[i] == null) {
                    roomsInTheGame[i] = room;
                    this.numberOfRooms++;
                    System.out.println(room.getName() + " was added to the game.");
                    break;
                }
            }
        }
    }
    /**
     * Adds an item to a room in the game.
     * @param room The room to add the item to.
     * @param item The item to add.
     */
    public void addItem(Room room, Item item) {
        if(!checkRoomInGame(room)){
            System.out.println("Could not add " + item.getName() + " to the game.");
            return;
        }
        if (room.getNumberOfItems() == room.getMaxNumberOfItems()) {
            System.out.println("Could not add " + item.getName() + " to the game.");
        } else {
            room.addItem(item);
            System.out.println(item.getName() + " was added to the game.");
        }
    }

    /**
     * Removes a player from the game.
     * @param player The player to remove.
     */
    public void removePlayer(Player player) {
        if (this.gamePlayer.equals(player)) {
            this.gamePlayer = null;
            this.deleteAllItemsFromPlayer(player);
            System.out.println(player.getName() + " was removed from the game.");
            player.setDoesExits(false);
        } else {
            System.out.println(player.getName() + " does not exist.");
        }

    }
    /**
     * Removes a room from the game.
     * @param room The room to remove.
     */
    public void removeRoom(Room room) {
        for (int i = 0; i < this.roomsInTheGame.length; i++) {
            if (this.roomsInTheGame[i].equals(room)) {
                if (this.gamePlayer != null && this.gamePlayer.getCurrRoom().equals(room)) {
                    System.out.println(room.getName() + " could not be removed.");
                } else {
                    this.roomsInTheGame[i] = null;
                    this.deleteAllItemsFromRoom(room);
                    System.out.println(room.getName() + " was removed from the game.");
                    room.setDoesExits(false);
                    this.numberOfRooms--;
                }
                return;
            }
        }
        System.out.println(room.getName() + " does not exist.");
    }

    /**
     * Connects two rooms together in a specified direction.
     * @param room1 The first room to connect.
     * @param room2 The second room to connect.
     * @param direction The direction in which to connect the rooms.
     */
    public void connectRooms(Room room1, Room room2, Direction direction) {
        if (room1.connectRooms(room2, direction)) {
            System.out.println(room1.getName() + " and " + room2.getName() + " are connected.");
        } else {
            System.out.println("Could not connect " + room1.getName() + " and " + room2.getName() + ".");
        }
    }

    /**
     * Starts the player in the wanted room.
     * @param room The room in which to start the player.
     */
    public void startPlayer(Room room) {
        if(gamePlayer.getCurrRoom() == null || gamePlayer.getCurrRoom().equals(room)) {
            gamePlayer.setCurrRoom(room);
            this.deleteAllItemsFromPlayer(this.gamePlayer);
            System.out.println(gamePlayer.getName() + " starts in " + room.getName() + ".");
        }
        else{
            System.out.println(gamePlayer.getName() + " has already started.");
        }
    }
    /**
     * Moves the player in the wanted direction if possible.
     * @param direction The direction in which to move the player.
     */
    public void movePlayer(Direction direction) {
        Room currRoom = gamePlayer.getCurrRoom();
        Room nextRoom = currRoom.getRoomsConnectedToEntries(direction.ordinal());

        if (!currRoom.getPuzzleState() && nextRoom != null) {
            System.out.println(gamePlayer.getName() + " moved from " + currRoom.getName() +
                    " to " + nextRoom.getName() + " via the " + direction.toString().toLowerCase() + " exit.");
            gamePlayer.setCurrRoom(nextRoom);
        } else {
            System.out.println(gamePlayer.getName() +
                    " could not move via the " + direction.toString().toLowerCase() + " exit.");
        }
    }
    /**
     * Allows the player to pick up an item from the current room and add it to their inventory.
     * @param item The item to pick up.
     */

    public void pickUpItem(Item item) {
        Room currRoom = gamePlayer.getCurrRoom();
        String nameOfRoom = gamePlayer.getCurrRoom().getName();
        String itemName = item.getName();
        Bag playerBag = gamePlayer.getPlayerBag();

        for (int i = 0; i < playerBag.getMaxSize(); i++) {
            if (playerBag.getItemInBag(i) == null) {
                if (this.checkRemoveItem(currRoom, item)) {
                    playerBag.setItem(item, i);
                    playerBag.addToBagSize(1);
                    this.removeItem(currRoom, item);
                    currRoom.setNumberOfItems(-1);
                    System.out.println(gamePlayer.getName() + " picked up " + itemName + " from "+ nameOfRoom+ ".");
                    return;
                }
                System.out.println(itemName + " is not in " + nameOfRoom + ".");
                return;
            }
        }
        System.out.println(gamePlayer.getName() + "'s inventory is full.");
    }
    /**
     * Allows the player to drop an item from their inventory into the current room.
     * @param item The item to drop.
     */
    public void dropItem(Item item) {
        Room currRoom = gamePlayer.getCurrRoom();
        Bag playerBag = gamePlayer.getPlayerBag();
        String nameOfPlayer = gamePlayer.getName();
        String itemName = item.getName();

        boolean roomGotSpace = currRoom.roomGotSpace();
        for (int i = 0; i < playerBag.getSize(); i++) {
            if (playerBag.getItemInBag(i).equals(item)) {
                if (roomGotSpace) {
                    playerBag.setItemNull(i);
                    currRoom.addItem(item);
                    System.out.println(nameOfPlayer + " dropped " + itemName + " in " + currRoom.getName() + ".");
                    playerBag.addToBagSize(i);
                } else {
                    System.out.println(currRoom.getName() + " is full.");
                }
                return;
            }
        }
        System.out.println(itemName + " is not in " + nameOfPlayer + "'s inventory.");
    }
    /**
     * Disassemble an item from the game.
     * @param item The item to disassemble.
     */
    public void disassembleItem(Item item) {
        Bag playerBag = gamePlayer.getPlayerBag();
        Room currRoom = gamePlayer.getCurrRoom();
        String nameOfPlayer = gamePlayer.getName();
        String itemName = item.getName();

        for (int i = 0; i < playerBag.getMaxSize(); i++) {
            if (playerBag.getItemInBag(i) != null && playerBag.getItemInBag(i).equals(item)) {
                playerBag.setItemNull(i);
                System.out.println(nameOfPlayer + " disassembled " + itemName + ".");
                item.setDoesExits(false);
                return;
            }
            for (int j = 0; j < currRoom.getMaxNumberOfItems(); j++) {
                if (currRoom.getItemsInRoom(j) != null && currRoom.getItemsInRoom(j).equals(item)) {
                    currRoom.setItemNull(j);
                    System.out.println(nameOfPlayer + " disassembled " + itemName + ".");
                    item.setDoesExits(false);
                    return;
                }
            }
        } System.out.println(nameOfPlayer + " could not disassemble " + itemName + ".");
    }

    /**
     * Solves the puzzle in the current room if there is an active puzzle.
     */
    public void solvePuzzle() {
        if (gamePlayer.getCurrRoom().getPuzzleState()) {
            gamePlayer.getCurrRoom().setPuzzleState(false);
            System.out.println(this.gamePlayer.getName() +
                    " is solving the puzzle in " + this.gamePlayer.getCurrRoom().getName() + ".");
        }
        else{
            System.out.println("There is no active puzzle in "
                    + this.gamePlayer.getCurrRoom().getName() + ".");
        }

    }
    /**
     * Activates the puzzle in the specified room.
     * @param currRoom The room in which to activate the puzzle.
     */
    public void activatePuzzle(Room currRoom) {
        currRoom.setPuzzleState(true);
    }
    /**
     * Deactivates the puzzle in the specified room.
     * @param currRoom The room in which to deactivate the puzzle.
     */
    public void deactivatePuzzle(Room currRoom) {
        currRoom.setPuzzleState(false);
    }
    /**
     * Checks if a given room is in the game.
     * @param room The room to check.
     * @return True if the room is present, false otherwise.
     */
    public boolean checkRoomInGame(Room room) {
        for (int i = 0; i < numberOfRooms; i++) {
            if (this.roomsInTheGame[i].equals(room)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a specified item can be removed from a given room.
     * @param room The room to check.
     * @param item The item to check for removal.
     * @return True if the item can be removed, false otherwise.
     */
    public boolean checkRemoveItem(Room room, Item item) {
        if (room.getNumberOfItems() == 0) {
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (room.getItemsInRoom(i) == null){
                continue;
            }
            if (room.getItemsInRoom(i).equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the wanted item from the given room.
     * @param room The room from which to remove the item.
     * @param item The item to remove.
     */
    public void removeItem(Room room, Item item) {
        for (int i = 0; i < room.getMaxNumberOfItems(); i++) {
            if (room.getItemsInRoom(i) != null && room.getItemsInRoom(i).equals(item)){
                room.setItemNull(i);
            }
        }
    }

    /**
     * Deletes all items from the specified room.
     * @param room The room from which to delete all items.
     */
    public void deleteAllItemsFromRoom(Room room){
        room.setItemsInRoom(new Item[room.getMaxNumberOfItems()]);
    }

    /**
     * Deletes all items from the specified player's inventory.
     * @param player The player whose inventory is to be cleared.
     */
    public void deleteAllItemsFromPlayer(Player player){
        player.setPlayerBag(new Bag(player.getPlayerBag().getMaxSize()));
    }
}

