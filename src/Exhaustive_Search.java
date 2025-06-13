public class Exhaustive_Search {

    private static int boardSize = 2;
    private static int[][] board = new int[boardSize][boardSize];
    private static int solutionsCount = 0;

    public static void solve() {
        placeQueen(0);
    }

    private static void placeQueen(int row) {
        if (row == boardSize) {
            solutionsCount++;
            printBoard();
            return;
        }

        for (int col = 0; col < boardSize; col++) {
            if (isSafe(row, col)) {
                board[row][col] = 1;
                placeQueen(row + 1);
                board[row][col] = 0; // Backtracking
            }
        }
    }

    private static boolean isSafe(int row, int col) {
        // Проверка на угрозы по горизонтали
        for (int i = 0; i < col; i++) {
            if (board[row][i] == 1) {
                return false;
            }
        }

        // Проверка на угрозы по диагонали (верх-лево)
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        // Проверка на угрозы по диагонали (верх-право)
        for (int i = row - 1, j = col + 1; i >= 0 && j < boardSize; i--, j++) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    private static void printBoard() {
        System.out.println("Solution " + solutionsCount + ":");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print((board[i][j] == 1 ? "Q " : ". "));
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        solve();
        System.out.println("Total solutions: " + solutionsCount);
    }
}
