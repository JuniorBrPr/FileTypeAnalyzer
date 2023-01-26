package analyzer.app.strategies;

import java.io.File;

public class KMP extends Strategy {
    public KMP(File file, String pattern, String fileType) {
        super(file, pattern, fileType);
    }

    @Override
    boolean matches() {
        return KMPSearch();
    }

    private boolean KMPSearch() {
        int patternLength = this.pattern.length();
        int contentLength = this.content.length();
        int[] lps = prefixFunction();

        int j = 0;
        int i = 0;
        do {
            if (pattern.charAt(j) == content.charAt(i)) {
                j++;
                i++;
            }
            if (j == patternLength) {
                return true;
            } else if (i < contentLength && pattern.charAt(j) != content.charAt(i)) {
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
        int[] pi = new int[pattern.length()];
        pi[0] = 0;
        int k = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) {
                k = pi[k - 1];
            }
            if (pattern.charAt(k) == pattern.charAt(i)) {
                k++;
            }
            pi[i] = k;
        }
        return pi;
    }
}
