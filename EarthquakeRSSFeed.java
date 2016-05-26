/**
 * This program am adaptation of an online tutorial on WebServices by Michel Fudge. 
 * This mini project helped me understand the process of parsing of XML files in Java.
 */
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
/**
 * EarthquakeRSSFeed is the main class that connects to the url of the atom feed and extracts the XML Document. 
 * We then look for appropriate tags, pick up the enclosed values and make objects of Quake class.
 * We then add the Quake object to an arrayList. This arrayList is then queried as per user choice 
 * in the following ways :
 * 1. By Intensity Range
 * 2. By Location (i.e, latitude and longitude)
 * 3. Show all records
 * @author Bhaskar
 */
public class EarthquakeRSSFeed {

    public static void main(String[] args) throws IOException {

         System.out.println("The data for this program is extracted from an RSS feed of the USGS Earthquake \nHazards Program, which is a is part of the National Earthquake Hazards Reduction Program (NEHRP),\nestablished by Congress in 1977. It monitors and reports earthquakes, assess earthquake impacts and hazards, \nand research the causes and effects of earthquakes.");
         ArrayList<Quake> earthquakes = extractQuakes("http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.atom");
        while (true) {
            System.out.println("Enter your choice :");
            System.out.println("To search by Intensity Range.......Enter INT");
            System.out.println("To search by Location..............Enter LOC");
            System.out.println("To show all records..............Enter ALL");
            System.out.println("Exit............................Enter EXIT");

            BufferedReader y = new BufferedReader(new InputStreamReader(System.in));
            String choice = y.readLine().trim();
            SimpleDateFormat df = new SimpleDateFormat("HH:MM");
            switch (choice.toUpperCase()) {
                case "INT":
                    System.out.println("Enter min value > 2.5");
                    String min = y.readLine().trim();
                    System.out.println("Enter max value");
                    String max = y.readLine().trim();
                    System.out.printf("%s\t%-25s\t%s\t%s\n", "TIME", "LAT / LONG", "MAGNITUDE", "DETAILS");
                    
                    if((Integer.parseInt(max) - Integer.parseInt(min))>7.5)
                    {
                        System.out.println("Not inside valid range");
                        continue;
                    }
                    int flag = 0;
                    for (Quake q : earthquakes) {
                        if (q.getMagnitude() >= Double.parseDouble(min) && q.getMagnitude() <= Double.parseDouble(max)) {
                            String quakeLoc = "Lat: " + q.getLocation()[0] + " Long: " + q.getLocation()[1];
                            System.out.printf("%s\t%-25s\t%f\t%s\n", df.format(q.getDate()), quakeLoc, q.getMagnitude(), q.getDetails());
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        System.out.println("No quake in that range recorded today");
                    }
                    break;

                case "LOC":
                    System.out.println("Enter latitude");
                    String lat = y.readLine().trim();
                    System.out.println("Enter longitude");
                    String longt = y.readLine().trim();
                    System.out.printf("%s\t%-25s\t%s\t%s\n", "TIME", "LAT / LONG", "MAGNITUDE", "DETAILS");
                    int flag1 = 0;
                    for (Quake q : earthquakes) {
                        if (q.getLocation()[0].equals(lat) && q.getLocation()[1].equals(longt)) {
                            String quakeLoc = "Lat: " + q.getLocation()[0] + " Long: " + q.getLocation()[1];
                            System.out.printf("%s\t%-25s\t%f\t%s\n", df.format(q.getDate()), quakeLoc, q.getMagnitude(), q.getDetails());
                        }
                    }
                                   if (flag1 == 0) {
                        System.out.println("No quake >2.5 recorded in that place today");
                    }
     
                    break;
                case "ALL":
                    System.out.printf("%s\t%-25s\t%s\t%s\n", "TIME", "LAT / LONG", "MAGNITUDE", "DETAILS");
                    for (Quake q : earthquakes) {
                        String quakeLoc = "Lat: " + q.getLocation()[0] + " Long: " + q.getLocation()[1];
                        System.out.printf("%s\t%-25s\t%f\t%s\n", df.format(q.getDate()), quakeLoc, q.getMagnitude(), q.getDetails());
                    }

                    break;
                case "EXIT":
                    System.out.println("Thank You !");
                    System.exit(0);
                default :
                    System.out.println("Wrong choice... try again");
            }
        }
    }
/**
 * What we do in extractQuakes
 * 1. Make HTTP Web request to Earthquake rss feed
 * 2. Parse the text response stream as XML
 * 3. Loop over the XML to get the data we need
 * 4. Create Quake objects and add them to a dynamic ArrayList
 * @param url
 * @return ArrayList<Quake>
 */
    public static ArrayList<Quake> extractQuakes(String url) {
        ArrayList<Quake> quakes = new ArrayList<Quake>();
        try {
            URL eqcenterURL = new URL(url);
            URLConnection connection = eqcenterURL.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.parse(in);
                Element docElement = dom.getDocumentElement();
                NodeList nodeList=dom.getElementsByTagName("*");
                Element element = (Element)nodeList.item(2);
                String lastUpdated = element.getFirstChild().getNodeValue();
                SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                Date qdate = new Date();
                System.out.println("Records on the rss feed last updated on "+lastUpdated.split("T")[0].trim()+" "+ lastUpdated.split("T")[1].split("Z")[0].trim());
                NodeList nl = docElement.getElementsByTagName("entry");
                if (nl != null && nl.getLength() > 0) {
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element entry = (Element) nl.item(i);
                        Element title = (Element) entry.getElementsByTagName("title").item(0);
                        Element georss = (Element) entry.getElementsByTagName("georss:point").item(0);
                        Element when = (Element) entry.getElementsByTagName("updated").item(0);
                        Element link = (Element) entry.getElementsByTagName("link").item(0);
                        String details = title.getFirstChild().getNodeValue();
                        String hostname = "http://earthquake.usgs.gov";
                        String linkString = hostname + link.getAttribute("href");
                        String point = georss.getFirstChild().getNodeValue();
                        String dt = when.getFirstChild().getNodeValue();
                        qdate = new Date();
                        qdate = sdformat.parse(dt);
                        String[] locationPair = point.split(" ");
                        String location = "Lat: " + locationPair[0] + " Lng: " + locationPair[1];
                        String magnitudeString = details.split(" ")[1];
                        double magnitude = Double.parseDouble(magnitudeString);
                        details = details.split("-")[1].trim();
                        quakes.add(new Quake(qdate, details, locationPair, magnitude, linkString));
                    }
                }
            }

        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (ParserConfigurationException ex) {
            System.out.println(ex);
        } catch (SAXException ex) {
            System.out.println(ex);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return quakes;
    }
}
