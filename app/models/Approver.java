/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enums.ApprovalType;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


/**
 * @author peter
 */

@Entity
@Table(name = "Approvers")
public class Approver extends Model{

    @Getter @Setter
    @ManyToOne
    public User user;

    @Getter @Setter
    @ManyToOne
    public User approver;

    @Getter @Setter
    @Column(name = "ApprovalType")
    public ApprovalType approvalType;
}
