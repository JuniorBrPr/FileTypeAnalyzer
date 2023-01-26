package analyzer.app;

import analyzer.app.strategies.KMP;
import analyzer.app.strategies.Strategy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Analyzer {
    private final String[] args;
    private final File file;
    private final HashMap<Integer, String[]> patterns;
    private final ExecutorService executor;

    public Analyzer(String[] args) {
        this.args = args;
        this.file = new File(args[0]);
        this.patterns = getPatterns(new File(args[1]));
        this.executor = Executors.newFixedThreadPool(10);
    }

    private HashMap<Integer, String[]> getPatterns(File file) {
        HashMap<Integer, String[]> patterns = new HashMap<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String line : lines) {
            String[] split = line.split(";");
            patterns.put(Integer.parseInt(split[0]), new String[]{split[1].replace("\"", " ").trim(),
                    split[2].replace("\"", " ").trim()});
            System.out.println(split[0] + " " + split[1] + " " + split[2]);
        }
        return patterns;
    }

    public void analyze() {
        ArrayList<Strategy> filesToAnalyze = getAllFilesToAnalyze(this.file);
        System.out.println("Files to analyze: " + filesToAnalyze.size());
        if (!filesToAnalyze.isEmpty()){
            List<Future<String>> futures;
            ArrayList<String> results = new ArrayList<>();
            try {
                futures = executor.invokeAll(filesToAnalyze);
                for (Future<String> future : futures) {
                    results.add(future.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error analyzing directory: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                executor.shutdown();
            }
            results.forEach(System.out::println);
        }
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
        // TODO: remove this
        return new KMP(file, patterns);
    }
}
