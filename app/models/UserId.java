/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author peter
 */
@Embeddable
public class UserId implements Serializable {

    @Getter @Setter
    public Long companyId;

    @Getter @Setter
    public Long pin;

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserId) {
            UserId userId = (UserId) object;
            return (userId.companyId.equals(this.companyId))
                    && (userId.pin.equals(pin));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
