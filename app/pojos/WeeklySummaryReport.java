package pojos;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by peter on 5/3/17.
 */
public class WeeklySummaryReport {
    @Getter
    @Setter
    public String firstName;
    @Getter @Setter
    public String lastName;
    @Getter @Setter
    public Long pin;
    @Getter @Setter
    public int numberOfLateness;
    @Getter @Setter
    public int numberOfTimesPunctual;
    @Getter @Setter
    public int numberOfTimesPresent;
    @Getter @Setter
    public int numberOfTimesAbsent;
    @Getter @Setter
    public String totalHoursOfWork;
    @Getter @Setter
    public String expectedHoursOfWork;
}
