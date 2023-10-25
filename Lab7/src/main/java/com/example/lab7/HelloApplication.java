package com.example.lab7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelloApplication extends Application {
    private List<String> allLines;
    private LineChart<String,Number> lineChart;
    private AreaChart<String,Number> areaChart;
    private BarChart<String,Number> barChart;
    private MenuBar menuBar;
    private BorderPane borderPane;
    private Label sauvegardeReussi;
    @Override
    public void start(Stage stage) {
        // Menu
        Menu importer = new Menu("Importer");
        Menu exporter = new Menu("Exporter");

        MenuItem lignes = new MenuItem("Lignes");
        MenuItem regions = new MenuItem("Régions");
        MenuItem barres = new MenuItem("Barres");

        MenuItem png = new MenuItem(".png");
        MenuItem jpg = new MenuItem(".jpg");

        importer.getItems().addAll(lignes,regions,barres);
        exporter.getItems().addAll(png,jpg);

        menuBar = new MenuBar(importer,exporter);
        borderPane = new BorderPane();
        borderPane.setTop(menuBar);

        // Sauvegarde reussi
        sauvegardeReussi = new Label();
        borderPane.setBottom(sauvegardeReussi);

        // MenuItem set on action
        // XY
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Température");

        lignes.setOnAction((event)-> {
            try {
                lineChart = new LineChart<>(xAxis,yAxis);
                lineChart.setTitle("Températures moyennes pour chaque mois 2023");
                lineChart.getData().add(choixFichier(stage));
                borderPane.setCenter(lineChart);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        regions.setOnAction((event)-> {
            try {
                areaChart = new AreaChart<>(xAxis,yAxis);
                areaChart.setTitle("Températures moyennes pour chaque mois 2023");
                areaChart.getData().add(choixFichier(stage));
                borderPane.setCenter(areaChart);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        barres.setOnAction((event)-> {
            try {

                barChart = new BarChart<>(xAxis,yAxis);
                barChart.setTitle("Températures moyennes pour chaque mois 2023");
                barChart.getData().add(choixFichier(stage));
                borderPane.setCenter(barChart);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        png.setOnAction((event)-> {
            ouMettreFichier(stage,png);
        });
        jpg.setOnAction((event)-> {
            ouMettreFichier(stage,png);
        });


        stage.setScene(new Scene(borderPane,400,400));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    //Fonction afin de trouver le fichier
    private XYChart.Series choixFichier(Stage stage) throws IOException {
        sauvegardeReussi.setText("");
        XYChart.Series series = new XYChart.Series();
        // choix fichier
        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Fichiers DAT", "*.dat"));
        File fichier = fc.showOpenDialog(stage);

        // quoi faire avec le fichier
        if (fichier!=null) {
            allLines = Files.readAllLines(Paths.get(fichier.toString()));
            List<String> info1 = new ArrayList<>();
            List<String> info2 = new ArrayList<>();
            if (allLines.size() == 2) {
                Collections.addAll(info1, allLines.get(0).split(","));
                Collections.addAll(info2, allLines.get(1).split(","));

                series.setName("Température/Mois");
                if (info1.size() == info2.size()) {
                    for (String i : info1) {
                        series.getData().add(new XYChart.Data(i, Integer.valueOf(info2.get(info1.indexOf(i)))));
                    }
                }
            }
        }
        return series;
    }
    //Fonction sauvegarder image non finie.
    private void ouMettreFichier(Stage stage,MenuItem type){
         FileChooser fc = new FileChooser();
         fc.setTitle("Veuillez sauvegarder l'image");
         fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                    "Fichiers " + type.getText(), "*" + type.getText()));
             File fichier = fc.showSaveDialog(stage);
             if(fichier!= null)
               sauvegardeReussi.setText("Sauvegarde réussi");
      }
      //Fonctionne clairement pas
      /*
   public void saveAsPng(Scene scene, String path) {
       WritableImage image = scene.snapshot(null);
       File file = new File(path);
       try {
           ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

       */
}
