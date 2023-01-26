package analyzer.app.strategies;

public class Naive extends Strategy {
    public Naive(String filename, String pattern, String fileType) {
        super(filename, pattern, fileType);
    }

    @Override
    boolean matches() {
        return this.pattern.matcher(this.content).find();
    }
}
