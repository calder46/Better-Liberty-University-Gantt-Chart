# Better-Liberty-University-Gnatt-Chart

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
    
   # identified hurdle: the HTML containing prerequisite info doesn't exist till you click on the link
   some way to do this automatically could work.
