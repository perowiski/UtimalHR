package pojos;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by peter on 5/1/17.
 */
public class AttendanceSummaryReport {

    @Getter @Setter
    public String firstName;
    @Getter @Setter
    public String lastName;
    @Getter @Setter
    public Long pin;
    @Getter @Setter
    public int numberOfLateness;
    @Getter @Setter
    public int noClockIn;
    @Getter @Setter
    public int noClockOut;
    @Getter @Setter
    public String totalHoursOfWork;
    @Getter @Setter
    public String expectedHoursOfWork;
    @Getter @Setter
    public int clockIn;
    @Getter @Setter
    public int clockOut;
    @Getter @Setter
    public int numberOfTimesPunctual;
    @Getter @Setter
    public int numberOfTimesPresent;
    @Getter @Setter
    public int numberOfEarlyClockOut;
    @Getter @Setter
    public int numberOfTimesAbsent;
}
