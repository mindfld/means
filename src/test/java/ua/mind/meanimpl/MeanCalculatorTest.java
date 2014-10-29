package ua.mind.meanimpl;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.mind.mean.Mean;
import ua.mind.mean.Means;

/**
 * Created by mind on 29.10.14.
 * Test for testing functional logic of Mean. Names are self-explanatory
 * @see ua.mind.mean.Mean
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class MeanCalculatorTest {
    @Autowired
    private Mean meanService;

    @Test(expected = IllegalArgumentException.class)
    public void testCheckOnZero() {
        Means testMeans;
        meanService.reset();
        testMeans = meanService.add(0);
        Assert.assertEquals(testMeans.getArithmetic(), 0.0);
        Assert.assertEquals(testMeans.getGeometric(), 0.0);
        Assert.assertEquals(testMeans.getInterquartile(), 0.0);
        //Here should be thrown Illegal argument exception because division by 0
        testMeans.getHarmonic();
    }

    @Test
    public void testGetArithmeticMeanofPositiveNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(10);
        meanService.add(11);
        testMeans = meanService.add(3);
        Assert.assertEquals(testMeans.getArithmetic(), 8.0);
    }

    @Test
    public void testGetArithmeticMeanWithNegativeNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(-10);
        meanService.add(11);
        meanService.add(2);
        testMeans = meanService.add(3);
        Assert.assertEquals(testMeans.getArithmetic(), 1.5);
    }

    @Test
    public void testGetArithmeticMeanWithNegativeOnlyNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(-10);
        meanService.add(-1);
        meanService.add(-2);
        testMeans = meanService.add(-3);
        Assert.assertEquals(testMeans.getArithmetic(), -4.0);
    }

    @Test
    public void testGetGeometricMeanofPositiveNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(5);
        meanService.add(5);
        testMeans = meanService.add(5);
        Assert.assertEquals(testMeans.getGeometric(), 5.0, 0.000000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGeometricMeanWithNegativeNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(-10);
        meanService.add(11);
        meanService.add(2);
        testMeans = meanService.add(3);
        testMeans.getGeometric();
    }

    @Test
    public void testGetHarmonicMeanofPositiveNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(2);
        meanService.add(4);
        testMeans = meanService.add(1);
        Assert.assertEquals(testMeans.getHarmonic(), 1.714285714, 0.000000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHarmonicMeanWithNegativeNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(-2);
        meanService.add(4);
        testMeans = meanService.add(1);
        Assert.assertEquals(testMeans.getHarmonic(), 4.0, 0.000000001);
    }

    @Test
    public void testGetEvenCountInterquartileMeanofPositiveNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(8);
        meanService.add(7);
        meanService.add(2);
        meanService.add(4);
        meanService.add(1);
        meanService.add(3);
        meanService.add(6);
        testMeans = meanService.add(5);
        Assert.assertEquals(testMeans.getInterquartile(), 4.5, 0.000000001);
    }

    @Test
    public void testGetOddCountInterquartileMeanofPositiveNumbers() {
        Means testMeans;
        meanService.reset();
        meanService.add(1);
        meanService.add(3);
        meanService.add(5);
        meanService.add(7);
        meanService.add(9);
        meanService.add(11);
        meanService.add(13);
        meanService.add(17);
        testMeans = meanService.add(15);
        Assert.assertEquals(testMeans.getInterquartile(), 10.0, 0.000000001);
    }


}