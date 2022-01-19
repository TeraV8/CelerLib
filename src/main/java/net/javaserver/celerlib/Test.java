package net.javaserver.celerlib;

public class Test {
    public static void main(String[] args) {
        OptimizedTrig.init();
        long time = System.currentTimeMillis() + 10;
        while (System.currentTimeMillis() < time);
        System.out.println("Testing regular sin()");
        time = System.currentTimeMillis() + 20000;
        int reps = 0;
        while (System.currentTimeMillis() < time) {
            Math.sin(Math.random());
            reps++;
        }
        System.out.println("Approximately "+reps/20+" calculations per second");
        System.out.println("Testing cached sin()");
        time = System.currentTimeMillis() + 20000;
        reps = 0;
        while (System.currentTimeMillis() < time) {
            OptimizedTrig.sin(Math.random());
            reps++;
        }
        System.out.println("Approximately "+reps/20+" calculations per second");
    }
}
