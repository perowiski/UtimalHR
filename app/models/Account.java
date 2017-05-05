/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import enums.AccountType;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

/**
 * @author peter
 */
@Entity
@Table(name = "Accounts")
public class Account extends Model{

    @Getter @Setter
    @Column(name = "CompanyLogo")
    @Lob
    public byte[] companyLogo;

    @Getter @Setter
    @Column(name = "ContactName")
    public String contactName;

    @Getter @Setter
    @Column(name = "CompanyName")
    public String companyName;

    @Getter @Setter
    @Embedded
    public Address address;

    @Getter @Setter
    @Column(name = "Phone")
    public String phone;

    @Getter @Setter
    @Column(name = "EmailAddress")
    public String emailAdress;

    @Getter @Setter
    @Column(name = "Website")
    public String website;

    @Getter @Setter
    @Column(name = "AccountType")
    public AccountType accountType;
}
