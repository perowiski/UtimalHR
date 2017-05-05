/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author peter
 */
@Entity
@Table(name = "Leaves")
public class Leave extends Model{

    @Getter @Setter
    @Column(name = "StartDate")
    public LocalDate startDate;

    @Getter @Setter
    @Column(name = "EndDate")
    public LocalDate endDate;

    @Getter @Setter
    @ManyToOne
    public User user;

    @Getter @Setter
    @ManyToOne
    public LeaveType leaveType;

    @Getter @Setter
    @Column(name = "StatusType")
    public String status;

}
