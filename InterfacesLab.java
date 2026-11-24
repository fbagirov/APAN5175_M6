/*
 * =====================================================================
 *  APAN 5175: Introduction to Programming using Java
 *  Module 6 LAB 1: INTERFACES (Smart Home)
 * ---------------------------------------------------------------------
 *  You are building the software for a smart-home hub. Every gadget is a
 *  Device. Some gadgets have extra abilities: a Light is also Dimmable,
 *  and a Thermostat is also Schedulable. The INTERFACES (the contracts)
 *  are given. Your job is to IMPLEMENT them.
 *
 *  HOW THIS LAB WORKS
 *    1. This file compiles and runs as-is:
 *           javac InterfacesLab.java
 *           java  InterfacesLab
 *       Most checks print FAIL. Turn them into PASS.
 *    2. Every place you must change is marked TODO.
 *    3. Use @Override on every method you implement.
 *    4. Do NOT change the interface method SIGNATURES or the checker.
 *
 *  Submit InterfacesLab.java with all checks passing and Task 7 answered.
 * =====================================================================
 */

// ---------------------------------------------------------------------
// PROVIDED CONTRACTS
// ---------------------------------------------------------------------
interface Device {
    String getName();
    void turnOn();
    void turnOff();
    boolean isOn();
    double getPowerWatts();          // current power draw; must be 0.0 when off

    // TASK 3a: Make this DEFAULT method return text like
    //     "Kitchen Light is ON (3.0 W)"   or   "Hall Thermostat is OFF (0.0 W)"
    // Build it ONLY from the abstract methods above.
    default String status() {
        return "TODO";
    }

    // TASK 3b: Make this STATIC method return the SUM of getPowerWatts()
    // over every device in the array.
    static double totalPower(Device[] devices) {
        return 0.0;                  // TODO
    }
}

interface Dimmable {
    void setBrightness(int percent);   // clamp to the range 0..100
    int getBrightness();
}

interface Schedulable {
    void scheduleOn(String time);      // e.g. "06:30"
    String getSchedule();              // "none" if nothing is scheduled
}

// ---------------------------------------------------------------------
// TASK 1: Light implements Device AND Dimmable
//   - A new Light starts OFF at brightness 100.
//   - getPowerWatts(): 0.0 when off; otherwise maxWatts x brightness / 100
//     (careful: brightness is an int!)
//   - setBrightness(): values below 0 become 0, above 100 become 100.
//   - Turning off and on again REMEMBERS the brightness.
// ---------------------------------------------------------------------
class Light implements Device, Dimmable {
    private final String name;
    private final double maxWatts;
    // TODO: add the fields you need (on/off state, brightness)

    public Light(String name, double maxWatts) {
        this.name = name;
        this.maxWatts = maxWatts;
    }

    @Override public String getName() { return name; }

    @Override
    public void turnOn() {
        // TODO
    }

    @Override
    public void turnOff() {
        // TODO
    }

    @Override
    public boolean isOn() {
        return false;                // TODO
    }

    @Override
    public double getPowerWatts() {
        return -1;                   // TODO
    }

    @Override
    public void setBrightness(int percent) {
        // TODO
    }

    @Override
    public int getBrightness() {
        return -1;                   // TODO
    }
}

// ---------------------------------------------------------------------
// TASK 2: Thermostat implements Device AND Schedulable
//   - Draws heaterWatts when on, 0.0 when off.
//   - getSchedule() returns "none" until scheduleOn(...) is called.
// ---------------------------------------------------------------------
class Thermostat implements Device, Schedulable {
    private final String name;
    private final double heaterWatts;
    // TODO: add the fields you need

    public Thermostat(String name, double heaterWatts) {
        this.name = name;
        this.heaterWatts = heaterWatts;
    }

    @Override public String getName() { return name; }

    @Override public void turnOn()  { /* TODO */ }
    @Override public void turnOff() { /* TODO */ }
    @Override public boolean isOn() { return false; /* TODO */ }
    @Override public double getPowerWatts() { return -1; /* TODO */ }

    @Override public void scheduleOn(String time) { /* TODO */ }
    @Override public String getSchedule() { return "TODO"; }

    // Not part of any interface. TODO: return e.g. "Hall Thermostat turns on at 06:30"
    public String describeSchedule() {
        return "TODO";
    }
}

// ---------------------------------------------------------------------
// TASK 5: Write a class SmartPlug HERE, from scratch.
//   - It implements Device ONLY (it is NOT Dimmable).
//   - Constructor: SmartPlug(String name, double ratedWatts)
//   - Draws ratedWatts when on, 0.0 when off.
//   Then uncomment the TASK 5 code in main().
// ---------------------------------------------------------------------


// ---------------------------------------------------------------------
public class InterfacesLab {

