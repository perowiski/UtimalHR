package controllers.organisation;

import models.Account;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by peter on 4/16/17.
 */
@Transactional
public class CompanyProfileController extends Controller{

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public CompanyProfileController(FormFactory formFactory, JPAApi jpaApi){
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType){
        Map<String, Object> modelMap = new HashMap<>();
        EntityManager entityManager = jpaApi.em();
        Long accountId = Long.parseLong(session().get("id"));
        Account account = entityManager.find(Account.class, accountId);
        modelMap.put("account", account);
        return ok(views.html.organisation.company_profile.render(modelMap));
    }
}
