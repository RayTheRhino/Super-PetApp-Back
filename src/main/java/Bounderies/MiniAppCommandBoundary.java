package Bounderies;

import java.util.Date;
import java.util.Map;

public class MiniAppCommandBoundary {
    private CommandId commandId;
    private String command;
    private TargetObject targetObject;
    private Date invocationTimeStamp;
    private Map<String,Object> commandAttribute;

    public MiniAppCommandBoundary(CommandId commandId, String command, TargetObject targetObject, Date invocationTimeStamp, Map<String, Object> commandAttribute) {
        this.commandId = commandId;
        this.command = command;
        this.targetObject = targetObject;
        this.invocationTimeStamp = invocationTimeStamp;
        this.commandAttribute = commandAttribute;
    }

    public MiniAppCommandBoundary() {
    }

    public CommandId getCommandId() {
        return commandId;
    }

    public void setCommandId(CommandId commandId) {
        this.commandId = commandId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public TargetObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(TargetObject targetObject) {
        this.targetObject = targetObject;
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
