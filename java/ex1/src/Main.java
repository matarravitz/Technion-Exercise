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
     * Change the board status based on certain rules.
     *
     * @param board The current state of the board.
     * @return A new board representing the updated board status after one semester.
     */
    public static int[][] changeStatus(int[][] board) {
        int[][] newBoard = copyBoard(board);
        int row = board.length;
        int column = board[0].length;
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < column; k++) {
                int countTakin = 0;
                if (i > 0) {
                    countTakin += board[i - 1][k];
                    if (k > 0) {
                        countTakin += board[i - 1][k - 1];
                    }
                    if (k < column - 1) {
                        countTakin += board[i - 1][k + 1];
                    }
                }

                // Check the tiles in the same row
                if (k > 0) {
                    countTakin += board[i][k - 1];
                }
                if (k < column - 1) {
                    countTakin += board[i][k + 1];
                }

                // Check the tiles in the row below
                if (i < row - 1) {
                    countTakin += board[i + 1][k];
                    if (k > 0) {
                        countTakin += board[i + 1][k - 1];
                    }
                    if (k < column - 1) {
                        countTakin += board[i + 1][k + 1];
                    }
                }
                switch (countTakin) {
                    case 2:
                        if (board[i][k] == 0) {
                            newBoard[i][k] = 0;
                        }
                        if (board[i][k] == 1) {
                            newBoard[i][k] = 1;
                        }
                        break;
                    case 3:
                        newBoard[i][k] = 1;
                        break;
                    case 0:
                    case 1:
                    case 4:
                    default:
                        newBoard[i][k] = 0;
                }
            }

        }return newBoard;
    }
    /**
     * Counts the number of cells with Academically improper status (0) on the board.
     *
     * @param board The current state of the board.
     * @return The count of cells with Academically improper status.
     */
    public static int countNotTakin(int[][] board) {
        int count = 0;
        int row = board.length;
        int column = board[0].length;
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < column; k++) {
                if (board[i][k] == 0) {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * Checks if all students are Academically improper.
     *
     * @param board The current state of the board.
     * @return True if all the students are Academically improper, false otherwise.
     */
    public static boolean checkNotTakin(int[][] board) {
        int notTakin = countNotTakin(board);
        return notTakin == board.length * board[0].length;
    }
    /**
     * Prints the current state of the board for a given semester, including the number of students.
     *
     * @param board The current state of the board.
     */
    public static void printBoard(int[][] board, int i) {
        int semester = i + 1;
        System.out.println("Semester number " + semester + ":");
        int row = board.length;
        int column = board[0].length;
        for (int j = 0; j < row; j++) {
            for (int k = 0; k < column - 1; k++) {
                if (board[j][k] == 0) {
                    System.out.print("-");
                }
                if (board[j][k] == 1) {
                    System.out.print("X");
                }
            }
            if (board[j][column-1] == 0) {
                System.out.println("-");
            }
            if (board[j][column-1] == 1) {
                System.out.println("X");
            }
        }int notTakin = countNotTakin(board);
        int takin = row*column - notTakin;
        System.out.println("Number of students: " + takin);
        System.out.println();
    }
    /**
     * Checks if the status of the board has stabilized between two semesters.
     * @param semestersBox A 3D array containing the status of each cell for each semester.
     * @return True if the status has stabilized, false otherwise.
     */
    public static boolean checkStability(int i, int[][][] semestersBox){
        int[][] prevBoard = semestersBox[i-1];
        int[][] currBoard = semestersBox[i];
        int row = prevBoard.length;
        int column = prevBoard[0].length;
        for (int k = 0; k < row; k++) {
            for (int j = 0; j < column; j++) {
                if(prevBoard[k][j] != currBoard[k][j]){return false;}
            }
        }
        return true;
    }
    /**
     * Checks if the given row and column indices are within the boundaries of the board.
     *
     * @param row The row index.
     * @param column The column index.
     * @param board The current state of the board.
     * @return True if the chosen student are within the board boundaries, false otherwise.
     */
    public static boolean checkInRange(int row, int column, int[][] board) {
        return !(row >= board.length || column >= board[0].length || row < 0 || column < 0);
    }
    /**
     * Creates a deep copy of the given board.
     *
     * @param board The current state of the board.
     * @return A new 2D array representing a copy of the board.
     */
    public static int[][] copyBoard(int[][] board){
        int row = board.length;
        int column = board[0].length;
        int[][] copy = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < column; k++) {
                copy[i][k] = board[i][k];
            }
        }
            return copy;
    }

    /**
     * Entry point for the student game.
     * Allows the president to choose the board size and the initial status of students on the board.
     * Print board for every semester.
     */
    public static void theStudentsGame() {
        int[][][] semestersBox;
        System.out.println("Dear president, please enter the board’s size.");
        String boardSize = scanner.nextLine();
        String[] dimensions = boardSize.split(" X ");
        int row = Integer.parseInt(dimensions[0]);
        int column = Integer.parseInt(dimensions[1]);
        int[][] gameBoard = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < column; k++) {
                gameBoard[i][k] = 0;
            }
        }
        boolean flag = false;
        while (true) {
            if(!flag) {
                System.out.println("Dear president, please enter the cell’s indexes.");
            }
            String chosenIndex = scanner.nextLine();
            if (chosenIndex.equals("Yokra")) {
                break;
            }
            String[] chosenIndex1 = chosenIndex.split(", ");
            int chosenRow = Integer.parseInt(chosenIndex1[0]);
            int chosenColumn = Integer.parseInt(chosenIndex1[1]);
            if (checkInRange(chosenRow, chosenColumn, gameBoard)) {
                gameBoard[chosenRow][chosenColumn] = gameBoard[chosenRow][chosenColumn] ^ 1;
                flag = false;
            } else {
                System.out.println("The cell is not within the board’s boundaries, enter a new cell.");
                flag = true;
            }
        }
        semestersBox = new int[100][row][column];
        semestersBox[0] = gameBoard;
        for (int i = 0; i < 100; i++) {
            if (i != 0) {
                if (checkStability(i, semestersBox)) {
                    System.out.println("The students have stabilized.");
                    break;
                }
            }
            printBoard(semestersBox[i], i);
            if (checkNotTakin(semestersBox[i])) {
                System.out.println("There are no more students.");
                break;
            }
            int[][] nextSemester = copyBoard(semestersBox[i]);
            nextSemester = changeStatus(nextSemester);
            if (i != 99) {
                semestersBox[i + 1] = nextSemester;
            }
            if(i == 99){
                System.out.println("The semesters limitation is over.");
                break;
            }
        }
    }
}
