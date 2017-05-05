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
@Table(name = "Shifts")
public class Shift extends Model{

    @Getter @Setter
    @Column(name = "ShiftDate")
    public LocalDate shiftDate;

    @Getter @Setter
    @Column(name = "ExpectedClockInTime")
    public LocalTime expectedClockIn;

    @Getter @Setter
    @Column(name = "ExpectedClockOutTime")
    public LocalTime expectedClockOut;

    @Getter @Setter
    @ManyToOne
    public User user;
}
