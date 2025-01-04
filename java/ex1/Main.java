import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;  // Note: Do not change this line.

    /**
     * Scans the number of games, than plays the student game the specified amount of times.
     *
     * @param args (String[]) args[0] is the path to the input file
     */
    public static void main(String[] args) throws IOException {
        String path = args[0];
        scanner = new Scanner(new File(path));
        int numberOfGames = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= numberOfGames; i++) {
            System.out.println("Game number " + i + " starts.");
            theStudentsGame();
            System.out.println("Game number " + i + " ended.");
            System.out.println("-----------------------------------------------");
        }
        System.out.println("All games have ended.");
    }

    /**
     * Add your description
     */
    public static void theStudentsGame() {
        int[][][] semestersBox;
    }
}
