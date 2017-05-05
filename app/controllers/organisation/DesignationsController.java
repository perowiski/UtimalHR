package controllers.organisation;

import enums.StatusType;
import models.Designation;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by peter on 4/15/17.
 */
@Transactional
public class DesignationsController extends Controller{

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public DesignationsController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        List designationList = getDesignations();
        modelMap.put("designationList", designationList);
        return ok(views.html.organisation.designations.render(modelMap));
    }

    private List<Designation> getDesignations(){
        EntityManager entityManager = jpaApi.em();
        Long accountId = Long.parseLong(session().get("id"));
        List designations = entityManager.createQuery("SELECT d, (SELECT COUNT(u.designation) FROM User u WHERE u.designation = d AND u.account.id = :accountId AND u.statusType = :statusType GROUP BY u.designation) as TotalDesignations FROM Designation d WHERE d.account.id =:accountId")
                .setParameter("accountId", accountId)
                .setParameter("statusType", StatusType.ACTIVE)
                .getResultList();
        return designations;
    }
}
