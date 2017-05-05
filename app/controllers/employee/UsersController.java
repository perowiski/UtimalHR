package controllers.employee;

import enums.StatusType;
import models.User;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.employee.users;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by peter on 2/20/17.
 */
@Transactional
public class UsersController extends Controller {
    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public UsersController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType) {
        Map<String, Object> modelMap = new HashMap<>();
        List<User> userList = getUserList();
        modelMap.put("userList", userList);
        modelMap.put("selectedTab", "allUsers");
        return ok(users.render(modelMap));
    }

    private List<User> getUserList() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        TypedQuery<User> users = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.userId.companyId = :companyId", User.class)
                .setParameter("companyId", companyId);
        return users.getResultList();
    }

    public Result getInActiveUserList(String roleType) {
        Map<String, Object> modelMap = new HashMap<>();
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        TypedQuery<User> users = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.userId.companyId = :companyId AND u.statusType = :statusType", User.class)
                .setParameter("companyId", companyId)
                .setParameter("statusType", StatusType.INACTIVE);
        modelMap.put("userList", users.getResultList());
        modelMap.put("selectedTab", "inActiveUsers");
        return ok(views.html.employee.inactive_users.render(modelMap));
    }

    public Result getActiveUserList(String roleType) {
        Map<String, Object> modelMap = new HashMap<>();
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        TypedQuery<User> users = entityManager.createQuery("SELECT u FROM User u " +
                "WHERE u.userId.companyId = :companyId AND u.statusType = :statusType", User.class)
                .setParameter("companyId", companyId)
                .setParameter("statusType", StatusType.ACTIVE);
        modelMap.put("userList", users.getResultList());
        modelMap.put("selectedTab", "activeUsers");
        return ok(views.html.employee.active_users.render(modelMap));
    }
}
