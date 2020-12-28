package com.company;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private DBConnection(DBConnection dbConnection) {
    }

    public static DBConnection getInstance() {
        return DBConnection.DBConnectionHold.INSTANCE;
    }

    public Connection getConnection() {
        FileInputStream w = null;
        Connection conn = null;

        try {
            Properties proper = new Properties();
            w = new FileInputStream("C:\\programm\\el_dost\\conf\\options.ini");
            proper.load(w);
            w.close();
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            String url = "jdbc:firebirdsql:" + proper.getProperty("server_name") + "/3050" + ":" + proper.getProperty("db_name");
            conn = DriverManager.getConnection(url, proper.getProperty("user"), proper.getProperty("password"));
        } catch (IOException var17) {
            System.out.println(var17.getMessage());
        } catch (SQLException var18) {
            System.out.println(var18.getMessage());
        } catch (ClassNotFoundException var19) {
            var19.printStackTrace();
        } finally {
            try {
                w.close();
            } catch (IOException var16) {
                System.out.println(var16.getMessage());
            }

        }

        return conn;
    }

    private static class DBConnectionHold {
        private static final DBConnection INSTANCE = new DBConnection((DBConnection)null);

        private DBConnectionHold() {
        }
    }
}

