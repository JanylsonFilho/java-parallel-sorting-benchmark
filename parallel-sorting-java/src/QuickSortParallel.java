import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class QuickSortParallel implements SortAlgorithm {
    private static final int THRESHOLD = 10_000;
    private final int threadCount;

    public QuickSortParallel(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("threadCount deve ser maior que zero.");
        }
        this.threadCount = threadCount;
    }

    @Override
    public String name() {
        return "QuickSortParallel";
    }

    @Override
    public void sort(int[] array) {
        parallelQuickSort(array, threadCount);
    }

    public static void parallelQuickSort(int[] arr, int threadCount) {
        if (arr.length <= 1) {
            return;
        }

        ForkJoinPool pool = new ForkJoinPool(threadCount);
        try {
            pool.invoke(new QuickSortTask(arr, 0, arr.length - 1));
        } finally {
            pool.shutdown();
        }
    }

    private static class QuickSortTask extends RecursiveAction {
        private final int[] arr;
        private final int low;
        private final int high;

        QuickSortTask(int[] arr, int low, int high) {
            this.arr = arr;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (low >= high) {
                return;
            }

            if (high - low + 1 <= THRESHOLD) {
                QuickSortSerial.quickSort(arr, low, high);
                return;
            }

            int pivotIndex = QuickSortSerial.partition(arr, low, high);

            QuickSortTask leftTask = new QuickSortTask(arr, low, pivotIndex - 1);
            QuickSortTask rightTask = new QuickSortTask(arr, pivotIndex + 1, high);

            invokeAll(leftTask, rightTask);
        }
    }
}
