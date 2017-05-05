package controllers.reports;

import enums.StatusType;
import models.Attendance;
import models.UserId;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.mvc.Controller;
import play.mvc.Result;
import pojos.AttendanceSummaryReport;
import pojos.WeeklySummaryReport;
import util.Utility;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by peter on 4/22/17.
 */
public class WeeklySummaryReportController extends Controller{

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public WeeklySummaryReportController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("selectedTab", "weeklyReport");
        return ok(views.html.reports.weekly_summary_report.render(modelMap));
    }

    public WeeklySummaryReport getWeeklySummaryReport() {
        Long companyId = Long.parseLong(session().get("companyId"));
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
        Object weeklySummary = entityManager.createQuery("SELECT u.firstName, u.lastName, u.userId.pin, (SELECT COUNT(a.clockIn) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn > a.shift.expectedClockIn AND a.user = u) as numberOfLateness, " +
                "(SELECT EXTRACT(HOUR_SECOND FROM SEC_TO_TIME(SUM((TIME_TO_SEC(TimeDiff(a.clockOut, a.clockIn)))))) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn IS NOT NULL AND a.clockOut IS NOT NULL AND a.user = u GROUP BY a.user) as totalHoursOfWork, " +
                "(SELECT EXTRACT(HOUR_SECOND FROM SEC_TO_TIME(SUM(TIME_TO_SEC(TimeDiff(a.shift.expectedClockOut, a.shift.expectedClockIn))))) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn IS NOT NULL AND a.clockOut IS NOT NULL AND a.user = u GROUP BY a.user) as expectedHoursOfWork, " +
                "(SELECT COUNT(a.id) FROM Attendance a WHERE a.clockDate IN :dateRange AND a.clockIn <= a.shift.expectedClockIn AND a.user = u) as numberOfTimesPunctual, "+
                "(SELECT COUNT(a.id) FROM Attendance a WHERE a.clockDate IN :dateRange AND (a.clockIn IS NOT NULL OR a.clockOut IS NOT NULL) AND a.user = u) as numberOfTimesPresent "+
                "FROM User u WHERE u.userId =:userId")
                .setParameter("userId", userId)
                .setParameter("dateRange", dateRange).getSingleResult();

        Object[] row = (Object[])weeklySummary;
        WeeklySummaryReport weeklySummaryReport = new WeeklySummaryReport();
        weeklySummaryReport.firstName = row[0].toString();
        weeklySummaryReport.lastName = row[1].toString();
        weeklySummaryReport.pin = Long.parseLong(row[2].toString());
        weeklySummaryReport.numberOfLateness = Integer.parseInt(row[3].toString());
        weeklySummaryReport.totalHoursOfWork = row[4].toString();
        weeklySummaryReport.expectedHoursOfWork = row[5].toString();
        weeklySummaryReport.numberOfTimesPunctual = Integer.parseInt(row[6].toString());
        weeklySummaryReport.numberOfTimesPresent = Integer.parseInt(row[7].toString());
        weeklySummaryReport.numberOfTimesAbsent = dateRange.size() - weeklySummaryReport.numberOfTimesPresent;
        return  weeklySummaryReport;
    }
}
