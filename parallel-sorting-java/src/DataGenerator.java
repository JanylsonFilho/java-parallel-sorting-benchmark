import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static int[] generate(int size, DataType type, long seed) {
        Random random = new Random(seed);
        int[] arr = new int[size];

        switch (type) {
            case RANDOM:
                for (int i = 0; i < size; i++) {
                    arr[i] = random.nextInt(size * 10 + 1);
                }
                break;

            case SORTED:
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }
                break;

            case REVERSED:
                for (int i = 0; i < size; i++) {
                    arr[i] = size - i;
                }
                break;

            case NEARLY_SORTED:
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }

                int swaps = Math.max(1, size / 100);

                for (int s = 0; s < swaps; s++) {
                    int i = random.nextInt(size);
                    int j = random.nextInt(size);

                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
                break;

            default:
                throw new IllegalArgumentException("Tipo de dado não suportado: " + type);
        }

        return arr;
    }
}
