package superapp.controllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import superapp.bounderies.MiniAppCommandBoundary;
import superapp.bounderies.UserBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import superapp.logic.MiniappCommandsService;
import superapp.logic.ObjectsService;
import superapp.logic.UsersService;

import java.util.ArrayList;
import java.util.List;


@RestController
public class AdminController {
    private UsersService usersService;
    private ObjectsService objectsService;
    private MiniappCommandsService miniappCommandsService;

    @Autowired
    public void setUsersService(UsersService usersService){
        this.usersService = usersService;
    }
    @Autowired
    public void setObjectsService(ObjectsService objectsService){
        this.objectsService = objectsService;
    }
    @Autowired
    public void setMiniappCommandsService(MiniappCommandsService miniappCommandsService){
        this.miniappCommandsService = miniappCommandsService;
    }


    @RequestMapping(
            path = {"/superapp/admin/users"},
            method = {RequestMethod.DELETE})
    public void deleteAllUsersInApp(){
        usersService.deleteAllUsers();
    }

    @RequestMapping(
            path = {"/superapp/admin/objects"},
            method = {RequestMethod.DELETE})
    public void deleteAllObjectsInApp (){
        objectsService.deleteAllObjects();
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapp"},
            method = {RequestMethod.DELETE})
    public void deleteAllCommandsHistory(){
        miniappCommandsService.deleteAll();
    }

    @RequestMapping(
            path = {"/superapp/admin/users"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary[] getAllUsers (){
        List<UserBoundary> allUsers = usersService.getAllUsers();
        return allUsers.toArray(new UserBoundary[0]);
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapps"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MiniAppCommandBoundary[] exportAllMiniAppCommands (){
        List<MiniAppCommandBoundary> allMiniAppcommands = miniappCommandsService.getAllCommands();
        return allMiniAppcommands.toArray(new MiniAppCommandBoundary[0]);
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapps/{miniAppName}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MiniAppCommandBoundary[] exportCommandsHistoryOfspecificMiniApp (@PathVariable("miniAppName") String miniAppName){
        List<MiniAppCommandBoundary> specificMiniAppcommands = miniappCommandsService.getAllMiniAppCommands(miniAppName);
        return specificMiniAppcommands.toArray(new MiniAppCommandBoundary[0]);

    }
}

