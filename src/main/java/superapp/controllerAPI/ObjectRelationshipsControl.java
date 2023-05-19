package superapp.controllerAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import superapp.bounderies.ObjectBoundary;
import superapp.bounderies.ObjectId;
import superapp.logic.ImprovedObjectService;
import superapp.logic.ObjectServiceWithBindingFunctionality;

import java.util.List;

@RestController
public class ObjectRelationshipsControl {
    private ImprovedObjectService objectServiceWBind;

    @Autowired
    public void setObjectServiceWBind(ImprovedObjectService objectServiceWBind){
        this.objectServiceWBind = objectServiceWBind;
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}/children"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public void bindChild (
            @PathVariable("superapp") String superapp,
            @PathVariable("InternalObjectId") String InternalObjectId,
            @RequestBody ObjectId input,
            @RequestParam (name = "userSuperapp") String userSuperapp,
            @RequestParam (name = "userEmail") String email
            ) {
        objectServiceWBind.ObjectBindingChild(new ObjectId(superapp,InternalObjectId), input,userSuperapp,email);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}/children"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary[] getChildren(
            @PathVariable("superapp") String superapp,
            @PathVariable("InternalObjectId") String InternalObjectId,
            @RequestParam (name = "userSuperapp") String userSuperapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page){

        List<ObjectBoundary> rv = this.objectServiceWBind
                .getChildren(superapp,InternalObjectId,userSuperapp,email,size,page);
        return rv.toArray(new ObjectBoundary[0]);
    }
    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}/parents"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary[] getParents(
            @PathVariable("superapp") String superapp,
            @PathVariable("InternalObjectId") String InternalObjectId,
            @RequestParam (name = "userSuperapp") String userSuperapp,
            @RequestParam (name = "userEmail") String email,
            @RequestParam (name = "size", required = false, defaultValue = "10") int size,
            @RequestParam (name = "page", required = false, defaultValue = "0") int page){
        List<ObjectBoundary> rv = this.objectServiceWBind
                .getParents(superapp,InternalObjectId,userSuperapp,email,size,page);
        return rv.toArray(new ObjectBoundary[0]);
    }


}
