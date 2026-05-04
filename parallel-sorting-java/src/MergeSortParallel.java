import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSortParallel implements SortAlgorithm {
    private static final int THRESHOLD = 10_000;
    private final int threadCount;

    public MergeSortParallel(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("threadCount deve ser maior que zero.");
        }
        this.threadCount = threadCount;
    }

    @Override
    public String name() {
        return "MergeSortParallel";
    }

    @Override
    public void sort(int[] array) {
        parallelMergeSort(array, threadCount);
    }

    public static void parallelMergeSort(int[] arr, int threadCount) {
        if (arr.length <= 1) {
            return;
        }

        int[] temp = new int[arr.length];

        ForkJoinPool pool = new ForkJoinPool(threadCount);
        try {
            pool.invoke(new MergeSortTask(arr, temp, 0, arr.length - 1));
        } finally {
            pool.shutdown();
        }
    }

    private static class MergeSortTask extends RecursiveAction {
        private final int[] arr;
        private final int[] temp;
        private final int left;
        private final int right;

        MergeSortTask(int[] arr, int[] temp, int left, int right) {
            this.arr = arr;
            this.temp = temp;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left + 1 <= THRESHOLD) {
                serialMergeSort(arr, temp, left, right);
                return;
            }

            int mid = left + (right - left) / 2;

            MergeSortTask leftTask = new MergeSortTask(arr, temp, left, mid);
            MergeSortTask rightTask = new MergeSortTask(arr, temp, mid + 1, right);

            invokeAll(leftTask, rightTask);

            MergeSortSerial.merge(arr, temp, left, mid, right);
        }

        private static void serialMergeSort(int[] arr, int[] temp, int left, int right) {
            if (left >= right) {
                return;
            }

            int mid = left + (right - left) / 2;
            serialMergeSort(arr, temp, left, mid);
            serialMergeSort(arr, temp, mid + 1, right);
            MergeSortSerial.merge(arr, temp, left, mid, right);
        }
    }
}
