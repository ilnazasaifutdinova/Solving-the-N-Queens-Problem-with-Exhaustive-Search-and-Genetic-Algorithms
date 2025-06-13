import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Exhaustive_Search {
    private final int size;
    private int[] positions;
    private int solutionCount = 0;
    private final boolean verbose;

    public Exhaustive_Search(int size, boolean verbose) {
        this.size = size;
        this.positions = new int[size];
        this.verbose = verbose;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of queens (N) for exhaustive search: ");
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

        Exhaustive_Search solver = new Exhaustive_Search(n, verbose);

        // Start timer
        Instant start = Instant.now();
        solver.solve(0);
        Instant end = Instant.now();

        // End timer and print elapsed time
        System.out.println("Elapsed time: " + Duration.between(start, end).toMillis() + " ms");
        System.out.println("Total solutions for " + n + " queens: " + solver.solutionCount);
    }

    //Recursive backtracking: place a queen in row 'row'
    private void solve(int row) {
        if (row == size) {
            if (verbose) {
                printSolution();
            }
            solutionCount++;
            return;
        }
        for (int col = 0; col < size; col++) {
            if (isSafe(row, col)) {
                positions[row] = col;
                solve(row + 1);
            }
        }
    }

    //Check if placing a queen at (row, col) is safe
    private boolean isSafe(int row, int col) {
        for (int prevRow = 0; prevRow < row; prevRow++) {
            int prevCol = positions[prevRow];
            if (prevCol == col || Math.abs(prevCol - col) == Math.abs(prevRow - row)) {
                return false;
            }
        }
        return true;
    }

    // Print the current solution
    private void printSolution() {
        System.out.println("Solution #" + (solutionCount + 1) + ":");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print(positions[row] == col ? "Q " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
