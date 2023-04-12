package superapp.data;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import superapp.bounderies.CommandId;
import superapp.bounderies.TargetObject;
import superapp.logic.SuperappConverterOfMapToJson;
@Entity
@Table(name="MINIAPPCOMMANDS")
public class MiniappCommandEntity {
//  private CommandId commandId;
    @Id
	private String commandId;
    private String commandSuperapp;
    private String commandMiniapp;
    private String command;
//  private TargetObject targetObject;
    private String targetSuperapp;
    private String targetObjectId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date invocationTimeStamp;
	@Convert(converter = SuperappConverterOfMapToJson.class)
	@Lob
    private Map<String,Object> commandAttribute;
	
	public MiniappCommandEntity() {	
		this.commandAttribute = new TreeMap<>();
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getCommandSuperapp() {
		return commandSuperapp;
	}

	public void setCommandSuperapp(String commandSuperapp) {
		this.commandSuperapp = commandSuperapp;
	}

	public String getCommandMiniapp() {
		return commandMiniapp;
	}

	public void setCommandMiniapp(String commandMiniapp) {
		this.commandMiniapp = commandMiniapp;
	}

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

	public Date getInvocationTimeStamp() {
		return invocationTimeStamp;
	}

	public void setInvocationTimeStamp(Date invocationTimeStamp) {
		this.invocationTimeStamp = invocationTimeStamp;
	}

	public Map<String, Object> getCommandAttribute() {
		return commandAttribute;
	}

	public void setCommandAttribute(Map<String, Object> commandAttribute) {
		this.commandAttribute = commandAttribute;
	}


}
