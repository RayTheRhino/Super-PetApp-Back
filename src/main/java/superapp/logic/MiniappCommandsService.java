package superapp.logic;

import java.util.List;

import superapp.bounderies.MiniAppCommandBoundary;

public interface MiniappCommandsService {

	
	public Object invokeCommand(MiniAppCommandBoundary command);
	
	public List<MiniAppCommandBoundary> getAllCommands();
	
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName);
		
	public void deleteAll();

}
