public class MainNew {
    public static void main(String[] args) {
        System.out.println("Test 1 starts");
        test1();
        System.out.println("Test 1 done");
        System.out.println("--------------------------------------------");

        System.out.println("Test 2 starts");
        test2();
        System.out.println("Test 2 done");
        System.out.println("--------------------------------------------");

        System.out.println("Test 3 starts");
        test3();
        System.out.println("Test 3 done");
        System.out.println("--------------------------------------------");

        System.out.println("Test 4 starts");
        test4();
        System.out.println("Test 4 done");
        System.out.println("--------------------------------------------");
    }

    /**
     * Run first test of hw2
     */
    public static void test1(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        gameManager.addRoom(room1);

        Player player = new Player("Player 1", 2);
        Item item1 = new Item("Item 1");

        gameManager.addPlayer(player);
        gameManager.addItem(room1, item1);


        gameManager.startPlayer(room1);

        Item item2 = new Item("Item 2");
        Item item3 = new Item("Item 3");

        gameManager.pickUpItem(item1);

        gameManager.addItem(room1, item2);
        gameManager.disassembleItem(item2);

        gameManager.dropItem(item1);
        gameManager.pickUpItem(item3);
        gameManager.pickUpItem(item1);

        gameManager.addItem(room1, item3);
        gameManager.pickUpItem(item3);
    }
    /**
     * Run second test of hw2
     */
    public static void test2(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        gameManager.addRoom(room1);

        Player player = new Player("Player 1", 0);

        gameManager.addPlayer(player);

        gameManager.startPlayer(room1);

        Item item1 = new Item("Item 1");
        Item item2 = new Item("Item 2");
        Item item3 = new Item("Item 3");

        gameManager.addItem(room1, item1);
        gameManager.addItem(room1, item2);

        gameManager.dropItem(item1);
        gameManager.pickUpItem(item1);
        gameManager.pickUpItem(item1);
        gameManager.pickUpItem(item2);

        gameManager.dropItem(item1);
        gameManager.pickUpItem(item3);
        gameManager.pickUpItem(item1);

        gameManager.addItem(room1, item3);
        gameManager.pickUpItem(item3);
        gameManager.disassembleItem(item2);
        gameManager.addItem(room1, item3);

    }
    /**
     * Run third test of hw2
     */
    public static void test3(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");

        Item item1 = new Item("Item 1");
        Item item2 = new Item("Item 2");
        Item item3 = new Item("Item 3");

        gameManager.addItem(room1, item1);

        gameManager.addRoom(room1);
        gameManager.addRoom(room2);

        gameManager.addItem(room2, item1);
        gameManager.addItem(room2, item2);
        gameManager.addItem(room1, item3);

        Player player = new Player("Player 1", 2);

        gameManager.addPlayer(player);
        gameManager.startPlayer(room1);

        gameManager.connectRooms(room1, room2, Direction.NORTH);

        gameManager.removeRoom(room2);
        gameManager.movePlayer(Direction.NORTH);

        gameManager.addRoom(room2);
        gameManager.movePlayer(Direction.NORTH);

        gameManager.connectRooms(room1, room2, Direction.NORTH);

        gameManager.movePlayer(Direction.NORTH);
        gameManager.pickUpItem(item2);
        gameManager.disassembleItem(item1);
    }
    /**
     * Run fourth test of hw2
     */
    public static void test4(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 3");


        Item item1 = new Item("Item 1");
        Item item2 = new Item("Item 2");
        Item item3 = new Item("Item 3");

        gameManager.addItem(room1, item1);

        gameManager.addRoom(room1);
        gameManager.addRoom(room2);
        gameManager.addRoom(room3);

        gameManager.connectRooms(room1, room2, Direction.NORTH);
        gameManager.connectRooms(room2, room3, Direction.NORTH);

        gameManager.addItem(room1, item1);
        gameManager.addItem(room2, item2);
        gameManager.addItem(room3, item3);

        Player player = new Player("Player 1", 2);

        gameManager.addPlayer(player);
        gameManager.startPlayer(room1);

        gameManager.connectRooms(room1, room2, Direction.NORTH);
        gameManager.removeRoom(room2);

        gameManager.addRoom(room2);
        gameManager.connectRooms(room1, room2, Direction.NORTH);

        gameManager.movePlayer(Direction.NORTH);
        gameManager.movePlayer(Direction.NORTH);

        gameManager.connectRooms(room2, room3, Direction.NORTH);
        gameManager.movePlayer(Direction.NORTH);
    }

}
