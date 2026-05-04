import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Versão paralela didática do Selection Sort.
 *
 * A cada posição i, o algoritmo procura o menor elemento no trecho [i, n - 1].
 * Na versão paralela, essa busca pelo menor elemento é dividida entre várias threads.
 *
 * Observação importante:
 * Selection Sort também não é naturalmente bom para paralelismo, pois cada passo depende
 * do passo anterior. Por isso, esta versão serve principalmente para análise comparativa.
 */
public class SelectionSortParallel implements SortAlgorithm {
    private final int threadCount;

    public SelectionSortParallel(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("threadCount deve ser maior que zero.");
        }
        this.threadCount = threadCount;
    }

    @Override
    public String name() {
        return "SelectionSortParallel";
    }

    @Override
    public void sort(int[] array) {
        parallelSelectionSort(array, threadCount);
    }

    public static void parallelSelectionSort(int[] arr, int threadCount) {
        int n = arr.length;

        if (n <= 1) {
            return;
        }

        int workers = Math.min(threadCount, n);
        ExecutorService executor = Executors.newFixedThreadPool(workers);

        try {
            for (int i = 0; i < n - 1; i++) {
                int rangeSize = n - i;
                int chunkSize = Math.max(1, (rangeSize + workers - 1) / workers);

                List<Callable<Integer>> tasks = new ArrayList<>();

                for (int start = i; start < n; start += chunkSize) {
                    int from = start;
                    int to = Math.min(n, start + chunkSize);

                    tasks.add(() -> findMinIndex(arr, from, to));
                }

                List<Future<Integer>> futures = executor.invokeAll(tasks);

                int globalMinIndex = i;

                for (Future<Integer> future : futures) {
                    int localMinIndex = future.get();

                    if (arr[localMinIndex] < arr[globalMinIndex]) {
                        globalMinIndex = localMinIndex;
                    }
                }

                SelectionSortSerial.swap(arr, i, globalMinIndex);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Execução interrompida.", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Erro durante execução paralela do Selection Sort.", e);
        } finally {
            executor.shutdown();
        }
    }

    private static int findMinIndex(int[] arr, int fromInclusive, int toExclusive) {
        int minIndex = fromInclusive;

        for (int i = fromInclusive + 1; i < toExclusive; i++) {
            if (arr[i] < arr[minIndex]) {
                minIndex = i;
            }
        }

        return minIndex;
    }
}
