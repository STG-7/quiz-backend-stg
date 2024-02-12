package com.nighthawk.spring_portfolio.mvc.person;

import static jakarta.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.vladmihalcea.hibernate.type.json.JsonType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/*
Person is an POJO, Plain Old Java Object.
First set of annotations add functionality to POJO
--- @Setter @Getter @ToString @NoArgsConstructor @RequiredArgsConstructor
The last annotation connect to database
--- @Entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Convert(attributeName ="person", converter = JsonType.class)
public class Person {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    @NotEmpty
    @Size(min=5)
    @Column(unique=true)
    @Email
    private String email;

    @NotEmpty
    private String password;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max = 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 30 chars)")
    private String name;

    @ManyToMany(fetch = EAGER)
    private Collection<PersonRole> roles = new ArrayList<>();

    
    private int csaPoints;
    private int cspPoints;
    private int profilePicInt;

    /* HashMap is used to store JSON for daily "stats"
    "stats": {
        "2022-11-13": {
            "calories": 2200,
            "steps": 8000
        }
    }
    */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Map<String, Object>> stats = new HashMap<>(); 
    

    // Constructor used when building object from an API
    public Person(String email, String password, String name, int csaPoints, int cspPoints, int profilePicInt) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.csaPoints = csaPoints;
        this.cspPoints = cspPoints;
        this.profilePicInt = profilePicInt;
    }


    // Initialize static test data
    public static Person[] init() {

        // basics of class construction
        Person p1 = new Person();
        p1.setName("Theo Huntalas");
        p1.setEmail("theo@gmail.com");
        p1.setPassword("123Theo!");
        p1.setCsaPoints(0);
        p1.setCspPoints(0);
        p1.setProfilePicInt(0);

        Person p2 = new Person();
        p2.setName("Justin Nguyen");
        p2.setEmail("justin@gmail.com");
        p2.setPassword("123Justin!");
        p2.setCsaPoints(0);
        p2.setCspPoints(0);
        p2.setProfilePicInt(0);

        Person p3 = new Person();
        p3.setName("Finn Carpenter");
        p3.setEmail("finn@gmail.com");
        p3.setPassword("123Finn!");
        p3.setCsaPoints(0);
        p3.setCspPoints(0);
        p3.setProfilePicInt(0);

        Person p4 = new Person();
        p4.setName("Rachit Jaiswal");
        p4.setEmail("rachit@gmail.com");
        p4.setPassword("123Rachit!");
        p4.setCsaPoints(0);
        p4.setCspPoints(0);
        p4.setProfilePicInt(0);

        Person p5 = new Person();
        p5.setName("Luna Iwazaki");
        p5.setEmail("luna@gmail.com");
        p5.setPassword("123Luna!");
        p5.setCsaPoints(0);
        p5.setCspPoints(0);
        p5.setProfilePicInt(0);

        Person p6 = new Person();
        p6.setName("Tanisha Patil");
        p6.setEmail("tanisha@gmail.com");
        p6.setPassword("123Tanisha!");
        p6.setCsaPoints(0);
        p6.setCspPoints(0);
        p6.setProfilePicInt(0);

        Person p7 = new Person();
        p7.setName("TestPlayer");
        p7.setEmail("testPlayer@gmail.com");
        p7.setPassword("123TestPlayer!");
        p7.setCsaPoints(0);
        p7.setCspPoints(0);
        p7.setProfilePicInt(0);

        // Array definition and data initialization
        Person persons[] = {p1, p2, p3, p4, p5, p6, p7};
        return(persons);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Person persons[] = init();

        // iterate using "enhanced for loop"
        for( Person person : persons) {
            System.out.println(person);  // print object
        }
    }

}