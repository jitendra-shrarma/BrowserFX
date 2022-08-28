package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookmarksManagement {
	private static Connection connection = SqliteConnection.databaseConnection;
	private static PreparedStatement preparedStatement = null;
	private static Date dateTime;
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");

	/**create BookmarkDatabase bookmark(url ,folderName ,title ,time ,date , Primary key(url))*/
	public static void create() {
		try {
			preparedStatement = connection.prepareStatement("create table if not exists bookmark(url text ,folderName varchar(30),"
					+ "title varchar (30), time varchar(20), date varchar(20), Primary key(url));");
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**insert Bookmark in database*/
	public static void insert(String url, String folder, String title) {
		try {
			dateTime= new Date();
			String time= timeFormat.format(dateTime);
			String date= dateFormat.format(dateTime);

			String insert = "insert or replace into bookmark(url,folderName,title,time,date)" + "values(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(insert);

			preparedStatement.setString(1, url);
			preparedStatement.setString(2, folder);
			preparedStatement.setString(3, title);
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, date);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**delete Bookmark*/
	public static void delete(String url){
		try {
			String insert = "delete from bookmark where url = ? ";
			preparedStatement = connection.prepareStatement(insert);
			preparedStatement.setString(1, url);
			boolean b = preparedStatement.execute();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**delete specific folder*/
	public static void deleteFolder(String folder) {
		try {
			String query = null;
			if(folder == "All Bookmarks") {
				query = "delete from bookmark;";
				preparedStatement = connection.prepareStatement(query);
			}else {
				query = "delete from bookmark where folderName = ? ;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, folder);
			}
			preparedStatement.execute();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**return bookmarks for specific folder*/
	public static ResultSet showBookmarks(String folder) {
		ResultSet rs = null;
		try {
			String query = null;
			if(folder == "All Bookmarks") {
				query = "select url, title, time , date from bookmark;";
				preparedStatement = connection.prepareStatement(query);
			}else {
				query = "select url, title, time , date from bookmark where url in (select url from bookmark where folderName = ?);";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, folder);
			}
			rs = preparedStatement.executeQuery();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return rs;
	}

	/**return all distinct folders*/
	public static ObservableList<String> folders() {
		ResultSet rs;
		String query = "select distinct folderName from bookmark order by folderName;";
		ObservableList<String> list = FXCollections.observableArrayList();
		try {
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String folder = rs.getString(1);
				if (folder != null) {
					list.add(folder);
				}
			}
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return list;
	}

	/**search database weather the url is bookmarked*/
	public static boolean isBookmarked(String url, String title, String folder){
		try{
			String query = "select title,folderName from bookmark where url = ? ;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,url);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()){
				title = rs.getString(1);
				folder = rs.getString(2);
				return true;
			}
		}catch (SQLException e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return false;
	}
}