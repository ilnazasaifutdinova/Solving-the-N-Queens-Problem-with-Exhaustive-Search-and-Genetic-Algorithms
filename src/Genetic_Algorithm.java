import java.time.Duration;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Genetic_Algorithm {
    private final int size;
    private final int populationSize;
    private final double mutationRate;
    private final boolean verbose;
    //private int[] bestChromosome;
    public int[] bestChromosome;

    private final Random rand = new Random();

    public Genetic_Algorithm(int size, int populationSize, double mutationRate, boolean verbose) {
        this.size = size;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.verbose = verbose;
        this.bestChromosome = new int[size];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of queens (N) for genetic algorithm search: ");
        int n = readPositiveInt(scanner, 8);
        System.out.print("Print solutions? (y/n): ");
        boolean verbose = scanner.next().trim().equalsIgnoreCase("y");
        System.out.print("Use random restarts? (y/n): ");
        boolean useRestarts = scanner.next().trim().equalsIgnoreCase("y");
        int maxAttempts = 1;
        if (useRestarts) {
            System.out.print("Enter number of restarts: ");
            maxAttempts = readPositiveInt(scanner, 10);
        }
        System.out.print("Enter population size (e.g., 100): ");
        int populationSize = readPositiveInt(scanner, 100);
        System.out.print("Enter mutation rate (e.g., 0.01): ");
        double mutationRate = readPositiveDouble(scanner, 0.01);
        scanner.nextLine();
        System.out.print("Enter number of generations (e.g., 500): ");
        int generations = readPositiveInt(scanner, 500);
        scanner.nextLine();
        scanner.close();

        int attempt = 0;
        int bestConflicts = Integer.MAX_VALUE;
        int[] bestChrom = null;

        Runtime rt = Runtime.getRuntime();
        rt.gc();
        long beforeUsedMem = rt.totalMemory() - rt.freeMemory();

        Instant startAll = Instant.now();
        while (attempt < maxAttempts && bestConflicts != 0) {
            attempt++;
            Genetic_Algorithm solver = new Genetic_Algorithm(n, populationSize, mutationRate, verbose);
            solver.initRandomPopulation();
            solver.solve(generations);
            int conflicts = solver.totalConflicts(solver.bestChromosome);
            if (conflicts < bestConflicts) {
                bestConflicts = conflicts;
                bestChrom = solver.bestChromosome.clone();
            }
            if (verbose) {
                System.out.println("Attempt " + attempt + ": conflicts = " + conflicts);
            }
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
        if (verbose && bestChrom != null) {
            System.out.println("Best board found:");
            printBoard(bestChrom);
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

//    private int[][] population;
//
//    private void initRandomPopulation() {
//        population = new int[populationSize][size];
//        for (int i = 0; i < populationSize; i++) {
//            for (int j = 0; j < size; j++) {
//                population[i][j] = rand.nextInt(size);
//            }
//        }
//    }

    public int[][] population;
    public void initRandomPopulation() {
        population = new int[populationSize][size];
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < size; j++) {
                population[i][j] = rand.nextInt(size);
            }
        }
    }

//    private void solve(int generations) {
//        for (int gen = 0; gen < generations && totalConflicts(bestChromosome) != 0; gen++) {
//            int[][] newPop = new int[populationSize][size];
//            for (int i = 0; i < populationSize; i++) {
//                int[] parent1 = tournamentSelect();
//                int[] parent2 = tournamentSelect();
//                int[] child = crossover(parent1, parent2);
//                if (rand.nextDouble() < mutationRate) {
//                    mutate(child);
//                }
//                newPop[i] = child;
//            }
//            population = newPop;
//            for (int[] chrom : population) {
//                int conflicts = totalConflicts(chrom);
//                if (conflicts < totalConflicts(bestChromosome)) {
//                    bestChromosome = chrom.clone();
//                }
//            }
//        }
//    }

    public void solve(int generations) {
        for (int gen = 0; gen < generations && totalConflicts(bestChromosome) != 0; gen++) {
            int[][] newPop = new int[populationSize][size];
            for (int i = 0; i < populationSize; i++) {
                int[] parent1 = tournamentSelect();
                int[] parent2 = tournamentSelect();
                int[] child = crossover(parent1, parent2);
                if (rand.nextDouble() < mutationRate) {
                    mutate(child);
                }
                newPop[i] = child;
            }
            population = newPop;
            for (int[] chrom : population) {
                int conflicts = totalConflicts(chrom);
                if (conflicts < totalConflicts(bestChromosome)) {
                    bestChromosome = chrom.clone();
                }
            }
        }
    }

    private int[] tournamentSelect() {
        int a = rand.nextInt(populationSize);
        int b = rand.nextInt(populationSize);
        return totalConflicts(population[a]) < totalConflicts(population[b]) ? population[a] : population[b];
    }

    private int[] crossover(int[] p1, int[] p2) {
        int[] child = new int[size];
        int point = rand.nextInt(size);
        System.arraycopy(p1, 0, child, 0, point);
        System.arraycopy(p2, point, child, point, size - point);
        return child;
    }

    private void mutate(int[] chrom) {
        int idx = rand.nextInt(size);
        chrom[idx] = rand.nextInt(size);
    }

    public int totalConflicts(int[] chrom) {
        int conflicts = 0;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (chrom[i] == chrom[j] || Math.abs(chrom[i] - chrom[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
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
