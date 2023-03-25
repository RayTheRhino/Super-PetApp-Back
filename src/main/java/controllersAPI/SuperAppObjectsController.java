package controllersAPI;

import Bounderies.ObjectBoundary;
import Bounderies.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class SuperAppObjectsController {
        @RequestMapping(
                path = {"/superapp/objects"},
                method = {RequestMethod.POST},
                produces = {MediaType.APPLICATION_JSON_VALUE},
                consumes = {MediaType.APPLICATION_JSON_VALUE}
        )

        public ObjectBoundary createObject(@RequestBody ObjectBoundary input){
            input.setObjectId(new ObjectId("2023b.demo","1"));
                return input;
        }

        @RequestMapping(
                path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
                method = {RequestMethod.PUT},
                consumes = {MediaType.APPLICATION_JSON_VALUE}
        )
        public void updateObject ( @PathVariable("superapp") String superapp,
                                   @PathVariable("InternalObjectId") String InternalObjectId,
                                   @RequestBody ObjectBoundary input){
                System.out.println("updated"+ input);
        }

        @RequestMapping(
                path = {"/superapp/objects/{superapp}/{InternalObjectId}"},
                method = {RequestMethod.GET},
                consumes = {MediaType.APPLICATION_JSON_VALUE},
                produces = {MediaType.APPLICATION_JSON_VALUE})
        public ObjectBoundary retrieveObject(
                @PathVariable("superapp") String superapp,
                @PathVariable("InternalObjectId") String InternalObjectId){
                return new ObjectBoundary();
        }

        @RequestMapping(
                path = {"/superapp/objects"},
                method = {RequestMethod.GET},
                consumes = {MediaType.APPLICATION_JSON_VALUE},
                produces = {MediaType.APPLICATION_JSON_VALUE})
        public ObjectBoundary[] getAllObjects (){
            return new ArrayList<ObjectBoundary>().toArray(new ObjectBoundary[0]); //Change maybe?

        }


    }