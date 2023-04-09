package superapp.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="SuperappObjects")
public class SuperappObjectsEntity {
	@Id private String objectId; 
	private String name;
	
	public SuperappObjectsEntity() {	
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String userId) {
		this.objectId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserEntity [objectId=" + objectId + ", name=" + name + "]";
	}
	
	
	

	
}
