package superapp.controllerAPI;

import superapp.bounderies.MiniAppCommandBoundary;
import superapp.bounderies.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MiniAppCommandController {
    //Post -1
    @RequestMapping(
            path = {"/superapp/miniapp/{miniAppName}"},
            method = {RequestMethod.POST},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object invokeMiniApp (
            @PathVariable("miniAppName") String miniAppName,
            @RequestBody MiniAppCommandBoundary miniAppCommandBoundary) {
        return null;

    }

}


