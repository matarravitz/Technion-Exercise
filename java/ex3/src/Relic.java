/**
 * Represents a relic item in the game.
 * Extends the Item class.
 */
public class Relic extends Item {

    /**
     * Constructor for the Relic class.
     * @param name The name of the relic.
     * @param value The value of the relic.
     */
    public Relic(String name, int value) {
        super(name, value);
    }

    /**
     * Method to use the relic item.
     * Checks if the relic is present in the current room or player's bag, and prints a corresponding message.
     * @param player The player using the relic.
     */
    @Override
    protected void useItem(Player player) {
        Relic temp = (Relic) findItem(player);
        int foundRelic = 0;

        // Check if the relic is present in the current room
        for(int i = 0; i < player.getCurrRoom().getMaxNumberOfItems(); i++){
            if (player.getCurrRoom().items[i] != null && player.getCurrRoom().items[i].equals(this)){
                foundRelic++;
            }
        }
        // Check if the relic is present in the player's bag
        for(int i = 0; i < player.playerBag.maxSize; i++){
            if (player.playerBag.bag[i] != null && player.playerBag.bag[i].equals(this)){
                foundRelic++;
            }
        }

        // Print a message based on whether the relic is found or not
        if(foundRelic > 0) {
            System.out.println(player.getName() + " is inspecting " + temp.getName() + ".");
        } else {
            System.out.println(temp.getName() + " is not near " + player.getName() + ".");
        }
    }

    /**
     * Equals method to compare two Relic objects.
     * Relics are considered equal if they have the same value.
     * @param other The object to compare with.
     * @return True if the relics are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Relic)) {
            return false;
        }
        Relic otherRelic = (Relic) other;
        return this.getValue() == otherRelic.getValue();
    }

    /**
     * Hashcode method to generate a hashcode for the Relic object.
     * Uses the value of the relic for generating hashcode.
     * @return The hashcode of the Relic object.
     */
    @Override
    public int hashCode(){
        return this.getValue();
    }
}
