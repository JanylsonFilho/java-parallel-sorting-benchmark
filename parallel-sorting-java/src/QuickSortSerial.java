public class QuickSortSerial implements SortAlgorithm {
    @Override
    public String name() {
        return "QuickSortSerial";
    }

    @Override
    public void sort(int[] array) {
        quickSort(array);
    }

    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    static void quickSort(int[] arr, int low, int high) {
        while (low < high) {
            int pivotIndex = partition(arr, low, high);

            /*
             * Otimização simples: processa recursivamente o menor lado
             * para reduzir profundidade de pilha.
             */
            if (pivotIndex - low < high - pivotIndex) {
                quickSort(arr, low, pivotIndex - 1);
                low = pivotIndex + 1;
            } else {
                quickSort(arr, pivotIndex + 1, high);
                high = pivotIndex - 1;
            }
        }
    }

    static int partition(int[] arr, int low, int high) {
        /*
         * Mediana de três para reduzir o risco de pior caso em dados ordenados.
         */
        int mid = low + (high - low) / 2;
        int pivotIndex = medianOfThree(arr, low, mid, high);
        swap(arr, pivotIndex, high);

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    private static int medianOfThree(int[] arr, int a, int b, int c) {
        int x = arr[a];
        int y = arr[b];
        int z = arr[c];

        if ((x <= y && y <= z) || (z <= y && y <= x)) {
            return b;
        }

        if ((y <= x && x <= z) || (z <= x && x <= y)) {
            return a;
        }

        return c;
    }

    static void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
