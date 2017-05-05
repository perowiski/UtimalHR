package controllers.colleagues;

import enums.StatusType;
import models.User;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;

/**
 * Created by peter on 2/20/17.
 */
@Transactional
public class ColleaguesController extends Controller {

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public ColleaguesController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType) {
        Map<String, Object> modelMap = new HashMap();
        List<User> colleagueList = getColleagueList();
        modelMap.put("colleagueList", colleagueList);
        return ok(views.html.colleagues.colleagues.render(modelMap));
    }

    private List<User> getColleagueList() {
        EntityManager entityManager = jpaApi.em();
        Long companyId = Long.parseLong(session().get("companyId"));
        Long pin = Long.parseLong(session().get("pin"));
        List<User> users = entityManager.createQuery("SELECT u FROM User u WHERE u.userId.companyId = :companyId " +
                "AND u.userId.pin != :pin AND u.statusType =  :statusType", User.class)
                .setParameter("companyId", companyId)
                .setParameter("pin", pin)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return users;
    }
}
