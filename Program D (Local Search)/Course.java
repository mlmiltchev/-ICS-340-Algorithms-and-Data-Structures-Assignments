import java.util.ArrayList;

class Course{
    //Course name.
    private String name;

    //What day this course is currently assigned to.
    private char assignedDay = ' ';

    //What days this course is offered.
    private String fallSchedule;
    private String springSchedule;
    private String summerSchedule;

    //This course is a prerequisite and co-requisite to the following courses.
    private ArrayList<String> prereqs = new ArrayList<>();
    private ArrayList<String> coreqs = new ArrayList<>();

    //Constructor
    Course(String name, String fallSchedule, String springSchedule, String summerSchedule) {
        this.name = name;
        this.fallSchedule = fallSchedule;
        this.springSchedule = springSchedule;
        this.summerSchedule = summerSchedule;
    }

    //Empty constructor
    Course() {
    }

    //Get name.
    public String getName() {
        return name;
    }

    //Get assigned day.
    public char getAssignedDay() {
        return assignedDay;
    }

    //Set assigned day.
    public void setAssignedDay(char assignedDay) {
        this.assignedDay = assignedDay;
    }

    //Get the courses that this course is a prerequisite to.
    public ArrayList<String> getPrereqs() {
        return prereqs;
    }

    //Add a course that this course is a prerequisite to.
    public void addPrereq(String prereq) {
        prereqs.add(prereq);
    }

    //Get the courses that this course is a co-requisite to.
    public ArrayList<String> getCoreqs() {
        return coreqs;
    }

    //Add a course that this course is a co-requisite to.
    public void addCoreq(String coreq) {
        coreqs.add(coreq);
    }

    //Get the correct schedule for the semester.
    public String getSchedule(char term){
        switch(term){
            case 's': return springSchedule;
            case 'f': return fallSchedule;
            case 'm': return summerSchedule;
        }
        return null;
    }
}

class Semester {
    //Semester name.
    private int name;

    //Semester term. (Spring, Summer, Fall)
    private char term;

    //Courses assigned to this semester.
    private ArrayList<Course> courses = new ArrayList<>();

    //Constructor.
    Semester(int name) {
        this.name = name;
        switch(Integer.parseInt(Integer.toString(name).substring(4))){
            case 1: term = 'm'; break;
            case 3: term = 'f'; break;
            case 5: term = 's'; break;
        }
    }

    //Removes course's previously assigned day and adds course to semester.
    public void addCourse(Course course) {
        course.setAssignedDay(' ');
        courses.add(course);
    }

    //Swap two courses in an ArrayList of Semester
    public static ArrayList<Semester> swapCourses(ArrayList<Semester> semesters, Course courseOne, Course courseTwo){
        int semesterOne = 0;
        int semesterTwo = 0;

        for (int i = 0; i < semesters.size(); i++) {
            if(semesters.get(i).getCourses().contains(courseOne)){
                semesterOne = i;
            }

            if(semesters.get(i).getCourses().contains(courseTwo)){
                semesterTwo = i;
            }
        }

        semesters.get(semesterOne).getCourses().remove(courseOne);
        semesters.get(semesterOne).addCourse(courseTwo);
        semesters.get(semesterTwo).getCourses().remove(courseTwo);
        semesters.get(semesterTwo).addCourse(courseOne);

        Semester.assignDaysToCourses(semesters);

        return semesters;
    }

    //Returns true if this semester has no more room for courses
    public boolean isFull(){
        if(term == 'm'){
            return courses.size() >= 2;
        }
        return courses.size() >= 3;

    }

    //Get courses assigned to this semester.
    public ArrayList<Course> getCourses() {
        return courses;
    }

    //Prints results.
    public static void printResults(ArrayList<Semester> semesters){
        for (Semester semester : semesters) {
            System.out.println(semester.toString());
        }
        System.out.println();
    }

    //Wrapper.
    public static void assignDaysToCourses(ArrayList<Semester> semesters){
        for (Semester semester : semesters) {
            semester.assignDaysToCourses();
        }
    }

    //Attempts to assign correct, non-overlapping days to each course in the semester.
    private void assignDaysToCourses(){
        ArrayList<Character> disallowedDays = new ArrayList<>();
        for (Course course : courses) {
            if(course.getSchedule(term).endsWith("O")){
                course.setAssignedDay('O');
            }else {
                for (int i = 0; i < course.getSchedule(term).length(); i++) {
                    if(!disallowedDays.contains(course.getSchedule(term).charAt(i))){
                        course.setAssignedDay(course.getSchedule(term).charAt(i));
                        disallowedDays.add(course.getSchedule(term).charAt(i));
                    }
                }
            }
        }
    }

    //To: String
    //From: Semester object
    public String toString() {
        StringBuilder outputText = new StringBuilder();

        outputText.append(name).append(":\t");
        for (int i = 0; i < courses.size(); i++) {
            outputText.append(courses.get(i).getName()).append("-");
            outputText.append(courses.get(i).getAssignedDay()).append("\t");
        }

        return outputText.toString();
    }
}