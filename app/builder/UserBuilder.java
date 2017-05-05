package builder;

import enums.RoleType;
import enums.StatusType;
import models.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author tolet
 */
public class UserBuilder {

    private final Long companyId;
    String[] years = {"1980", "1982", "1986", "1984", "1970", "1990", "1993", "1995", "1975", "1978", "1987"};
    String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String[] days = {"01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12", "13", "14",
            "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "25", "27", "28"};

    private ArrayList<User> users = new ArrayList<>();
    private String nameFile;
    private String addressFile;
    private String emailDomain;
    private RoleType roleType;
    private StatusType statusType;
    private String city;
    private String country;
    private Long accountId;

    public UserBuilder(Long companyId, String nameFile, String addressFile, String emailDomain, RoleType roleType, StatusType statusType, String city, String country, Long accountId) {
        this.companyId = companyId;
        this.nameFile = nameFile;
        this.addressFile = addressFile;
        this.emailDomain = emailDomain;
        this.roleType = roleType;
        this.statusType = statusType;
        this.city = city;
        this.country = country;
        this.accountId = accountId;
    }

    public void build() throws FileNotFoundException, IOException {
        Random random = new Random();
        User user = null;
        FileReader fileInputStream = new FileReader(new File(nameFile));
        BufferedReader bufferedReader = new BufferedReader(fileInputStream);

        String s;
        int count = 211;
        Long pin = 2L;
        while ((s = bufferedReader.readLine()) != null) {
            user = new User();
            StringTokenizer stringTokenizer = new StringTokenizer(s);
            while (stringTokenizer.hasMoreTokens()) {
                UserId userId = new UserId();
                userId.companyId = companyId;
                userId.pin = pin;
                user.userId = userId;
                user.firstName = stringTokenizer.nextToken();
                user.lastName = stringTokenizer.nextToken();
                user.emailAddress = user.firstName.charAt(0) + "" + user.lastName.charAt(1) + "" + count + "" + emailDomain;
                user.roleType = roleType;
                user.statusType = statusType;
                user.password = "password";

                String[] states = {"Lagos", "Ogun", "Ibadan"};

                Address address = new Address();
                address.city = states[random.nextInt(2)];
                address.state = states[random.nextInt(2)];
                address.country = country;

                int[] locations = {1, 2, 3};

                Location location = new Location();
                location.id = (long)locations[random.nextInt(2)];
                user.location = location;

                int[] departments = {1, 2, 3};
                Department department = new Department();
                department.id = (long)departments[random.nextInt(2)];
                user.department = department;

                user.address = address;

                Account account = new Account();
                account.id = accountId;

                user.account = account;
                int r1 = random.nextInt(2);
                int r2 = 200 + random.nextInt(700);
                int r3 = 200 + random.nextInt(700);
                int r4 = 10 + random.nextInt(70);

                String ph = "08" + r1 + "" + r2 + "" + r3 + "" + r4;
                user.phone1 = ph;

                int yIndex = random.nextInt(years.length);
                int mIndex = random.nextInt(months.length);
                int dIndex = random.nextInt(days.length);

                String d = days[dIndex] + "-" + months[mIndex] + "-" + years[yIndex];

                System.out.println(d);

                try {
                    user.dateOfBirth = LocalDate.parse(new SimpleDateFormat("dd-MM-yyyy").format(d));
                    System.out.println(user.dateOfBirth);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                users.add(user);
                ++count;
                ++pin;
            }
        }
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }
}
