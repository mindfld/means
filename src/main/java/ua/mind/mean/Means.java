package ua.mind.mean;

/**
 * Created by Сергій on 28.10.14.
 */
public interface Means {

    /**
     * Method that count arithmetic mean
     *
     * @return arithmetic mean off all values.
     */
    public double getArithmetic();

    /**
     * Method that count geometric mean
     *
     * @return geometric mean off all values.
     */
    public double getGeometric();

    /**
     * Method that count harmonic mean
     *
     * @return harmonic mean off all values.
     */
    public double getHarmonic();

    /**
     * Method that count interquartile mean
     *
     * @return interquartile mean off all values.
     */
    public double getInterquartile();

}
