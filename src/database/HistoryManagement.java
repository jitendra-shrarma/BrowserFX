package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import controllers.HistoryController;
import javafx.collections.ObservableList;

public class HistoryManagement {

    private static PreparedStatement perp = null;
    private static Date dateTime;
    private static SimpleDateFormat timeFormat;
    private static SimpleDateFormat dateFormat;
    private static boolean queryOutput = false;

    static {
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    }

    /**
     * create table if not exist (tableName = history(url,title,time,date) primary key(url,domain))
     */
    public static void create() {
        try {
            String createQuery = "create table if not exists history(url text ,title varchar(40) ,time varchar(30) ,date varchar(30), primary key (url,date));";
            perp = SqliteConnection.Connector().prepareStatement(createQuery);
            perp.execute();
            perp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * insert data in history table
     */
    public static void insert(String url, String title) {
        try {
            dateTime = new Date();
            String insertQuery = "insert or replace into history(url,title,time,date) values(?,?,?,?)";
            String time = timeFormat.format(dateTime);
            String date = dateFormat.format(dateTime);

            System.out.println("Current time = "+time);
            System.out.println("Current date = "+date);

            perp = SqliteConnection.Connector().prepareStatement(insertQuery);
            perp.setString(1,url);
            perp.setString(2,title);
            perp.setString(3, time);
            perp.setString(4, date);
            queryOutput = perp.execute();

            if(queryOutput)
                System.out.println("data has been inserted");

            perp.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * delete All the history
     */
    public static void deleteAll() {
        try {
            String deleteQeury = "delete from history;";
            perp = SqliteConnection.Connector().prepareStatement(deleteQeury);
            System.out.println("Histroy cleared");
            perp.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * it returns ObservableList containing fullHistory
     */
    public static ObservableList getfullHistory(ObservableList list) {
        ResultSet rs = null;
        try {
            String query = "select * from history order by date,time DESC";
            perp = SqliteConnection.Connector().prepareStatement(query);
            rs = perp.executeQuery();

            while (rs.next()) {
                String link1 = rs.getString(1);
                String title1 = rs.getString(2);
                String time1 = rs.getString(3);
                String date1 = rs.getString(4);

                System.out.println(link1 + title1 + time1 + date1);
                HistoryController.addDataInList(link1, title1, time1, date1, list);
            }
            rs.close();
            perp.close();
        } catch (Exception e) {
            System.out.println("issue in pasthourHistory");
        }
        finally {
            return list;
        }
    }

    /**
     * this function returns history in a specific duration
     */
    public static ObservableList getHistory(ObservableList list, int dateRange) {
        ResultSet rs = null;

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dateRange);
        String pastDate = dateFormat.format(cal.getTime());
        pastDate = "'" + pastDate + "'";
        System.out.println("Past Date : " +pastDate);

        try {
            String retrieveQuery = "select * from history where Date like" + pastDate +";";
            perp = SqliteConnection.Connector().prepareStatement(retrieveQuery);
            rs = perp.executeQuery();

            while (rs.next()) {
                String link1 = rs.getString(1);
                String title1 = rs.getString(2);
                String time1 = rs.getString(3);
                String date1 = rs.getString(4);

                System.out.println(link1 + title1 + time1 + date1);
                HistoryController.addDataInList(link1, title1, time1, date1, list);
            }
            rs.close();
            perp.close();
        } catch (Exception e) {
            System.out.println("isseus in getHistory method ");
        }
        return list;
    }

    /**
     * this function returns pastHourHistory
     */
    public static ObservableList pastHoursHistory(ObservableList pastHour, int time) {
        dateTime = new Date();
        ResultSet rs = null;
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, time);
        Date date = cal.getTime();

        String pastHourTime = timeFormat.format(date);
        pastHourTime = "'" + pastHourTime + "'";
        System.out.println("Past Hour Time : "+pastHourTime);

        String currentDate = dateFormat.format(dateTime);
        currentDate = "'" + currentDate + "'";
        System.out.println("Current date : "+currentDate);

        try {
            String retrieveQeury = "select * from history where Time>" + pastHourTime + "AND Date LIKE " + currentDate + "order by Time DESC;";
            perp = SqliteConnection.Connector().prepareStatement(retrieveQeury);
            rs = perp.executeQuery();
            while (rs.next()) {
                String link1 = rs.getString(1);
                String title1 = rs.getString(2);
                String time1 = rs.getString(3);
                String date1 = rs.getString(4);

                System.out.println(link1 + title1 + time1 + date1);
                HistoryController.addDataInList(link1, title1, time1, date1, pastHour);
            }
        } catch (Exception e) {
            System.out.println("issue in pasthourHistory");
        }
        return pastHour;
    }
}
