package controllers.organisation;

import enums.StatusType;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Result;
import views.html.organisation.locations;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;

import static play.mvc.Controller.session;
import static play.mvc.Results.ok;

/**
 * Created by peter on 2/20/17.
 */
@Transactional
public class LocationsController {

    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public LocationsController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    public Result index(String roleType) {
        List locationList = getLocationList();
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("locationList", locationList);
        return ok(locations.render(modelMap));
    }

    private List getLocationList() {
        EntityManager entityManager = jpaApi.em();
        Long accountId = Long.parseLong(session().get("id"));
        List locations = entityManager.createQuery("SELECT l, (SELECT COUNT(u.location) FROM User u WHERE u.location = l AND u.account.id = :accountId AND u.statusType = :statusType GROUP BY u.location) as TotalLocations FROM Location l WHERE l.account.id =:accountId")
                .setParameter("accountId", accountId)
                .setParameter("statusType", StatusType.ACTIVE).getResultList();
        return locations;
    }
}
