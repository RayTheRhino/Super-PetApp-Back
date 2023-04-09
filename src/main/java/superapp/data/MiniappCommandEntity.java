package superapp.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="MINIAPPCOMMANDS")
public class MiniappCommandEntity {
	@Id private String commandId; 
	private String name;
	
	public MiniappCommandEntity() {	
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String userId) {
		this.commandId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserEntity [commandId=" + commandId + ", name=" + name + "]";
	}
	
	
	

	
}
