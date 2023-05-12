package superapp.controllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import superapp.bounderies.CommandId;
import superapp.bounderies.MiniAppCommandBoundary;
import org.springframework.http.MediaType;
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
            @RequestBody MiniAppCommandBoundary miniAppCommandBoundary,
            @RequestParam (name = "async", required = false, defaultValue = "false") boolean async)) {

        miniAppCommandBoundary.setCommandId(new CommandId("SuperPetApp", miniAppName, ""));
        return miniappCommandsService.invokeCommand(miniAppCommandBoundary);
    }

}


