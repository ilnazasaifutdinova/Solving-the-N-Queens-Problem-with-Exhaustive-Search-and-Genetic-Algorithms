import java.time.Duration;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Exhaustive_Search {
    private final int size;
    private final boolean verbose;
    private int[] positions;
    private int solutionCount = 0;

    public Exhaustive_Search(int size, boolean verbose) {
        this.size = size;
        this.positions = new int[size];
        this.verbose = verbose;
    }

    public static void main(String[] args) {
        int n = 8;
        boolean verbose = false;

        // 1) Попытка чтения из аргументов
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
                if (n < 1) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Invalid N argument. Using default size 8.");
                n = 8;
            }
            if (args.length > 1 && args[1].equalsIgnoreCase("y")) {
                verbose = true;
            }
        } else {
            // 2) Ввод из консоли
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print("Enter the number of queens (N) for exhaustive search: ");
                n = scanner.nextInt();
                if (n < 1) {
                    System.out.println("Board size must be at least 1. Using default size 8.");
                    n = 8;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Using default size 8.");
                n = 8;
            } finally {
                System.out.print("Print solutions? (y/n): ");
                String ans = scanner.next();
                verbose = ans.trim().equalsIgnoreCase("y");
                scanner.close();
            }
        }

        Exhaustive_Search solver = new Exhaustive_Search(n, verbose);

        // Принудительный сборщик мусора перед замером памяти
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        long beforeUsedMem = rt.totalMemory() - rt.freeMemory();

// Замер времени
        Instant start = Instant.now();
        solver.solve(0);
        Instant end = Instant.now();

        long afterUsedMem = rt.totalMemory() - rt.freeMemory();
        long usedMemBytes = afterUsedMem - beforeUsedMem;
// Не допускаем отрицательных значений
        if (usedMemBytes < 0) {
            usedMemBytes = 0;
        }

        long millis = Duration.between(start, end).toMillis();

// Итоговый вывод
        System.out.println();
        System.out.println("Elapsed time: " + millis + " ms");
        System.out.println("Total solutions for " + n + " queens: " + solver.getSolutionCount());
        System.out.printf("Approximate memory used: %.2f MB%n", usedMemBytes / (1024.0 * 1024.0));
    }

    // Рекурсивный backtracking: размещаем ферзя в строке 'row'
//    private void solve(int row) {
//        if (row == size) {
//            if (verbose) {
//                printSolution();
//            }
//            solutionCount++;
//            return;
//        }
//        for (int col = 0; col < size; col++) {
//            if (isSafe(row, col)) {
//                positions[row] = col;
//                solve(row + 1);
//            }
//        }
//    }

    public void solve(int row) {
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

    // Проверка безопасности
    private boolean isSafe(int row, int col) {
        for (int prevRow = 0; prevRow < row; prevRow++) {
            int prevCol = positions[prevRow];
            if (prevCol == col || Math.abs(prevCol - col) == Math.abs(prevRow - row)) {
                return false;
            }
        }
        return true;
    }

    // Печать конфигурации
    private void printSolution() {
        System.out.println("Solution #" + (solutionCount + 1) + ":");
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                System.out.print(positions[r] == c ? "Q " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Геттер для внешнего использования
    public int getSolutionCount() {
        return solutionCount;
    }
}
