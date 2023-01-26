package analyzer.app.strategies;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Pattern;

public abstract class Strategy {
    private final String filename;
    Pattern pattern;
    private final String fileType;
    String content;

    public Strategy(String filename, String pattern, String fileType) {
        this.filename = filename;
        this.pattern = Pattern.compile(pattern);
        this.fileType = fileType;
        this.content = readFile() == null ? "" : readFile();
    }

    abstract boolean matches();

    private String readFile() {
        File file = new File(this.filename);
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String getFileType() {
        return matches() ? this.fileType : "Unknown file type";
    }
}
