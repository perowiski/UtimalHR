package models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

/**
 * Created by peter on 2/20/17.
 */
@Entity
@Table(name = "LeaveApprovals")
public class LeaveApproval extends Model{

    @Getter @Setter
    @ManyToOne
    public Leave leave;

    @Getter @Setter
    @ManyToOne
    public Approver approver;
}
