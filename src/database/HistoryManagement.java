package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.collections.ObservableList;

public class HistoryManagment {

    private static Connection connection = null;
    private static PreparedStatement perp = null;
    private static Date dateTime;
    private static SimpleDateFormat timeFormat;
    private static SimpleDateFormat dateFormat;
    private static boolean queryOutput = false;

    static {
        connection = SqliteConnection.Connector();
        dateTime = new Date();
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    }


    /**
     * create table if not exist (tableName = history(url,domain,time,date))
     */
    public static void create() {
        try {
            String createQuery = "create table if not exists history(url text ,domain varchar(40) ,time varchar(30) ,date varchar(30));";
            perp = connection.prepareStatement(createQuery);
            perp.execute();
            perp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * insert data in history table
     */
    public static void insert(String url, String domain) {
        try {
            String insertQuery = "insert or replace into history(url,domain,time,date) values(?,?,?,?)";
            String time = timeFormat.format(dateTime);
            String date = dateFormat.format(dateTime);

            System.out.println("Current time = "+time);
            System.out.println("Current date = "+date);

            perp = connection.prepareStatement(insertQuery);
            perp.setString(1,url);
            perp.setString(2,domain);
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


    public static void deleteAll() {
        try {
            String deleteQeury = "delete from history;";
            perp = SqliteConnection.Connector().prepareStatement(qeury);
            perp.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Issues in delete method");
        }

    }
    // ---------------------------------Full HISTORY
    // Show--------------------------------------------------------------------------//

    public static ObservableList fullHistoryShow(ObservableList fullHistory) {

        ResultSet rs = null;
        try {
            // Class.forName("org.sqlite.JDBC");
            // c = DriverManager.getConnection("jdbc:sqlite:History.db");
            String str = "select * from(select * from history order by Time DESC) history order by Date DESC";
            perp = SqliteConnection.Connector().prepareStatement(str);
            rs = perp.executeQuery();

            while (rs.next()) // loop for data fetching and pass it to GUI table
            // view
            {
                String email1 = rs.getString(1);
                String link1 = rs.getString(2);
                String time1 = rs.getString(3);
                String date1 = rs.getString(4);
                String domain1 = rs.getString(5);
                String title1 = rs.getString(6);

                // System.out.println(link1, time1,date1,domain1,title1);
                fullHistory = HistoryController.addDataInList(email1,link1, time1, date1, domain1, title1, fullHistory);
            }
            rs.close();
            perp.close();
            SqliteConnection.Connector().close();
        } catch (Exception e) {
            System.out.println("Issues in fullHistoryShow method");
        }
        return fullHistory;
    }

    public static ObservableList getDomainNames(ObservableList domainNames) {

        ResultSet rs = null;
        try {
            String str = "select url from history";
            perp = SqliteConnection.Connector().prepareStatement(str);
            rs = perp.executeQuery();

            while (rs.next()) // loop for data fetching and pass it to GUI table
            // view
            {
                String link1 = rs.getString(1);

                // ObservableList<String> domainNamesList =
                // FXCollections.observableArrayList();
                domainNames.add(link1);

            }
            rs.close();
            perp.close();
            SqliteConnection.Connector().close();
        } catch (Exception e) {
            System.out.println("Issues in fullHistoryShow method");
        }
        return domainNames;
    }

    // ------------------------------------this method return user specified
    // histories--------------------------------------------------------------------//1
    public static ObservableList getHistory(ObservableList list, int dateRange) {
        ResultSet rs = null;
        dateTime = new Date();

        // past dates denpending upon the function parameter 'dateRange'
        dateFormat = new SimpleDateFormat("yy-MM-dd");
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dateRange);
        String pastDate = dateFormat.format(cal.getTime());
        pastDate = "'" + pastDate + "'";

        String qeury;
        try {
            // Class.forName("org.sqlite.JDBC");
            // c = DriverManager.getConnection("jdbc:sqlite:History.db");
            // user aske for today or yesterday history
            if (dateRange == 0 || dateRange == -1) {
                qeury = "select * from (select * from history order by Time DESC) history where Date like" + pastDate
                        + ";";
                perp = SqliteConnection.Connector().prepareStatement(qeury);
                rs = perp.executeQuery();
            }
            // if user asks for more two day history
            else {
                qeury = "select * from (select * from history order by Time DESC) history where Date>=" + pastDate
                        + " Order BY Date DESC;";
                perp = SqliteConnection.Connector().prepareStatement(qeury);
                rs = perp.executeQuery();
            }

            while (rs.next())// loop for data fetching and pass it to GUI table
            // view
            {
                String email1 = rs.getString(1);
                String link1 = rs.getString(2);
                String time1 = rs.getString(3);
                String date1 = rs.getString(4);
                String domain1 = rs.getString(5);
                String title1 = rs.getString(6);

                list = HistoryController.addDataInList(email1,link1, time1, date1, domain1, title1, list);
            }

            rs.close();
            perp.close();
            SqliteConnection.Connector().close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("isseus in getHistory method ");
        }
        return list;
    }
    // end method

    // -------------------------------------Past Hour Histroy
    // Show-------------------------------------------------------------//
    public static ObservableList pastHoursHistory(ObservableList pastHour, int time) {
        ResultSet rs = null;
        dateTime = new Date();
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("yy-MM-dd");

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, time);
        Date date = cal.getTime();

        // current and past time
        String pastHourTime = timeFormat.format(date);
        pastHourTime = "'" + pastHourTime + "'";

        // current date
        String currentDate = dateFormat.format(dateTime);
        currentDate = "'" + currentDate + "'";
        System.out.println(currentDate);
        try {
            // Class.forName("org.sqlite.JDBC");
            // c = DriverManager.getConnection("jdbc:sqlite:History.db");
            String qeury = "select * from history where Time>" + pastHourTime + "AND Date LIKE " + currentDate
                    + "order by Time DESC;";
            perp = SqliteConnection.Connector().prepareStatement(qeury);
            rs = perp.executeQuery();
            System.out.println(rs.next());
            while (rs.next()) // loop for data fetching and pass it to GUI table
            // view
            {
                String email1 = rs.getString(1);
                String link1 = rs.getString(2);
                String time1 = rs.getString(3);
                String date1 = rs.getString(4);
                String domain1 = rs.getString(5);
                String title1 = rs.getString(6);

                pastHour = HistoryController.addDataInList(email1,link1, time1, date1, domain1, title1, pastHour);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("isseus in pastHours mehtod");
        }

        return pastHour;
    }
    // end method
}
// end Class
