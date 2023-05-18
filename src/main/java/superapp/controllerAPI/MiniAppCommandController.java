package superapp.controllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import superapp.bounderies.CommandId;
import superapp.bounderies.MiniAppCommandBoundary;
import org.springframework.http.MediaType;
import superapp.logic.ImprovedMiniappCommandService;




@RestController
public class MiniAppCommandController {
    private ImprovedMiniappCommandService miniappCommandsService;

    @Autowired
    public void setMiniappCommandsService(ImprovedMiniappCommandService miniappCommandsService){
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
            @RequestParam (name = "async", required = false, defaultValue = "false") boolean async) {
        miniAppCommandBoundary.setCommandId(new CommandId(miniAppName));
        return miniappCommandsService.invokeCommand(miniAppCommandBoundary, async);
    }

}


