public class Exhaustive_Search {
    private final int size;
    private int[] positions;
    private int solutionCount = 0;

    public Exhaustive_Search(int size) {
        this.size = size;
        this.positions = new int[size];
    }

    public static void main(String[] args) {
        int n = 4; // default size
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
                if (n < 1) {
                    System.out.println("Board size must be at least 1. Using default size 8.");
                    n = 8;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid size argument. Using default size 8.");
            }
        }

        Exhaustive_Search solver = new Exhaustive_Search(n);
        solver.solve(0);
        System.out.println("Total solutions for " + n + " queens: " + solver.solutionCount);
    }

    // Recursive backtracking: place a queen in row 'row'
    private void solve(int row) {
        if (row == size) {
            printSolution();
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

    // Check if placing a queen at (row, col) is safe
    private boolean isSafe(int row, int col) {
        for (int prevRow = 0; prevRow < row; prevRow++) {
            int prevCol = positions[prevRow];
            // Same column
            if (prevCol == col) {
                return false;
            }
            // Diagonals
            if (Math.abs(prevCol - col) == Math.abs(prevRow - row)) {
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

