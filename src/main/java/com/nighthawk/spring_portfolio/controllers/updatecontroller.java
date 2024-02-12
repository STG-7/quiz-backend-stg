package com.nighthawk.spring_portfolio.controllers;
/* MVC code that shows defining a simple Model, calling View, and this file serving as Controller
 * Web Content with Spring MVCSpring Example: https://spring.io/guides/gs/serving-web-con
 */

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;
import com.nighthawk.spring_portfolio.mvc.person.PersonJpaRepository;
@Controller  // HTTP requests are handled as a controller, using the @Controller annotation
public class updatecontroller {

    @Autowired
    private PersonJpaRepository repository;

    // @GetMapping handles GET request for /greet, maps it to greeting() method
    @GetMapping("/updating")
    @PreAuthorize("isAuthenticated()")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    // @RequestParam handles variables binding to frontend, defaults, etc
    public String person(Model model) {
        List<Person> persons = repository.findAllByOrderByNameAsc();
        model.addAttribute("persons", persons);
        // System.out.println(persons.toString()); for testing purposes
        return "reading";
        }
    

}