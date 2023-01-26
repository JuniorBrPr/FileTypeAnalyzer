package analyzer.app;

public class Analyzer {
    private final String[] args;
    private final FileType fileType;

    public Analyzer(String[] args) {
        this.args = args;
        this.fileType = createFileType();
    }

    public void analyze() {
        System.out.println(this.fileType.getFileType());
    }

    private FileType createFileType() {
        return new FileType(args[0], args[1], args[2]);
    }
}
