/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enums.DeviceType;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


/**
 * @author peter
 */
@Entity
@Table(name = "Devices")
public class Device extends Model{

    @Getter @Setter
    @Column(name = "SerialNumber")
    public String serialNumber;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    public DeviceType deviceType;

    @Getter @Setter
    @ManyToOne
    public Account account;
}
