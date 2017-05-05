/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

/**
 * @author peter
 */
@Entity
@Table(name = "Departments")
public class Department extends Model{

    @Getter @Setter
    @Column(name = "DepartmentName")
    public String departmentName;

    @Getter @Setter
    @ManyToOne
    public User departmentHeadId;

    @Getter @Setter
    @ManyToOne
    public Account account;

    @Getter @Setter
    @Column(name = "DepartmentMailAlias")
    public String departmentMailAlias;

}