    public static void main(String[] args) {

        section("TASK 1: Light (Device + Dimmable)");
        Light kitchen = new Light("Kitchen Light", 12.0);
        check("1a starts OFF", kitchen.isOn(), false);
        check("1b off -> 0 W", kitchen.getPowerWatts(), 0.0);
        kitchen.turnOn();
        check("1c on at 100% -> 12 W", kitchen.getPowerWatts(), 12.0);
        kitchen.setBrightness(25);
        check("1d 25% -> 3 W", kitchen.getPowerWatts(), 3.0);
        kitchen.setBrightness(150);
        check("1e clamp high -> 100", kitchen.getBrightness(), 100);
        kitchen.setBrightness(-5);
        check("1f clamp low -> 0", kitchen.getBrightness(), 0);
        kitchen.setBrightness(25);
        kitchen.turnOff();
        check("1g off again -> 0 W", kitchen.getPowerWatts(), 0.0);
        kitchen.turnOn();
        check("1h remembers 25% -> 3 W", kitchen.getPowerWatts(), 3.0);

        section("TASK 2: Thermostat (Device + Schedulable)");
        Thermostat hall = new Thermostat("Hall Thermostat", 1500.0);
        check("2a no schedule yet", hall.getSchedule(), "none");
        hall.scheduleOn("06:30");
        check("2b scheduled", hall.getSchedule(), "06:30");
        hall.turnOn();
        check("2c on -> 1500 W", hall.getPowerWatts(), 1500.0);

        section("TASK 3: default and static interface methods");
        check("3a status() ON", kitchen.status(), "Kitchen Light is ON (3.0 W)");
        hall.turnOff();
        check("3a status() OFF", hall.status(), "Hall Thermostat is OFF (0.0 W)");

        Light lamp = new Light("Bedroom Lamp", 8.0);
        lamp.turnOn();
        Device[] home = {kitchen, hall, lamp};
        check("3b totalPower", Device.totalPower(home), 11.0);

        section("TASK 4: Polymorphism with an interface type");
        // TODO: Write ONE for-each loop over home that, for every device:
        //   1. turns it on
        //   2. IF the device is Dimmable, sets its brightness to 50
        //      (hint: instanceof, then a cast)
        //   3. prints its status()
        // Do NOT call kitchen/lamp/hall by name inside the loop.


        check("4a kitchen dimmed to 50%", kitchen.getBrightness(), 50);
        check("4b lamp dimmed to 50%", lamp.getBrightness(), 50);
        check("4c total after loop", Device.totalPower(home), 1510.0);

        Device d0 = hall;
        // Uncomment the next line, compile, and record the error in Task 7.
        // d0.scheduleOn("07:00");
        check("4d same object, Thermostat view", ((Thermostat) d0).describeSchedule(),
                "Hall Thermostat turns on at 06:30");

        section("TASK 5: SmartPlug (written from scratch)");
        // TODO: uncomment after writing SmartPlug
        // SmartPlug fan = new SmartPlug("Desk Fan Plug", 45.0);
        // check("5a plug off -> 0 W", fan.getPowerWatts(), 0.0);
        // fan.turnOn();
        // Device[] home2 = {kitchen, hall, lamp, fan};
        // check("5b total with plug", Device.totalPower(home2), 1555.0);

        section("TASK 6: Which devices are Schedulable?");
        int schedulableCount = 0;
        // TODO (after Task 5): loop over home2 and count the devices that are Schedulable

        check("6a schedulable devices", schedulableCount, 1);
        // TODO: uncomment after Task 5
        // check("6b SmartPlug is NOT Dimmable", (Object) fan instanceof Dimmable, false);

        /*
         * TASK 7 (WRITTEN): answer in the ANSWER lines.
         * Q7a. Temporarily delete getBrightness() from Light and compile.
         *      What does the compiler say?
         *      ANSWER:
         * Q7b. Why can't we write  new Device()?
         *      ANSWER:
         * Q7c. Why is status() a good DEFAULT method, while getPowerWatts()
         *      has to be abstract?
         *      ANSWER:
         * Q7d. What error did  d0.scheduleOn("07:00")  give, and why, when d0
         *      really IS a Thermostat?
         *      ANSWER:
         * Q7e. Fill in YES / NO:
         *                           Abstract class    Interface
         *      Multiple inheritance?   ____              ____
         *      Instance variables?     ____              ____
         *      Concrete methods?       ____              ____
         *      Constructors?           ____              ____
         * Q7f. Light IS-A Device and IS-A Dimmable. Why could we NOT do that
         *      if Device and Dimmable were abstract classes?
         *      ANSWER:
         */

        summary();
    }

    // =================================================================
    // PROVIDED CHECKER: DO NOT MODIFY
    // =================================================================
    private static int passed = 0, total = 0;

    private static void section(String t) { System.out.println(); System.out.println("=== " + t + " ==="); }

    private static void report(String label, boolean ok, String actual, String expected) {
        total++;
        if (ok) { passed++; System.out.println("PASS " + label); }
        else System.out.println("FAIL " + label + "  -> got " + actual + ", expected " + expected);
    }
    private static void check(String l, long a, long e)       { report(l, a == e, "" + a, "" + e); }
    private static void check(String l, double a, double e)   { report(l, Math.abs(a - e) < 1e-9, "" + a, "" + e); }
    private static void check(String l, boolean a, boolean e) { report(l, a == e, "" + a, "" + e); }
    private static void check(String l, String a, String e)   { report(l, e.equals(a), "\"" + a + "\"", "\"" + e + "\""); }

    private static void summary() {
        System.out.println();
        System.out.println("Checks passed: " + passed + " / " + total);
    }
}
