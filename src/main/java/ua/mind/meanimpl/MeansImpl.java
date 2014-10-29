package ua.mind.meanimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
 * Class that implements all counting logic for different kind of means.
 * As base list was chosen ArrayList which might be not fastest collection for adding values
 * due to time that it spend for copying values when it's capacity. But works very was if we retrieving values by index.
 * And as operation of sequential retrieving is used much more often then adding ArrayList was chosen.
 * In order to save time for copying values to new array recommended usage of constructor with initial capacity parameter.
 *
 * @see ua.mind.mean.Means
 * @see ua.mind.mean.Mean
 */
@Component
public class MeansImpl implements Means {

    /**
     * Default capacity of array if it is not defined by user
     */
    public static final int INITIAL_CAPACITY = 13;
    /**
     * Maximum number of values might be counted inside one thread.
     * Also used for dividing base list in to partition for counting in multiple threads.
     */
    public static final int COUNTING_THREAD_QUANTITY = 10000;

    /**
     * Base list for storing of all values
     */
    List<Integer> baseList;

    /**
     * Sum of all elements designed for speeding up counting of arithmetic mean
     */
    double overalSum;
    /**
     * Product of all elements designed for speeding up counting of geometric mean
     */
    double overalProduct;

    private static final Logger LOGGER = LoggerFactory.getLogger(MeansImpl.class);

    /**
     * Default constructor for cases user didn't defined initial capacity
     */
    public MeansImpl() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructor that used for initialization of Means class with capacity defined by user.
     *
     * @param baseSize - initial capacity of base list
     */
    public MeansImpl(Integer baseSize) {
        this.baseList = new ArrayList<Integer>(baseSize);
        overalSum = 0.0;
        overalProduct = 1.0;
    }

    /**
     * Method that counts arithmetic mean.
     *
     * @return arithmetic mean of all values in base list
     */
    @Override
    public double getArithmetic() {
        return overalSum / baseList.size();
    }

    /**
     * Method that counts geometric mean. Due to specification can't be calculated for
     * negative values (result will be in imaginary numbers).
     *
     * @return geometric mean of all values in base list
     * @see http://en.wikipedia.org/wiki/Geometric_mean
     */
    @Override
    public double getGeometric() {
        if (overalProduct < 0) {
            throw new IllegalArgumentException("The geometric mean only applies to positive numbers. Taking the root of a negative product will result in imaginary numbers.");
        }
        return Math.pow(overalProduct, 1.0 / baseList.size());
    }

    /**
     * Method that count harmonic mean of all values in base list. If number of values greater than COUNTING_THREAD_QUANTITY,
     * base list is splits into parts and sum calculated in multiple threads.
     *
     * @return harmonic mean of values in base list
     */
    @Override
    public double getHarmonic() {
        double harmonicMean = 0.0;

        //decide number of counting threads
        int numberOfCountThreads = baseList.size() / COUNTING_THREAD_QUANTITY + 1;
        List<CountingJob> jobs = new ArrayList<>();

        for (int i = 0; i < numberOfCountThreads; i++) {
            //find start and end indexes in base list for current thread
            int start = i * COUNTING_THREAD_QUANTITY;
            int end = (i + 1) * COUNTING_THREAD_QUANTITY < baseList.size() ? (i + 1) * COUNTING_THREAD_QUANTITY : baseList.size();
            jobs.add(new HarmonicCountingJob(baseList, start, end));
        }
        //denominator in harmonic mean formula
        harmonicMean = countInThreads(numberOfCountThreads, jobs);
        return baseList.size() / harmonicMean;
    }

    /**
     * Method that count interquartile mean of all values in base list. If number of values greater than COUNTING_THREAD_QUANTITY,
     * base list is splits into parts and sum calculated in multiple threads.
     *
     * @return interquartile mean of values in base list
     */
    @Override
    public double getInterquartile() {

        double interquartile = 0.0;
        // as there no business requirement to save order of elements in base list,
        // we sort base list and not copy of it to save some time.
        Collections.sort(baseList);
        //remove first quarter and fourth quarter from counting of mean.
        int numberOfCountThreads = (baseList.size() - 2 * baseList.size() / 4) / COUNTING_THREAD_QUANTITY + 1;
        List<CountingJob> jobs = new ArrayList<>();

        for (int i = 0; i < numberOfCountThreads; i++) {
            //starting index is the begining of <b>second</b> quarter
            int start = baseList.size() / 4 + i * COUNTING_THREAD_QUANTITY;
            //end ndex is the end of <b>third</b> quarter
            int end = (i + 1) * COUNTING_THREAD_QUANTITY < baseList.size() - baseList.size() / 4 ? (i + 1) * COUNTING_THREAD_QUANTITY : baseList.size() - baseList.size() / 4;
            jobs.add(new InterquartileCountingJob(baseList, start, end));
        }
        //numerator in interquartile mean formula
        interquartile = countInThreads(numberOfCountThreads, jobs);
        return (2.0 * interquartile) / baseList.size();
    }

    /**
     * Reset all values in base list and auxiliary variables
     */
    public void reset() {
        overalSum = 0.0;
        overalProduct = 1.0;
        baseList = new ArrayList<Integer>(INITIAL_CAPACITY);
    }

    /**
     * Add value to base list and auxiliary variables.
     */
    public void add(int value) {
        baseList.add(value);
        overalSum += value;
        overalProduct *= value;
    }

    /**
     * Method that runs counting jobs in multiple threads and return results
     *
     * @param numberOfCountThreads number of threads to start
     * @param jobs                 list of jobs to be run
     * @return result of counting job execution.
     */
    private double countInThreads(int numberOfCountThreads, List<CountingJob> jobs) {
        double result = 0.0;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfCountThreads);
        List<Future<Double>> results = null;
        try {
            results = executor.invokeAll(jobs);
            executor.shutdown();
            //get sum from all jobs
            for (Future<Double> item : results) {
                result += item.get();
            }
        } catch (InterruptedException e) {
            LOGGER.error("Jobs was interrupted" + e);
        } catch (ExecutionException e) {
            LOGGER.error("Jobs unable to be executed" + e);
            //rethrow job exceptions
            throw new IllegalArgumentException(e);
        }
        return result;
    }
}
