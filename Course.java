/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaFinalProject;

import java.util.ArrayList;

/**
 *
 * @author cfurm
 */
public class Course {
    //attributes
    private ArrayList<Course> prerequisites;
    private int creditHours = 0; //initializg to 0 is important to not break things that use this expecting a value
    private String name;
    private int colorCode = 0; //the plan is that the rendering GUI reads this and adjusts the background using a switch. (i.e 1 = red, 2 = purple, 0 = white (default))
    
    /**
     * parent can have a "master record" of every course
        if a course has an existing prerequisite, search the master list for the prerequisite course
        then put that in the array. This is the caller's responsibility.
     * @param name
     * @param creditHours
     */
    public Course(String name, String creditHours) {
        this.name = name;
        this.creditHours = Integer.parseInt(creditHours);
    }
    public Course(String name) {
        this.name = name;
        this.colorCode = 1; //1 should be the "Liberty University does not offer this course but you can get it elsewhere" color
    }
    
    public void addPrerequisite(Course c) {
        if (prerequisites == null) {
            prerequisites = new ArrayList<Course>();
        }
        prerequisites.add(c);
    }
    public int getColorCode() {
        return colorCode;
    }
    public String getName() {
        return name;
    }
    public int getHours() {
        return creditHours;
    }
    public String getPrerequisites() {//this may need to get changed to returning Course later but it's fine for now
        String response = "";
            if (!(prerequisites == null)) {
            for(Course c : prerequisites) {
                response += (", " + c.getName());
            }
            }
        return response;
    }
    public void setColorcode(int i) {
        colorCode = i;
    }
}
