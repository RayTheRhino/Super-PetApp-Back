package superapp.controllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import superapp.bounderies.MiniAppCommandBoundary;
import superapp.bounderies.UserBoundary;
import org.springframework.http.MediaType;
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
    public void deleteAllUsersInApp(
            @RequestParam(name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email){
        usersService.deleteAllUsers();  //TODO: Update Functions
    }

    @RequestMapping(
            path = {"/superapp/admin/objects"},
            method = {RequestMethod.DELETE})
    public void deleteAllObjectsInApp (
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email
    ){
        objectsService.deleteAllObjects(); //TODO: Update Functions
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapp"},
            method = {RequestMethod.DELETE})
    public void deleteAllCommandsHistory(
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email
    ){
        miniappCommandsService.deleteAll(); //TODO: Update Functions
    }

    @RequestMapping(
            path = {"/superapp/admin/users"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserBoundary[] getAllUsers (
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page
    ){
        List<UserBoundary> allUsers = usersService.getAllUsers(); //TODO: Update Functions
        return allUsers.toArray(new UserBoundary[0]);
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapp"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MiniAppCommandBoundary[] exportAllMiniAppCommands (
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page
    ){
        List<MiniAppCommandBoundary> allMiniAppcommands = miniappCommandsService.getAllCommands();
        return allMiniAppcommands.toArray(new MiniAppCommandBoundary[0]); //TODO: Update Functions
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapp/{miniAppName}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MiniAppCommandBoundary[] exportCommandsHistoryOfspecificMiniApp (
            @PathVariable("miniAppName") String miniAppName,
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page){
        List<MiniAppCommandBoundary> specificMiniAppcommands = miniappCommandsService.getAllMiniAppCommands(miniAppName);
        return specificMiniAppcommands.toArray(new MiniAppCommandBoundary[0]); //TODO: Update Functions

    }
}

