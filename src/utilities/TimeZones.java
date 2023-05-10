/**
 *  @auther Keegan Melton
 *  TimeZones Class
 *  Appointment validation across timezones
 */
package utilities;

import javafx.scene.control.Alert;
import model.DatabaseInfo;

import java.sql.Timestamp;
import java.time.*;
import java.util.TimeZone;

public class TimeZones {

    /**
     *  Checks if the appointment is being scheduled before office hours
     *  Alerts if before office hours
     *
     *  @param date
     *  @param time
     *  @return false or true
     */
    public static boolean beforeOfficeHoursCheck(LocalDate date, LocalTime time){
        ZoneId officeZID = ZoneId.of("US/Eastern");
        LocalTime officeOpenTime = LocalTime.of(8,00);

        ZoneId localZID = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime localSelected = ZonedDateTime.of(date,time,localZID);

        ZonedDateTime officeOpenZDT = ZonedDateTime.of(date,officeOpenTime, officeZID);
        Instant officeOpenInstant = officeOpenZDT.toInstant();

        if(localSelected.isBefore(officeOpenZDT)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Start Time must not be before office hours");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Office hours begin at " + officeOpenInstant.atZone(localZID).toLocalTime() + " " + localZID);
            alert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     *  Checks if the appointment is being scheduled after office hours
     *  Alerts if after office hours
     *
     *  @param date
     *  @param time
     *  @return false or true
     */
    public static boolean afterOfficeHoursCheck(LocalDate date, LocalTime time) {
        ZoneId officeZID = ZoneId.of("US/Eastern");
        LocalTime officeCloseTime = LocalTime.of(22,00);

        ZoneId localZID = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime localSelected = ZonedDateTime.of(date,time,localZID);

        ZonedDateTime officeCloseZDT = ZonedDateTime.of(date,officeCloseTime, officeZID);
        Instant officeCloseInstant = officeCloseZDT.toInstant();

        if(localSelected.isAfter(officeCloseZDT)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"End Time must not be after office hours");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Office hours conclude at " + officeCloseInstant.atZone(localZID).toLocalTime() + " " + localZID);
            alert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     *  Checks if the new appointment date matches a current appointment
     *  Checks if the new appointment date is within the date range of another appointment
     *  If a match is found, times for that date are checked
     *  alerts if a confliction is found
     *
     *  @param id
     *  @param newAppointmentSD
     *  @param newAppointmentST
     *  @param newAppointmentED
     *  @param newAppointmentET
     *  @return false or true
     */
    public static boolean conflictCheck(int id, LocalDate newAppointmentSD, LocalTime newAppointmentST, LocalDate newAppointmentED, LocalTime newAppointmentET){
        for(int i = 0; i < DatabaseInfo.getAllAppointment().size(); i++) {
            LocalDate currentAppointmentSD = DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalDate();
            LocalDate currentAppointmentED = DatabaseInfo.getAllAppointment().get(i).getEnd().toLocalDateTime().toLocalDate();
            if(id == DatabaseInfo.getAllAppointment().get(i).getId()){
                continue;
            }

            if(newAppointmentSD.equals(currentAppointmentSD) || newAppointmentED.equals(currentAppointmentED)) {
                LocalTime currentAppointmentST = DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalTime();
                LocalTime currentAppointmentET = DatabaseInfo.getAllAppointment().get(i).getEnd().toLocalDateTime().toLocalTime();

                if (newAppointmentST.isAfter(currentAppointmentST) && newAppointmentST.isBefore(currentAppointmentET)) {
                    if(newAppointmentSD.equals(currentAppointmentSD)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the Start Time or Date");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("Appointment starts during another appointment between " + currentAppointmentST + " and " + currentAppointmentET + " on " + currentAppointmentSD);
                        alert.showAndWait();
                        return true;
                    }
                    if(newAppointmentED.equals(currentAppointmentED)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the Start Time or Date");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("Appointment starts during another appointment between " + currentAppointmentST + " and " + currentAppointmentET + " on " + currentAppointmentED);
                        alert.showAndWait();
                        return true;
                    }
                }

                if (newAppointmentST.isAfter(currentAppointmentST) && newAppointmentET.isBefore(currentAppointmentET)) {
                    if(newAppointmentSD.equals(currentAppointmentSD)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("Appointment concludes during another appointment between " + currentAppointmentST + " and " + currentAppointmentET + " on " + currentAppointmentSD);
                        alert.showAndWait();
                        return true;
                    }
                    if(newAppointmentED.equals(currentAppointmentED)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("Appointment concludes during another appointment between " + currentAppointmentST + " and " + currentAppointmentET + " on " + currentAppointmentED);
                        alert.showAndWait();
                        return true;
                    }
                }

                if (newAppointmentST.equals(currentAppointmentST)) {
                    if(newAppointmentSD.equals(currentAppointmentSD)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please Change the Start Time or Date");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("Another appointment begins at " + currentAppointmentST + " on " + currentAppointmentSD);
                        alert.showAndWait();
                        return true;
                    }
                    if(newAppointmentED.equals(currentAppointmentED)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the Start Time or Date");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("Another appointment begins at " + currentAppointmentST + " on " + currentAppointmentED);
                        alert.showAndWait();
                        return true;
                    }
                }

                if (newAppointmentET.equals(currentAppointmentET)) {
                    if(newAppointmentSD.equals(currentAppointmentSD)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("Another meeting ends at " + currentAppointmentET + " on " + currentAppointmentSD);
                        alert.showAndWait();
                        return true;
                    }
                    if(newAppointmentED.equals(currentAppointmentED)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("Another meeting ends at " + currentAppointmentET + " on " + currentAppointmentED);
                        alert.showAndWait();
                        return true;
                    }
                }

                if (currentAppointmentST.isAfter(newAppointmentST) && currentAppointmentST.isBefore(newAppointmentET)) {
                    if(newAppointmentSD.equals(currentAppointmentSD)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the time frame of the appointment");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("There is another appointment between " + newAppointmentST + " and " + newAppointmentET + " on " + currentAppointmentSD);
                        alert.showAndWait();
                        return true;
                    }
                    if(newAppointmentED.equals(currentAppointmentED)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the time frame of the appointment");
                        alert.setTitle("Appointment Conflicts!");
                        alert.setHeaderText("There is another appointment between " + newAppointmentST + " and " + newAppointmentET + " on " + currentAppointmentED);
                        alert.showAndWait();
                        return true;
                    }
                }
            }

            if((newAppointmentSD.equals(currentAppointmentSD) || newAppointmentSD.isAfter(currentAppointmentSD)) &&
                    (newAppointmentED.equals(currentAppointmentED) || newAppointmentED.isBefore(currentAppointmentED))){
                LocalTime currentAppointmentST = DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalTime();
                LocalTime currentAppointmentET = DatabaseInfo.getAllAppointment().get(i).getEnd().toLocalDateTime().toLocalTime();

                if (newAppointmentST.isAfter(currentAppointmentST) && newAppointmentST.isBefore(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the Start Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Appointment starts during another appointment between " + currentAppointmentST + " and " + currentAppointmentET +
                              "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentST.isAfter(currentAppointmentST) && newAppointmentET.isBefore(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Appointment concludes during another appointment between " + currentAppointmentST + " and " + currentAppointmentET +
                             "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentST.equals(currentAppointmentST)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please Change the Start Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Another appointment begins at " + currentAppointmentST +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentET.equals(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Another meeting ends at " + currentAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (currentAppointmentST.isAfter(newAppointmentST) && currentAppointmentST.isBefore(newAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the time frame of the appointment");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("There is another appointment between " + newAppointmentST + " and " + newAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }
            }
            if((newAppointmentSD.equals(currentAppointmentSD)) || (newAppointmentSD.isAfter(currentAppointmentSD) && newAppointmentSD.isBefore(currentAppointmentED)) || (newAppointmentSD.equals(currentAppointmentED))){

                LocalTime currentAppointmentST = DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalTime();
                LocalTime currentAppointmentET = DatabaseInfo.getAllAppointment().get(i).getEnd().toLocalDateTime().toLocalTime();

                if (newAppointmentST.isAfter(currentAppointmentST) && newAppointmentST.isBefore(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the Start Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Appointment starts during another appointment between " + currentAppointmentST + " and " + currentAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentST.isAfter(currentAppointmentST) && newAppointmentET.isBefore(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Appointment concludes during another appointment between " + currentAppointmentST + " and " + currentAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentST.equals(currentAppointmentST)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please Change the Start Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Another appointment begins at " + currentAppointmentST +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentET.equals(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Another meeting ends at " + currentAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (currentAppointmentST.isAfter(newAppointmentST) && currentAppointmentST.isBefore(newAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the time frame of the appointment");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("There is another appointment between " + newAppointmentST + " and " + newAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }
            }

            if((newAppointmentED.equals(currentAppointmentSD)) || (newAppointmentED.isAfter(currentAppointmentSD) && newAppointmentED.isBefore(currentAppointmentED)) || (newAppointmentED.equals(currentAppointmentED))){

                LocalTime currentAppointmentST = DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalTime();
                LocalTime currentAppointmentET = DatabaseInfo.getAllAppointment().get(i).getEnd().toLocalDateTime().toLocalTime();

                if (newAppointmentST.isAfter(currentAppointmentST) && newAppointmentST.isBefore(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the Start Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Appointment starts during another appointment between " + currentAppointmentST + " and " + currentAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentST.isAfter(currentAppointmentST) && newAppointmentET.isBefore(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Appointment concludes during another appointment between " + currentAppointmentST + " and " + currentAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentST.equals(currentAppointmentST)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please Change the Start Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Another appointment begins at " + currentAppointmentST +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (newAppointmentET.equals(currentAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the End Time or Date");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("Another meeting ends at " + currentAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }

                if (currentAppointmentST.isAfter(newAppointmentST) && currentAppointmentST.isBefore(newAppointmentET)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please change the time frame of the appointment");
                    alert.setTitle("Appointment Conflicts!");
                    alert.setHeaderText("There is another appointment between " + newAppointmentST + " and " + newAppointmentET +
                            "\n" + currentAppointmentSD + " - " + currentAppointmentED);
                    alert.showAndWait();
                    return true;
                }
            }
        }
     return false;
    }


    public static Timestamp localToUTC(LocalDate date, LocalTime time){
        ZoneId localZID = ZoneId.of(TimeZone.getDefault().getID());

        ZonedDateTime zonedDateTime = ZonedDateTime.of(date,time,localZID);
        Instant instant = zonedDateTime.toInstant();
        Timestamp timestamp = Timestamp.from(instant);

        return timestamp;

    }


}
