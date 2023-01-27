package analyzer.app;

import analyzer.app.strategies.KMP;
import analyzer.app.strategies.Strategy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Analyzer {
    private final File file;
    private final ArrayList<FileType> fileTypes;
    private final ExecutorService executor;

    public Analyzer(String[] args) {
        this.file = new File(args[0]);
        this.fileTypes = getPatterns(new File(args[1]));
        this.executor = Executors.newFixedThreadPool(10);
    }

    private ArrayList<FileType> getPatterns(File file) {
        ArrayList<FileType> fileTypes = new ArrayList<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String line : lines) {
            String[] split = line.split(";");
            FileType p = new FileType(Integer.parseInt(split[0]), split[1].replace("\"", " ").trim(),
                    split[2].replace("\"", " ").trim());
            fileTypes.add(p);
        }
        return fileTypes;
    }

    public void analyze() {
        ArrayList<Strategy> filesToAnalyze = getAllFilesToAnalyze(this.file);
        if (!filesToAnalyze.isEmpty()) {
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
        return new KMP(file, fileTypes);
    }
}
