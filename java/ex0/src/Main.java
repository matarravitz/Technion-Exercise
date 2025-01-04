import java.util.Scanner;
public class Main {

    public static int get_size() {
        int MIN_SIZE = 6;
        System.out.print("Enter the size of the head: ");
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        while (size < MIN_SIZE) {
            System.out.print("Invalid size. Try again: ");
            size = scanner.nextInt();
        }
        return size;
    }
    public static int get_left_eye_column(int size) {
        System.out.print("Enter the left eye's column: ");
        Scanner scanner = new Scanner(System.in);
        int left_eye_column = scanner.nextInt();
        while (left_eye_column <= 0 || left_eye_column >= size / 2) {
            System.out.print("Invalid column. Try again: ");
            left_eye_column = scanner.nextInt();
        }
        return left_eye_column;
    }
    public static String[] get_characters() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter three chars: ");
        String str = scanner.nextLine();
        return str.split(" ");
    }
    public static void main(String[] args) {
        int EYES_ROW = 2;
        int size = get_size();
        int left_eye_column = get_left_eye_column(size);
        String[] characters = get_characters();
        String head = characters[0];
        String eyes = characters[1];
        String mouth = characters[2];

        int right_eye_column = size - 1 - left_eye_column;
        int mouth_row = size - 1 - EYES_ROW;

        for(int j = 0; j < size; j++){
            System.out.print(head);
            int i = 1;
            while (i < size - 1){
                if (j == 0 || j == size - 1){
                    System.out.print(head);
                } else if (j == EYES_ROW && (i == left_eye_column || i == right_eye_column)) {
                        System.out.print(eyes);
                } else if (j == mouth_row && i > left_eye_column && i < right_eye_column){
                    System.out.print(mouth);
                } else {
                    System.out.print(" ");
                }
                i += 1;
            }
            if (j != size -1){System.out.println(head);}
            else {
                System.out.print(head);
            }

        }
    }
}