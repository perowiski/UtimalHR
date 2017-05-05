package models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

/**
 * l
 * Created by peter on 2/20/17.
 */
@Entity
@Table(name = "ExemptionApprovals")
public class ExemptionApproval extends Model{

    @Getter @Setter
    @ManyToOne
    public Exemption exemption;

    @Getter @Setter
    @ManyToOne
    public Approver approver;
}
