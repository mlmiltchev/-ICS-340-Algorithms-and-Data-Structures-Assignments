import java.util.ArrayList;
import java.util.Collections;

/**
 * Box --- Abstraction of a box
 */
class Box implements Comparable<Box>{
    private String name;
    private int height, width, depth, weight;

    /**
     * Constructor for Box class.
     * @param arr Box in String array form.
     */
    private Box(String[] arr) {
        name = arr[0];
        height = Integer.parseInt(arr[1]);
        width = Integer.parseInt(arr[2]);
        depth = Integer.parseInt(arr[3]);
        weight = Integer.parseInt(arr[4]);
    }

    /**
     * Gets a file filled with boxes, one on each line. (ex. "Alpha 4 3 7 34")
     * Calls generateBoxes() and getHeaviestStack()
     */
    public static void getResults(){
        FileChooser fc = new FileChooser();

        ArrayList<Box> listOfBoxes = Box.generateBoxes(fc.readFile());
        int length = listOfBoxes.size();

        Box.getHeaviestStack(listOfBoxes, length);
    }

    /**
     * Generates an ArrayList of type Box from an ArrayList of type String
     * and sorts the resulting ArrayList.
     * @param arr ArrayList of boxes in String format (ex. "Alpha 4 3 7 34")
     * @return ArrayList of type Box.
     */
    private static ArrayList<Box> generateBoxes(ArrayList<String[]> arr){
        ArrayList<Box> result = new ArrayList<>();
        for (String[] box : arr) {
            result.add(new Box(box));
        }

        Collections.sort(result);

        return result;
    }

    /**
     * Generates the heaviest stack of boxes where any box with a box below it
     * has <= width/depth and weighs less than the box below it.
     * Uses tabulation (Bottom Up dynamic programming).
     * @param listOfBoxes ArrayList of all boxes as Box objects.
     * @param n Length of listOfBoxes ArrayList.
     */
    private static void getHeaviestStack(ArrayList<Box> listOfBoxes, int n){
        int[] weightArr = new int[n];       //Stores the weight of any given stack
        int[] heightArr = new int[n];       //Stores the height of any given stack
        int[] stackOrderArr = new int[n];   //Stores the final order of boxes

        //Initialize arrays
        for (int i = 0; i < n; i++) {
            weightArr[i] = listOfBoxes.get(i).weight;
            heightArr[i] = listOfBoxes.get(i).height;
            stackOrderArr[i] = i;
        }

        //Check the short document included with this program.
        for(int i = 1; i < n; i++){
            for(int j = 0; j < i; j++){
                if(((listOfBoxes.get(i).width <= listOfBoxes.get(j).width && listOfBoxes.get(i).depth <= listOfBoxes.get(j).depth)
                        || (listOfBoxes.get(i).width <= listOfBoxes.get(j).depth && listOfBoxes.get(i).depth <= listOfBoxes.get(j).width))
                        && (listOfBoxes.get(i).weight <= listOfBoxes.get(j).weight)
                        && (weightArr[i] < weightArr[j] + listOfBoxes.get(i).weight)){
                    weightArr[i] = weightArr[j] + listOfBoxes.get(i).weight;
                    heightArr[i] = heightArr[j] + listOfBoxes.get(i).height;
                    stackOrderArr[i] = j;
                }
            }
        }

        //Get the maximum values.
        int[] max = findMaxValues(weightArr, heightArr);

        //Print final information
        printStack(listOfBoxes, stackOrderArr, max);
    }

    /**
     * Identifies the heaviest stack of boxes. Returns an array whose three elements hold the following:
     * [0]: The weight of the heaviest stack.
     * [1]: The height of the heaviest stack.
     * [2]: The position in stackOrderArr that holds the first (top) Box of the stack.
     * @param weightArr Integer array that holds the weights of any given stack calculated by getHeaviestStack()
     * @param heightArr Integer array that holds the heights of any given stack calculated by getHeaviestStack()
     * @return Three element integer array.
     */
    private static int[] findMaxValues(int[] weightArr, int[] heightArr){
        int[] max = new int[3];
        for(int i = 0; i < weightArr.length; i++){
            if(weightArr[i] > max[0]){
                max[0] = weightArr[i];
                max[1] = heightArr[i];
                max[2] = i;
            }
        }

        return max;
    }

    /**
     * Prints the final results including the entire stack (from top to bottom),
     * the weight of the stack, and the height of the stack.
     * @param listOfBoxes ArrayList of all boxes as Box objects.
     * @param stackOrderArr Integer array that holds the order of the heaviest stack.
     * @param max Integer array that contains the heaviest weight, corresponding height,
     *            and position in stackOrderArr that holds the first (top) Box of the stack.
     */
    private static void printStack(ArrayList<Box> listOfBoxes, int[] stackOrderArr, int[] max){
        int position = max[2];
        int i = 0;
        while(i != max[0]) {
            System.out.println(listOfBoxes.get(position));
            i += listOfBoxes.get(position).weight;
            position = stackOrderArr[position];
        }

        System.out.println("\nHeaviest stack weight: " + max[0]);
        System.out.println("Stack height: " + max[1]);
    }

    /**
     * Compares two Box objects by base area.
     * @param box Box object.
     * @return Integer that signifies which Box has a larger base area.
     */
    public int compareTo(Box box){
        return (box.width * box.depth) - (this.width * this.depth);
    }

    /**
     * It's a toString method. Not much to say here.
     * @return String representation of a Box object.
     */
    public String toString() {
        return name + " " + height + " " + width + " " + depth + " " + weight;
    }
}

public class Main {
    public static void main(String[] args){
        Box.getResults();
    }
}