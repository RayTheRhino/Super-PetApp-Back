package superapp.bounderies;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class MiniAppCommandBoundary {
    private CommandId commandId;
    private String command;
    private TargetObject targetObject;
    private Date invocationTimestamp;
    private InvokedBy invokedBy;
    private Map<String,Object> commandAttributes;


    public MiniAppCommandBoundary() {
        this.commandAttributes = new TreeMap<>();
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

    public Date getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public InvokedBy getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public Map<String, Object> getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }

    @Override
    public String toString() {
        return "MiniAppCommandBoundary{" +
                "commandId= {"+(commandId != null ? commandId.toString() : "null")+'}'+
                ", command='" + (command != null ? command : "null") + '\'' +
                ", invocationTimestamp=" + (invocationTimestamp != null ? invocationTimestamp.toString() : "null") +
                ", targetObject={" + (targetObject != null ? targetObject.toString() : "null") + '}' +
                ", invokedBy={" + (invokedBy != null ? invokedBy.toString() : "null") + '}' +
                ", commandAttributes=" + (commandAttributes != null ? commandAttributes.toString() : "null") +
                "}";
    }
}
