package analyzer.app.strategies;

import analyzer.app.FileType;

import java.io.File;
import java.util.ArrayList;

public class KMP extends Strategy {
    public KMP(File file, ArrayList<FileType> patterns) {
        super(file, patterns);
    }

    @Override
    boolean matches(String pattern) {
        return KMPSearch(pattern);
    }

    private boolean KMPSearch(String pattern) {
        int patternLength = pattern.length();
        int contentLength = this.content.length();
        int[] lps = prefixFunction(pattern);

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

    private int[] prefixFunction(String pattern) {
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
