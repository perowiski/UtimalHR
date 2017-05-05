package controllers.dashboard;

import enums.ApprovalType;
import enums.StatusType;
import models.UserId;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by peter on 2/18/17.
 */
@Transactional
public class DashboardController extends Controller {

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public DashboardController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType) {
        Map<String, Object> modelMap = new HashMap();
        List presentUsers = getPresentUsers();
        List punctualUsers = getPunctualUsers();
        List activeUsers = getActiveUsers();
        List inActiveUsers = getInActiveUsers();
        List absenteeUsers = getAbsenteeUsers();
        List latenessUsers = getLatenessUsers();
        List earlyClockOutUsers = getEarlyClockOutUsers();
        List usersDateOfBirth = getUsersDateOfBirth();
        List usersOnLeave = getUsersOnLeave();
        List pendingExemptionRequests = getPendingExemptionRequestList();
        List pendingLeaveRequests = getPendingLeaveRequestList();
        modelMap.put("presentUsers", presentUsers);
        modelMap.put("punctualUsers", punctualUsers);
        modelMap.put("activeUsers", activeUsers);
        modelMap.put("inActiveUsers", inActiveUsers);
        modelMap.put("absenteeUsers", absenteeUsers);
        modelMap.put("latenessUsers", latenessUsers);
        modelMap.put("earlyClockOutUsers", earlyClockOutUsers);
        modelMap.put("usersDateOfBirth", usersDateOfBirth);
        modelMap.put("usersOnLeave", usersOnLeave);
        modelMap.put("pendingExemptionRequests", pendingExemptionRequests);
        modelMap.put("pendingLeaveRequests", pendingLeaveRequests);
        return ok(views.html.dashboard.dashboard.render(modelMap));
    }

    private List getPresentUsers() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        LocalDate today = LocalDate.now();
        List attendees = entityManager.createQuery("SELECT a FROM Attendance a " +
                "WHERE a.user.userId.companyId = :companyId AND a.clockDate = :clockDate " +
                "AND a.user.statusType = :statusType")
                .setParameter("companyId", companyId)
                .setParameter("clockDate", today)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return attendees;
    }

    private List getPunctualUsers() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        LocalDate today = LocalDate.now();
        List latenessList = entityManager.createQuery("SELECT a FROM Attendance a " +
                "WHERE a.user.userId.companyId = :companyId " +
                "AND a.clockDate =:clockDate AND a.clockIn <= a.shift.expectedClockIn " +
                "AND a.user.statusType = :statusType")
                .setParameter("companyId", companyId)
                .setParameter("clockDate", today)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return latenessList;
    }

    public List getActiveUsers() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        List users = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.userId.companyId = :companyId " +
                "AND u.statusType = :statusType")
                .setParameter("companyId", companyId)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return users;
    }

    public List getInActiveUsers() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        List users = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.userId.companyId = :companyId " +
                "AND u.statusType = :statusType")
                .setParameter("companyId", companyId)
                .setParameter("statusType", StatusType.INACTIVE).getResultList();
        return users;
    }

    private List getAbsenteeUsers() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        LocalDate today = LocalDate.now();
        List users = entityManager.createQuery("SELECT u FROM User u WHERE u.userId " +
                "NOT IN (SELECT a.user.userId FROM Attendance a " +
                "WHERE a.user.userId.companyId = :companyId " +
                "AND a.clockDate = :clockDate) AND u.statusType = :statusType")
                .setParameter("companyId", companyId)
                .setParameter("clockDate", today)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return users;
    }

    private List getLatenessUsers() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        LocalDate today = LocalDate.now();
        List latenessList = entityManager.createQuery("SELECT a FROM Attendance a " +
                "WHERE a.user.userId.companyId = :companyId " +
                "AND a.clockDate =:clockDate AND a.clockIn > a.shift.expectedClockIn " +
                "AND a.user.statusType = :statusType")
                .setParameter("companyId", companyId)
                .setParameter("clockDate", today)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return latenessList;
    }

    public List getEarlyClockOutUsers() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        LocalDate today = LocalDate.now();
        List earlyClockOutList = entityManager.createQuery("SELECT a FROM Attendance a " +
                "WHERE a.user.userId.companyId = :companyId " +
                "AND a.clockDate =:clockDate AND a.clockOut > (a.shift.expectedClockOut  - ((a.shift.expectedClockOut - a.shift.expectedClockIn)/2)) " +
                "AND a.clockOut < a.shift.expectedClockOut AND a.user.statusType = :statusType")
                .setParameter("companyId", companyId)
                .setParameter("clockDate", today)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return earlyClockOutList;
    }

    public List getUsersDateOfBirth() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        LocalDate today = LocalDate.now();
        int day = today.getDayOfMonth();
        int month = today.getMonth().getValue();
        List users = entityManager.createQuery("SELECT u FROM User u WHERE Day(u.dateOfBirth) = :day " +
                "AND MONTH(u.dateOfBirth) = :month " +
                "AND u.userId.companyId = :companyId AND u.statusType = :statusType")
                .setParameter("day", day)
                .setParameter("month", month)
                .setParameter("companyId", companyId)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return users;
    }


    public List getUsersOnLeave() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        LocalDate today = LocalDate.now();
        List leaveList = entityManager.createQuery("SELECT l FROM Leave l " +
                "WHERE l.startDate <= :today " +
                "AND l.endDate <= :today AND l.user.userId.companyId = :companyId " +
                "AND l.user.statusType = :statusType AND l.status = :status")
                .setParameter("today", today)
                .setParameter("companyId", companyId)
                .setParameter("statusType", StatusType.ACTIVE)
                .setParameter("status", "Approved").getResultList();
        return leaveList;
    }

    public List getPendingExemptionRequestList() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        UserId userId = new UserId();
        userId.companyId = companyId;
        userId.pin = Long.parseLong(session().get("pin"));
        List pendingExemptionRequestList = entityManager.createQuery("SELECT e " +
                "FROM Exemption e " +
                "WHERE e.user.userId IN ((SELECT a.user.userId FROM Approver a " +
                "WHERE a.approvalType = :approvalType AND a.approver.userId = :approver)) " +
                "AND (e.id NOT IN(SELECT ea.exemption.id FROM ExemptionApproval ea WHERE ea.approver.approver.userId = :approver)) ")
                .setParameter("approvalType", ApprovalType.EXEMPTION)
                .setParameter("approver", userId).getResultList();
        return pendingExemptionRequestList;
    }

    public List getPendingLeaveRequestList() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        UserId userId = new UserId();
        userId.companyId = companyId;
        userId.pin = Long.parseLong(session().get("pin"));
        List pendingLeaveRequestList = entityManager.createQuery("SELECT l " +
                "FROM Leave l " +
                "WHERE l.user.userId IN ((SELECT a.user.userId FROM Approver a " +
                "WHERE a.approvalType = :approvalType AND a.approver.userId = :approver)) " +
                "AND (l.id NOT IN(SELECT la.leave.id FROM LeaveApproval la WHERE la.approver.approver.userId = :approver)) ")
                .setParameter("approvalType", ApprovalType.LEAVE)
                .setParameter("approver", userId).getResultList();
        return pendingLeaveRequestList;
    }
}
