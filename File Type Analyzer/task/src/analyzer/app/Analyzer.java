package analyzer.app;

import analyzer.app.strategies.KMP;
import analyzer.app.strategies.Naive;
import analyzer.app.strategies.Strategy;

import java.util.Arrays;

import static java.lang.System.nanoTime;

public class Analyzer {
    private final String[] args;
    private final Strategy strategy;

    public Analyzer(String[] args) {
        this.args = args;
        strategy = initializeStrategy();
    }

    public void analyze() {
        long start = nanoTime();
        System.out.println(this.strategy.getFileType());
        long end = nanoTime();
        double elapsed = (end - start) / 1_000_000_000.0;
        System.out.printf("Elapsed time: %.3f seconds", elapsed);
        System.out.println(Arrays.toString(this.args));
    }

    private Strategy initializeStrategy() {
        switch (this.args[0]) {
            case "--KMP" -> {
                return new KMP(args[1], args[2], args[3]);
            }
            case "--naive" -> {
                return new Naive(args[1], args[2], args[3]);
            }
            default -> {
                System.out.println("Unknown strategy");
                System.exit(1);
                return null;
            }
        }
    }
}
