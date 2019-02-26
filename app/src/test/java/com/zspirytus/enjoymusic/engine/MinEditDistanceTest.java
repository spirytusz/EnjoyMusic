package com.zspirytus.enjoymusic.engine;

import org.junit.Test;

public class MinEditDistanceTest {

    @Test
    public void test() {
        double d = MinEditDistance.SimilarDegree("adf berbb,", "adfberba.");
        System.out.println("degree = " + d);
    }

}