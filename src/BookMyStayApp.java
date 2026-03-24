import java.util.*;

abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;
    protected double roomSize;

    public Room(String roomType, int numberOfBeds, double pricePerNight, double roomSize) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.roomSize = roomSize;
    }

    public String getRoomType() {
        return roomType;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000.0, 150.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price: ₹" + pricePerNight);
        System.out.println("Size: " + roomSize + " sq.ft");
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500.0, 250.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price: ₹" + pricePerNight);
        System.out.println("Size: " + roomSize + " sq.ft");
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000.0, 400.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price: ₹" + pricePerNight);
        System.out.println("Size: " + roomSize + " sq.ft");
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Read-only search method
    public void searchAvailableRooms(List<Room> rooms) {

        System.out.println("===== AVAILABLE ROOMS =====\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Validation → Only show available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available Rooms: " + available);
                System.out.println();
            }
        }

        System.out.println("===== END OF SEARCH =====");
    }
}

// Main Class
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Step 1: Create Room Objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Step 2: Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // Step 3: Create Search Service
        RoomSearchService searchService = new RoomSearchService(inventory);

        searchService.searchAvailableRooms(rooms);
    }
}