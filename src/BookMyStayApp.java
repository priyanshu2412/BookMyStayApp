import java.io.*;
import java.util.*;

// Reservation Class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// Inventory Class (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }

    public void displayInventory() {
        System.out.println("\n===== INVENTORY =====");
        for (String key : inventory.keySet()) {
            System.out.println(key + " → " + inventory.get(key));
        }
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getHistory() {
        return history;
    }

    public void displayHistory() {
        System.out.println("\n===== BOOKING HISTORY =====");
        for (Reservation r : history) {
            r.display();
        }
    }
}

// Wrapper Class to Persist Entire System State
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// Persistence Service (CORE)
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("\nSystem state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("\nNo previous state found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("\nError loading state. Starting fresh.");
        }
        return null;
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Try loading existing state
        SystemState state = PersistenceService.load();

        RoomInventory inventory;
        BookingHistory history;

        if (state != null) {
            inventory = state.inventory;
            history = state.history;
        } else {
            // Fresh start
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        // Step 2: Simulate booking activity
        history.addReservation(new Reservation("RES201", "Priyanshu", "Single Room"));
        history.addReservation(new Reservation("RES202", "Amit", "Double Room"));

        // Step 3: Display current state
        history.displayHistory();
        inventory.displayInventory();

        // Step 4: Save state before shutdown
        SystemState newState = new SystemState(inventory, history);
        PersistenceService.save(newState);
    }
}