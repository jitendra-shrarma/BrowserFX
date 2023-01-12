package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookmarksManagement {

	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	private static Date dateTime;
	private static SimpleDateFormat timeFormat;
	private static SimpleDateFormat dateFormat;

	static {
		timeFormat = new SimpleDateFormat("HH:mm:ss");
		dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	}
	
	public static void create() {
		try {
			preparedStatement = SqliteConnection.Connector().prepareStatement("create table if not exists bookmark(url text,"
					+" name varchar(30) ,time varchar(30) ,date varchar(30) ,folderName varchar(40) , PRIMARY KEY(url));");
			preparedStatement.executeUpdate();
			System.out.println("Bookmark database created");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isBookmarked(String url, String name, String folder){
		try {
			preparedStatement = SqliteConnection.Connector().prepareStatement("select title,folderName from bookmark where url = ?;");
			preparedStatement.setString(1, url);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				name = resultSet.getString(1);
				folder = resultSet.getString(2);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void insert(String url, String folder, String name) {
		try {
			dateTime = new Date();
			String time = timeFormat.format(dateTime);
			String date = dateFormat.format(dateTime);

			String insert = "insert or replace into bookmark(url,title,time,date,folderName)" + "values(?,?,?)";
			preparedStatement = SqliteConnection.Connector().prepareStatement(insert);
			preparedStatement.setString(1, url);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, time);
			preparedStatement.setString(4, date);
			preparedStatement.setString(5, folder);
			preparedStatement.executeUpdate();
			System.out.println("Bookmark Entry added.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResultSet showBookmarks(String folder) {
		try {
			String query = "select url,name,time,date from bookmark where folderName = ?;";
			preparedStatement = SqliteConnection.Connector().prepareStatement(query);
			preparedStatement.setString(1, folder);
			resultSet = preparedStatement.executeQuery();
		} catch (Exception e) {
			System.out.println("Exception showing bookmarks:");
			e.printStackTrace();
		} finally {
			return resultSet;
		}
	}

	public static ObservableList<String> folders() {
		String query = query = "select distinct folderName from bookmark;";
		ObservableList<String> list = FXCollections.observableArrayList();
		try {
			preparedStatement = SqliteConnection.Connector().prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String folder = resultSet.getString(1);
				if (folder != null) {
					list.add(folder);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving folder names.");
		}
		return list;
	}
}