package controllers.organisation;

import enums.StatusType;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Result;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;
import static play.mvc.Controller.session;
import static play.mvc.Results.ok;

/**
 * Created by peter on 2/20/17.
 */
@Transactional
public class DepartmentsController {
    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public DepartmentsController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType) {
        List departmentList = getDepartmentList();
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("departmentList", departmentList);
        return ok(views.html.organisation.departments.render(modelMap));
    }

    private List getDepartmentList() {
        EntityManager entityManager = jpaApi.em();
        Long accountId = Long.parseLong(session().get("id"));
        List departments = entityManager.createQuery("SELECT d, (SELECT COUNT(u.department) FROM User u WHERE u.department = d  AND u.account.id = :accountId AND u.statusType = :statusType GROUP BY u.department) AS TotalEmployees FROM Department d WHERE d.account.id =:accountId")
                .setParameter("accountId", accountId)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return departments;
    }
}
