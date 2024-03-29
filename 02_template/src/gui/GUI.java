package gui;

import configuration.Configuration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logging.Logger;
import network.Network;
import parser.Handler;
import parser.SendMessageHandler;
import persistence.HSQLDB;

public class GUI extends Application {
    private static TextArea outputArea;
    private final Handler handler = new SendMessageHandler();
    private TextArea inputArea;
    private Button execute;
    private Button close;

    public static TextArea getOutputArea() {
        return outputArea;
    }

    public void init() {
        HSQLDB.instance.setupConnection();
        Network.instance.startup();
    }

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

        this.inputArea = commandLineArea;
        GUI.outputArea = outputArea;
        this.execute = executeButton;
        this.close = closeButton;

        executeButton.setOnAction(this::handleExecute);

        closeButton.setOnAction(actionEvent -> {
            // close window
            Platform.exit();
        });
        hBox.getChildren().addAll(executeButton, closeButton);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(hBox, commandLineArea, outputArea);

        Scene scene = new Scene(vbox, 950, 500);
        commandLineArea.requestFocus();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F3) {
                Configuration.instance.switchDebugMode();
                if (Configuration.instance.debugMode) {
                    outputArea.setText("Debug mode activated");
                } else {
                    outputArea.setText("Debug mode deactivated");
                }
            } else if (e.getCode() == KeyCode.F8) {
                Logger.displayLatestLogFile(outputArea);
            } else if (e.getCode() == KeyCode.F5) {
                this.handleExecute();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void stop() {
        HSQLDB.instance.shutdown();
    }

    private void handleExecute(ActionEvent event) {
        // get input and handle it
        handler.accept(inputArea.getText(), outputArea);
    }

    private void handleExecute() {
        this.handleExecute(new ActionEvent());
    }

    public TextArea getInputArea() {
        return inputArea;
    }

    public Button getExecute() {
        return execute;
    }

    public Button getClose() {
        return close;
    }
}