package ua.mind.meanimpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.mind.mean.Mean;
import ua.mind.mean.Means;

import java.util.Random;

/**
 * Created by mind on 29.10.14.
 * Test for testing ability of Mean to handle adding of "will be called many, many, many, many times!!!" business requirement
 * and showing calculation time. Names are self-explanatory
 * @see ua.mind.mean.Mean
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class MeanCalculatorLoadTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeansImpl.class);
    public static final int NUMBER_OF_ITER = 100000;

    @Autowired
    private Mean meanService;

    @Test
    public void testArithmeticBigNumberMeanCount() {
        Means testMeans;
        Random rnd = new Random(13);
        meanService.reset();
        for (int i = 0; i < NUMBER_OF_ITER; i++) {
            meanService.add(rnd.nextInt());
        }
        testMeans = meanService.add(1);
        long benchMark = System.nanoTime();
        double result = testMeans.getArithmetic();
        benchMark = System.nanoTime() - benchMark;
        LOGGER.info("Arithmetic mean of 100000 random values: " + String.valueOf(result));
        LOGGER.info("Time of counting: " + String.valueOf(benchMark) + " nanoseconds");
    }

    @Test
    public void testGeometricBigNumberMeanCount() {
        Means testMeans;
        Random rnd = new Random(13);
        meanService.reset();
        for (int i = 0; i < NUMBER_OF_ITER; i++) {
            int val = rnd.nextInt();
            val = val < 0 ? -val : val;
            meanService.add(val);
        }
        testMeans = meanService.add(1);
        long benchMark = System.nanoTime();
        double result = testMeans.getGeometric();
        benchMark = System.nanoTime() - benchMark;
        LOGGER.info("Geometric mean of 100000 random values: " + String.valueOf(result));
        LOGGER.info("Time of counting: " + String.valueOf(benchMark) + " nanoseconds");
    }

    @Test
    public void testHarmonicBigNumberMeanCount() {
        Means testMeans;
        Random rnd = new Random(13);
        meanService.reset();
        for (int i = 0; i < NUMBER_OF_ITER; i++) {
            int val = rnd.nextInt();
            val = val < 0 ? -val : val;
            meanService.add(val);
        }
        testMeans = meanService.add(1);
        long benchMark = System.currentTimeMillis();
        double result = testMeans.getHarmonic();
        benchMark = System.currentTimeMillis() - benchMark;
        LOGGER.info("Geometric mean of 100000 random values: " + String.valueOf(result));
        LOGGER.info("Time of counting: " + String.valueOf(benchMark) + " milliseconds");
    }

    @Test
    public void testInterquartileBigNumberMeanCount() {
        Means testMeans;
        Random rnd = new Random(13);
        meanService.reset();
        for (int i = 0; i < NUMBER_OF_ITER; i++) {
            int val = rnd.nextInt();
            val = val < 0 ? -val : val;
            meanService.add(val);
        }
        testMeans = meanService.add(1);
        long benchMark = System.currentTimeMillis();
        double result = testMeans.getHarmonic();
        benchMark = System.currentTimeMillis() - benchMark;
        LOGGER.info("Geometric mean of 100000 random values: " + String.valueOf(result));
        LOGGER.info("Time of counting: " + String.valueOf(benchMark) + " milliseconds");
    }

}