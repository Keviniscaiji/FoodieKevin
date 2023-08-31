package items;

public class Comments {
    public int comment_id;
    public String location_name;
    public String username;
    public String comment;
    public String title;
    public float rate;

    public Comments(int comment_id,String ln,String un, String c, String t, float r){
        this.comment_id = comment_id;
        location_name = ln;
        title = t;
        username  = un;
        comment = c;
        rate = r;
    }
}
