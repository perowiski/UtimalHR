/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enums.RoleType;
import enums.StatusType;
import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author peter
 */
@Entity
@Table(name = "Users")
public class User {

    @Getter @Setter
    @EmbeddedId
    public UserId userId;

    @Getter @Setter
    @Column(name = "ActivateLogin")
    public boolean activateLogin;

    @Getter @Setter
    @Column(name = "Password")
    public String password;

    @Getter @Setter
    @Column(name = "RoleType")
    public RoleType roleType;

    @Getter @Setter
    @Column(name = "Photo")
    @Lob
    public byte[] photo;

    @Getter @Setter
    @Column(name = "FirstName")
    public String firstName;

    @Getter @Setter
    @Column(name = "LastName")
    public String lastName;

    @Getter @Setter
    @Column(name = "Phone1")
    public String phone1;

    @Getter @Setter
    @Column(name = "Phone2")
    public String phone2;

    @Getter @Setter
    @Embedded
    public Address address;

    @Getter @Setter
    @Column(name = "EmailAddress")
    public String emailAddress;

    @Getter @Setter
    @Column(name = "DateOfBirth")
    public LocalDate dateOfBirth;

    @Getter @Setter
    @Column(name = "StatusType")
    public StatusType statusType;

    @Getter @Setter
    @ManyToOne
    public Department department;

    @Getter @Setter
    @ManyToOne
    public Location location;

    @Getter @Setter
    @ManyToOne
    public Account account;

    @Getter @Setter
    @ManyToOne
    public Designation designation;

    @Getter @Setter
    @Column(name = "DateCreated")
    public LocalDateTime dateCreated;
}
