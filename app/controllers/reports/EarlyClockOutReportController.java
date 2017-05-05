package controllers.reports;

import enums.StatusType;
import models.Attendance;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import util.Utility;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by peter on 4/21/17.
 */
@Transactional
public class EarlyClockOutReportController extends Controller{

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public EarlyClockOutReportController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("selectedTab", "earlyClockOutReport");
        modelMap.put("earlyClockOutReport", getEarlyClockOutReport());
        return ok(views.html.reports.early_clock_out_report.render(modelMap));
    }

    public List<Attendance> getEarlyClockOutReport() {
        Long companyId = Long.parseLong(session().get("companyId"));
        //String startDate = Utility.getQueryString("start_date");
        //String endDate = Utility.getQueryString("end_date");
        String startDate = "2017-03-01";
        String endDate = "2017-03-05";
        boolean excludeWeekend = Boolean.valueOf(Utility.getQueryString("exclude_weekend"));
        List<LocalDate> dateRange =  Utility.getDateRange(startDate, endDate, excludeWeekend);
        //Long pin = Long.parseLong(session().get("pin"));
        Long pin = 2L;
        EntityManager entityManager = jpaApi.em();
        List<Attendance> earlyClockOutList = entityManager.createQuery("SELECT a FROM Attendance a " +
                "WHERE a.user.userId.companyId = :companyId AND a.user.userId.pin =:pin " +
                "AND a.clockDate IN :clockDate AND a.clockOut > (a.shift.expectedClockOut  - ((a.shift.expectedClockOut - a.shift.expectedClockIn)/2)) " +
                "AND a.clockOut < a.shift.expectedClockOut AND a.user.statusType = :statusType", Attendance.class)
                .setParameter("companyId", companyId)
                .setParameter("pin", pin)
                .setParameter("clockDate", dateRange)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return earlyClockOutList;
    }
}
