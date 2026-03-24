import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public boolean isCancelled() { return isCancelled; }

    public void cancel() {
        this.isCancelled = true;
    }
}

// Inventory Class
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }
}

// Booking History
class BookingHistory {
    private HashMap<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }
}

// Cancellation Service (CORE)
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback tracking (LIFO)
    private Stack<String> releasedRoomStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        try {
            // Step 1: Validate existence
            Reservation res = history.getReservation(reservationId);

            if (res == null) {
                throw new CancellationException("Reservation does not exist.");
            }

            if (res.isCancelled()) {
                throw new CancellationException("Reservation already cancelled.");
            }

            // Step 2: Record rollback (Stack)
            releasedRoomStack.push(res.getRoomId());

            // Step 3: Restore Inventory
            inventory.incrementRoom(res.getRoomType());

            // Step 4: Mark cancelled
            res.cancel();

            // Step 5: Confirm
            System.out.println("Cancellation SUCCESS for Reservation ID: " + reservationId);

        } catch (CancellationException e) {
            System.out.println("Cancellation FAILED: " + e.getMessage());
        }
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack (Recent Releases): " + releasedRoomStack);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Setup
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed bookings
        Reservation r1 = new Reservation("R101", "Priyanshu", "Single Room", "S1");
        Reservation r2 = new Reservation("R102", "Amit", "Double Room", "D1");

        history.addReservation(r1);
        history.addReservation(r2);

        // Step 3: Cancellation Service
        CancellationService service = new CancellationService(inventory, history);

        // Step 4: Test cases

        // Valid cancellation
        service.cancelBooking("R101");

        // Duplicate cancellation
        service.cancelBooking("R101");

        // Invalid reservation
        service.cancelBooking("R999");

        // Show rollback stack
        service.showRollbackStack();
    }
}