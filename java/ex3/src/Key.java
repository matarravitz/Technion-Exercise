/**
 * Represents a key item in the game.
 * Extends the Item class.
 */
public class Key extends Item {

    /**
     * Constructor for the Key class.
     * @param name The name of the key.
     * @param value The value of the key.
     */
    public Key(String name, int value) {
        super(name, value);
    }

    /**
     * Method to use the key item.
     * If the key is used, it unlocks a puzzle in the current room.
     * @param player The player using the key.
     */
    @Override
    protected void useItem(Player player) {
        Key temp = (Key) findItem(player);
        if (temp.getCurrRoom().useKeyStatus) {
            System.out.println(player.getName() + " was already unlocked.");
            return;
        }
        if (!(temp.inPlayersBag || temp.currRoom.equals(player.getCurrRoom()))) {
            System.out.println(this.getName() + " is not near " + player.getName() + ".");
            return;
        }
        currRoom.setPuzzleState(false);
        temp.currRoom.setUseKeyStatus(true);
        temp.currRoom.setUsedKey(temp);
        temp.disassembleItemWithoutOutput(player);
        System.out.println(player.getName() + " used " + temp.getName() +
                " in " + player.getCurrRoom().getName() + ".");
    }

    /**
     * Equals method to compare two Key objects.
     * Keys are considered equal if they have the same value.
     * @param other The object to compare with.
     * @return True if the keys are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Key)) {
            return false;
        }
        Key otherKey = (Key) other;
        return this.getValue() == otherKey.getValue();
    }

    /**
     * Hashcode method to generate a hashcode for the Key object.
     * Uses the value of the key for generating hashcode.
     * @return The hashcode of the Key object.
     */
    @Override
    public int hashCode() {
        return this.getValue();
    }
}
