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
import java.time.LocalTime;

/**
 * @author peter
 */
@Entity
@Table(name = "Attendances")
public class Attendance extends Model{

    @Getter @Setter
    @Column(name = "ClockDate")
    public LocalDate clockDate;

    @Getter @Setter
    @Column(name = "ClockIn")
    public LocalTime clockIn;

    @Getter @Setter
    @Column(name = "clockOut")
    public LocalTime clockOut;

    @Getter @Setter
    @ManyToOne
    public User user;

    @Getter @Setter
    @OneToOne
    public Shift shift;
}
