package superapp.bounderies;

public class Location {
    private Double lat;
    private Double lng;

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public Location(){

    }
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String toString(){
        return "{Lat= "+(lat != null ? lat : "null")+", Lng= "+(lng != null ? lng : "null")+"}";
    }
}
