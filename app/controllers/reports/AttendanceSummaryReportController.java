package controllers.reports;

import models.UserId;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import pojos.AttendanceSummaryReport;
import util.Utility;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by peter on 4/23/17.
 */
@Transactional
public class AttendanceSummaryReportController extends Controller{

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public AttendanceSummaryReportController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("selectedTab", "attendanceSummaryReport");
        modelMap.put("attendanceSummaryReport", getAttendanceSummaryReport());
        return ok(views.html.reports.attendance_summary_report.render(modelMap));
    }

    private AttendanceSummaryReport getAttendanceSummaryReport(){

        Long companyId = Long.parseLong(session().get("companyId"));
        Long pin = Long.parseLong(session().get("pin"));
        //String startDate = Utility.getQueryString("start_date");
        //String endDate = Utility.getQueryString("end_date");
        String startDate = "2017-03-01";
        String endDate = "2017-03-05";

        UserId userId = new UserId();
        userId.companyId = companyId;
        userId.pin = 2L;

        boolean excludeWeekend = Boolean.valueOf(Utility.getQueryString("exclude_weekend"));
        List<LocalDate> dateRange =  Utility.getDateRange(startDate, endDate, excludeWeekend);

        EntityManager entityManager = jpaApi.em();
        Object attendanceSummary = entityManager.createQuery("SELECT u.firstName, u.lastName, u.userId.pin, (SELECT COUNT(a.clockIn) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn > a.shift.expectedClockIn AND a.user = u) as numberOfLateness, " +
                "(SELECT COUNT (id) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn IS NULL AND a.user = u) as noClockIn, " +
                "(SELECT COUNT (id) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockOut IS NULL AND a.user = u) as noClockOut, " +
                "(SELECT EXTRACT(HOUR_SECOND FROM SEC_TO_TIME(SUM((TIME_TO_SEC(TimeDiff(a.clockOut, a.clockIn)))))) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn IS NOT NULL AND a.clockOut IS NOT NULL AND a.user = u GROUP BY a.user) as totalHoursOfWork, " +
                "(SELECT EXTRACT(HOUR_SECOND FROM SEC_TO_TIME(SUM(TIME_TO_SEC(TimeDiff(a.shift.expectedClockOut, a.shift.expectedClockIn))))) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn IS NOT NULL AND a.clockOut IS NOT NULL AND a.user = u GROUP BY a.user) as expectedHoursOfWork, " +
                "(SELECT COUNT (id) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn IS NOT NULL AND a.user = u) as clockIn, " +
                "(SELECT COUNT (id) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockOut IS NOT NULL AND a.user = u) as clockOut, " +
                "(SELECT COUNT(a.id) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn <= a.shift.expectedClockIn AND a.user = u) as numberOfTimesPunctual, "+
                "(SELECT COUNT(a.id) FROM Attendance a WHERE a.clockDate IN :dateRange AND (a.clockIn IS NOT NULL OR a.clockOut IS NOT NULL) AND a.user = u) as numberOfTimesPresent, "+
                "(SELECT COUNT(a.id) FROM Attendance a WHERE a.clockDate IN :dateRange AND (a.clockOut > (a.shift.expectedClockOut  - ((a.shift.expectedClockOut - a.shift.expectedClockIn)/2))) AND a.clockOut < a.shift.expectedClockOut AND a.user = u) as numberOfEarlyClockOut "+
                "FROM User u WHERE u.userId =:userId")
                .setParameter("userId", userId)
                .setParameter("dateRange", dateRange).getSingleResult();

        Object[] row = (Object[])attendanceSummary;
        AttendanceSummaryReport attendanceSummaryReport = new AttendanceSummaryReport();
        attendanceSummaryReport.firstName = row[0].toString();
        attendanceSummaryReport.lastName = row[1].toString();
        attendanceSummaryReport.pin = Long.parseLong(row[2].toString());
        attendanceSummaryReport.numberOfLateness = Integer.parseInt(row[3].toString());
        attendanceSummaryReport.noClockIn = Integer.parseInt(row[4].toString());
        attendanceSummaryReport.noClockOut = Integer.parseInt(row[5].toString());
        attendanceSummaryReport.totalHoursOfWork = row[6].toString();
        attendanceSummaryReport.expectedHoursOfWork = row[7].toString();
        attendanceSummaryReport.clockIn = Integer.parseInt(row[8].toString());
        attendanceSummaryReport.clockOut = Integer.parseInt(row[9].toString());
        attendanceSummaryReport.numberOfTimesPunctual = Integer.parseInt(row[10].toString());
        attendanceSummaryReport.numberOfTimesPresent = Integer.parseInt(row[11].toString());
        attendanceSummaryReport.numberOfEarlyClockOut = Integer.parseInt(row[12].toString());
        attendanceSummaryReport.numberOfTimesAbsent = dateRange.size() - attendanceSummaryReport.numberOfTimesPresent;
        return attendanceSummaryReport;
    }
}
