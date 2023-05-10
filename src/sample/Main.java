/**
 *  @auther Keegan Melton
 */
package sample;

import utilities.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.SQL_Queries;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {

    /**
     *  Begins application
     *
     * @param primaryStage
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {

        //Used to test localization
/*
        Locale france = new Locale("fr", "FR");
        System.out.println(Locale.getDefault());

        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter a language(en or fr)");
        String languageCode = keyboard.nextLine();

        if(languageCode.equals("fr")){
            Locale.setDefault(france);
        }
        System.out.println(Locale.getDefault());
 */

        ResourceBundle rb = ResourceBundle.getBundle("resource/Language", Locale.getDefault());

        SQL_Queries.selectAllCustomer();
        SQL_Queries.selectAllUsers();
        SQL_Queries.selectAllAppointments();
        SQL_Queries.selectAllCountries();
        SQL_Queries.selectAllNationalDivision();
        SQL_Queries.selectAllContacts();

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        primaryStage.setTitle(rb.getString("Login"));
        primaryStage.setScene(new Scene(root, 600,400));
        primaryStage.show();

    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        // Opens Connection
        JDBC.openConnection();

        launch();
    }
}
