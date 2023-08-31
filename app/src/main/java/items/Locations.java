package items;

public class Locations {
//    create location entity
//    [Loaction_id, username, location_name, location]
    public int id;
    public String location_name;
    public String location;
    public Locations(int id, String location_name, String location){
        this.id = id;
        this.location_name = location_name;
        this.location = location;
    }
}
