package gui;

import company.Branch;
import encryption.RSA;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import parser.InputHandler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;

public class GUI extends Application {
    private InputHandler handler = new InputHandler();

    public void start(Stage primaryStage) {
        primaryStage.setTitle("MSA | Mosbach Security Agency");

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");

        Button executeButton = new Button("Execute");
        executeButton.setPrefSize(100, 20);

        Button closeButton = new Button("Close");
        closeButton.setPrefSize(100, 20);





        TextArea commandLineArea = new TextArea();
        commandLineArea.setWrapText(true);

        TextArea outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // next three lines are just for demo creating key files
                /*RSA rsa = new RSA();
                Branch branch = new Branch("SYD");
                branch.storeKeys(rsa.generateKeyPair());*/

                // get input and handle it
                String raw = commandLineArea.getText();
                handler.handle(raw, outputArea);
            }
        });



        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                // close window
                System.exit(0);
            }
        });
        hBox.getChildren().addAll(executeButton, closeButton);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(hBox, commandLineArea, outputArea);

        Scene scene = new Scene(vbox, 950, 500);

        scene.setOnKeyPressed(e -> {
            // not yet implemented
            if(e.getCode() == KeyCode.F3){
                System.out.println("Debug mode activated!");
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}