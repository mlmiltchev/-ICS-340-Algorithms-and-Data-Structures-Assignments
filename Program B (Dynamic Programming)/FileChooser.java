import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

class FileChooser{
    private String fileName;

    //Open the JFileChooser window in the cwd
    public FileChooser(){
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
        jfc.showOpenDialog(null);
        fileName = jfc.getName(jfc.getSelectedFile());
    }

    //Read the selected file line by line
    public ArrayList<String[]> readFile(){
        String readLine = "";
        ArrayList<String[]> arr = new ArrayList<>();

        //Read file line by line
        //Save the last string in each line to the arr ArrayList and return
        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            while((readLine = bf.readLine()) != null){
                String[] split = readLine.split(" ");
                arr.add(split);
            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        return arr;
    }
}