package analyzer.app.strategies;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.Callable;

public abstract class Strategy implements Callable<String> {
    private final File file;
    final String pattern;
    private final String fileType;
    String content;

    public Strategy(File file, String pattern, String fileType) {
        this.file = file;
        this.pattern = pattern;
        this.fileType = fileType;
        this.content = readFile() == null ? "" : readFile();
    }

    abstract boolean matches();

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
        return this.file.getName() + ": " + (matches() ? this.fileType : "Unknown file type");
    }

    @Override
    public String call() {
        return getFileType();
    }
}
