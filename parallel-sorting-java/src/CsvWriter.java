import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {
    private CsvWriter() {
    }

    public static void write(String filePath, List<BenchmarkResult> results) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(BenchmarkResult.csvHeader());
            writer.newLine();

            for (BenchmarkResult result : results) {
                writer.write(result.toCsvLine());
                writer.newLine();
            }
        }
    }
}
