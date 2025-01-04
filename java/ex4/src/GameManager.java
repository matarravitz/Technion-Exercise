/**
 * Manages the game, including players, rooms, items.
 */
public class GameManager implements Cloneable{
    protected QuartlyLinkedList<Room> roomsInTheGame;
    protected Player gamePlayer;

    /**
     * Constructs a GameManager object.
     */
    public GameManager() {
        this.gamePlayer = null;
        this.roomsInTheGame = new QuartlyLinkedList<>();
    }
    /**
     * Returns the linked list containing all the rooms in the game.
     *
     * @return The linked list containing all the rooms in the game.
     */
    public QuartlyLinkedList<Room> getRooms() {
        return roomsInTheGame;
    }

    /**
     * Adds a player to the game.
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        if (this.gamePlayer != null) {
            System.out.println("Could not add " + player.getName() + "to the game.");
        } else {
            this.gamePlayer = player;
            System.out.println(player.getName() + " was added to the game.");

        }
    }
    /**
     * Adds a room to the game.
     * @param toInsert The room to add.
     * @param target The room to add to.
     * @param direction The direction to add to.
     */
    public void addRoom(Room toInsert, Room target, Direction direction) {
        try {
            if(roomsInTheGame.isEmpty()){
                roomsInTheGame.add(toInsert.clone(),target,direction);
                System.out.println(toInsert.getName() + "  was added.");
            }else {
                roomsInTheGame.add(toInsert.clone(),target,direction);
                System.out.println(toInsert.getName() +
                        " was added and is connected to "
                        + target.getName() + " from the " + direction.toString().toLowerCase() + " exit.");
            }
        }
        catch (NoSuchElement e){
            throw new RoomDoesNotExist();
        }
        catch (DirectionIsOccupied e){
            throw new ExitIsOccupied();
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
            item.setCurrRoom(room);
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
        try{
            roomsInTheGame.remove(room);
        }
        catch (NoSuchElement e){
            throw new RoomDoesNotExist();
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
        QuartNode<Room> currNode = roomsInTheGame.search(gamePlayer.getCurrRoom());
        if(currNode == null){
            System.out.println(gamePlayer.getName() +
                    " could not move via the " + direction.toString().toLowerCase() + " exit.");
            return;
        }
        QuartNode<Room> nextNode = currNode.getRoomInDirection(direction);
        if(nextNode == null){
            System.out.println(gamePlayer.getName() +
                    " could not move via the " + direction.toString().toLowerCase() + " exit.");
            return;
        }
        Room currRoom = this.gamePlayer.getCurrRoom();
        Room nextRoom = nextNode.getValue();

        if (!currRoom.getPuzzleState() && nextRoom.getDoesExits()) {
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
                Item temp = item.findItemInRoom(currRoom);
                if(temp == null){
                    System.out.println(itemName + " is not in " + nameOfRoom + ".");
                    return;
                }
                if (playerBag instanceof LargeBag){
                    if( item instanceof LargeBag) {
                        System.out.println(itemName + " is not valid for storing.");
                        return;
                    }
                }
                if (this.checkRemoveItem(currRoom, item)) {
                    playerBag.setItem(item, i);
                    playerBag.addToBagSize(1);
                    this.removeItem(currRoom, item);
                    currRoom.setNumberOfItems(-1);
                    temp.setCurrRoom(null);
                    temp.inPlayersBag=true;
                    System.out.println(gamePlayer.getName() + " picked up " + item.getName() +
                            " from "+ nameOfRoom+ ".");
                    return;
                }
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
            if (playerBag.getItemInBag(i) != null && playerBag.getItemInBag(i).equals(item)) {
                if (roomGotSpace) {
                    currRoom.addItem(item);
                    item.setCurrRoom(gamePlayer.getCurrRoom());
                    playerBag.getItemInBag(i).inPlayersBag=false;
                    System.out.println(nameOfPlayer + " dropped "
                            + playerBag.getItemInBag(i).getName() + " in " + currRoom.getName() + ".");
                    playerBag.setItemNull(i);
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
        item.disassembleItem(this.gamePlayer);
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
        if(currRoom.isUseKeyStatus()){
            System.out.println(currRoom.getName() + " was unlocked with " + currRoom.getUsedKey().getName() + ".");
            return;
        }
        currRoom.setPuzzleState(true);
    }
    /**
     * Deactivates the puzzle in the specified room.
     * @param currRoom The room in which to deactivate the puzzle.
     */
    public static void deactivatePuzzle(Room currRoom) {
        if(currRoom.isUseKeyStatus()){
            System.out.println(currRoom.getName() + " was unlocked with " + currRoom.getUsedKey().getName() + ".");
            return;
        }
        currRoom.setPuzzleState(false);
    }
    /**
     * Checks if a given room is in the game.
     * @param room The room to check.
     * @return True if the room is present, false otherwise.
     */
    public boolean checkRoomInGame(Room room) {
        return roomsInTheGame.search(room) != null;
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
        player.setPlayerBag(new Bag(player.getPlayerBag().getName(), player.getPlayerBag().getMaxSize(),player.getPlayerBag().getValue()));
    }
    /**
     * Uses the selected item.
     * @param item The item who need to be used.
     */
    public void useItem(Item item){
        item.useItem(gamePlayer);
    }
    /**
     * Creates and returns a deep copy of this GameManager object.
     *
     * @return A deep copy of this GameManager object.
     */
    @Override
    public GameManager clone() {
        try {
            GameManager clone = (GameManager) super.clone();
            clone.roomsInTheGame = this.roomsInTheGame != null ? this.roomsInTheGame.clone(): null;
            clone.gamePlayer = this.gamePlayer != null ? this.gamePlayer.clone(): null;
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

