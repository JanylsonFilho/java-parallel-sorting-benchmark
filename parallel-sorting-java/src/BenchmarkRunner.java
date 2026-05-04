import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BenchmarkRunner {
    private static final int SAMPLES = 5;

    /*
     * Bubble Sort e Selection Sort são O(n²), então usam tamanhos menores.
     * Merge Sort e Quick Sort são O(n log n), então usam tamanhos maiores.
     */
    private static final int[] QUADRATIC_SIZES = {1_000, 2_000, 5_000, 10_000};
    private static final int[] N_LOG_N_SIZES = {10_000, 50_000, 100_000, 500_000};

    public void runAll(String csvPath) throws IOException {
        List<BenchmarkResult> results = new ArrayList<>();

        int available = Runtime.getRuntime().availableProcessors();
        int[] threadOptions = buildThreadOptions(available);

        System.out.println("Processadores disponíveis: " + available);
        System.out.println("Threads testadas: " + Arrays.toString(threadOptions));

        for (DataType dataType : DataType.values()) {
            runBubbleBenchmarks(results, dataType, threadOptions);
            runSelectionBenchmarks(results, dataType, threadOptions);
            runMergeBenchmarks(results, dataType, threadOptions);
            runQuickBenchmarks(results, dataType, threadOptions);
        }

        CsvWriter.write(csvPath, results);

        System.out.println();
        System.out.println("Arquivo CSV gerado em: " + csvPath);
        System.out.println("Total de medições: " + results.size());
    }

    private void runBubbleBenchmarks(List<BenchmarkResult> results, DataType dataType, int[] threadOptions) {
        for (int size : QUADRATIC_SIZES) {
            for (int sample = 1; sample <= SAMPLES; sample++) {
                long seed = seed(size, dataType, sample);
                int[] original = DataGenerator.generate(size, dataType, seed);

                runOne(results, new BubbleSortSerial(), "BubbleSort", "SERIAL", 1, original, size, dataType, sample);

                for (int threads : threadOptions) {
                    runOne(results, new BubbleSortParallel(threads), "BubbleSort", "PARALLEL", threads, original, size, dataType, sample);
                }
            }
        }
    }

    private void runSelectionBenchmarks(List<BenchmarkResult> results, DataType dataType, int[] threadOptions) {
        for (int size : QUADRATIC_SIZES) {
            for (int sample = 1; sample <= SAMPLES; sample++) {
                long seed = seed(size, dataType, sample);
                int[] original = DataGenerator.generate(size, dataType, seed);

                runOne(results, new SelectionSortSerial(), "SelectionSort", "SERIAL", 1, original, size, dataType, sample);

                for (int threads : threadOptions) {
                    runOne(results, new SelectionSortParallel(threads), "SelectionSort", "PARALLEL", threads, original, size, dataType, sample);
                }
            }
        }
    }

    private void runMergeBenchmarks(List<BenchmarkResult> results, DataType dataType, int[] threadOptions) {
        for (int size : N_LOG_N_SIZES) {
            for (int sample = 1; sample <= SAMPLES; sample++) {
                long seed = seed(size, dataType, sample);
                int[] original = DataGenerator.generate(size, dataType, seed);

                runOne(results, new MergeSortSerial(), "MergeSort", "SERIAL", 1, original, size, dataType, sample);

                for (int threads : threadOptions) {
                    runOne(results, new MergeSortParallel(threads), "MergeSort", "PARALLEL", threads, original, size, dataType, sample);
                }
            }
        }
    }

    private void runQuickBenchmarks(List<BenchmarkResult> results, DataType dataType, int[] threadOptions) {
        for (int size : N_LOG_N_SIZES) {
            for (int sample = 1; sample <= SAMPLES; sample++) {
                long seed = seed(size, dataType, sample);
                int[] original = DataGenerator.generate(size, dataType, seed);

                runOne(results, new QuickSortSerial(), "QuickSort", "SERIAL", 1, original, size, dataType, sample);

                for (int threads : threadOptions) {
                    runOne(results, new QuickSortParallel(threads), "QuickSort", "PARALLEL", threads, original, size, dataType, sample);
                }
            }
        }
    }

    private void runOne(
            List<BenchmarkResult> results,
            SortAlgorithm algorithm,
            String baseAlgorithm,
            String executionType,
            int threads,
            int[] original,
            int size,
            DataType dataType,
            int sample
    ) {
        int[] copy = Arrays.copyOf(original, original.length);

        long start = System.nanoTime();
        algorithm.sort(copy);
        long end = System.nanoTime();

        boolean sorted = MergeSortSerial.isSorted(copy);

        BenchmarkResult result = new BenchmarkResult(
                algorithm.name(),
                baseAlgorithm,
                executionType,
                threads,
                size,
                dataType.name(),
                sample,
                end - start,
                sorted
        );

        results.add(result);

        System.out.printf(
                java.util.Locale.US,
                "%-24s | %-8s | threads=%2d | n=%7d | %-13s | amostra=%d | %.3f ms | sorted=%s%n",
                algorithm.name(),
                executionType,
                threads,
                size,
                dataType.name(),
                sample,
                result.timeMs,
                sorted
        );
    }

    private static int[] buildThreadOptions(int availableProcessors) {
        if (availableProcessors >= 16) {
            return new int[]{2, 4, 8, 16};
        }

        if (availableProcessors >= 8) {
            return new int[]{2, 4, 8};
        }

        if (availableProcessors >= 4) {
            return new int[]{2, 4};
        }

        return new int[]{2};
    }

    private static long seed(int size, DataType dataType, int sample) {
        return 31L * size + 17L * dataType.ordinal() + sample;
    }
}
