package superapp.logic;

import superapp.bounderies.MiniAppCommandBoundary;

import java.util.List;

public interface ImprovedMiniappCommandService extends MiniappCommandsService{

    public Object invokeCommand(MiniAppCommandBoundary command, boolean async);

    public List<MiniAppCommandBoundary> getAllCommands(String superapp, String email, int size, int page);

    public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName, String superapp, String email, int size, int page);

    public void deleteAll(String superapp, String email);
}
