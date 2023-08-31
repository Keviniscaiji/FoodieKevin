package items;

public class Routes {
    public int id;
    public String title;
    public String route_points;
    public String route_lines;
    public String route_notes;
    public Routes(int id, String title, String route_points, String route_lines, String route_notes){
        this.id = id;
        this.title = title;
        this.route_lines = route_lines;
        this.route_notes = route_notes;
        this.route_points = route_points;
    }
}
