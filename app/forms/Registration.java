package forms;

import play.data.validation.Constraints;

import java.util.Date;

/**
 * Created by peter on 4/2/17.
 */
public class Registration {
    @Constraints.Required
    public String email;
    @Constraints.Required
    public Date dateOfBirth;
}
