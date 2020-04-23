package com.poloproduction.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
    private String url;
    private String username;
    private String password;

    DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {

        }

    	// add ?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC
    	// see https://www.developpez.net/forums/d1876029/java/general-java/server-time-zone-non-reconnu/
    	// add ?serverTimezone=UTC
    	// see https://mkyong.com/jdbc/java-sql-sqlexception-the-server-time-zone-value-xx-time-is-unrecognized/
    	// of change C:\ProgramData\MySQL\MySQL Server 8.0\my.ini
        DaoFactory instance = new DaoFactory(
                "jdbc:mysql://localhost:3306/javaee?serverTimezone=UTC", "root", "yolo76");
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection connexion = DriverManager.getConnection(url, username, password);
        connexion.setAutoCommit(false);
        return connexion; 
    }

    // Récupération du Dao
    public UtilisateurDao getUtilisateurDao() {
        return new UtilisateurDaoImpl(this);
    }
}