import java.util.*;

abstract class Room {
    protected String roomType;

    public Room(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomType() {
        return roomType;
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room");
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room");
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room");
    }
}

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n===== CURRENT INVENTORY =====");
        for (String key : inventory.keySet()) {
            System.out.println(key + " → " + inventory.get(key));
        }
    }
}

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // removes from queue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (CORE LOGIC)
class BookingService {

    private RoomInventory inventory;

    // Track allocated rooms
    private HashMap<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "_" + UUID.randomUUID().toString().substring(0, 5);
    }

    public void processBookings(BookingRequestQueue queue) {

        System.out.println("\n===== PROCESSING BOOKINGS =====");

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();

            String roomType = request.getRoomType();
            String guest = request.getGuestName();

            System.out.println("\nProcessing request for: " + guest);

            // Step 1: Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Step 2: Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Step 3: Store in Set (uniqueness ensured)
                allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                allocatedRooms.get(roomType).add(roomId);

                // Step 4: Update inventory (atomic logic)
                inventory.decrementRoom(roomType);

                // Step 5: Confirm booking
                System.out.println("Booking CONFIRMED for " + guest);
                System.out.println("Room Type: " + roomType);
                System.out.println("Allocated Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + guest + " (No rooms available)");
            }
        }

        System.out.println("\n===== ALLOCATION COMPLETE =====");
    }

    public void displayAllocatedRooms() {
        System.out.println("\n===== ALLOCATED ROOMS =====");

        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + " → " + allocatedRooms.get(type));
        }
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Step 1: Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // Step 2: Create Queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Step 3: Add Requests
        queue.addRequest(new Reservation("Priyanshu", "Single Room"));
        queue.addRequest(new Reservation("Amit", "Single Room"));
        queue.addRequest(new Reservation("Neha", "Single Room")); // should fail

        BookingService bookingService = new BookingService(inventory);

        bookingService.processBookings(queue);

        bookingService.displayAllocatedRooms();
        inventory.displayInventory();
    }
}