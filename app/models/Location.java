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
@Table(name = "Locations")
public class Location extends Model{

    @Getter @Setter
    @Column(name = "LocationName")
    public String locationName;

    @Getter @Setter
    @ManyToOne
    public Account account;

    @Getter @Setter
    @Column(name = "LocationMailAlias")
    public String locationMailAlias;

    @Getter @Setter
    @Column(name = "Description")
    public String description;

    @Getter @Setter
    @Column(name = "Country")
    public String country;
}
