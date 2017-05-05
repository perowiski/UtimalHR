/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enums.ClockType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author peter
 */
@Entity
@Table(name = "Clocks")
public class Clock extends Model {

    @Getter @Setter
    @ManyToOne
    public User user;

    @Getter @Setter
    @Column(name = "Day")
    public LocalDate day;

    @Getter @Setter
    @Column(name = "Clock")
    public LocalTime clock;

    @Getter @Setter
    @Column(name = "ClockType")
    public ClockType clockType;
}
