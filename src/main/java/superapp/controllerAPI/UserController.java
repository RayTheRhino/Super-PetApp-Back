package superapp.controllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import superapp.bounderies.NewUserBoundary;
import superapp.bounderies.UserBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import superapp.logic.UserNotFoundException;
import superapp.logic.UsersService;

@RestController
public class UserController {
    private UsersService usersService;

    @Autowired
    public void setUsersService(UsersService usersService){
        this.usersService = usersService;
    }
    @RequestMapping(
            path = {"/superapp/users"},
            method = {RequestMethod.POST},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary createNewUser (@RequestBody NewUserBoundary input) {
        UserBoundary user = new UserBoundary(input, "2023b.tal.benita");
        return usersService.createUser(user);
    }

    @RequestMapping(
            path = {"/superapp/users/login/{superapp}/{email}"},
            method = {RequestMethod.GET},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary loginAndRetriveUserDetails (
            @PathVariable("superapp") String superapp,
            @PathVariable("email") String email){
        return  usersService.login(superapp,email).orElseThrow(
				() -> new UserNotFoundException("Couldn't find user by this email: "+email));
    }

    
    @RequestMapping(
            path = {"/superapp/users/{superapp}/{userEmail}"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateUserDetails (
            @PathVariable("superapp") String superapp,
            @PathVariable("userEmail") String userEmail,
            @RequestBody UserBoundary input) throws Exception {
            usersService.update(superapp , userEmail ,input);
    }




}
