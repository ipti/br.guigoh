/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.ipti.guigoh.util;

import java.util.Random;

/**
 *
 * @author ipti004
 */
public class RandomGenerator {
    
    public static String generate(int max) {
        Random random = new Random(System.currentTimeMillis());
        String stemp = "";
        for (int i = 0; i < max; ++i) {
            stemp += random.nextInt(10);
        }
        return stemp;
    }
}
