/**
 * Represents a player in the game.
 */
public class Player {

    final private String name;
    private Room currRoom;
    private Bag playerBag;
    private boolean doesExits;
    /**
     * return the game player's name
     * @return: the player's name
     */
    public String getName() {
        return name;
    }
    /**
     * return the name of room that the player is in
     * @return: name of room that the player is in
     */

    public Room getCurrRoom() {
        return currRoom;
    }
    /**
     * setting the room that the player will be in
     * @param room: the room that the player is in
     */
    public void setCurrRoom(Room room) {
        this.currRoom = room;
    }
    /**
     * Sets the existence status of the item.
     * @param bool True if the item exists, false otherwise.
     */
    public void setDoesExits(boolean bool){this.doesExits = bool;}
    /**
     * Sets the bag of items carried by the player.
     * @param bag The bag of items.
     */
    public void setPlayerBag(Bag bag) {this.playerBag = bag;}
    /**
     * Returns the bag of items carried by the player.
     * @return The bag of items.
     */
    public Bag getPlayerBag() {return playerBag;}

    /**
     * constructor function
     * @param name: player's name
     * @param sizeBag: player's bag size
     */
    public Player(String name, int sizeBag) {
        this.name = name;
        this.playerBag = new Bag(sizeBag);
        this.doesExits = true;
    }
}
