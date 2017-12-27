import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileParser {
    //Actual distances file
    static String actualFile = "MnDOTactualDistances-commas.txt";

    //Heuristics file
    static String heuristicFile = "MnDOTheuristicDistances-commas.txt";

    /**
     * Gets short names.
     *
     * @return the short names
     */
    public static ArrayList<String> getShortNames() {
        String readLine = "";
        ArrayList<String> arr = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(actualFile))) {
            readLine = bf.readLine();
            String[] split = readLine.split(",");
            arr.addAll(Arrays.asList(split));
            arr.remove(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return arr;
    }

    /**
     * Gets long names.
     *
     * @return the long names
     */
    public static ArrayList<String> getLongNames() {
        String readLine = "";
        ArrayList<String> arr = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(actualFile))) {
            while ((readLine = bf.readLine()) != null) {
                String[] split = readLine.split(",");
                arr.add(split[0]);
            }
            arr.remove(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return arr;
    }

    /**
     * Gets actual distances.
     *
     * @return the actual distances
     */
    public static ArrayList<String> getActualDistances() {
        String readLine = "";
        ArrayList<String> arr = new ArrayList<>();
        boolean skip = true;
        try (BufferedReader bf = new BufferedReader(new FileReader(actualFile))) {
            while ((readLine = bf.readLine()) != null) {
                if (skip) {
                    skip = false;
                    continue;
                }
                String[] split = readLine.split(",");
                for (int column = 0; column < split.length; column++) {
                    if (column % split.length == 0) {
                        continue;
                    }
                    arr.add(split[column]);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return arr;
    }

    /**
     * Gets heuristic distances.
     *
     * @return the heuristic distances
     */
    public static ArrayList<String> getHeuristicDistances() {
        String readLine = "";
        ArrayList<String> arr = new ArrayList<>();
        boolean skip = true;
        try (BufferedReader bf = new BufferedReader(new FileReader(heuristicFile))) {
            while ((readLine = bf.readLine()) != null) {
                if (skip) {
                    skip = false;
                    continue;
                }
                String[] split = readLine.split(",");
                for (int column = 0; column < split.length; column++) {
                    if (column % split.length == 0) {
                        continue;
                    }
                    arr.add(split[column]);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return arr;
    }

    /**
     * Export finished graph to text file.
     *
     * @param fileName the file name
     * @param fileData the file data
     */
    public static void exportToFile( String fileName, String fileData){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){
            bw.write(fileData);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}