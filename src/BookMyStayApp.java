abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;
    protected double roomSize;

    // Constructor
    public Room(String roomType, int numberOfBeds, double pricePerNight, double roomSize) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.roomSize = roomSize;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 2000.0, 150.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price per Night: ₹" + pricePerNight);
        System.out.println("Room Size: " + roomSize + " sq.ft");
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 3500.0, 250.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price per Night: ₹" + pricePerNight);
        System.out.println("Room Size: " + roomSize + " sq.ft");
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 6000.0, 400.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price per Night: ₹" + pricePerNight);
        System.out.println("Room Size: " + roomSize + " sq.ft");
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        System.out.println("===== HOTEL ROOM DETAILS =====\n");

        System.out.println("--- Single Room ---");
        single.displayDetails();
        System.out.println("Available Rooms: " + singleAvailability + "\n");

        System.out.println("--- Double Room ---");
        doubleRoom.displayDetails();
        System.out.println("Available Rooms: " + doubleAvailability + "\n");

        System.out.println("--- Suite Room ---");
        suite.displayDetails();
        System.out.println("Available Rooms: " + suiteAvailability + "\n");

        System.out.println("===== END OF LIST =====");
    }
}