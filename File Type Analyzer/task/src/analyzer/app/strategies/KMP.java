package analyzer.app.strategies;

public class KMP extends Strategy {
    String patternString;

    public KMP(String filename, String pattern, String fileType) {
        super(filename, pattern, fileType);
        this.patternString = pattern;
    }

    @Override
    boolean matches() {
        return KMPSearch();
    }

    private boolean KMPSearch() {
        int patternLength = this.pattern.pattern().length();
        int contentLength = this.content.length();
        int[] lps = prefixFunction();

        int j = 0;
        int i = 0;
        do {
            if (patternString.charAt(j) == content.charAt(i)) {
                j++;
                i++;
            }
            if (j == patternLength) {
                return true;
            } else if (i < contentLength && patternString.charAt(j) != content.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i = i + 1;
                }
            }
        } while ((contentLength - i) >= (patternLength - j));
        return false;
    }

    private int[] prefixFunction() {
        int[] pi = new int[patternString.length()];
        pi[0] = 0;
        int k = 0;
        for (int i = 1; i < patternString.length(); i++) {
            while (k > 0 && patternString.charAt(k) != patternString.charAt(i)) {
                k = pi[k - 1];
            }
            if (patternString.charAt(k) == patternString.charAt(i)) {
                k++;
            }
            pi[i] = k;
        }
        return pi;
    }
}
