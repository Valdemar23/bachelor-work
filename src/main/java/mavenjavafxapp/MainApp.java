package mavenjavafxapp;

/*
 * Created by HP on 18.03.2017.Теперь создадим класс, который будет хавать наш конфиг-файл и возвращать нам объект типа SessionFactory, который отвечает за создание hibernate-сессии.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static hibernate.HibernateUtil.sessionFactory;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            String databaseName = "bachelor";
            String userName = "root";//need config with your system configuration
            String password = "";//need config with your system configuration

            String url = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull";
            Connection connection = (Connection) DriverManager.getConnection(url,userName, password);

            String sql = "CREATE DATABASE " + databaseName;

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            JOptionPane.showMessageDialog(null, databaseName + " Database has been created successfully", "System Message", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }


        String fxmlFile = "/fxml/hello.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("JavaFX and Maven");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() {
        sessionFactory.close();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}