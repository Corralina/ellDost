package com.company;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.Class.forName;

public class MySQLConnection {
    public MySQLConnection(MySQLConnection mySQLConnection) {
    }

    public static MySQLConnection getInstance() {
        return MySQLConnection.MySQLConnectionHold.INSTANCE;
    }

    public Connection getConnection() throws IOException {
        Setting setting = new Setting();
        Properties prop = setting.properties();
        Connection conn = null;

        try {
            forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + prop.getProperty("mysql_server") + ":3306" + "/" + prop.getProperty("mysql_db") + "?useTimezone=true&serverTimezone=UTC";
            conn = DriverManager.getConnection(url, prop.getProperty("mysql_user"), prop.getProperty("mysql_password"));
        } catch (SQLException var5) {
            System.out.println(var5.getMessage());
        } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
        }

        return conn;
    }

    private static class MySQLConnectionHold {
        private static final MySQLConnection INSTANCE = new MySQLConnection((MySQLConnection)null);

        private MySQLConnectionHold() {
        }
    }
}
