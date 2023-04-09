package superapp.controllerAPI;

import superapp.bounderies.MiniAppCommandBoundary;
import superapp.bounderies.ObjectBoundary;
import superapp.bounderies.UserBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
public class AdminController {
    @RequestMapping(
            path = {"/superapp/admin/users"},
            method = {RequestMethod.DELETE})
    public void deleteAllUsersInApp(){
        System.out.println("d1");
    }

    @RequestMapping(
            path = {"/superapp/admin/objects"},
            method = {RequestMethod.DELETE})
    public void deleteAllObjectsInApp (){
        System.out.println("d2");
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapp"},
            method = {RequestMethod.DELETE})
    public void deleteAllCommandsHistory(){
        System.out.println("delteall");
    }

    @RequestMapping(
            path = {"/superapp/admin/users"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary[] getAllUsers (){
        return new ArrayList<UserBoundary>().toArray(new UserBoundary[0]);
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapps"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MiniAppCommandBoundary[] exportAllMiniAppCommands (){
        return new ArrayList<MiniAppCommandBoundary>().toArray(new MiniAppCommandBoundary[0]);
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapps/{miniAppName}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MiniAppCommandBoundary[] exportCommandsHistoryOfspecificMiniApp (@PathVariable("miniAppName") String miniAppName){
        return new ArrayList<MiniAppCommandBoundary>().toArray(new MiniAppCommandBoundary[0]);
    }
}

