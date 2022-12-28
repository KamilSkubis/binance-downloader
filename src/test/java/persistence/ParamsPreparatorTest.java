package persistence;

import org.junit.Test;

import java.time.LocalDateTime;

public class ParamsPreparatorTest {

    @Test
    public void givenDateWithMoreThan1000units_fromToday_ShouldCalculateProperParams() {
        var from = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        var today = LocalDateTime.now();


    }

}
