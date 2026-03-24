import java.util.*;

// Reservation Class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// Thread-Safe Inventory
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
    }

    // Synchronized method (CRITICAL SECTION)
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }

        return false;
    }

    public synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// Thread-Safe Booking Queue
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation request;

            // Critical section for queue access
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) break;

            String guest = request.getGuestName();
            String roomType = request.getRoomType();

            // Critical section for allocation
            synchronized (inventory) {

                if (inventory.allocateRoom(roomType)) {
                    System.out.println(Thread.currentThread().getName() +
                            " → Booking SUCCESS for " + guest);
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            " → Booking FAILED for " + guest);
                }
            }
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Shared Resources
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Step 2: Simulate concurrent requests
        queue.addRequest(new Reservation("Priyanshu", "Single Room"));
        queue.addRequest(new Reservation("Amit", "Single Room"));
        queue.addRequest(new Reservation("Neha", "Single Room")); // should fail

        // Step 3: Create multiple threads
        Thread t1 = new BookingProcessor(queue, inventory);
        Thread t2 = new BookingProcessor(queue, inventory);

        // Step 4: Start threads
        t1.start();
        t2.start();

        // Step 5: Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        // Step 6: Final Inventory
        System.out.println("\nFinal Availability: " +
                inventory.getAvailability("Single Room"));
    }
}