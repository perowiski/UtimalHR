package pojos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by peter on 4/22/17.
 */
public class SimpleAttendanceReport {
    @Getter @Setter
    public String firstName;
    @Getter @Setter
    public String lastName;
    @Getter @Setter
    public Long pin;
    @Getter @Setter
    public LocalDate clockDate;
    @Getter @Setter
    public LocalTime clockIn;
    @Getter @Setter
    public LocalTime clockOut;

}
