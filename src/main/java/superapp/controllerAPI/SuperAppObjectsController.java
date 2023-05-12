package superapp.controllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import superapp.bounderies.ObjectBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import superapp.logic.ObjectsService;

import java.util.List;

@RestController
public class SuperAppObjectsController {

    private ObjectsService objectsService;
    @Autowired
    public void setObjectsService(ObjectsService objectsService){
        this.objectsService = objectsService;
    }
    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.POST},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    public ObjectBoundary createObject(
            @RequestBody ObjectBoundary input){
            return objectsService.CreateObject(input);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public void updateObject (
            @PathVariable("superapp") String superapp,
            @PathVariable("InternalObjectId") String InternalObjectId,
            @RequestBody ObjectBoundary input,
            @RequestParam (name = "userSuperapp") String userSuperapp,
            @RequestParam (name = "userEmail") String email) {

        objectsService.updateObject(superapp,InternalObjectId,input);//TODO: Update Functions
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary retrieveObject(
            @PathVariable("superapp") String superapp,
            @PathVariable("InternalObjectId") String InternalObjectId,
            @RequestParam (name = "userSuperapp") String userSuperapp,
            @RequestParam (name = "userEmail") String email) {
            return objectsService.getSpecificObject(superapp,InternalObjectId); //TODO: Update Functions
    }

    @RequestMapping(
            path = {"/superapp/objects/search/byType/{type}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary[] retrieveObjectByType(
            @PathVariable("type") String type,
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page) {
        return objectsService.getSpecificObjectsByType(superapp,InternalObjectId); // TODO: create the new get functions
    }

    @RequestMapping(
            path = {"/superapp/objects/search/byAlias/{alias}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary[] retrieveObjectByAlias(
            @PathVariable("alias") String alias,
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page){
    return objectsService.getSpecificObjectsByAlias(superapp,email); // TODO: create the new get functions
    }
    @RequestMapping(
            path = {"/superapp/objects/search/byLocation/{lat}/{lng}/{distance}"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary[] retrieveObject(
            @PathVariable("lat") double lat,
            @PathVariable("lng") double lng,
            @PathVariable("distance") double distance,
            @RequestParam (name = "units", required = false, defaultValue = "NEUTRAL") String distanceUnits,
            @RequestParam (name = "userSuperapp") String superapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page){
    return objectsService.getSpecificObjectsByLocation(superapp,InternalObjectId);// TODO: create the new get functions
    }

    @RequestMapping(
            path = {"/superapp/objects"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary[] getAllObjects (
            @RequestParam (name = "userSuperapp") String userSuperapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page
    ){
        List<ObjectBoundary> allObjects = objectsService.getAllObjects(); //TODO: Update Functions
        return allObjects.toArray(new ObjectBoundary[0]);

    }

}