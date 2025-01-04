public class MainNew {
    public static void main(String[] args) {
        try {
            System.out.println("Test 1 starts");
            test1();
            System.out.println("Test 1 done");
            System.out.println("--------------------------------------------");
        }catch (Exception e){
            System.out.println("exception " + e);
        }

        try {
            System.out.println("Test 2 starts");
            test2();
            System.out.println("Test 2 done");
            System.out.println("--------------------------------------------");
        }catch (Exception e){
            System.out.println("exception " + e);
        }

        try {
            System.out.println("Test 3 starts");
            test3();
            System.out.println("Test 3 done");
            System.out.println("--------------------------------------------");
        }catch (Exception e){
            System.out.println("exception " + e);
        }
        try {
            System.out.println("Test 4 starts");
            test4();
            System.out.println("Test 4 done");
            System.out.println("--------------------------------------------");
        }catch (Exception e){
            System.out.println("exception " + e);
        }



    }

    /**
     * Run first test of hw3
     */
    public static void test1(){
        GameManager gameManager1 = new GameManager();
        GameManager gameManager2 = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 1");
        Room room4 = new Room("Room 2");

        gameManager1.addRoom(room1);
        gameManager1.addRoom(room2);

        gameManager2.addRoom(room3);
        gameManager2.addRoom(room4);

        System.out.println("Are room1 and room3 equal: " + room1.equals(room3));

        Item item1 = new Relic("Item 1", 9);
        Item item2 = new Relic("Item 1", 4);
        Relic relic1 = new Relic("Relic 1", 5);

        gameManager1.addItem(room3, item1);
        System.out.println("Are room1 and room3 equal: " + room1.equals(room3));
        gameManager2.addItem(room3, item2);
        System.out.println("Are room1 and room3 equal: " + room1.equals(room3));
        gameManager2.addItem(room3, relic1);
        System.out.println("Are room1 and room3 equal: " + room1.equals(room3));
        gameManager1.activatePuzzle(room1);
        System.out.println("Are room1 and room3 equal: " + room1.equals(room3));
        System.out.println("Are room3 and room1 equal: " + room3.equals(room1));
    }
    /**
     * Run second test of hw3
     */
    public static void test2(){
        GameManager gameManager1 = new GameManager();
        GameManager gameManager2 = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 1");
        Room room4 = new Room("Room 2");

        gameManager1.addRoom(room1);
        gameManager1.addRoom(room2);

        gameManager2.addRoom(room3);
        gameManager2.addRoom(room4);

        Item item1 = new Relic("Item 1", 9);
        Item item2 = new Relic("Item 1", 4);
        Relic relic1 = new Relic("Relic 1", 5);

        gameManager1.addItem(room3, item1);

        gameManager2.addItem(room3, item2);

        gameManager2.addItem(room3, relic1);


        Player player1 = new Player("Player 1", 2);
        Player player2 = new Player("Player 2", 2);

        gameManager1.addPlayer(player1);
        gameManager1.startPlayer(room1);
        gameManager2.addPlayer(player2);
        gameManager2.startPlayer(room3);

        System.out.println("Are player1 and player2 equal: " + player1.equals(player2));
        System.out.println("Are player2 and player1 equal: " + player2.equals(player1));

        gameManager1.pickUpItem(item1);
        System.out.println("Are player1 and player2 equal: " + player1.equals(player2));
        System.out.println("Are player2 and player1 equal: " + player2.equals(player1));
        gameManager2.pickUpItem(item2);
        System.out.println("Are player1 and player2 equal: " + player1.equals(player2));
        System.out.println("Are player2 and player1 equal: " + player2.equals(player1));
        gameManager2.pickUpItem(relic1);

        System.out.println("Are player1 and player2 equal: " + player1.equals(player2));
        System.out.println("Are player2 and player1 equal: " + player2.equals(player1));
    }
    /**
     * Run third test of hw3
     */
    public static void test3(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 2");


        Item item1 = new Relic("Item 1", 2);
        Item item2 = new Key("Key 2", 6);
        Item item3 = new Relic("Item 3", 7);

        gameManager.addItem(room1, item1);

        gameManager.addRoom(room1);
        gameManager.addRoom(room2);

        gameManager.removeRoom(room3);
        gameManager.addRoom(room2);

        gameManager.addItem(room2, item1);
        gameManager.addItem(room2, item2);
        gameManager.addItem(room1, item3);

        Player player = new Player("Player 1", 4);

        gameManager.addPlayer(player);
        gameManager.startPlayer(room1);

        gameManager.connectRooms(room1, room2, Direction.NORTH);

        gameManager.pickUpItem(item3);
        gameManager.activatePuzzle(room1);

        gameManager.movePlayer(Direction.NORTH);
        gameManager.solvePuzzle();
        gameManager.movePlayer(Direction.NORTH);

        gameManager.solvePuzzle();
        gameManager.pickUpItem(item1);
        gameManager.activatePuzzle(room2);

        gameManager.movePlayer(Direction.SOUTH);
        gameManager.dropItem(item3);
        gameManager.pickUpItem(item3);
        gameManager.useItem(item2);
        gameManager.movePlayer(Direction.SOUTH);

        Item item4 = new Key("Item 4", 7);
        gameManager.dropItem(item4);
        gameManager.dropItem(item1);
        gameManager.addItem(room1, item4);
        gameManager.activatePuzzle(room1);
        gameManager.useItem(item4);
        gameManager.movePlayer(Direction.NORTH);
        gameManager.activatePuzzle(room1);
        gameManager.deactivatePuzzle(room1);

        gameManager.addItem(room1, new Relic("Temp 1", 9));
        gameManager.addItem(room1, new Relic("Temp 2", 3));
    }
    /**
     * Run fourth test of hw3
     */
    public static void test4(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");


        LargeBag item1 = new LargeBag("Bag 1", 5, 3);
        Key item2 = new Key("Key 2", 6);
        Relic item3 = new Relic("Item 3", 7);
        Item item4 = new Relic("Relic 4", 2);

        gameManager.addItem(room1, item1);

        gameManager.addRoom(room1);
        gameManager.addRoom(room2);

        gameManager.connectRooms(room1, room2, Direction.NORTH);

        gameManager.addItem(room2, item1);
        gameManager.addItem(room2, item2);
        gameManager.addItem(room1, item3);
        gameManager.addItem(room1, item4);

        Player player = new Player("Player 1", 2);

        gameManager.addPlayer(player);
        gameManager.startPlayer(room1);

        gameManager.pickUpItem(item3);
        gameManager.pickUpItem(item4);

        gameManager.movePlayer(Direction.NORTH);
        gameManager.solvePuzzle();

        gameManager.useItem(item4);
        gameManager.useItem(item3);

        gameManager.useItem(item1);
        gameManager.useItem(item2);
        gameManager.pickUpItem(item2);
        gameManager.pickUpItem(item1);

        gameManager.activatePuzzle(room2);
        gameManager.movePlayer(Direction.SOUTH);

        gameManager.useItem(item4);
        gameManager.useItem(item3);



        Bag item5 = new Bag("Bag 2", 3, 2);
        gameManager.addItem(room1, item5);
        gameManager.pickUpItem(item5);

        LargeBag item6 = new LargeBag("Bag 3", 7, 1);
        gameManager.addItem(room1, item6);
        gameManager.pickUpItem(item6);

        Relic item7 = new Relic("Relic 7", 8);
        gameManager.addItem(room1, item7);
        gameManager.pickUpItem(item7);

        Bag item8 = new Bag("Bag 8", 3, 2);
        gameManager.addItem(room1, item8);
        gameManager.pickUpItem(item8);
    }
}
