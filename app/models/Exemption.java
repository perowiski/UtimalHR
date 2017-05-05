/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enums.ExemptionType;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author peter
 */
@Entity
@Table(name = "Exemptions")
public class Exemption extends Model{

    @Getter @Setter
    @ManyToOne
    public User user;

    @Getter @Setter
    @Column(name = "StatusType ")
    public String status;

    @Getter @Setter
    @Column(name = "Reason")
    @Lob
    public String reason;

    @Getter @Setter
    @Column(name = "ClockDate")
    public LocalDate clockDate;

    @Getter @Setter
    @Column(name = "ExemptionType")
    public ExemptionType exemptionType;
}
