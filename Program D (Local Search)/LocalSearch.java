import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LocalSearch {
    //Initialize the schedule
    public static ArrayList<Semester> initializeSchedule(ArrayList<Semester> semesters, ArrayList<Course> courses){
        //Randomly assign days to courses
        Random random = new Random();
        for (int course = 0; course < 30; course++) {
            int semester = random.nextInt(11);
            while(semesters.get(semester).isFull()) {
                semester = random.nextInt(11);
            }
            semesters.get(semester).addCourse(courses.get(course));
        }

        //Attempt to assign proper days to courses
        Semester.assignDaysToCourses(semesters);

        return semesters;
    }

    //generate a HashMap that maps the name of a course to the number of times it breaks a constraint
    public static HashMap<String, Integer> generateConflicts(ArrayList<Semester> semesters){
        ArrayList<String> visitedCourses = new ArrayList<>();
        HashMap<String, Integer> coursesWithConflicts = new HashMap<>();

        for (int i = 0; i < semesters.size(); i++) {
            for (int j = 0; j < semesters.get(i).getCourses().size(); j++) {
                //if no courses have been visited yet
                if(visitedCourses.isEmpty()){
                    visitedCourses.add(semesters.get(i).getCourses().get(j).getName());
                    continue;
                }

                //if the course is not a pre or co requisite to another course
                if(semesters.get(i).getCourses().get(j).getPrereqs().isEmpty() &&
                        semesters.get(i).getCourses().get(j).getCoreqs().isEmpty()){
                    visitedCourses.add(semesters.get(i).getCourses().get(j).getName());
                }

                //if the course is a prerequisite to other courses
                if(!semesters.get(i).getCourses().get(j).getPrereqs().isEmpty()){
                    coursesWithConflicts = conflictCounter(visitedCourses, semesters.get(i).getCourses().get(j), coursesWithConflicts);
                    visitedCourses.add(semesters.get(i).getCourses().get(j).getName());
                }

                //if the course is a co-requisite to other courses
                if(!semesters.get(i).getCourses().get(j).getCoreqs().isEmpty()){
                    ArrayList<String> sameSemesterCourses = new ArrayList<>();
                    for (int k = 0; k < semesters.get(i).getCourses().size(); k++) {
                        sameSemesterCourses.add(semesters.get(i).getCourses().get(k).getName());
                    }
                    coursesWithConflicts = conflictCounter(visitedCourses, semesters.get(i).getCourses().get(j), coursesWithConflicts, sameSemesterCourses);
                    visitedCourses.add(semesters.get(i).getCourses().get(j).getName());
                }

                //if the course is not assigned a correct day
                if(semesters.get(i).getCourses().get(j).getAssignedDay() == ' ' || semesters.get(i).getCourses().get(j).getAssignedDay() == '-'){
                    if(coursesWithConflicts.containsKey(semesters.get(i).getCourses().get(j).getName())){
                        coursesWithConflicts.put(semesters.get(i).getCourses().get(j).getName(), coursesWithConflicts.get(semesters.get(i).getCourses().get(j).getName()) + 1);
                    }else{
                        coursesWithConflicts.put(semesters.get(i).getCourses().get(j).getName(), 1);
                    }
                }
            }
        }
        return coursesWithConflicts;
    }

    //Generate a proper schedule
    //Implements a min-conflicts local search
    public static void generateSchedule(HashMap<String, Integer> coursesWithConflicts, ArrayList<Semester> semesters){
        //print initial schedule
        Semester.printResults(semesters);

        //Create random object for later use
        Random random = new Random();

        //Initialize numberOfConflicts to the correct number of conflicts
        int numberOfConflicts = 0;
        ArrayList<String> courseNames = new ArrayList<>(coursesWithConflicts.keySet());
        for (String courseName : courseNames) {
            numberOfConflicts += coursesWithConflicts.get(courseName);
        }

        //While conflicts remain
        while(numberOfConflicts > 0){
            //Choose a conflicted course object randomly
            Course conflictedCourse = new Course();
            int randomSemester = random.nextInt(semesters.size());
            int randomCourse = random.nextInt(semesters.get(randomSemester).getCourses().size());

            //5% chance to choose a random course rather than a conflicted one
            int randomDraw = random.nextInt(100);
            if(randomDraw < 5){
                conflictedCourse = semesters.get(randomSemester).getCourses().get(randomCourse);
            }else{
                String conflictName = courseNames.get(random.nextInt(courseNames.size()));
                for (int i = 0; i < semesters.size(); i++) {
                    for (int j = 0; j < semesters.get(i).getCourses().size(); j++) {
                        if(semesters.get(i).getCourses().get(j).getName().equals(conflictName)){
                            conflictedCourse = semesters.get(i).getCourses().get(j);
                        }
                    }
                }
            }

            //Select a random course to swap the conflicted course with
            randomSemester = random.nextInt(semesters.size());
            randomCourse = random.nextInt(semesters.get(randomSemester).getCourses().size());
            Course swappedCourse = semesters.get(randomSemester).getCourses().get(randomCourse);

            //Swap courses
            semesters = Semester.swapCourses(semesters, conflictedCourse, swappedCourse);


            //Check to see if the swap was an improvement, if so, update relevant variables, else, undo the swap.
            HashMap<String, Integer> newCoursesWithConflicts = LocalSearch.generateConflicts(semesters);
            ArrayList<String> newCourseNames = new ArrayList<>(newCoursesWithConflicts.keySet());
            int newNumberOfConflicts = 0;

            for (String courseName : newCourseNames) {
                newNumberOfConflicts += newCoursesWithConflicts.get(courseName);
            }

            if (newNumberOfConflicts <= numberOfConflicts){
                coursesWithConflicts = newCoursesWithConflicts;
                courseNames = newCourseNames;
                numberOfConflicts = newNumberOfConflicts;
                Semester.printResults(semesters);

            }else{
                semesters = Semester.swapCourses(semesters, conflictedCourse, swappedCourse);
            }
        }
        System.out.println("\nDone!");
    }

    //Count the prerequisite conflicts
    private static HashMap<String, Integer> conflictCounter(ArrayList<String> visitedCourses, Course course, HashMap<String, Integer> coursesWithConflicts){
        for (String requisite : course.getPrereqs()) {
            if(visitedCourses.contains(requisite)){
                if(!coursesWithConflicts.containsKey(course.getName())){
                    coursesWithConflicts.put(course.getName(), 1);
                }else{
                    coursesWithConflicts.put(course.getName(), coursesWithConflicts.get(course.getName()) + 1);
                }
                if(!coursesWithConflicts.containsKey(requisite)){
                    coursesWithConflicts.put(requisite, 1);
                }else{
                    coursesWithConflicts.put(requisite, coursesWithConflicts.get(requisite) + 1);
                }
            }
        }

        return coursesWithConflicts;
    }

    //Count the co-requisite conflicts
    private static HashMap<String, Integer> conflictCounter(ArrayList<String> visitedCourses,  Course course, HashMap<String, Integer> coursesWithConflicts, ArrayList<String> sameSemesterCourses){
        for (String requisite : course.getCoreqs()) {
            if(visitedCourses.contains(requisite) && !sameSemesterCourses.contains(requisite)){
                if(!coursesWithConflicts.containsKey(course.getName())){
                    coursesWithConflicts.put(course.getName(), 1);
                }else{
                    coursesWithConflicts.put(course.getName(), coursesWithConflicts.get(course.getName()) + 1);
                }
                if(!coursesWithConflicts.containsKey(requisite)){
                    coursesWithConflicts.put(requisite, 1);
                }else{
                    coursesWithConflicts.put(requisite, coursesWithConflicts.get(requisite) + 1);
                }
            }
        }
        return coursesWithConflicts;
    }
}
