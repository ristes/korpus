package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Lexicon;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;

@With(Authentication.class)
public class LexiconController extends JsonController {

	public static void initLexicon() throws Exception {
		jdbc();
	}

	public static void find(String word) {
		toJson(Lexicon.find("word", word).fetch());
	}

	private static void jdbc() {
		Connection conn = null;
		Statement stmt = null;
		File lexicon = Play.getFile("/uploads/lexicon.tbl");

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(lexicon)));
			String line = null;

			// STEP 2: Register JDBC driver
			Class.forName(Play.configuration.getProperty("db.driver"));

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(
					Play.configuration.getProperty("db.url"),
					Play.configuration.getProperty("db.user"),
					Play.configuration.getProperty("db.pass"));

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();

			while ((line = reader.readLine()) != null) {
				String[] elms = line.split("	");
				if (elms.length == 3) {
					String sql = String
							.format("INSERT INTO Lexicon( word, lema, tag) VALUES ('%s', '%s', '%s')",
									elms[0].trim().replace("'", "`"), elms[1]
											.trim().replace("'", "`"), elms[2]
											.trim().replace("'", "﻿`"));

					stmt.addBatch(sql);
				} else {
					System.out.println("bed line: " + line);
				}
			}
			reader.close();
			System.out.println("Executing batch...");
			stmt.executeBatch();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try

		System.out.println("Goodbye!");
	}
}
