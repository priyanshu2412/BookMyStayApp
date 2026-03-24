import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
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
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void decrementRoom(String roomType) throws InvalidBookingException {
        int available = getAvailability(roomType);

        if (available < 0) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (available == 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        inventory.put(roomType, available - 1);
    }
}

class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (inventory.getAvailability(reservation.getRoomType()) == -1) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available.");
        }
    }
}

class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(Reservation reservation) {

        try {
            // Step 1: Validate (Fail Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate Room
            inventory.decrementRoom(reservation.getRoomType());

            // Step 3: Confirm
            System.out.println("Booking SUCCESS for " + reservation.getGuestName() +
                    " (" + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking FAILED: " + e.getMessage());
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        BookingService service = new BookingService(inventory);

        service.bookRoom(new Reservation("Priyanshu", "Single Room"));

        service.bookRoom(new Reservation("Amit", "Deluxe Room"));

        service.bookRoom(new Reservation("Neha", "Single Room"));

        service.bookRoom(new Reservation("", "Double Room"));
    }
}