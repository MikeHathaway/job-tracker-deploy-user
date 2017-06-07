package com.userService.api;


import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.userService.model.User;
import com.userService.model.UserRepository;

/*
helpful guide:
    http://websystique.com/spring-boot/spring-boot-rest-api-example/
    https://github.com/viralpatel/spring4-restful-example/blob/master/src/main/java/net/viralpatel/spring/controller/CustomerRestController.java
    https://spring.io/guides/gs/rest-service-cors/
 */

//CrossOrigin takes an argument - the whitelisted call sources

@RestController
//@CrossOrigin(origins = "http://localhost:5000", methods = RequestMethod.POST)
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }


    // ********** Get ALL Users **********
    @GetMapping("")
    public Iterable<User> all() {
        return this.repository.findAll();
    }


    // ********** Add User **********
    @PostMapping("")
    public User create(@RequestBody User user) {
        System.out.println("Creating User " + user.getUsername());
        return this.repository.save(user);
    }


    // ********** Get Single User **********
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
        User user = this.repository.findById(id);

        if(user == null){
            return new ResponseEntity("Unable to find. User with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
    }


    // **********  User Login **********
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        //take username and compare password with the matching username in the db
        User existingUser = this.repository.findByUsername(user.getUsername());

        if(existingUser.getPassword().equals(user.getPassword())){
            System.out.println("Authenticated User " + user.getUsername());
            return existingUser;

        }
        return null;
    }


    // ********** Update A User **********
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {

        User currentUser = this.repository.findById(id);

        if (currentUser == null) {
            return new ResponseEntity("Unable to upate. User with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }
        else {
            currentUser.setUsername(user.getUsername());
            currentUser.setPassword(user.getPassword());
            currentUser.setCity(user.getCity());
            currentUser.setState(user.getState());
            currentUser.setEmail(user.getEmail());

            this.repository.save(currentUser);
            return new ResponseEntity<User>(currentUser, HttpStatus.OK);
        }
    }


    // ********** Delete A User **********
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        User user = this.repository.findById(id);
        this.repository.deleteById(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

}
