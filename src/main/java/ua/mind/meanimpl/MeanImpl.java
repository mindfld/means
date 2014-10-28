package ua.mind.meanimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mind.mean.Means;
import ua.mind.meanimpl.helpers.CountingJob;
import ua.mind.meanimpl.helpers.HarmonicCountingJob;
import ua.mind.meanimpl.helpers.InterquartileCountingJob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Сергій on 28.10.14.
 */
public class MeanImpl implements Means {

    public static final int BASE_SIZE = 13;
    public static final int COUNTING_THREAD_QUANTITY = 10000;
    List<Integer> baseList;
    long overalSum;
    long overalProduct;

    private static final Logger LOGGER = LoggerFactory.getLogger(MeanImpl.class);

    public MeanImpl() {
        this(BASE_SIZE);
    }

    public MeanImpl(Integer baseSize) {
        this.baseList = new ArrayList<Integer>(baseSize);
        overalSum = 0L;
        overalProduct = 0L;
    }


    @Override
    public double getArithmetic() {
        return overalSum / baseList.size();
    }

    @Override
    public double getGeometric() {
        if (overalProduct < 0) {
            throw new IllegalArgumentException("The geometric mean only applies to positive numbers. Taking the root of a negative product will result in imaginary numbers.");
        }
        return Math.pow(overalProduct, 1.0 / baseList.size());
    }

    @Override
    public double getHarmonic() {
        double harmonicMean = 0.0;
        int numberOfCountThreads = baseList.size() / COUNTING_THREAD_QUANTITY + 1;

        List<CountingJob> jobs = new ArrayList<>();
        for (int i = 0; i < numberOfCountThreads; i++) {
            int start = i * COUNTING_THREAD_QUANTITY;
            int end = (i + 1) * COUNTING_THREAD_QUANTITY < baseList.size() ? (i + 1) * COUNTING_THREAD_QUANTITY : baseList.size();
            jobs.add(new HarmonicCountingJob(baseList, start, end));
        }
        harmonicMean = countInThreads(numberOfCountThreads, jobs);
        return baseList.size() / harmonicMean;
    }


    @Override
    public double getInterquartile() {
        double interquartile = 0.0;
        Collections.sort(baseList);

        int numberOfCountThreads = (baseList.size() - 2 * baseList.size() / 4) / COUNTING_THREAD_QUANTITY + 1;
        List<CountingJob> jobs = new ArrayList<>();
        for (int i = 0; i < numberOfCountThreads; i++) {
            int start = baseList.size() / 4 + i * COUNTING_THREAD_QUANTITY;
            int end = (i + 1) * COUNTING_THREAD_QUANTITY < baseList.size() - baseList.size() / 4 ? (i + 1) * COUNTING_THREAD_QUANTITY : baseList.size() - baseList.size() / 4;
            jobs.add(new InterquartileCountingJob(baseList, start, end));
        }
        interquartile  = countInThreads(numberOfCountThreads, jobs);
        return (2*interquartile)/baseList.size() ;
    }


    public void reset() {
        overalSum = 0L;
        overalProduct = 0L;
        baseList = new ArrayList<Integer>(BASE_SIZE);
    }

    public void add(int value) {
        baseList.add(value);
        overalSum += value;
        overalProduct *= value;
    }

    private double countInThreads(int numberOfCountThreads, List<CountingJob> jobs) {
        double result = 0.0;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfCountThreads);
        List<Future<Double>> results = null;
        try {
            results = executor.invokeAll(jobs);
            executor.shutdown();
            for (Future<Double> item : results) {
                result += item.get();
            }
        } catch (InterruptedException e) {
            LOGGER.error("Jobs was interrupted" + e);
        } catch (ExecutionException e) {
            LOGGER.error("Jobs unable to be executed" + e);
        }
        return result;
    }
}
