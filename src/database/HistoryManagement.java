package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import controllers.HistoryController;
import javafx.collections.ObservableList;

public class HistoryManagement {

    private static Connection connection = SqliteConnection.databaseConnection;
    private static PreparedStatement preparedStatement = null;
    private static Date dateTime;
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a , E");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * create table if not exist (tableName = history(url,title,time,date) primary key(url,domain))
     */
    public static void create() {
        try {
            String createQuery = "create table if not exists history(url text ,title varchar(40) ,time varchar(20) ,date varchar(20), primary key (url,date));";
            preparedStatement = connection.prepareStatement(createQuery);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() + " create1");
        }
    }

    /**
     * insert data in history table
     */
    public static void insert(String url, String title) {
        try {
            dateTime = new Date();
            String insertQuery = "insert or replace into history(url,title,time,date) values(?,?,?,?)";
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,url);
            preparedStatement.setString(2,title);
            preparedStatement.setString(3, timeFormat.format(dateTime));
            preparedStatement.setString(4, dateFormat.format(dateTime));
            preparedStatement.execute();
            preparedStatement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() + " create2");
        }
    }

    /**
     * delete All the history
     */
    public static void deleteAll() {
        try {
            String deleteQeury = "delete from history;";
            preparedStatement = connection.prepareStatement(deleteQeury);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * it returns ResultSet containing fullHistory
     */
    public static ResultSet getFullHistory() {
        try {
            String query = "select * from history order by date,time DESC";
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() + " getFullHistory");
            return null;
        }
    }

    /**
     * this function returns history in a specific duration
     */
    public static ResultSet getHistory(int dateRange) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dateRange);
        String pastDate = "'" + dateFormat.format(cal.getTime()) + "'";
        try {
            String retrieveQuery = null;
            if (dateRange == 0 || dateRange == -1) {
                retrieveQuery = "select * from history where date like" + pastDate + " Order BY time DESC;";
            } else {
                retrieveQuery = "select * from history where date>=" + pastDate + " Order BY date DESC;";
            }
            preparedStatement = connection.prepareStatement(retrieveQuery);
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() + " getHistory");
            return null;
        }
    }

    /**
     * this function returns pastHourHistory
     */
    public static ResultSet pastHoursHistory(int time) {
        dateTime = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, time);
        Date date = cal.getTime();

        String pastHourTime = "'" + timeFormat.format(date) + "'" ;
        String currentDate = "'" + dateFormat.format(dateTime) + "'";
        try {
            String retrieveQeury = "select * from history where Time>" + pastHourTime + "AND Date LIKE " + currentDate + "order by Time DESC;";
            preparedStatement = connection.prepareStatement(retrieveQeury);
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage() + " pastHoursHistory");
        }
        return null;
    }
}
