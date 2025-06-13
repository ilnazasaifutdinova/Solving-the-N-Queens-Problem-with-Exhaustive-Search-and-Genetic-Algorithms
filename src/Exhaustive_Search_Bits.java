// File: Hill_Climbing.java
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Exhaustive_Search_Bits {
    private final int size;
    private int[] positions;
    private final Random rand = new Random();
    private final boolean verbose;

    public Exhaustive_Search_Bits(int size, boolean verbose) {
        this.size = size;
        this.positions = new int[size];
        this.verbose = verbose;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of queens (N) for hill climbing search: ");
        int n;
        try {
            n = scanner.nextInt();
            if (n < 1) {
                System.out.println("Board size must be at least 1. Using default size 8.");
                n = 8;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Using default size 8.");
            n = 8;
        }
        System.out.print("Print solutions? (y/n): ");
        boolean verbose = scanner.next().trim().equalsIgnoreCase("y");
        scanner.close();

        Exhaustive_Search_Bits solver = new Exhaustive_Search_Bits(n, verbose);
        Instant start = Instant.now();
        boolean solved = solver.solve();
        Instant end = Instant.now();

        long millis = Duration.between(start, end).toMillis();
        double seconds = millis / 1000.0;
        long totalSeconds = Duration.between(start, end).getSeconds();
        long minutes = totalSeconds / 60;
        long remSeconds = totalSeconds % 60;

        System.out.println();
        System.out.println("Elapsed time: " + millis + " ms");
        //System.out.println("Total solutions for " + n + " queens: " + solver.solutionCount);

        if (solved) {
            System.out.println("Solution found:");
            if (verbose) solver.printBoard();
        } else {
            System.out.println("Local optimum reached, conflicts: " + solver.totalConflicts());
            if (verbose) solver.printBoard();
        }
    }

    public boolean solve() {
        initRandom();
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
            if (bestDelta <= 0) break; // No improving move
            positions[bestRow] = bestCol;
            currentConflicts -= bestDelta;
        }
        return currentConflicts == 0;
    }

    private void initRandom() {
        for (int row = 0; row < size; row++) {
            positions[row] = rand.nextInt(size);
        }
    }

    private int totalConflicts() {
        int conflicts = 0;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (positions[i] == positions[j] ||
                        Math.abs(positions[i] - positions[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    private void printBoard() {
        System.out.println("Board:");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print(positions[row] == col ? "Q " : ". ");
            }
            System.out.println();
        }
    }
}
