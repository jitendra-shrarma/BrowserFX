package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookmarksManagement {
	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	
	public static void createDataBase() {
		try {
			connection = SqliteConnection.Connector();
			preparedStatement = connection.prepareStatement("CREATE TABLE if not exists bookmark(url text, title varchar(30) ,folderName varchar(40), PRIMARY KEY(url));");
			preparedStatement.executeUpdate();
			System.out.println("Bookmark database created");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isBookmarked(String url, String title, String folder){
		try {
			preparedStatement = connection.prepareStatement("select title,folderName from bookmark where url = ?;");
			preparedStatement.setString(1, url);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				title = resultSet.getString(1);
				folder = resultSet.getString(2);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void insert(String url, String folder, String title) {
		try {
			String insert = "insert or replace into bookmark(url,title,folderName)" + "values(?,?,?)";
			preparedStatement = connection.prepareStatement(insert);
			preparedStatement.setString(1, url);
			preparedStatement.setString(2, title);
			preparedStatement.setString(3, folder);
			preparedStatement.executeUpdate();
			System.out.println("Bookmark Entry added.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResultSet showBookmarks(String folder) {
		try {
			String query = "select url from bookmark where folderName = ?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, folder);
			resultSet = preparedStatement.executeQuery();
		} catch (Exception e) {
			System.out.println("Exception showing bookmarks:");
			e.printStackTrace();
		}
		return resultSet;
	}

	public static ObservableList<String> folders() {
		String query = query = "select distinct folderName from bookmark;";
		ObservableList<String> list = FXCollections.observableArrayList();
		try {
			preparedStatement = connection.prepareStatement(query);
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