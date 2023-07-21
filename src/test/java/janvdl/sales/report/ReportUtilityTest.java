package janvdl.sales.report;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static janvdl.sales.report.ReportUtility.periodKey;
import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ReportUtilityTest {

    @Test
    void testPeriodKey() {

        long period = 15 * 60; // seconds

        LocalDateTime date = of(2017, 6, 5, 12, 15);
        long key = periodKey(date, period);

        for (int i = 1; i <= 29; i++) {
            LocalDateTime dateToTest = date.plusSeconds(30 * i);
            // System.out.println("Test " + dateToTest);
            assertEquals(key, periodKey(dateToTest, period), "Key is invalid" + dateToTest);
        }

        LocalDateTime dateToTest = date.plusSeconds(period + 1);
        assertNotEquals(key, periodKey(dateToTest, period), "Key must be different ");

        dateToTest = date.plusSeconds(-1);
        assertNotEquals(key, periodKey(dateToTest, period), "Key must be different ");
    }

}
