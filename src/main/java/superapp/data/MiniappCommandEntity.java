package superapp.data;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MiniappCommands")
public class MiniappCommandEntity {
	@Id
	private String commandId;
	private String command;
    private String targetSuperapp;
    private String targetObjectId;
    private Date invocationTimestamp;
	private String invokedByEmail;
	private String invokedBySuperapp;
    private Map<String,Object> commandAttributes;


	public MiniappCommandEntity() {
		this.commandAttributes = new TreeMap<>();
	}

	public String getInvokedByEmail() {
		return invokedByEmail;
	}

	public void setInvokedByEmail(String invokedByEmail) {
		this.invokedByEmail = invokedByEmail;
	}

	public String getInvokedBySuperapp() {
		return invokedBySuperapp;
	}

	public void setInvokedBySuperapp(String invokedBySuperapp) {
		this.invokedBySuperapp = invokedBySuperapp;
	}


	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getCommandSuperApp(){ return commandId.split("/")[0];}
	public String getCommandMiniApp(){ return commandId.split("/")[1];}
	public String getCommandInternalId(){ return commandId.split("/")[2];}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTargetSuperapp() {
		return targetSuperapp;
	}

	public void setTargetSuperapp(String targetSuperapp) {
		this.targetSuperapp = targetSuperapp;
	}

	public String getTargetObjectId() {
		return targetObjectId;
	}

	public void setTargetObjectId(String targetObjectId) {
		this.targetObjectId = targetObjectId;
	}

	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}

	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}

	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}


}
