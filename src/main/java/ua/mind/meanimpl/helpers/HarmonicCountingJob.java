package ua.mind.meanimpl.helpers;

import java.util.List;

/**
 * Created by Сергій on 28.10.14.
 */
public class HarmonicCountingJob extends CountingJob {

    public HarmonicCountingJob(List<Integer> baseList, int start, int end) {
        super(baseList, start, end);
    }

    @Override
    public Double call() throws Exception {
        Double sum = 0.0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += 1 / (list.get(i));
        }
        return sum;
    }
}
