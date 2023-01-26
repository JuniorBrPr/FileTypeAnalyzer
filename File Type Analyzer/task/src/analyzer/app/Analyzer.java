package analyzer.app;

import analyzer.app.strategies.KMP;
import analyzer.app.strategies.Strategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Analyzer {
    private final String[] args;
    private final File file;
    private final ExecutorService executor;

    public Analyzer(String[] args) {
        this.args = args;
        this.file = new File(args[0]);
        this.executor = Executors.newFixedThreadPool(10);
    }

    public void analyze() {
        ArrayList<Strategy> filesToAnalyze = getAllFilesToAnalyze(this.file);
        List<Future<String>> futures;
        ArrayList<String> results = new ArrayList<>();
        try {
            futures = executor.invokeAll(filesToAnalyze);
            for (Future<String> future : futures) {
                results.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error analyzing directory: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
        results.forEach(System.out::println);
    }

    private ArrayList<Strategy> getAllFilesToAnalyze(File file) {
        ArrayList<Strategy> filesToAnalyze = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        filesToAnalyze.addAll(getAllFilesToAnalyze(f));
                    } else {
                        filesToAnalyze.add(initializeStrategy(f));
                    }
                }
            }
        } else {
            filesToAnalyze.add(initializeStrategy(file));
        }
        return filesToAnalyze;
    }

    private Strategy initializeStrategy(File file) {
        return new KMP(file, args[1], args[2]);
    }
}
