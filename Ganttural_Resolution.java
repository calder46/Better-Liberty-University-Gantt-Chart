package JavaFinalProject;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author cfurm
 */
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class I_fucking_hate_ads {
/*
basic plan: Liberty University has certain course requirements for each major. It's registration website does not contain
a simple visual representation of course precidence. I want to make a mapping tool to help students plan their college courses.
To do this, we'll get a pdf of course requirements from the liberty site, then read the restrictions from the site HTML
    or get the user to manually input them.
    the Gnatt chart will be visualized in a GUI for ovbious reasons. Since a user may desire it, the following features 
    should be implemented: 
    1. the ability to 'check off' completed or bypassed courses. 
    2. a timeline of how soon graduation can be pursued, based on course precidence and an adjustable credits per year slider. 
    (this could also be editable on a per-year basis).
    3. the ability to save and load serializable files so users may easily retain information.
    4. as a stretch goal, the ability to add more majors to the course selection.
    5. verify that the user is attempting to access liberty's website before getting data to prevent security risks (throw exception if input url is invalid)
    
    identified hurdle: the HTML containing prerequisite info doesn't exist till you click on the link
    some way to do this automatically could work
 */
    
    public static ArrayList<Course> courses = new ArrayList();
    
    // Gemini: Added this method so the DashboardController can trigger the scrape using the URL from the TextField
    public static void runScraper(String urlString) {
        try {
            courses.clear(); // Gemini: Clear list so we don't double-up on multiple loads
            URL url = new URL(urlString);
            InputStream is = url.openStream();
            processScrape(is); // Gemini: Calls the original logic moved into processScrape
        } catch (Exception e) {
            System.err.println("Gemini Error: Scraper failed - " + e.getMessage());
        }
    }

    // Gemini: Moved your original main logic here so it can be called by both main() and the GUI
    public static void processScrape(InputStream is) throws IOException {
        int ptr = 0;
        StringBuffer buffer = new StringBuffer();
        while ((ptr = is.read()) != -1) {
            buffer.append((char)ptr);
        }

        /**
         * Inital data loading
         */
        //resource initialization
        Pattern findStrongdiv = Pattern.compile("<strong>.+?</strong>");
        Pattern findPrereq = Pattern.compile("<div class=\"cou.+?Prerequisite.+?</div>");
        Pattern courseName = Pattern.compile("title=\"[A-Z]{4}..\\d{3}");
        
        /**
         * Subdata loading
         */
        //get data from each of the sublinks, to see if any course has prerequisites
        Matcher subCatOpener = Pattern.compile("\"/search/.+?\"").matcher(buffer); //this finds prerequisites
        while (subCatOpener.find()) {
            String nextPR = subCatOpener.group();
            URL catalogue = new URL("https://catalog.liberty.edu/" + nextPR.replace('"', ' ').strip());
            
            InputStream query = catalogue.openStream();
            StringBuffer loader = new StringBuffer();
            while ((ptr = query.read()) != -1) {
                loader.append((char)ptr);
            }

            //get text from catalogue page
            String catalogueText = loader.toString();
            
            //create corresponding course 
            //get course title description index 
            int titleInd = catalogueText.indexOf("<div class=\"cols noindent\">");
            //initiate quickstop flag
            boolean quickstop = false;
            if (titleInd != -1) {
                String title = catalogueText.substring(titleInd, titleInd + 12048); //this seems large enough
                //create matcher to extract content from strong text (bolded sections)
                Matcher strongExtractor = findStrongdiv.matcher(title);
                
                //find name 
                strongExtractor.find(); //get next match for <strong>.+?</strong>
                String name = strongExtractor.group(); //return match 
                name = name.substring(8, name.length()-9); //remove extra locator bits [<strong>,</strong>]
                
                //check if course already exists
                for(Course c: courses) {
                    if (c.getName().equals(name)) {
                        strongExtractor.find(); strongExtractor.find(); //skip the next 2
                        quickstop = true;
                    }
                }
                if (quickstop) {
                    //don't do more than you need to
                } else {
                    
                    //find credit hours
                    strongExtractor.find(); //skip past proper name section we don't need it
                    strongExtractor.find(); //get next match
                    String creditHours = strongExtractor.group(); //return match
                    creditHours = creditHours.replaceAll("\\D", ""); //remove anything that isn't a digit ("3 Credit Hour(s)" -> "3")

                    courses.add(new Course(name, creditHours));

                    //find prerequisites
                    Matcher prereqExtractor = findPrereq.matcher(catalogueText);
                    if (prereqExtractor.find()){ //if a prerequisite is found
                        //prep a matcher to scan the prerequisites, based on the match from prereqextractor
                        Matcher courseExtractor = courseName.matcher(prereqExtractor.group());

                        /**
                         * Course addition block
                         */
                        while (courseExtractor.find()) {//get prerequisite course name, format: "title=\"[A-Z]{4} \\d{3}".
                            String namelocal = courseExtractor.group().substring(7); //7 converts [title="ESOL 100] -> [ESOL 100]
                            namelocal = namelocal.substring(0, 4) + " " + namelocal.substring(6); //removes HTML 'à' artifact
                            isREALcourse(namelocal); 
                        } //repeat until no more course names are given.
                    } 
                }   
            } else {            
                System.out.println(" No Prerequisites for this course");
            }
            // Gemini: Progress print kept for console visibility during GUI run
            System.out.printf("\n Progress: %.3f", ((float) subCatOpener.end()/buffer.length()*100));
        }
        
        // Gemini: Test data generation kept from your original code
        Course c = new Course("TEST 001", "5");
        if(courses.size() > 10) {
            c.addPrerequisite(courses.get(1));
            c.addPrerequisite(courses.get(10));
        }
        courses.add(c);
        printAllCourses();
    }

    public static void main(String[] args) throws MalformedURLException, IOException {
        // Gemini: Commented out Scanner/Console input as this is now handled by the Dashboard GUI
        /*
        Scanner userInput = new Scanner(System.in); 
        System.out.print("Please copy-paste liberty's URL for the course plan:");
        String theURL = userInput.next();
        URL url = new URL(theURL);
        InputStream is = url.openStream();
        processScrape(is);
        */
        runScraper("https://catalog.liberty.edu/undergraduate/colleges-schools/business/information-technology-major-bs/application-database-development-resident/index.html?_gl=1*1vvwump*_ga*OTI5NDU1MDMxLjE3NTk5ODMzNTU.*_ga_YK8WH8QD6R*czE3NzY2NzU1MDUkbzIxMSRnMSR0MTc3NjY3NzIyNyRqNDIkbDAkaDA.*_ga_T1DG1LRPCP*czE3NzY2NzU1MTEkbzY2JGcxJHQxNzc2Njc3MjI3JGo0NiRsMCRoMA..#coursesequencetext");
        
    }

    private static void printAllCourses() {
        for (Course c : courses) {
            System.out.print(c.getName()); 
            if (c.getHours() == 0) {
                System.out.print(", external.");
            } else {
                System.out.printf(", credit hours: %d", c.getHours());
            }
            System.out.printf("%s%n", c.getPrerequisites());
        }
    }

    private static void isREALcourse (String s) { 
    int index = -1;//this boolean tracks [if a course exists already, add it as a prerequisite]
        //scan courses for match
        for (int i = 0; i < courses.size(); i++) {//read from the entire list of courses to see if there's any match
            if (courses.get(i).getName().equals(s)) {
                index = i; break;   
            }
        }
        //add new course as appropriate
        if (index != -1) {
            //add existing course as prerequisiste
            courses.get(courses.size() - 1).addPrerequisite(courses.get(index));
        } else {
            //add new course as prerequisite
            courses.add(courses.size() - 1, new Course(s));
        }
    }
}
