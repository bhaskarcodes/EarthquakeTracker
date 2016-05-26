import java.util.Date;
/**
 * This class helps us to keep all info about a Quake in an organized manner..
 * @author Bhaskar
 */
public class Quake {
    private Date date;
    private String details;
    private String[]  location;
    private double magnitude;
    private String link;
    
    Quake(Date date, String details, String[] location, double magnitude, String link){
        this.date = date;
        this.details = details;
        this.location = location;
        this.magnitude= magnitude;
        this.link = link;        
    }
} 
