package com.example.cronometro;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;

import java.io.*;

public class ContadorTiempo extends Application {
    private TextField inputField;
    private ProgressBar progressBar;
    private Label tiempoLabel;
    private Button iniciarButton;
    private Button cancelarButton;
    private ComboBox<String> tiempoGuardadoBox;
    private ToggleButton temaButton;
    private int tiempoTotal;
    private int tiempoActual;
    private boolean contando;
    private boolean temaOscuro = false;
    private static final String FILE_NAME = "tiempos_guardados.txt";
    private AudioClip alertaSonido;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Contador de Tiempo");

        // Configuración de interfaz
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Elementos de la UI
        Label instruccionLabel = new Label("Introduce el tiempo en segundos:");
        inputField = new TextField();
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);
        tiempoLabel = new Label("Tiempo: 00:00:00");

        // Botones
        iniciarButton = new Button("Iniciar");
        cancelarButton = new Button("Cancelar");
        cancelarButton.setDisable(true);
        temaButton = new ToggleButton("Tema Oscuro");
        temaButton.setOnAction(e -> cambiarTema(root));

        // ComboBox para tiempos predefinidos
        tiempoGuardadoBox = new ComboBox<>();
        cargarTiemposGuardados();
        tiempoGuardadoBox.setPromptText("Tiempos guardados");
        tiempoGuardadoBox.setOnAction(e -> {
            if (tiempoGuardadoBox.getValue() != null) {
                inputField.setText(tiempoGuardadoBox.getValue());
            }
        });

        // Añadir elementos al layout
        root.getChildren().addAll(instruccionLabel, inputField, tiempoGuardadoBox, progressBar, tiempoLabel, iniciarButton, cancelarButton, temaButton);

        // Eventos de los botones
        iniciarButton.setOnAction(e -> iniciarContador());
        cancelarButton.setOnAction(e -> cancelarContador());

        // Configuración de la escena
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Cargar el sonido de alerta
        alertaSonido = new AudioClip(getClass().getResource("/alerta.mp3").toExternalForm());

        // Aplicar el tema claro por defecto
        aplicarTema(root, temaOscuro);
    }

    private void iniciarContador() {
        try {
            tiempoTotal = Integer.parseInt(inputField.getText());
            if (tiempoTotal <= 0) throw new NumberFormatException();

            tiempoActual = 0;
            contando = true;
            progressBar.setProgress(0);
            iniciarButton.setDisable(true);
            cancelarButton.setDisable(false);
            inputField.setDisable(true);

            new Thread(() -> {
                while (contando && tiempoActual < tiempoTotal) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        break;
                    }
                    tiempoActual++;
                    Platform.runLater(this::actualizarUI);
                }

                if (tiempoActual >= tiempoTotal) {
                    Platform.runLater(() -> {
                        alertaSonido.play();
                        mostrarAlertaFinalizacion();
                        reiniciarUI();
                    });
                }
            }).start();
        } catch (NumberFormatException ex) {
            mostrarError("Por favor, introduce un número válido mayor que cero.");
        }
    }

    private void cancelarContador() {
        contando = false;
        reiniciarUI();
    }

    private void actualizarUI() {
        double progreso = (double) tiempoActual / tiempoTotal;
        progressBar.setProgress(progreso);
        tiempoLabel.setText("Tiempo: " + formatearTiempo(tiempoActual));
    }

    private String formatearTiempo(int segundos) {
        int horas = segundos / 3600;
        int minutos = (segundos % 3600) / 60;
        int seg = segundos % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, seg);
    }

    private void mostrarAlertaFinalizacion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tiempo completado");
        alert.setHeaderText(null);
        alert.setContentText("¡El tiempo ha finalizado!");
        alert.showAndWait();
    }

    private void reiniciarUI() {
        iniciarButton.setDisable(false);
        cancelarButton.setDisable(true);
        inputField.setDisable(false);
        progressBar.setProgress(0);
        tiempoLabel.setText("Tiempo: 00:00:00");
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cambiarTema(VBox root) {
        temaOscuro = !temaOscuro;
        aplicarTema(root, temaOscuro);
        temaButton.setText(temaOscuro ? "Tema Claro" : "Tema Oscuro");
    }

    private void aplicarTema(VBox root, boolean oscuro) {
        if (oscuro) {
            root.setStyle("-fx-background-color: #2E2E2E;");
            tiempoLabel.setTextFill(Color.WHITE);
            inputField.setStyle("-fx-background-color: #3E3E3E; -fx-text-fill: white;");
            iniciarButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
            cancelarButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
            tiempoGuardadoBox.setStyle("-fx-background-color: #3E3E3E; -fx-text-fill: white;");
            temaButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
        } else {
            root.setStyle("-fx-background-color: #FFFFFF;");
            tiempoLabel.setTextFill(Color.BLACK);
            inputField.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: black;");
            iniciarButton.setStyle("-fx-background-color: #DDDDDD; -fx-text-fill: black;");
            cancelarButton.setStyle("-fx-background-color: #DDDDDD; -fx-text-fill: black;");
            tiempoGuardadoBox.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: black;");
            temaButton.setStyle("-fx-background-color: #DDDDDD; -fx-text-fill: black;");
        }
    }

    private void cargarTiemposGuardados() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                tiempoGuardadoBox.getItems().add(linea);
            }
        } catch (IOException e) {
            mostrarError("No se pudieron cargar los tiempos guardados.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
