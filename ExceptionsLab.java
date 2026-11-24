/*
 * =====================================================================
 *  APAN 5175: Introduction to Programming using Java
 *  Module 6 LAB 2: EXCEPTION HANDLING (Warehouse Order Processor)
 * ---------------------------------------------------------------------
 *  A warehouse receives order lines like "LAPTOP,2" from an upstream
 *  system. Some lines are bad: wrong numbers, unknown products, not
 *  enough stock, even corrupted data. Your program must handle EVERY
 *  line without crashing, and report exactly what went wrong.
 *
 *  HOW THIS LAB WORKS
 *    1. This file compiles and runs as-is:
 *           javac ExceptionsLab.java
 *           java  ExceptionsLab
 *       Most checks print FAIL. Turn them into PASS.
 *    2. Do the tasks IN ORDER. Task 3 will intentionally make the program
 *       crash; Task 4 is about reading that crash.
 *    3. Every place you must change is marked TODO.
 *    4. Do NOT modify the provided helpers or the checker.
 *
 *  Submit ExceptionsLab.java with all checks passing and Tasks 4-5 answered.
 * =====================================================================
 */

// ---------------------------------------------------------------------
// TASK 1: Custom exceptions
//   Complete each class so the Task 1 checks pass. The exact messages:
//     ProductNotFoundException("TABLET")
//         -> "Unknown product code: TABLET"
//     OutOfStockException("LAPTOP", 4, 3)
//         -> "Not enough LAPTOP in stock: requested 4, available 3"
//     InvalidQuantityException(-1)
//         -> "Quantity must be positive, got -1"
//   Hint: pass the message to the parent constructor with super(...).
//
//   DECIDE: two of these should be CHECKED and one UNCHECKED.
//   Which one is a programming error rather than a normal business
//   situation? Change its parent class accordingly.
// ---------------------------------------------------------------------
class ProductNotFoundException extends Exception {
    // TODO: field for the product code

    public ProductNotFoundException(String productCode) {
        super("TODO");
        // TODO
    }

    public String getProductCode() {
        return "TODO";
    }
}

class OutOfStockException extends Exception {
    // TODO: fields

    public OutOfStockException(String productCode, int requested, int available) {
        super("TODO");
        // TODO
    }

    /** How many units are missing (requested minus available). */
    public int getShortBy() {
        return 0;                    // TODO
    }
}

class InvalidQuantityException extends Exception {    // TODO: checked or unchecked?
    public InvalidQuantityException(int quantity) {
        super("TODO");
    }
}

// ---------------------------------------------------------------------
// TASK 2: Inventory
//   a) findIndex(): if the code is not found, THROW ProductNotFoundException
//      instead of returning 0.
//   b) reserve(): in this order,
//        1. quantity <= 0            -> throw InvalidQuantityException
//        2. unknown product          -> (findIndex throws for you)
//        3. quantity > stock         -> throw OutOfStockException
//        4. otherwise subtract quantity from stock
//      A FAILED reserve must NOT change the stock.
// ---------------------------------------------------------------------
class Inventory {
    private final String[] codes = {"LAPTOP", "MOUSE", "KEYBOARD", "MONITOR"};
    private final int[] stock = {5, 20, 0, 3};

    public int getStock(String code) throws ProductNotFoundException {
        return stock[findIndex(code)];
    }

    private int findIndex(String code) throws ProductNotFoundException {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(code)) {
                return i;
            }
        }
        return 0;                    // TODO: throw instead
    }

    public void reserve(String code, int quantity)
            throws ProductNotFoundException, OutOfStockException {
        // TODO
    }
}

// ---------------------------------------------------------------------
public class ExceptionsLab {

    static int processed = 0, succeeded = 0, formatErrors = 0,
               notFound = 0, outOfStock = 0, invalidQty = 0, totalShortBy = 0;

    /**
     * Parses one order line like "LAPTOP,2" and reserves the stock.
     * TASK 4: you will come back and fix this method.
     */
    static void processLine(Inventory inv, String line)
            throws ProductNotFoundException, OutOfStockException {
        String[] parts = line.split(",");
        String code = parts[0].trim().toUpperCase();
        int qty = Integer.parseInt(parts[1].trim());
        inv.reserve(code, qty);
    }

