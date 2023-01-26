package analyzer.app.strategies;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class Strategy implements Callable<String> {
    private final File file;
    private final HashMap<Integer, String[]> patterns;
    private String fileType;
    String content;

    public Strategy(File file, HashMap<Integer, String[]> patterns) {
        this.file = file;
        this.patterns = patterns;
        this.fileType = null;
        this.content = readFile() == null ? "" : readFile();
    }

    abstract boolean matches(String pattern);

    private void matchPatterns() {
        int i = 0;
        for (Map.Entry<Integer, String[]> entry : patterns.entrySet()) {
            Integer key = entry.getKey();
            String[] value = entry.getValue();
            System.out.println("Searching for " + value[0] + " in " + this.file.getName());
            if (matches(value[0]) && key > i) {
                i = key;
                this.fileType = value[1];
            }
        }
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

    @Override
    public String call() {
        matchPatterns();
        return getFileType();
    }
}
