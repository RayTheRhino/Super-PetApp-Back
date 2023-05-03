package superapp.controllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import superapp.bounderies.CommandId;
import superapp.bounderies.MiniAppCommandBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import superapp.logic.MiniappCommandsService;



@RestController
public class MiniAppCommandController {
    private MiniappCommandsService miniappCommandsService;

    @Autowired
    public void setMiniappCommandsService(MiniappCommandsService miniappCommandsService){
        this.miniappCommandsService = miniappCommandsService;
    }
    @RequestMapping(
            path = {"/superapp/miniapp/{miniAppName}"},
            method = {RequestMethod.POST},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object invokeMiniApp (
            @PathVariable("miniAppName") String miniAppName,
            @RequestBody MiniAppCommandBoundary miniAppCommandBoundary) {

        miniAppCommandBoundary.setCommandId(new CommandId("SuperPetApp", miniAppName, ""));
        return miniappCommandsService.invokeCommand(miniAppCommandBoundary);
    }

}