    public static void main(String[] args) {

        // ============================================================
        section("TASK 1: Custom exception classes");
        OutOfStockException oos = new OutOfStockException("LAPTOP", 4, 3);
        check("1a OutOfStock message", oos.getMessage(),
                "Not enough LAPTOP in stock: requested 4, available 3");
        check("1b OutOfStock shortBy", oos.getShortBy(), 1);
        check("1c OutOfStock is CHECKED", (Object) oos instanceof RuntimeException, false);

        ProductNotFoundException pnf = new ProductNotFoundException("TABLET");
        check("1d NotFound message", pnf.getMessage(), "Unknown product code: TABLET");
        check("1e NotFound code field", pnf.getProductCode(), "TABLET");

        InvalidQuantityException iq = new InvalidQuantityException(-1);
        check("1f InvalidQty message", iq.getMessage(), "Quantity must be positive, got -1");
        check("1g InvalidQty is UNCHECKED", (Object) iq instanceof RuntimeException, true);

        // ============================================================
        section("TASK 2: Inventory.reserve() throws the right exception");
        Inventory test = new Inventory();
        check("2a valid reserve", tryReserve(test, "MOUSE", 5), "OK");
        check("2b stock reduced", stockOf(test, "MOUSE"), 15);
        check("2c unknown product", tryReserve(test, "TABLET", 1), "ProductNotFoundException");
        check("2d not enough stock", tryReserve(test, "MONITOR", 4), "OutOfStockException");
        check("2e zero quantity", tryReserve(test, "MOUSE", 0), "InvalidQuantityException");
        check("2f stock untouched by failures", stockOf(test, "MONITOR"), 3);

        // ============================================================
        section("TASK 3: Process the day's orders");
        Inventory inv = new Inventory();
        String[] orders = {
            "LAPTOP,2",
            "MOUSE,abc",
            "KEYBOARD,1",
            "TABLET,1",
            "MONITOR,-1",
            "WEBCAM",
            "monitor, 3",
            "LAPTOP,4",
            null,
            "MOUSE,20"
        };

        for (String line : orders) {
            try {
                processLine(inv, line);
                succeeded++;
                System.out.println("  OK        " + line);
            } catch (Exception e) {
                System.out.println("  ERROR     " + line + " -> " + e);
            }
            // TODO (Task 3): REPLACE the single catch (Exception e) above with
            //   SPECIFIC catch blocks, one per problem, each incrementing its
            //   counter and printing a clear message:
            //     NumberFormatException      -> formatErrors++
            //     IllegalArgumentException   -> formatErrors++   (used in Task 4)
            //     InvalidQuantityException   -> invalidQty++
            //     ProductNotFoundException   -> notFound++
            //     OutOfStockException        -> outOfStock++ and add getShortBy()
            //                                   to totalShortBy
            //   Then add a FINALLY block that increments processed.
            //   Watch the ORDER of your catch blocks (see Q5c).
            //
            // After Task 3, run the program. It will CRASH. That is expected:
            // go to Task 4.
        }

        check("3a processed (finally)", processed, 10);
        check("3b succeeded", succeeded, 3);
        check("3c format errors", formatErrors, 3);
        check("3d not found", notFound, 1);
        check("3e out of stock", outOfStock, 2);
        check("3f invalid quantity", invalidQty, 1);
        check("3g total short by", totalShortBy, 2);
        check("3h LAPTOP left", stockOf(inv, "LAPTOP"), 3);
        check("3i MONITOR left", stockOf(inv, "MONITOR"), 0);
        check("3j MOUSE left", stockOf(inv, "MOUSE"), 0);

        /*
         * TASK 4: READ THE STACK TRACES, THEN FIX THE BUGS
         * After Task 3 the program crashes, and the checks above never run.
         * Fix ONE crash at a time: read the trace, fix processLine(), rerun.
         *
         * CRASH 1: paste the first 3 lines of the crash output:
         *
         *   Q4a. Exception type, and the method + line it happened in
         *        (the TOP "at" line)?
         *        ANSWER:
         *   Q4b. Which order line caused it?
         *        ANSWER:
         *   FIX: if the line does not split into exactly 2 parts, throw
         *          new IllegalArgumentException("Expected CODE,QUANTITY but got \"" + line + "\"")
         *
         * CRASH 2 (after fixing crash 1): paste the first 3 lines:
         *
         *   Q4c. Exception type, and which order line caused it?
         *        ANSWER:
         *   FIX: at the START of processLine, if line is null or blank, throw
         *          new IllegalArgumentException("Empty order line")
         *
         * Q4d. The ORIGINAL program (with catch (Exception e)) never crashed.
         *      Why not? Was that a good thing?
         *      ANSWER:
         * Q4e. Why fix these by validating input, rather than adding
         *      catch (ArrayIndexOutOfBoundsException e) or
         *      catch (NullPointerException e)?
         *      ANSWER:
         *
         * TASK 5 (WRITTEN)
         * Q5a. Remove "throws ProductNotFoundException, OutOfStockException"
         *      from processLine and compile. What is the error, and why?
         *      ANSWER:
         * Q5b. Your reserve() throws InvalidQuantityException, but it is not
         *      in reserve()'s "throws" list. Why does that compile?
         *      ANSWER:
         * Q5c. Put the IllegalArgumentException catch BEFORE the
         *      NumberFormatException catch. What happens, and why?
         *      ANSWER:
         * Q5d. Why is running out of stock a CHECKED exception, but a negative
         *      quantity UNCHECKED?
         *      ANSWER:
         * Q5e. Why must reserve() validate BEFORE subtracting stock?
         *      ANSWER:
         */

        summary();
    }

    // ---------- PROVIDED helpers for Task 2 checks: DO NOT MODIFY ----------
    static String tryReserve(Inventory inv, String code, int qty) {
        try {
            inv.reserve(code, qty);
            return "OK";
        } catch (Exception e) {     // OK HERE: test helper reports the exception TYPE
            return e.getClass().getSimpleName();
        }
    }

    static int stockOf(Inventory inv, String code) {
        try {
            return inv.getStock(code);
        } catch (ProductNotFoundException e) {
            return -999;
        }
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
    private static void check(String l, boolean a, boolean e) { report(l, a == e, "" + a, "" + e); }
    private static void check(String l, String a, String e)   { report(l, e.equals(a), "\"" + a + "\"", "\"" + e + "\""); }

    private static void summary() {
        System.out.println();
        System.out.println("Checks passed: " + passed + " / " + total);
    }
}
