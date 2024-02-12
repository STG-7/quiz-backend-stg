package com.nighthawk.spring_portfolio.mvc.person;
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

@RestController
@RequestMapping("/api/person")
public class PersonApiController {
    //     @Autowired
    // private JwtTokenUtil jwtGen;
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private PersonJpaRepository repository;

    @Autowired
    private PersonDetailsService personDetailsService;

    /*
    GET List of People
     */
    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Person>> getPeople() {
        return new ResponseEntity<>( repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    @GetMapping("/jwt")
    @PreAuthorize("isAuthenticated()")  // Restrict access to authenticated users
    public ResponseEntity<Person> getAuthenticatedPersonData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = repository.findByEmail(username);  // Retrieve data for the authenticated user
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    /*
    GET individual Person using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/leaderboardCSP")
    public List<Person> getLeaderboardCSP() {
        // Get top 5 users based on cspPoints
        return repository.findTop5ByOrderByCspPointsDesc();
    }

    @GetMapping("/leaderboardCSA")
    public List<Person> getLeaderboardCSA() {
        // Get top 5 users based on cspPoints
        return repository.findTop5ByOrderByCsaPointsDesc();
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/post")
    public ResponseEntity<Object> postPerson(@RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("name") String name) {
    
        // Check if a person with the same name already exists
        Person existingPerson = personDetailsService.getByName(name);
        if (existingPerson != null) {
            return new ResponseEntity<>("User with name " + name + " already exists", HttpStatus.BAD_REQUEST);
        }

        // Check if a person with the same email already exists
        Person existingPersonByEmail = personDetailsService.getByEmail(email);
        if (existingPersonByEmail != null) {
            return new ResponseEntity<>("User with email " + email + " already exists", HttpStatus.BAD_REQUEST);
        }
    
        // If no existing person with the same name, create and save the new person
        int csaPoints = 0;
        int cspPoints = 0;
        int profilePicInt = 0;
        Person person = new Person(email, password, name, csaPoints, cspPoints, profilePicInt);
        personDetailsService.save(person);
    
        return new ResponseEntity<>(email + " is created successfully", HttpStatus.CREATED);
    }

    /*
    The personSearch API looks across database for partial match to term (k,v) passed by RequestEntity body
     */
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personSearch(@RequestBody final Map<String,String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Person> list = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
    The personStats API adds stats by Date to Person table 
    */
    @PostMapping(value = "/setStats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> personStats(@RequestBody final Map<String,Object> stat_map) {
        // find ID
        long id=Long.parseLong((String)stat_map.get("id"));  
        Optional<Person> optional = repository.findById((id));
        if (optional.isPresent()) {  // Good ID
            Person person = optional.get();  // value from findByID

            // Extract Attributes from JSON
            Map<String, Object> attributeMap = new HashMap<>();
            for (Map.Entry<String,Object> entry : stat_map.entrySet())  {
                // Add all attribute other thaN "date" to the "attribute_map"
                if (!entry.getKey().equals("date") && !entry.getKey().equals("id"))
                    attributeMap.put(entry.getKey(), entry.getValue());
            }

            // Set Date and Attributes to SQL HashMap
            Map<String, Map<String, Object>> date_map = new HashMap<>();
            date_map.put( (String) stat_map.get("date"), attributeMap );
            person.setStats(date_map);  // BUG, needs to be customized to replace if existing or append if new
            repository.save(person);  // conclude by writing the stats updates

            // return Person with update Stats
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    @PostMapping("/addPointsCSA")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Person> addPointsCSA(@RequestParam("points") int points) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = repository.findByEmail(username);  // Retrieve data for the authenticated user
        person.setCsaPoints(person.getCsaPoints() + points);
        repository.save(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/addPointsCSP")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Person> addPointsCSP(@RequestParam("points") int points) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = repository.findByEmail(username);  // Retrieve data for the authenticated user
        person.setCspPoints(person.getCspPoints() + points);
        repository.save(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/changeProfilePic")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Person> changeProfilePic(@RequestParam("profilePicInt") int profilePicInt) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = repository.findByEmail(username);
        person.setProfilePicInt(profilePicInt);
        repository.save(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
}
