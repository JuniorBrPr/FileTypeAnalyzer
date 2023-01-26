package analyzer.app;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;
import java.util.regex.Pattern;

class FileType {
    private final String filename;
    private final Pattern pattern;
    private final String fileType;

    protected FileType(String filename, String pattern, String fileType) {
        this.filename = filename;
        this.pattern = Pattern.compile(pattern);
        this.fileType = fileType;
    }

    private boolean matches() {
        String fileContent = readFile();
        return Objects.nonNull(fileContent) && this.pattern.matcher(fileContent).find();
    }

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

    protected String getFileType() {
        return matches() ? this.fileType : "Unknown file type";
    }
}
