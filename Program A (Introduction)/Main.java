import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

class ProgramA{
    private String fileName;

    //Open the JFileChooser window in the cwd
    public ProgramA(){
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
        jfc.showOpenDialog(null);
        fileName = jfc.getName(jfc.getSelectedFile());
    }

    public void getLastStrings(){
        //Read input file.
        ArrayList<String> results = readFile();

        //Create output file name (append "_out" to the end of the file name)
        // https://stackoverflow.com/questions/7935858/the-split-method-in-java-does-not-work-on-a-dot
        ArrayList<String> outputFileName = new ArrayList<>(Arrays.asList(fileName.split("\\.")));
        outputFileName.add(1, "_out.");

        //Write output file.
        writeFile(results, String.join("", outputFileName));
    }

    //Read the selected file line by line
    private ArrayList<String> readFile(){
        String readLine = "";
        ArrayList<String> arr = new ArrayList<>();

        //Read file line by line
        //Save the last string in each line to the arr ArrayList and return
        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            while((readLine = bf.readLine()) != null){
                String[] split = readLine.split(" ");
                arr.add(split[split.length-1]);
            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        return arr;
    }

    //Write output file
    private void writeFile(ArrayList<String> arr, String fileName){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){
            for (String x : arr){
                bw.write(x);
                bw.newLine();
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ProgramA prog = new ProgramA();
        prog.getLastStrings();
    }
}