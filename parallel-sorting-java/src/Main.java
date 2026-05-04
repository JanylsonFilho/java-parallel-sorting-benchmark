public class Main {
    public static void main(String[] args) throws Exception {
        /*
         * Por padrão, o CSV será salvo dentro da pasta out.
         * Isso facilita manter juntos os resultados da execução e os .class compilados.
         */
        String csvPath = args.length > 0 ? args[0] : "out/resultados_ordenacao.csv";

        BenchmarkRunner runner = new BenchmarkRunner();
        runner.runAll(csvPath);
    }
}
