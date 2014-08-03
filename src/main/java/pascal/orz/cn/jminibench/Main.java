/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pascal.orz.cn.jminibench;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;

/**
 *
 * @author koduki
 */
public class Main {

    public static void main(String[] args) {

        int trialNumber = 3;
        printBenchInfomation(trialNumber);

        bench(trialNumber, "concat string with StringBuilder(100,000,000)", new Runnable() {
            @Override
            public void run() {
                int limit = 100000000;

                String[] base = {"a", "b", "c"};
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < limit; i++) {
                    sb.append(base[i%3]);
                }

                sb.toString();
            }
        });

        bench(trialNumber, "concat string with plus(100,000)", new Runnable() {
            @Override
            public void run() {
                int limit = 100000;

                String xs = "";
                for (int i = 0; i < limit; i++) {
                    xs = xs + "a";
                }
            }
        });
    }

    static void bench(int trialNumber, String msg, Runnable callback) throws MathIllegalArgumentException {
        List<Double> results = new ArrayList<>();
        for (int i = 0; i < trialNumber; i++) {
            long start = System.currentTimeMillis();
            callback.run();
            long end = System.currentTimeMillis();

            double result = (double) (end - start);
            results.add(result);
        }
        pinrtReport(msg, results);
    }

    static void printBenchInfomation(int trialNumber) {
        System.out.print("Java Version:");
        System.out.println(System.getProperty("java.version"));

        System.out.print("OS Name:");
        System.out.println(System.getProperty("os.name"));

        System.out.println("Number of trials:" + trialNumber);
    }

    static void pinrtReport(String msg, List<Double> results) throws MathIllegalArgumentException {
        double[] xs = new double[results.size()];
        for (int i = 0; i < results.size(); i++) {
            xs[i] = results.get(i);
        }

        double median = new Median().evaluate(xs);
        int max = (int) StatUtils.max(xs);
        int min = (int) StatUtils.min(xs);

        System.err.print("Method: " + msg + ", ");
        System.out.printf("median:%.2f ms, max:%d ms, min:%d ms", median, max, min);
        System.out.println();
    }

}
