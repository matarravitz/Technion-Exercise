/**
 * Represents a player in the game.
 */
public class Player {

    final protected String name;
    protected Room currRoom;
    protected Bag playerBag;
    protected boolean doesExits;

    /**
     * constructor function
     *
     * @param name: player's name
     * @param sizeBag: player's bag size
     */
    public Player(String name, int sizeBag) {
        this.name = name;
        this.playerBag = new Bag("Starting bag", sizeBag, 1);
        this.doesExits = true;
    }
    /**
     * return the player's name
     *
     * @return: the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * return the name of the room that the player is in
     *
     * @return: name of room that the player is in
     */
    public Room getCurrRoom() {
        return currRoom;
    }

    /**
     * setting the room that the player will be in
     *
     * @param room: the room that the player is in
     */
    public void setCurrRoom(Room room) {
        this.currRoom = room;
    }

    /**
     * Sets the existence status of the item.
     *
     * @param bool True if the item exists, false otherwise.
     */
    public void setDoesExits(boolean bool) {
        this.doesExits = bool;
    }

    /**
     * Sets the bag of items carried by the player.
     *
     * @param bag The bag of items.
     */
    public void setPlayerBag(Bag bag) {
        this.playerBag = bag;
    }

    /**
     * Returns the bag of items carried by the player.
     *
     * @return The bag of items.
     */
    public Bag getPlayerBag() {
        return playerBag;
    }
    /**
     * Method to solve a puzzle in the current room.
     * Checks if there is an active puzzle in the current room.
     * If yes, sets the puzzle state to solved and prints a message.
     * If no, prints a message indicating no active puzzle.
     */
    public void solvePuzzle() {
        if (this.getCurrRoom().getPuzzleState()) {
            this.getCurrRoom().setPuzzleState(false);
            System.out.println(this.getName() +
                    " is solving the puzzle in " + this.getCurrRoom().getName() + ".");
        } else {
            System.out.println("There is no active puzzle in "
                    + this.getCurrRoom().getName() + ".");
        }
    }

    /**
     * Compares players based on the sum of the values of items in their bags.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Player)) {
            return false;
        }
        Player otherPlayer = (Player) other;

        int thisSumOfBagItems = 0;
        int otherSumOfBagItems = 0;

        for (int i = 0; i < this.playerBag.size; i++) {
            if (this.playerBag.bag[i] != null) {
                thisSumOfBagItems += this.playerBag.bag[i].getValue();
            }
        }
        for (int i = 0; i < otherPlayer.playerBag.size; i++) {
            if (otherPlayer.playerBag.bag[i] != null) {
                otherSumOfBagItems += otherPlayer.playerBag.bag[i].getValue();
            }
        }

        return thisSumOfBagItems == otherSumOfBagItems;
    }

    /**
     * Calculates the hash code based on the sum of values of items in the player's bag.
     *
     * @return The hash code of the Player object.
     */
    @Override
    public int hashCode() {
        int sumOfBagItems = 0;
        for (int i = 0; i < this.playerBag.size; i++) {
            if (this.playerBag.bag[i] != null) {
                sumOfBagItems += this.playerBag.bag[i].getValue();
            }
        }
        return sumOfBagItems;
    }
}
