package controllers;

import builder.AttendanceBuilder;
import builder.ShiftBuilder;
import builder.UserBuilder;
import enums.RoleType;
import enums.StatusType;
import models.*;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import pojos.Login;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by peter on 2/16/17.
 */

public class AuthenticationController extends Controller {

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public AuthenticationController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    @Transactional
    public Result authenticate() {
        Form<Login> loginForm = formFactory.form(Login.class);
        Login login = loginForm.bindFromRequest().get();
        UserId userId = new UserId();
        userId.companyId = login.getCompanyId();
        userId.pin = login.getPin();

        EntityManager entityManager = jpaApi.em();
        User user = entityManager.find(User.class, userId);

        if (user != null) {
            if (login.getPassword().equals(user.password)
                    && user.statusType != StatusType.INACTIVE) {
                session().put("companyId", String.valueOf(userId.companyId));
                session().put("pin", String.valueOf(userId.pin));
                session().put("roleType", String.valueOf(user.roleType));
                session().put("firstName", user.firstName);
                session().put("lastName", user.lastName);
                session().put("id", String.valueOf(user.account.id));
                return redirect("/" + user.roleType.toString().toLowerCase() + "/dashboard");
            }
        }
        return redirect(routes.IndexController.index());
    }

    @Transactional
    public Result buildUsers() {
        EntityManager entityManager = jpaApi.em();
        UserBuilder userBuilder = new UserBuilder(2000L, "/home/peter/Desktop/users.dat", "", "@ultimailhr.com.ng", RoleType.USER, StatusType.ACTIVE, "Lagos", "Nigeria", 1L);
        try {
            userBuilder.build();
            ArrayList<User> users = userBuilder.getUsers();
            for (User user : users) {

                entityManager.persist(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok("Successful");
    }

    @Transactional
    public Result buildAttendances() {
        EntityManager entityManager = jpaApi.em();

        ArrayList<User> users = new ArrayList<>();
        String result = "Success";
        List list = entityManager.createQuery("SELECT u FROM User u").getResultList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            User user = (User) iterator.next();
            users.add(user);
        }

        AttendanceBuilder attendanceBuilder = new AttendanceBuilder(users, "20-02-2017");

        try {
            attendanceBuilder.build();
            ArrayList<Attendance> attendances = attendanceBuilder.getAttendanceList();
            for (Attendance attendance : attendances) {
                entityManager.persist(attendance);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ok("" + result);
    }


    @Transactional
    public Result buildShifts() {
        EntityManager entityManager = jpaApi.em();

        ArrayList<User> users = new ArrayList<>();
        String result = "Success";
        List list = entityManager.createQuery("SELECT u FROM User u").getResultList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            User user = (User) iterator.next();
            users.add(user);
        }
        ShiftBuilder shiftBuilder = new ShiftBuilder(users, "08:00:00", "17:00:00", "20-02-2017");
        try {
            shiftBuilder.build();
            ArrayList<Shift> shifts = shiftBuilder.getShifts();
            for (Shift shift : shifts) {
                entityManager.persist(shift);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ok("" + result);
    }

    @Transactional
    public Result buildShiftAndClock(){

        final String[] clockIns = {"06:30:10", "07:15:45", "07:45:22", "08:00:00", "08:00:01", "07:23:02", "07:05:15", "07:11:22", "07:18:23", "07:59:00"};
        final String[] clockOuts = {"17:30:31", "19:25:31", "17:07:13", "18:00:00", "18:03:07", "17:04:09", "17:05:30", "17:11:21", "17:12:15", "17:30:55"};
        final Random random = new Random();

        EntityManager entityManager = jpaApi.em();
        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        List<LocalDate> localDates = getDateBetween("2017-03-01", "2017-05-31");
        for(User user : users){
            for(LocalDate localDate : localDates){
                Shift shift = new Shift();
                shift.user = user;
                shift.shiftDate = localDate;
                shift.expectedClockIn = LocalTime.parse("08:00:00");
                shift.expectedClockOut = LocalTime.parse("17:00:00");
                entityManager.persist(shift);

                Attendance attendance = new Attendance();
                attendance.shift = shift;
                attendance.user = user;
                attendance.clockDate = localDate;
                attendance.clockIn = LocalTime.parse(clockIns[random.nextInt(clockIns.length)]);
                attendance.clockOut = LocalTime.parse(clockOuts[random.nextInt(clockOuts.length)]);
                entityManager.persist(attendance);
            }
        }

        return ok("Finished.....");
    }

    public List<LocalDate> getDateBetween(String s, String e){
        LocalDate startDate = LocalDate.parse(s);
        LocalDate endDate = LocalDate.parse(e);
        List<LocalDate> localDates = new ArrayList<>();
        while (!startDate.isAfter(endDate)){
            localDates.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return localDates;
    }

    @Transactional
    public Result buildBirthDays(){
        String[] years = {"1980", "1982", "1986", "1984", "1970", "1990", "1993", "1995", "1975", "1978", "1987"};
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] days = {"01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "25", "27", "28"};
        EntityManager entityManager = jpaApi.em();
        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();

        Random random = new Random();

        for(User user : users){

            int yIndex = random.nextInt(years.length);
            int mIndex = random.nextInt(months.length);
            int dIndex = random.nextInt(days.length);
            //String d = days[dIndex] + "-" + months[mIndex] + "-" + years[yIndex];

            String d = years[yIndex] + "-" + months[mIndex] + "-" + days[dIndex];


            try {
                user.dateOfBirth = LocalDate.parse(d);
                System.out.println(user.dateOfBirth);
                entityManager.merge(user);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return  ok("Successful ....");
    }


    @Transactional
    public Result buildAttendanceDateCreated(){
        EntityManager entityManager = jpaApi.em();
        List<Attendance> attendances = entityManager.createQuery("SELECT u FROM Attendance u", Attendance.class).getResultList();

        Random random = new Random();


        for(Attendance attendance : attendances){
            String dateCreated = attendance.clockDate +" "+attendance.clockIn;
            System.out.println(dateCreated);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            attendance.dateCreated = LocalDateTime.parse(dateCreated, formatter);
            entityManager.merge(attendance);

        }
        return  ok("Successful ....");
    }


    public Result logout() {
        session().clear();
        return redirect(routes.IndexController.index());
    }
}
