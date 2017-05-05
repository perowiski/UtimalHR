package models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Embeddable;
/**
 * Created by peter on 4/11/17.
 */
@Embeddable
public class Address {

    @Getter @Setter
    public String address;

    @Getter @Setter
    public String city;

    @Getter @Setter
    public String state;

    @Getter @Setter
    public String country;
}
