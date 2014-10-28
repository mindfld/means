package ua.mind.mean;

/**
 * Created by Сергій on 28.10.14.
 */
public interface Mean {
    // resets the means
    public void reset();

    // add a new data-point and return the current (running) means
    public Means add(int value);    // will be called many, many, many, many times!!!

}

