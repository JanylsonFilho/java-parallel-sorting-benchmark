import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Versão paralela baseada em Odd-Even Transposition Sort.
 *
 * Mantém a ideia de comparação e troca de elementos adjacentes do Bubble Sort,
 * mas compara pares independentes em paralelo:
 *
 * fase par:   (0,1), (2,3), (4,5)...
 * fase ímpar: (1,2), (3,4), (5,6)...
 */
public class BubbleSortParallel implements SortAlgorithm {
    private final int threadCount;

    public BubbleSortParallel(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("threadCount deve ser maior que zero.");
        }
        this.threadCount = threadCount;
    }

    @Override
    public String name() {
        return "BubbleSortParallel";
    }

    @Override
    public void sort(int[] array) {
        oddEvenTranspositionSort(array, threadCount);
    }

    public static void oddEvenTranspositionSort(int[] arr, int threadCount) {
        int n = arr.length;

        if (n <= 1) {
            return;
        }

        int workers = Math.min(threadCount, Math.max(1, n / 2));
        CyclicBarrier barrier = new CyclicBarrier(workers);
        List<Thread> threads = new ArrayList<>();

        for (int workerId = 0; workerId < workers; workerId++) {
            final int id = workerId;

            Thread t = new Thread(() -> {
                try {
                    for (int phase = 0; phase < n; phase++) {
                        int start = phase % 2;

                        for (int i = start + (id * 2); i < n - 1; i += workers * 2) {
                            if (arr[i] > arr[i + 1]) {
                                swap(arr, i, i + 1);
                            }
                        }

                        barrier.await();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException("Erro na barreira de sincronização.", e);
                }
            });

            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Execução interrompida.", e);
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
