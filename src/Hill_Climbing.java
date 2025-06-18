import java.time.Duration;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Hill_Climbing {
    private final int size;
    private int[] positions;
    private final Random rand = new Random();
    private final boolean verbose;

    public Hill_Climbing(int size, boolean verbose) {
        this.size = size;
        this.positions = new int[size];
        this.verbose = verbose;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of queens (N) for hill climbing search: ");
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
        scanner.close();

        Hill_Climbing solver;
        int attempt = 0;
        int bestConflicts = Integer.MAX_VALUE;
        int[] bestPositions = null;

        Runtime rt = Runtime.getRuntime();
        rt.gc();
        long beforeUsedMem = rt.totalMemory() - rt.freeMemory();

        Instant startAll = Instant.now();
        while (attempt < maxAttempts) {
            attempt++;
            solver = new Hill_Climbing(n, verbose);
            solver.initRandom();
            boolean solved = solver.solve();
            int conflicts = solver.totalConflicts();
            if (conflicts < bestConflicts) {
                bestConflicts = conflicts;
                bestPositions = solver.positions.clone();
            }
            if (verbose) {
                System.out.println("Attempt " + attempt + ": conflicts = " + conflicts);
            }
            if (solved) break;
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

    public boolean solve() {
        int currentConflicts = totalConflicts();
        while (currentConflicts > 0) {
            int bestRow = -1, bestCol = -1;
            int bestDelta = 0;
            for (int row = 0; row < size; row++) {
                int oldCol = positions[row];
                for (int col = 0; col < size; col++) {
                    if (col == oldCol) continue;
                    positions[row] = col;
                    int newConflicts = totalConflicts();
                    int delta = currentConflicts - newConflicts;
                    if (delta > bestDelta) {
                        bestDelta = delta;
                        bestRow = row;
                        bestCol = col;
                    }
                }
                positions[row] = oldCol;
            }
            if (bestDelta <= 0) break;
            positions[bestRow] = bestCol;
            currentConflicts -= bestDelta;
        }
        return totalConflicts() == 0;
    }

    public void initRandom() {
        for (int i = 0; i < size; i++) {
            positions[i] = rand.nextInt(size);
        }
    }

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

    private static void printBoard(int[] pos) {
        int size = pos.length;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print(pos[row] == col ? "Q " : ". ");
            }
            System.out.println();
        }
    }
}
