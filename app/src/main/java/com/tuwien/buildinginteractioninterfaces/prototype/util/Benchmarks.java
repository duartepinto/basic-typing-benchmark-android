package com.tuwien.buildinginteractioninterfaces.prototype.util;

import android.os.Build;

public class Benchmarks {
    public static double keystrokesPerChar(String is, String t){
        return keystrokesPerChar(is.length(), t.length());
    }

    public static double keystrokesPerChar(int isLength, int tLength){
        return (double) isLength/(double) tLength;
    }

    public static double msdErrorRate(String p, String t){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return msd(p,t)/(double)Integer.max(p.length(), t.length());
        }else{
            return msd(p,t)/(double)max(p.length(), t.length());
        }
    }

    private static int max(int a, int b){
        if (a >= b)
            return a;
        else
            return b;
    }

    public static int msd(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }
}
