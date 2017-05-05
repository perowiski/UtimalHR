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
@Table(name = "LeaveTypes")
public class LeaveType extends Model{

    @Getter @Setter
    @Column(name = "Duration")
    public Long duration;

    @Getter @Setter
    @Column(name = "leaveName")
    public String leaveName;

    @Getter @Setter
    @ManyToOne
    public Account account;
}
