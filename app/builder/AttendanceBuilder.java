package builder;

import models.Attendance;
import models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author tolet
 */
public class AttendanceBuilder {

    private final ArrayList<User> users;
    private final String today;

    private final String[] clockIns = {"06:30:10", "07:45:22", "08:00:00", "08:00:01", "07:23:02", "07:05:15", "07:11:22", "07:18:23", "07:59:00"};
    private final String[] clockOuts = {"17:30:31", "17:07:13", "18:00:00", "18:03:07", "17:04:09", "17:05:30", "17:11:21", "17:12:15", "17:30:55"};
    private final Random random = new Random();

    private final ArrayList<Attendance> attendanceList = new ArrayList<>();

    public AttendanceBuilder(ArrayList<User> users, String today) {
        this.users = users;
        this.today = today;
    }

    public void build() throws ParseException {
        for (User user : users) {

            Attendance attendance = new Attendance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date d = dateFormat.parse(today);
            attendance.setClockDate(d);
            attendance.setUser(user);
            int clockInIndex = random.nextInt(clockIns.length);
            int clockOutIndex = random.nextInt(clockOuts.length);

            String clockIn = clockIns[clockInIndex];
            String clockOut = clockOuts[clockOutIndex];

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
            Date s1 = dateFormat1.parse(clockIn);
            Date s2 = dateFormat1.parse(clockOut);

            attendance.setClockIn(s1);
            attendance.setClockOut(s2);

            attendanceList.add(attendance);
        }
    }

    public ArrayList<Attendance> getAttendanceList() {
        return this.attendanceList;
    }

}
