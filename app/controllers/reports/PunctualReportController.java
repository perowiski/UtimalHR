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
 * Created by peter on 4/17/17.
 */
@Transactional
public class PunctualReportController extends Controller{

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public PunctualReportController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("selectedTab", "punctualReport");
        modelMap.put("punctualReport", getPunctualReport());
        return ok(views.html.reports.punctual_report.render(modelMap));
    }


    public List<Attendance> getPunctualReport() {

        Long companyId = Long.parseLong(session().get("companyId"));
        //String startDate = Utility.getQueryString("start_date");
        String startDate = "2017-03-01";
        String endDate = "2017-12-31";
        //String endDate = Utility.getQueryString("end_date");
        boolean excludeWeekend = Boolean.valueOf(Utility.getQueryString("exclude_weekend"));
        List<LocalDate> dateRange =  Utility.getDateRange(startDate, endDate, excludeWeekend);
        Long pin = Long.parseLong(session().get("pin"));
        EntityManager entityManager = jpaApi.em();
        List<Attendance> punctualList = entityManager.createQuery("SELECT a FROM Attendance a " +
                "WHERE a.user.userId.companyId = :companyId AND a.user.userId.pin =:pin " +
                "AND a.clockDate IN :dateRange AND a.clockIn <= a.shift.expectedClockIn " +
                "AND a.user.statusType = :statusType", Attendance.class)
                .setParameter("companyId", companyId)
                .setParameter("pin", pin)
                .setParameter("dateRange", dateRange)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return punctualList;
    }


}
