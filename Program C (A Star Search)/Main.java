import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    //My first JavaFX application
    //Resources used:
    //Java A Beginner's Guide Sixth Edition by Herbert Schildt
    //https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm
    public void start(Stage stage){
        //Give the stage a title
        stage.setTitle("Program C");

        //FlowPane for root node
        FlowPane rootNode = new FlowPane(10, 10);

        //Center the controls in the scene
        rootNode.setAlignment(Pos.CENTER);

        //Create scene
        Scene scene = new Scene(rootNode, 300, 100);

        //set scene in stage
        stage.setScene(scene);

        //Create comboboxes (dropdown menu)
        ObservableList<String> comboOptionsOne = FXCollections.observableArrayList();
        ObservableList<String> comboOptionsTwo = FXCollections.observableArrayList();

        //Generate graph of nodes. Each node is a city.
        ArrayList<Node> nodes = Node.buildGraph();

        //Assign names to comboboxes
        for (Node n : nodes) {
            comboOptionsOne.add(n.getLongName());
            comboOptionsTwo.add(n.getLongName());
        }

        ComboBox comboOne = new ComboBox(comboOptionsOne);
        ComboBox comboTwo = new ComboBox(comboOptionsTwo);

        //Create button
        Button button = new Button("Go");

        //Handle button event
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //Record the user selected cities
                Node start = new Node();
                Node end = new Node();
                //If the user has not selected a city, do not continue.
                if(!(comboOne.getValue() == null || comboTwo.getValue() == null)){
                    for (Node n : nodes) {
                        if(n.getLongName().equals(comboOne.getValue().toString())){
                            start = n;
                        }
                        if(n.getLongName().equals(comboTwo.getValue().toString())){
                            end = n;
                        }
                    }
                    //Begin the search and close the window
                    AStar.startSearch(start, end, nodes);
                    stage.close();
                }
            }
        });

        //Add to stage
        rootNode.getChildren().addAll(comboOne, comboTwo, button);

        //Show window
        stage.show();
    }
}