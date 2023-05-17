package superapp.controllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import superapp.bounderies.MiniAppCommandBoundary;
import superapp.bounderies.UserBoundary;
import org.springframework.http.MediaType;
import superapp.logic.ImprovedMiniappCommandService;
import superapp.logic.ImprovedObjectService;
import superapp.logic.ImprovedUsersService;

import java.util.List;


@RestController
public class AdminController {
    private ImprovedUsersService usersService;
    private ImprovedObjectService objectsService;
    private ImprovedMiniappCommandService miniappCommandsService;

    @Autowired
    public void setUsersService(ImprovedUsersService usersService){
        this.usersService = usersService;
    }
    @Autowired
    public void setObjectsService(ImprovedObjectService objectsService){
        this.objectsService = objectsService;
    }
    @Autowired
    public void setMiniappCommandsService(ImprovedMiniappCommandService miniappCommandsService){
        this.miniappCommandsService = miniappCommandsService;
    }


    @RequestMapping(
            path = {"/superapp/admin/users"},
            method = {RequestMethod.DELETE})
    public void deleteAllUsersInApp(
            @RequestParam(name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email){
        usersService.deleteAllUsers(superapp, email);
    }

    @RequestMapping(
            path = {"/superapp/admin/objects"},
            method = {RequestMethod.DELETE})
    public void deleteAllObjectsInApp (
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email
    ){
        objectsService.deleteAllObjects(superapp, email);
    }

    @RequestMapping(
            path = {"/superapp/admin/miniapp"},
            method = {RequestMethod.DELETE})
    public void deleteAllCommandsHistory(
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email
    ){
        miniappCommandsService.deleteAll(superapp, email);
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
        List<UserBoundary> allUsers = usersService.getAllUsers(superapp, email, size, page);
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
        List<MiniAppCommandBoundary> allMiniAppcommands = miniappCommandsService.getAllCommands(superapp, email, size, page);
        return allMiniAppcommands.toArray(new MiniAppCommandBoundary[0]);
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
        List<MiniAppCommandBoundary> specificMiniAppcommands = miniappCommandsService.getAllMiniAppCommands(
                                                            miniAppName, superapp, email, size, page);
        return specificMiniAppcommands.toArray(new MiniAppCommandBoundary[0]);

    }
}

