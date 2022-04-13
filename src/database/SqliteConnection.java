package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {

	public static Connection databaseConnection = null;
	static {
		try {
			Class.forName("org.sqlite.JDBC");
			databaseConnection = DriverManager.getConnection("jdbc:sqlite:DataBase.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}