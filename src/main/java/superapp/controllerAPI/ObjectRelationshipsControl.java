package superapp.controllerAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import superapp.bounderies.ObjectBoundary;
import superapp.bounderies.ObjectId;
import superapp.logic.ObjectServiceWithBindingFunctionality;

import java.util.List;

@RestController
public class ObjectRelationshipsControl {
    private ObjectServiceWithBindingFunctionality objectServiceWBind;

    @Autowired
    public void setObjectServiceWBind(ObjectServiceWithBindingFunctionality objectServiceWBind){
        this.objectServiceWBind = objectServiceWBind;
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}/children"},
            method = {RequestMethod.PUT},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public void bindChild ( @PathVariable("superapp") String superapp,
                            @PathVariable("InternalObjectId") String InternalObjectId,
                            @RequestBody ObjectId input) {

        objectServiceWBind.ObjectBindingChild(new ObjectId(superapp,InternalObjectId), input);
    }

    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}/children"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary[] getChildren(@PathVariable("superapp") String superapp,
                                        @PathVariable("InternalObjectId") String InternalObjectId){

        List<ObjectBoundary> rv = this.objectServiceWBind
                .getChildren(superapp,InternalObjectId);
        return rv.toArray(new ObjectBoundary[0]);
    }
    @RequestMapping(
            path = {"/superapp/objects/{superapp}/{InternalObjectId}/parents"},
            method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ObjectBoundary[] getParents(@PathVariable("superapp") String superapp,
                                       @PathVariable("InternalObjectId") String InternalObjectId){
        List<ObjectBoundary> rv = this.objectServiceWBind
                .getParents(superapp,InternalObjectId);
        return rv.toArray(new ObjectBoundary[0]);
    }


}
