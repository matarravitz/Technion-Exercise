/**
 * Represents a bag that can hold items for a player.
 */
public class Bag extends Item implements Cloneable{
    protected int size;
    protected int maxSize;
    protected Item[] bag;

    /**
     * Constructs a new bag with selected maximum size.
     *
     * @param maxSize The maximum size of the bag.
     */
    public Bag(String name, int maxSize, int value) {
        super(name, value);
        this.maxSize = maxSize;
        this.bag = new Item[maxSize];
        this.size = 0;
    }

    /**
     * Gets the maximum size of the bag.
     *
     * @return The maximum size of the bag.
     */
    public int getMaxSize() {
        return maxSize;
    }
    /**
     * Sets the bag size.
     *
     * @param i The increasing of the size.
     */
    public void setSize(int i) {
        this.size = size + i;
    }

    /**
     * Gets the current size of the bag.
     *
     * @return The current size of the bag.
     */
    public int getSize() {
        return size;
    }
    /**
     * Gets the item at the wanted index in the bag.
     *
     * @param i The index of the item to retrieve.
     * @return The item at the specified index.
     */
    public Item getItemInBag(int i) {
        return bag[i];
    }
    /**
     * Sets the item at the wanted index in the bag to null.
     *
     * @param i The index of the item to set to null.
     */
    public void setItemNull(int i) {
        this.bag[i] = null;
    }
    /**
     * Sets the item at the wanted index in the bag.
     *
     * @param item The item to set.
     * @param i    The index where the item should be set.
     */
    public void setItem(Item item, int i) {
        this.bag[i] = item;
    }

    /**
     * Increases the size of the bag by the wanted amount.
     *
     * @param i The amount by which to increase the size of the bag.
     */
    public void addToBagSize(int i) {
        this.size = this.size + i;
    }
    /**
     * Method to transfer the player bag to a new bag.
     * @param player The player whose switching bags.
     */
    @Override
    protected void useItem(Player player) {
        Bag temp = (Bag) findItem(player);
        if(temp == null){
            System.out.println(this.getName() +" is not near "+ player.getName()+".");
            return;
        }
        if (temp.inPlayersBag || temp.getCurrRoom().equals(player.getCurrRoom())) {
            if (temp.maxSize >= player.playerBag.size) {
                int i = 0;
                int j = 0;
                while (i < temp.maxSize && j < player.playerBag.maxSize) {
                    if (player.playerBag.bag[j] != null) {
                        temp.bag[i] = player.playerBag.bag[j];
                        temp.size++;
                        i++;
                    }
                    j++;
                }
                if ((player.playerBag instanceof LargeBag || !(temp instanceof LargeBag))
                        || temp.maxSize < temp.size + 1) {
                    player.playerBag.disassembleItem(player);
                } else {
                    temp.bag[maxSize - 1] = player.playerBag;
                }
                player.playerBag = temp;
                System.out.println(player.getName() + " is now carrying " + temp.getName() + ".");
                for(int k = 0; k < player.getCurrRoom().getMaxNumberOfItems(); k++ ){
                    if(player.getCurrRoom().getItemsInRoom(k).equals(temp)){
                        player.getCurrRoom().setItemNull(k);
                        player.getCurrRoom().setNumberOfItems(-1);
                        break;
                    }
                }
                return;
            }
            System.out.println(temp.getName() + "  is too small.");
        }
    }

    /**
     * Method to calculate the total value of items in the bag.
     * @return The total value of items in the bag.
     */
    public int getItemsValue(){
        int count = 0;
        for (int i = 0; i < maxSize; i++){
            if(this.bag[i] != null) {
                count = count + bag[i].getValue();
            }
        }
        return count;
    }

    /**
     * Equals method to compare two Bag objects.
     * @param other The object to compare with.
     * @return True if the bags are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other){
        if (!(other instanceof Bag)){
            return false;
        }
        Bag bag = (Bag) other;
        return  this.value == bag.value &&
                this.getItemsValue() == bag.getItemsValue() &&
                this.maxSize == bag.maxSize &&
                this.hashCode() == bag.hashCode();
    }

    /**
     * Hashcode method to generate a hashcode for the Bag object.
     * @return The hashcode of the Bag object.
     */
    @Override
    public int hashCode(){
        return  17 * this.getItemsValue() +
                19 * this.value + 23 * this.maxSize;
    }
    /**
     * Creates and returns a deep copy of this Bag object.*
     *
     * @return A deep copy of this Bag object.
     */
    @Override
    public Bag clone() {
            Bag clone = (Bag) super.clone();
            clone.bag = new Item[this.bag.length];

            for (int i = 0; i < this.bag.length; i++) {
                if (this.bag[i] != null) {
                    clone.bag[i] = this.bag[i] != null ? this.bag[i].clone(): null;
                }
            }
            return clone;
    }
}
