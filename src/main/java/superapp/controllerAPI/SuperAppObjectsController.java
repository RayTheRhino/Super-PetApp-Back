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

        public ObjectBoundary createObject(@RequestBody ObjectBoundary input){
                return objectsService.CreateObject(input);
        }

        @RequestMapping(
                path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
                method = {RequestMethod.PUT},
                consumes = {MediaType.APPLICATION_JSON_VALUE}
        )
        public void updateObject ( @PathVariable("superapp") String superapp,
                                   @PathVariable("InternalObjectId") String InternalObjectId,
                                   @RequestBody ObjectBoundary input) {
                objectsService.updateObject(superapp,InternalObjectId,input); // TODO: need to ask how to update the children
        }

        @RequestMapping(
                path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
                method = {RequestMethod.GET},
                produces = {MediaType.APPLICATION_JSON_VALUE})
        public ObjectBoundary retrieveObject(
                @PathVariable("superapp") String superapp,
                @PathVariable("InternalObjectId") String InternalObjectId) {
                return objectsService.getSpecificObject(superapp,InternalObjectId);
        }

        @RequestMapping(
                path = {"/superapp/objects"},
                method = {RequestMethod.GET},
                produces = {MediaType.APPLICATION_JSON_VALUE})
        public ObjectBoundary[] getAllObjects (){
            List<ObjectBoundary> allObjects = objectsService.getAllObjects();
            return allObjects.toArray(new ObjectBoundary[0]);

        }

    }