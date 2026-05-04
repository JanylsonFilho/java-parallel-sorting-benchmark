public class BenchmarkResult {
    public final String algorithm;
    public final String baseAlgorithm;
    public final String executionType;
    public final int threads;
    public final int size;
    public final String dataType;
    public final int sample;
    public final long timeNs;
    public final double timeMs;
    public final boolean sorted;

    public BenchmarkResult(
            String algorithm,
            String baseAlgorithm,
            String executionType,
            int threads,
            int size,
            String dataType,
            int sample,
            long timeNs,
            boolean sorted
    ) {
        this.algorithm = algorithm;
        this.baseAlgorithm = baseAlgorithm;
        this.executionType = executionType;
        this.threads = threads;
        this.size = size;
        this.dataType = dataType;
        this.sample = sample;
        this.timeNs = timeNs;
        this.timeMs = timeNs / 1_000_000.0;
        this.sorted = sorted;
    }

    public String toCsvLine() {
        return String.format(
                java.util.Locale.US,
                "%s,%s,%s,%d,%d,%s,%d,%d,%.6f,%s",
                algorithm,
                baseAlgorithm,
                executionType,
                threads,
                size,
                dataType,
                sample,
                timeNs,
                timeMs,
                sorted
        );
    }

    public static String csvHeader() {
        return "algorithm,base_algorithm,execution_type,threads,size,data_type,sample,time_ns,time_ms,sorted";
    }
}
