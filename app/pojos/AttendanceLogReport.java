package pojos;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by peter on 4/22/17.
 */
public class AttendanceLogReport {

    public String firstName;
    public String lastName;
    public Long pin;
    public LocalDate day;
    public List<LocalTime> clockList;
}
