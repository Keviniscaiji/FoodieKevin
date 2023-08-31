package items;

public class LocationsInfo implements Comparable<LocationsInfo>{
    String location_id,location_name;
    float rate;
    double longitude,latitude;
    public LocationsInfo(String location_id,String location_name,float rate, double longitude, double latitude){
        this.location_id = location_id;
        this.location_name = location_name;
        this.rate = rate;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public String getLocationName(){
        return location_name;
    }
    public String getLocationId(){
        return location_id;
    }
    public double getLocationLatitude(){
        return latitude;
    }
    public double getLocationLongitude(){
        return longitude;
    }
    public float getRate(){
        return rate;
    }
    @Override
    public int compareTo(LocationsInfo o) {
        if(this.rate>o.rate){
            return -1;
        }else{
            return 1;
        }
    }
}
