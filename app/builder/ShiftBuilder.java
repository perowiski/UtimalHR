package builder;

import models.Shift;
import models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by peter on 2/20/17.
 */
public class ShiftBuilder {

    private ArrayList<Shift> shifts = new ArrayList<>();
    private String today;
    private ArrayList<User> users;
    private String expectedClockIn;
    private String expectedClockOut;

    public ShiftBuilder(ArrayList<User> user, String expectedClockIn, String expectedClockOut, String today) {
        this.users = user;
        this.today = today;
        this.expectedClockIn = expectedClockIn;
        this.expectedClockOut = expectedClockOut;
    }

    public void build() throws ParseException {

        for (User user : users) {
            Shift shift = new Shift();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date t = dateFormat.parse(today);
            Date cIn = simpleDateFormat.parse(expectedClockIn);
            Date cOut = simpleDateFormat.parse(expectedClockOut);
            //shift.setShiftDate(t);
            //shift.setExpectedClockIn(cIn);
            //shift.setExpectedClockOut(cOut);

            //shift.setUser(user);
            shifts.add(shift);
        }
    }

    public ArrayList<Shift> getShifts() {
        return shifts;
    }
}
