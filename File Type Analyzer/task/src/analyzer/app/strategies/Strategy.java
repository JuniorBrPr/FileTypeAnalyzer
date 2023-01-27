package analyzer.app.strategies;

import analyzer.app.FileType;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class Strategy implements Callable<String> {
    private final File file;
    private final ArrayList<FileType> patterns;
    String content;
    private String fileType;

    public Strategy(File file, ArrayList<FileType> patterns) {
        this.file = file;
        this.patterns = patterns;
        this.fileType = null;
        this.content = readFile() == null ? "" : readFile();
    }

    abstract boolean matches(String pattern);

    @Override
    public String call() {
        matchPatterns();
        return getFileType();
    }

    private void matchPatterns() {
        AtomicInteger i = new AtomicInteger(0);
        patterns.forEach((pattern) -> {
            if (pattern.priority() > i.get() && matches(pattern.pattern())) {
                i.set(pattern.priority());
                this.fileType = pattern.name();
            }
        });
    }

    private String readFile() {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String getFileType() {
        return this.file.getName() + ": " + (this.fileType != null ? this.fileType : "Unknown file type");
    }
}
