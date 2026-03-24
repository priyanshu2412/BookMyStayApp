import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
    }
}

// Booking History (CORE STORAGE)
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to history: " + reservation.getReservationId());
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display history
    public void displayHistory() {
        System.out.println("\n===== BOOKING HISTORY =====");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : history) {
            r.display();
        }

        System.out.println("===========================");
    }
}

// Reporting Service (READ-ONLY)
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Generate summary report
    public void generateSummaryReport() {

        System.out.println("\n===== BOOKING SUMMARY REPORT =====");

        List<Reservation> reservations = history.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        HashMap<String, Integer> roomTypeCount = new HashMap<>();

        // Count bookings by room type
        for (Reservation r : reservations) {
            String type = r.getRoomType();
            roomTypeCount.put(type, roomTypeCount.getOrDefault(type, 0) + 1);
        }

        // Display report
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " → Total Bookings: " + roomTypeCount.get(type));
        }

        System.out.println("Total Reservations: " + reservations.size());
        System.out.println("==================================");
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Initialize Booking History
        BookingHistory history = new BookingHistory();

        // Step 2: Add Confirmed Reservations (Simulated from UC6)
        history.addReservation(new Reservation("RES101", "Priyanshu", "Single Room", "SI_123"));
        history.addReservation(new Reservation("RES102", "Amit", "Double Room", "DO_456"));
        history.addReservation(new Reservation("RES103", "Neha", "Single Room", "SI_789"));

        // Step 3: Display Full History
        history.displayHistory();

        // Step 4: Generate Report
        BookingReportService reportService = new BookingReportService(history);
        reportService.generateSummaryReport();
    }
}