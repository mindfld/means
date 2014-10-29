package ua.mind.mean;

/**
 * Created by Сергій on 28.10.14.
 */
public interface Mean {
    /**
     * resets all means
     */
    public void reset();

    /**
     * add a new data-point and return the current (running) means
     */
    public Means add(int value);

}

