package controllers.reports;

import enums.StatusType;
import models.User;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import pojos.AbsenteeReport;
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
 * //http://www.aku.vn/idea
 */
@Transactional
public class AbsenteeReportController extends Controller{

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public AbsenteeReportController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("selectedTab", "absenteeReport");
        modelMap.put("absenteeReport", getAbsenteeReport());
        return ok(views.html.reports.absentee_report.render(modelMap));
    }

    public List<AbsenteeReport> getAbsenteeReport() {

        Long companyId = Long.parseLong(session().get("companyId"));
        //String startDate = Utility.getQueryString("start_date");
        //String endDate = Utility.getQueryString("end_date");
        String startDate = "2017-03-01";
        String endDate = "2017-04-30";
        boolean excludeWeekend = Boolean.valueOf(Utility.getQueryString("exclude_weekend"));
        List<LocalDate> dateRange =  Utility.getDateRange(startDate, endDate, excludeWeekend);
        Long pin = Long.parseLong(session().get("pin"));
        EntityManager entityManager = jpaApi.em();

        List<LocalDate> attendances = entityManager.createQuery("SELECT a.clockDate FROM Attendance a " +
                "WHERE a.user.userId.companyId =:companyId " +
                "AND a.user.userId.pin =:pin AND a.user.statusType = :statusType " +
                "AND a.clockDate IN :dateRange", LocalDate.class)
                .setParameter("companyId", companyId)
                .setParameter("pin", pin)
                .setParameter("dateRange", dateRange)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();

        User user = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.userId.companyId =:companyId " +
                "AND u.userId.pin =:pin", User.class)
                .setParameter("companyId", companyId)
                .setParameter("pin", pin).getSingleResult();

        List<AbsenteeReport> absenteeReports = new ArrayList<>();
        for(LocalDate date : dateRange){
            if(!attendances.contains(date)) {
                AbsenteeReport absenteeReport = new AbsenteeReport();
                absenteeReport.firstName = user.firstName;
                absenteeReport.lastName = user.lastName;
                absenteeReport.pin = user.userId.pin;
                absenteeReport.clockDate = date;
                absenteeReports.add(absenteeReport);
            }
        }
        return absenteeReports;
    }
}
