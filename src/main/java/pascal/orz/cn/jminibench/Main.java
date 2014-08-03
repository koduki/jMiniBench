/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pascal.orz.cn.jminibench;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;

/**
 *
 * @author koduki
 */
public class Main {

    public static class Person {

        String firstName;
        String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    }

    public static void main(String[] args) {

        int trialNumber = 10;
        printBenchInfomation(trialNumber);

        bench(trialNumber, "concat string with StringBuilder(100,000,000)", new Runnable() {
            @Override
            public void run() {
                int limit = 100000000;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < limit; i++) {
                    sb.append("a");
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

        bench(trialNumber, "replace string with regular expression", new Runnable() {
            @Override
            public void run() {
                int limit = 1000000;
                for (int i = 0; i < limit; i++) {
                    "<p>abcdefghireoagjeaiojgioeajgrioeajgioeajiogjaeoigjaeiogjaeiojgioaejgrioaejgioj3a8ty378hro2uifgaergyq3ojgio4yg83jqptoq4jg98qyfjopegoieut89g3uq0tkq3pogkoug89tuh4k[phkq4ug89q4khopjhiojdsoijklmn</p>"
                            .replaceAll("<", "&lt;")
                            .replaceAll(">", "&gt;");
                }
            }
        });

        bench(trialNumber, "property access with refrection", new Runnable() {
            @Override
            public void run() {
                //               int limit = 100000;
                int limit = 1000000;
                Person person = new Person();
                try {
                    for (int i = 0; i < limit; i++) {
                        Method setter1 = Person.class.getMethod("setFirstName", String.class);
                        Method setter2 = Person.class.getMethod("setLastName", String.class);

                        Method getter1 = Person.class.getMethod("getFirstName");
                        Method getter2 = Person.class.getMethod("getLastName");

                        setter1.invoke(person, "first name");
                        setter2.invoke(person, "last name");
                        getter1.invoke(person);
                        getter2.invoke(person);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        bench(trialNumber, "property access with beanutils", new Runnable() {
            @Override
            public void run() {
                //               int limit = 100000;
                int limit = 1000000;
                Person person = new Person();
                try {
                    for (int i = 0; i < limit; i++) {
                        BeanUtils.setProperty(person, "firstName", "first name");
                        BeanUtils.setProperty(person, "lastName", "last name");

                        BeanUtils.getProperty(person, "firstName");
                        BeanUtils.getProperty(person, "lastName");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    static void bench(int trialNumber, String msg, Runnable callback) throws MathIllegalArgumentException {
        List<Double> results = new ArrayList<Double>();
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

        System.out.print("Java Name:");
        System.out.print(System.getProperty("java.vm.vendor") + " ");
        System.out.println(System.getProperty("java.vm.name"));
        
        System.out.print("Java Version:");
        System.out.print(System.getProperty("java.version"));
        System.out.print("(");
        System.out.print(System.getProperty("java.vm.version"));
        System.out.print(")");
        System.out.println();
        
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
