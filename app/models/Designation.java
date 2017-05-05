package models;



import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

/**
 * Created by peter on 4/11/17.
 */
@Table(name = "Designations")
@Entity
public class Designation extends Model{

    @Getter @Setter
    @Column(name = "DesignationName")
    public String designationName;

    @Getter @Setter
    @ManyToOne
    public Account account;

    @Getter @Setter
    @Column(name = "DesignationMailAlias")
    public String designationMailAlias;
}
