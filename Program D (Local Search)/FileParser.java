import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileParser {
    //File that contains courses.
    static String coursesFile = "classes.txt";

    //File that contains course constraints.
    static String constraintsFile = "constraints.txt";

    //Read courses file.
    public static ArrayList<Course> getCourses() {
        String readLine = "";
        ArrayList<String> arr = new ArrayList<>();
        ArrayList<Course> courses = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(coursesFile))) {
            bf.readLine();
            while((readLine = bf.readLine()) != null){
                String[] split = readLine.split(" ");
                arr.addAll(Arrays.asList(split));
            }

            //Remove whitespace
            arr.removeAll(Arrays.asList(null, ""));

            //Build an ArrayList of Courses and their schedules
            for (int i = 0; i < arr.size(); i += 4) {
                courses.add(new Course(arr.get(i), arr.get(i + 1), arr.get(i + 2), arr.get(i + 3)));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

    //Read constraints file and add constraints to courses.
    public static ArrayList<Course> getConstraints(ArrayList<Course> courses){
        String readLine = "";
        ArrayList<String> arr = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(constraintsFile))) {
            while((readLine = bf.readLine()) != null){
                String[] split = readLine.split(" ");
                arr.addAll(Arrays.asList(split));
            }

            //Remove whitespace
            arr.removeAll(Arrays.asList(null, ""));

            //Update courses ArrayList with pre and co requisites
            for (Course course : courses) {
                for (int i = 0; i < arr.size(); i += 3) {
                    if(course.getName().equals(arr.get(i))){
                        if(arr.get(i + 1).equals("<")){
                            course.addPrereq(arr.get(i + 2));
                        }else{
                            course.addCoreq(arr.get(i + 2));
                        }
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return courses;
    }
}