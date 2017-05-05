package controllers.reports;

import enums.StatusType;
import models.Attendance;
import models.User;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import pojos.SimpleAttendanceReport;
import util.Utility;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by peter on 4/17/17.
 */
@Transactional
public class SimpleAttendanceReportController extends Controller{

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public SimpleAttendanceReportController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("selectedTab", "simpleAttendance");
        modelMap.put("simpleAttendanceReport", getSimpleAttendanceReport());
        return ok(views.html.reports.simple_attendance.render(modelMap));
    }

    public List<SimpleAttendanceReport> getSimpleAttendanceReport() {
        Long companyId = Long.parseLong(session().get("companyId"));
        String startDate =  Utility.getQueryString("start_date");
        String endDate = Utility.getQueryString("end_date");
        boolean excludeWeekend = Boolean.valueOf(Utility.getQueryString("exclude_weekend"));
        List<LocalDate> dateRange =  Utility.getDateRange(startDate, endDate, excludeWeekend);
        Long pin = Long.parseLong(session().get("pin"));
        EntityManager entityManager = jpaApi.em();

        List<Attendance> attendances = entityManager.createQuery("SELECT a FROM Attendance a " +
                "WHERE a.user.userId.companyId =:companyId " +
                "AND a.user.userId.pin =:pin AND a.user.statusType = :statusType " +
                "AND a.clockDate IN :dateRange", Attendance.class)
                .setParameter("companyId", companyId)
                .setParameter("pin", pin)
                .setParameter("dateRange", dateRange)
                .setParameter("statusType", StatusType.ACTIVE)
                .getResultList();

        User user = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.userId.companyId =:companyId " +
                "AND u.userId.pin =:pin", User.class)
                .setParameter("companyId", companyId)
                .setParameter("pin", pin).getSingleResult();

        List<SimpleAttendanceReport> simpleAttendanceReports = new ArrayList<>();

        for(LocalDate date : dateRange){
            Attendance attendance = removeDateFromAttendances(attendances, date);
            if(attendance != null){
                SimpleAttendanceReport simpleAttendanceReport = new SimpleAttendanceReport();
                simpleAttendanceReport.firstName = attendance.user.firstName;
                simpleAttendanceReport.lastName = attendance.user.lastName;
                simpleAttendanceReport.pin = attendance.user.userId.pin;
                simpleAttendanceReport.clockDate = attendance.clockDate;
                simpleAttendanceReport.clockIn = attendance.clockIn;
                simpleAttendanceReport.clockOut = attendance.clockOut;
                simpleAttendanceReports.add(simpleAttendanceReport);
            }else {
                SimpleAttendanceReport simpleAttendanceReport = new SimpleAttendanceReport();
                simpleAttendanceReport.firstName = user.firstName;
                simpleAttendanceReport.lastName = user.lastName;
                simpleAttendanceReport.pin = user.userId.pin;
                simpleAttendanceReport.clockDate = date;
                simpleAttendanceReports.add(simpleAttendanceReport);
            }
        }

        return simpleAttendanceReports;
    }

    private Attendance removeDateFromAttendances(List<Attendance> attendances, LocalDate date) {
        for(Attendance attendance : attendances){
            if(attendance.clockDate.equals(date)){
                attendances.remove(attendance);
                return attendance;
            }
        }
        return  null;
    }
}
