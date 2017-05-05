package controllers.reports;

import models.Clock;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import pojos.AttendanceLogReport;
import util.Utility;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by peter on 4/22/17.
 */
@Transactional
public class AttendanceLogsReportController extends Controller{
    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public AttendanceLogsReportController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("selectedTab", "attendanceLogsReport");
        modelMap.put("attendanceLogsReport", getAttendanceLogsReport());
        return ok(views.html.reports.attendance_logs.render(modelMap));
    }

    public List<AttendanceLogReport> getAttendanceLogsReport(){
        Long companyId = Long.parseLong(session().get("companyId"));
        //String startDate = Utility.getQueryString("start_date");
        //String endDate = Utility.getQueryString("end_date");
        String startDate = "2017-03-01";
        String endDate = "2017-12-31";
        boolean excludeWeekend = Boolean.valueOf(Utility.getQueryString("exclude_weekend"));
        List<LocalDate> dateRange =  Utility.getDateRange(startDate, endDate, excludeWeekend);
        Long pin = Long.parseLong(session().get("pin"));
        EntityManager entityManager = jpaApi.em();

        List<Clock> clocks = entityManager.createQuery("SELECT c FROM Clock c WHERE c.day IN :dateRange " +
                "AND c.user.userId.companyId = :companyId AND c.user.userId.pin =:pin", Clock.class)
                .setParameter("dateRange", dateRange)
                .setParameter("companyId", companyId)
                .setParameter("pin", pin).getResultList();

        List<AttendanceLogReport> attendanceLogReportList = new ArrayList<>();
        Map<LocalDate, List<Clock>> clocksAggregate = aggregateClocks(clocks);
        Set<LocalDate> dateSet = clocksAggregate.keySet();

        for(LocalDate date : dateSet){
            AttendanceLogReport attendanceLogReport = new AttendanceLogReport();
            List<Clock> clockList = clocksAggregate.get(date);
            List<LocalTime> timeList = new ArrayList<>();
            for(Clock clock : clockList){
                timeList.add(clock.clock);
                attendanceLogReport.firstName = clock.user.firstName;
                attendanceLogReport.lastName = clock.user.lastName;
                attendanceLogReport.day = clock.day;
                attendanceLogReport.pin = clock.user.userId.pin;
                attendanceLogReport.clockList = timeList;
            }
            attendanceLogReportList.add(attendanceLogReport);
        }

        return attendanceLogReportList;
    }

    private Map<LocalDate, List<Clock>> aggregateClocks(List<Clock> clocks){
        Map<LocalDate, List<Clock>> clocksAggregate = new HashMap<>();
        for(Clock clock : clocks){
            List<Clock> clockList = clocksAggregate.get(clock.day); // find clock list in map
            if(clockList == null){
                List<Clock> temp = new ArrayList<>();
                temp.add(clock);
                clocksAggregate.put(clock.day, temp);
            }else {
                clockList.add(clock);
                clocksAggregate.replace(clock.day, clockList);
            }
        }
        return clocksAggregate;
    }
 }
