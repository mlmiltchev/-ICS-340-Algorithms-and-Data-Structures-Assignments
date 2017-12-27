import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //Get list of courses with constraints from files.
        ArrayList<Course> courses = FileParser.getCourses();
        courses = FileParser.getConstraints(courses);

        //Create an ArrayList of Semester objects.
        int[] semesterNames = {20193, 20195, 20201, 20203, 20205, 20211, 20213, 20215, 20221, 20223, 20225};
        ArrayList<Semester> semesters = new ArrayList<>();

        for (int semesterName : semesterNames) {
            semesters.add(new Semester(semesterName));
        }

        //Initialize schedule.
        semesters = LocalSearch.initializeSchedule(semesters, courses);

        //Generate conflicts and then do a local search based on results.
        LocalSearch.generateSchedule(LocalSearch.generateConflicts(semesters), semesters);
    }
}
