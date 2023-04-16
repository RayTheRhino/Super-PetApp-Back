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
import superapp.bounderies.CreatedBy;
import superapp.bounderies.Location;
import superapp.bounderies.ObjectId;
import superapp.logic.SuperappConverterOfMapToJson;

@Entity
@Table(name="SuperappObjects")
public class SuperappObjectsEntity {
    @Id
	private String internalObjectId;
	private String superapp;
    private String type;
    private String alias;
    private boolean active;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp;
    private double lat;
    private double lng;
    private String byEmail;
    private String bySuperapp;
	@Convert(converter = SuperappConverterOfMapToJson.class)
	@Lob
    private Map<String, Object> objectDetails;
	
	public SuperappObjectsEntity() {	
		this.objectDetails = new TreeMap<>();
	}

	public String getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(String internalObjectId) {
		this.internalObjectId = internalObjectId;
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getByEmail() {
		return byEmail;
	}

	public void setByEmail(String byEmail) {
		this.byEmail = byEmail;
	}
	
	public String getBySuperapp() {
		return bySuperapp;
	}

	public void setBySuperapp(String bySuperapp) {
		this.bySuperapp = bySuperapp;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

	@Override
	public String toString() {
		return "SuperappObjectsEntity [internalObjectId=" + internalObjectId + ", superapp=" + superapp + ", type="
				+ type + ", alias=" + alias + ", active=" + active + ", creationTimestamp=" + creationTimestamp
				+ ", lat=" + lat + ", lng=" + lng + ", createdBy=" + byEmail + bySuperapp + ", objectDetails=" + objectDetails
				+ "]";
	}


}
