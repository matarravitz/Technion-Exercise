/**
 * Represents a puzzle in the game.
 */
public class Puzzle implements Cloneable {
    protected boolean puzzleState;

    /**
     * builder function
     */
    public Puzzle() {
        this.puzzleState = false;
    }

    /**
     * check whether there exists a puzzle
     * @return: existence of puzzle
     */
    public boolean getPuzzleState() {
        return puzzleState;
    }

    /**
     * setting whether the puzzle is solved or unsolved
     * @param state: the state of puzzle(solved or unsolved)
     */
    public void setPuzzleState(boolean state) {
        this.puzzleState = state;
    }
    /**
     * Creates and returns a deep copy of this Puzzle object.
     *
     * @return A deep copy of this Puzzle object.
     */
    @Override
    public Puzzle clone() {
        try {
            return (Puzzle) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}