import java.time.Duration;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Simulated_Annealing {
    private final int size;
    private int[] positions;
    private final Random rand = new Random();
    private final boolean verbose;
    private final double initialTemp;
    private final double coolingRate;

    public Simulated_Annealing(int size, boolean verbose, double initialTemp, double coolingRate) {
        this.size = size;
        this.positions = new int[size];
        this.verbose = verbose;
        this.initialTemp = initialTemp;
        this.coolingRate = coolingRate;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of queens (N) for simulated annealing search: ");
        int n = readPositiveInt(scanner, 8);
        System.out.print("Print solutions? (y/n): ");
        boolean verbose = scanner.next().trim().equalsIgnoreCase("y");
        System.out.print("Use random restarts? (y/n): ");
        boolean useRestarts = scanner.next().trim().equalsIgnoreCase("y");
        int maxAttempts = 1;
        if (useRestarts) {
            System.out.print("Enter number of restarts: ");
            maxAttempts = readPositiveInt(scanner, 100);
        }
        System.out.print("Enter initial temperature (e.g., 1000): ");
        double initialTemp = readPositiveDouble(scanner, 1000);
        System.out.print("Enter cooling rate (e.g., 0.95): ");
        double coolingRate = readPositiveDouble(scanner, 0.95);
        scanner.close();

        int attempt = 0;
        int bestConflicts = Integer.MAX_VALUE;
        int[] bestPositions = null;

        Runtime rt = Runtime.getRuntime();
        rt.gc();
        long beforeUsedMem = rt.totalMemory() - rt.freeMemory();

        Instant startAll = Instant.now();
        while (attempt < maxAttempts) {
            attempt++;
            Simulated_Annealing solver = new Simulated_Annealing(n, verbose, initialTemp, coolingRate);
            solver.initRandom();
            solver.solve();
            int conflicts = solver.totalConflicts();
            if (conflicts < bestConflicts) {
                bestConflicts = conflicts;
                bestPositions = solver.positions.clone();
            }
            if (verbose) {
                System.out.println("Attempt " + attempt + ": conflicts = " + conflicts);
            }
            if (bestConflicts == 0) break;
        }
        Instant endAll = Instant.now();

        rt.gc();
        long afterUsedMem = rt.totalMemory() - rt.freeMemory();
        long usedMemBytes = afterUsedMem - beforeUsedMem;
        if (usedMemBytes < 0) usedMemBytes = 0;

        long millis = Duration.between(startAll, endAll).toMillis();

        System.out.println();
        System.out.println("Elapsed time: " + millis + " ms");
        System.out.println("Attempts: " + attempt);
        if (bestConflicts == 0) {
            System.out.println("Conflict-free solution found.");
        } else {
            System.out.println("No conflict-free solution found after " + attempt + " attempts.");
            System.out.println("Best conflicts count: " + bestConflicts);
        }
        System.out.printf(Locale.US,
                "Approximate memory used: %.2f MB%n", usedMemBytes / (1024.0 * 1024.0));
        if (verbose && bestPositions != null) {
            System.out.println("Best board found:");
            printBoard(bestPositions);
        }
    }

    private static int readPositiveInt(Scanner scanner, int defaultVal) {
        try {
            int val = scanner.nextInt();
            if (val < 1) throw new NumberFormatException();
            return val;
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private static double readPositiveDouble(Scanner scanner, double defaultVal) {
        try {
            double val = scanner.nextDouble();
            if (val <= 0) throw new NumberFormatException();
            return val;
        } catch (Exception e) {
            return defaultVal;
        }
    }

    // Initialize random board
    public void initRandom() {
        for (int i = 0; i < size; i++) {
            positions[i] = rand.nextInt(size);
        }
    }

    // Solves using Simulated Annealing
    public void solve() {
        double temp = initialTemp;
        while (temp > 1) {
            int row = rand.nextInt(size);
            int oldCol = positions[row];
            int newCol = rand.nextInt(size);
            positions[row] = newCol;
            int currentConflicts = totalConflicts();
            int oldConflicts = conflictsWithRow(row, oldCol);
            int newConflicts = conflictsWithRow(row, newCol);
            int delta = newConflicts - oldConflicts;
            if (delta > 0 && Math.exp(-delta / temp) < rand.nextDouble()) {
                positions[row] = oldCol;
            }
            temp *= coolingRate;
        }
    }

    // Calculate total conflicts
    public int totalConflicts() {
        int conflicts = 0;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (positions[i] == positions[j] || Math.abs(positions[i] - positions[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    // Conflicts contributed by a single row change
    private int conflictsWithRow(int row, int col) {
        int conflicts = 0;
        for (int i = 0; i < size; i++) {
            if (i == row) continue;
            if (positions[i] == col || Math.abs(positions[i] - col) == Math.abs(i - row)) {
                conflicts++;
            }
        }
        return conflicts;
    }

    private static void printBoard(int[] pos) {
        for (int r = 0; r < pos.length; r++) {
            for (int c = 0; c < pos.length; c++) {
                System.out.print(pos[r] == c ? "Q " : ". ");
            }
            System.out.println();
        }
    }
}



