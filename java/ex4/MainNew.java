public class MainNew {
    public static void main(String[] args) {
        try {
            System.out.println("Test 1 starts");
            test1();
            System.out.println("Test 1 done");
            System.out.println("--------------------------------------------");
        } catch (Exception e) {
            System.out.println("exception " + e);
        }

        try {
            System.out.println("Test 2 starts");
            test2();
            System.out.println("Test 2 done");
            System.out.println("--------------------------------------------");
        } catch (Exception e) {
            System.out.println("exception " + e);
        }

        try {
            System.out.println("Test 3 starts");
            test3();
            System.out.println("Test 3 done");
            System.out.println("--------------------------------------------");
        } catch (Exception e) {
            System.out.println("exception " + e);
        }

        try {
            System.out.println("Test 4 starts");
            test4();
            System.out.println("Test 4 done");
            System.out.println("--------------------------------------------");
        } catch (Exception e) {
            System.out.println("exception " + e);
        }
    }

    public static void test1(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 3");
        Room room4 = new Room("Room 4");
        Room room5 = new Room("Room 5");
        Room room6 = new Room("Room 6");
        Room room7 = new Room("Test room");

        gameManager.addRoom(room1, null, null);
        gameManager.addRoom(room2, room1, Direction.NORTH);
        try {
            gameManager.addRoom(room3, room7, Direction.WEST);
        }
        catch(RoomDoesNotExist e){
            System.out.println("Room does not exist.");
        }
        gameManager.addRoom(room3, room2, Direction.WEST);
        try {
            gameManager.addRoom(room3, room2, Direction.WEST);
        }
        catch(ExitIsOccupied e){
            System.out.println("Exit is occupied.");
        }
        gameManager.addRoom(room4, room2, Direction.EAST);
        gameManager.addRoom(room5, room1, Direction.SOUTH);
        gameManager.addRoom(room6, room2, Direction.NORTH);
    }

    public static void test2(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 3");
        Room room4 = new Room("Room 4");
        Room room5 = new Room("Room 5");
        Room room6 = new Room("Room 6");

        gameManager.addRoom(room1, null, null);
        gameManager.addRoom(room2, room1, Direction.NORTH);
        gameManager.addRoom(room3, room2, Direction.WEST);
        gameManager.addRoom(room4, room2, Direction.EAST);
        gameManager.addRoom(room5, room1, Direction.SOUTH);
        gameManager.addRoom(room6, room2, Direction.NORTH);

        GameManager clone = gameManager.clone();
        QuartNode<Room> roomNode = clone.getRooms().getRoot();

        boolean areSame = roomNode.getValue() == room1;
        System.out.println("Are room1 and room2 the same: " + areSame);
        System.out.println("Are room1 and room2 equals: " + roomNode.getValue().equals(room1));

        roomNode = roomNode.getNeighbor(Direction.NORTH);

        areSame = roomNode.getValue() == room2;
        System.out.println("Are room1 and room2 the same: " + areSame);
        System.out.println("Are room1 and room2 equals: " + roomNode.getValue().equals(room2));
    }

    public static void test3(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");

        gameManager.addRoom(room1, null, null);

        Relic relic = new Relic("Relic 1", 8);
        gameManager.addItem(room1, relic);

        GameManager clone = gameManager.clone();
        QuartNode<Room> roomNode = clone.getRooms().getRoot();

        Room room = roomNode.getValue();

        boolean areSame = room.getItems() == room1.getItems();
        System.out.println("Are items the same: " + areSame);


        Item itemCloned = null;
        for (Item item: room.getItems()){
            if (item != null){
                itemCloned = item;
                break;
            }
        }

        areSame = itemCloned == relic;
        System.out.println("Are cloned item and relic the same: " + areSame);
        System.out.println("Are cloned item and relic equals: " + itemCloned.equals(relic));
    }
    public static void test4(){
        GameManager gameManager = new GameManager();

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");

        gameManager.addRoom(room1, null, null);
        gameManager.addRoom(room2, room1, Direction.NORTH);

        Player player = new Player("Player 1", 5);

        gameManager.addPlayer(player);
        gameManager.startPlayer(room1);

        GameManager cloned = gameManager.clone();

        gameManager.removeRoom(room2);
        cloned.movePlayer(Direction.NORTH);
        gameManager.movePlayer(Direction.NORTH);
    }
}
