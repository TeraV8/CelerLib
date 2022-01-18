package net.javaserver.celerlib;

import org.apache.logging.log4j.Level;

public class OptimizedTrig {
    private static double[] sinResults;
    private static double[] sinFactors;
    private static boolean sinConcurrent = false;
    private static double[] cosResults;
    private static double[] cosFactors;
    private static boolean cosConcurrent = false;
    private static double[] tanResults;
    private static double[] tanFactors;
    private static boolean tanConcurrent = false;
    private static double[] asinResults;
    private static double[] asinFactors;
    private static boolean asinConcurrent = false;
    private static double[] acosResults;
    private static double[] acosFactors;
    private static boolean acosConcurrent = false;
    private static double[] atanResults;
    private static double[] atanFactors;
    private static boolean atanConcurrent = false;
    private static double[] sinhResults;
    private static double[] sinhFactors;
    private static boolean sinhConcurrent = false;
    private static double[] coshResults;
    private static double[] coshFactors;
    private static boolean coshConcurrent = false;
    private static double[] tanhResults;
    private static double[] tanhFactors;
    private static boolean tanhConcurrent = false;
    private static int cacheSize;
    private static boolean sorted;
    private OptimizedTrig() {}
    static void init() {
        cacheSize = 0;
        if (!CelerConfig.optimization.optimizeTrig) return;
        if (CelerConfig.caching.trigCacheSize <= 0) return;
        sorted = CelerConfig.optimization.concurrentCacheSorting;
        cacheSize = CelerConfig.caching.trigCacheSize;
        sinResults = new double[cacheSize];
        sinFactors = new double[cacheSize];
        cosResults = new double[cacheSize];
        cosFactors = new double[cacheSize];
        tanResults = new double[cacheSize];
        tanFactors = new double[cacheSize];
        asinResults = new double[cacheSize];
        asinFactors = new double[cacheSize];
        acosResults = new double[cacheSize];
        acosFactors = new double[cacheSize];
        atanResults = new double[cacheSize];
        atanFactors = new double[cacheSize];
        sinhResults = new double[cacheSize];
        sinhFactors = new double[cacheSize];
        coshResults = new double[cacheSize];
        coshFactors = new double[cacheSize];
        tanhResults = new double[cacheSize];
        tanhFactors = new double[cacheSize];
        for (int i=0;i<cacheSize;i++) {
            sinResults[i] = Double.NaN;
            sinFactors[i] = Double.NaN;
            cosResults[i] = Double.NaN;
            cosFactors[i] = Double.NaN;
            tanResults[i] = Double.NaN;
            tanFactors[i] = Double.NaN;
            asinResults[i] = Double.NaN;
            asinFactors[i] = Double.NaN;
            acosResults[i] = Double.NaN;
            acosFactors[i] = Double.NaN;
            atanResults[i] = Double.NaN;
            atanFactors[i] = Double.NaN;
            sinhResults[i] = Double.NaN;
            sinhFactors[i] = Double.NaN;
            coshResults[i] = Double.NaN;
            coshFactors[i] = Double.NaN;
            tanhResults[i] = Double.NaN;
            tanhFactors[i] = Double.NaN;
        }
    }
    private static class Sorter implements Runnable {
        public void run() {
            try {
                
            } catch (Throwable t) {
                Init.logger.catching(Level.ERROR, t);
                sorted = false;
            }
        }
    }
    public static double sin(double in) {
        if (cacheSize == 0) return Math.sin(in);
        if (sinConcurrent) return Math.sin(in); // concurrent modifications could cause problems
        // check cache
        int nextCacheIndex = -1;
        if (sorted) {
            // go through the cache sorted
            int mindex = 0;
            int maxdex = cacheSize;
            do {
                int probe = (maxdex - mindex) / 2 + mindex;
                if (sinConcurrent) {
                    // we didn't get to finish our search ;(
                    return Math.sin(in);
                }
                int cmp = Double.compare(in, sinFactors[probe]);
                if (cmp == 0) {
                    // this is the one!
                    return sinResults[probe];
                } else if (cmp < 0) {
                    // we are too high
                    maxdex = probe;
                } else if (cmp > 0) {
                    // we are too low
                    mindex = probe;
                }
            } while (maxdex - mindex > 2);
            // now if we haven't found it yet, we've narrowed it down to three positions
            if (sinConcurrent) return Math.sin(in);
            for (;mindex<=maxdex;mindex++) {
                int cmp = Double.compare(in, sinFactors[mindex]);
                if (cmp == 0) {
                    // this is it!
                    return sinResults[mindex];
                } else if (cmp < 0) {
                    // too low, our result isn't here
                    nextCacheIndex = mindex;
                    break;
                }
                // too high, keep going
            }
        } else {
            // uh oh go through the cache one-by-one
            for (int i=0;i<cacheSize;i++) {
                if (Double.compare(sinFactors[i], in) == 0) {
                    // this is the one
                    return sinResults[i];
                } else if (Double.isNaN(sinFactors[i])) {
                    // reached the end of the cache
                    nextCacheIndex = i;
                    break;
                }
            }
        }
        // not found in the cache, calculate the hard way
        double result = Math.sin(in);
        if (nextCacheIndex == -1) {
            // looks like no space was found in the cache, guess we won't cache the result after all
        } else {
            // even though it wasn't in the cache, we picked out a good place for it
            // put it on in, and if it's sorted already, it'll stay that way
            if (sinConcurrent) return result;
            sinConcurrent = true;
            sinFactors[nextCacheIndex] = in;
            sinResults[nextCacheIndex] = result;
            sinConcurrent = false;
        }
        return result;
    }
}
