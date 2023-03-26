package controllersAPI;

import Bounderies.NewUserBoundary;
import Bounderies.UserBoundary;
import demo.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @RequestMapping(
            path = {"/superapp/users"},
            method = {RequestMethod.POST},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary createNewUser (@RequestBody NewUserBoundary input) {
        UserBoundary user = new UserBoundary(input, "2023b.tal.benita");
        return user;
    }

    @RequestMapping(
            path = {"/superapp/users/login/{superapp}/{email}"},
            method = {RequestMethod.GET},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary loginAndRetriveUserDetails (
            @PathVariable("superapp") String superapp,
            @PathVariable("email") String email){
        return new UserBoundary();
    }

    
    @RequestMapping(
            path = {"/superapp/users/{superapp}/{userEmail}"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateUserDetails (
            @PathVariable("superapp") String superapp,
            @PathVariable("userEmail") String userEmail,
            @RequestBody UserBoundary input)
    {

    }




}
