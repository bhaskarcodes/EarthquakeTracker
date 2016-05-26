# EarthquakeTracker

The data for this program is extracted from an RSS feed of the USGS Earthquake  Hazards Program, which is a is part of the National Earthquake Hazards Reduction Program (NEHRP), established by Congress in 1977. It monitors and reports earthquakes, assess earthquake impacts and hazards, and research the causes and effects of earthquakes.  

Source : http://earthquake.usgs.gov/  

<b>Structure of Program :</b>  
EarthquakeRSSFeed is the main class that connects to the url of the atom feed and extracts the XML Document. We then look for appropriate tags, pick up the enclosed values and make objects of Quake class, which is a class we use to organize information about earthquakes properly. We then add the Quake object to an arrayList. This arrayList is then queried as per user choice in the following ways :   

1. By Intensity Range
2. By Location (i.e, latitude and longitude)
3. Show all records  

<b>Steps Followed : </b>   
1. Make HTTP Web request to Earthquake rss feed  
2. Parse the text response stream as XML  
3. Loop over the XML to get the data we need  
4. Create Quake objects and add them to a dynamic ArrayList  
