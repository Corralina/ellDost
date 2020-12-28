package com.company;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import static jdk.nashorn.internal.codegen.OptimisticTypesPersistence.load;

public class Main {
    FTPClient ftp = null;

    public Main(String host, String user, String pwd) throws IOException {
        this.ftp = new FTPClient();
        this.ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try {
            this.ftp.connect(host);
            int reply = this.ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("Отсутствует подключение к серверу FTP");
                this.ftp.disconnect();
                System.exit(0);
            }

            this.ftp.login(user, pwd);
            this.ftp.setFileType(2);
            this.ftp.enterLocalPassiveMode();
        } catch (SocketException var6) {
            System.out.println(var6.getMessage());
        } catch (IOException var7) {
            System.out.println(var7.getMessage());
        }

    }

    public void disconnect() throws IOException {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException var2) {
                System.out.println(var2.getMessage());
            }
        }

    }


    public void uploadFile(String localDirectory, String hostDir) throws IOException {
        File dirDocument = new File(localDirectory);
        File[] lists = dirDocument.listFiles();

        try {
            File[] var9 = lists;
            int var8 = lists.length;
//            System.out.println("-----------------------------------------------------");
//            System.out.println( "Documents to FTP: " + var8);
//            System.out.println(localDirectory);

            for(int var7 = 0; var7 < var8; ++var7) {
                File list = var9[var7];
                InputStream input = new FileInputStream(new File(localDirectory + list.getName()));
                this.ftp.storeFile(hostDir + list.getName(), input);
                input.close();
                if (list.isFile()) {
                    list.delete();
                }
            }

        } catch (FileNotFoundException var10) {
            System.out.println(var10.getMessage());
        } catch (IOException var11) {
            System.out.println(var11.getMessage());
        }

    }

    public void delFiles(String hostDir) throws IOException {
        try {
            FTPFile[] lists = this.ftp.listFiles(hostDir);
            if (lists.length > 0) {
                int count = 0;
                FTPFile[] var7 = lists;
                int var6 = lists.length;

                for(int var5 = 0; var5 < var6; ++var5) {
                    FTPFile list = var7[var5];
                    if (list.isFile()) {
                        this.ftp.deleteFile(hostDir + list.getName());
                    }

                    ++count;
                }

            }
        } catch (IOException var8) {
            System.out.println(var8.getMessage());
        }

    }

    public static void main(String[] args) throws IOException {


        if (args.length == 0) {
            System.out.println("Start");
            Setting setting = new Setting();
            Properties prop = setting.properties();
            System.out.println("Проверка соединения с БД FireBirds");
            DBConnection dc = DBConnection.getInstance();
            Connection conn = dc.getConnection();
            if (conn == null) {
                System.out.println("Отсутствует соединение с БД FireBirds");
                System.exit(0);
            } else {
                System.out.println("OK!");
            }

            try {
                conn.close();
            } catch (SQLException var9) {
                var9.getMessage();
            }

            System.out.println("Проверка подключения к папке с прикреплёнными файлами");
            File file = new File(prop.getProperty("path_attach"));
            if (!file.exists()) {
                System.out.println("Отсутствует подключение к папке с прикреплёнными файлами");
                System.exit(0);
            } else {
                System.out.println("OK!");
            }

            System.out.println("Проверка подключения к серверу FTP");
            Main ftpUploader = new Main(prop.getProperty("ftp_host"), prop.getProperty("ftp_user"), prop.getProperty("ftp_passw"));
            ftpUploader.disconnect();
            System.out.println("OK!");
            System.out.println("Проверка соединения с БД MySQL на хосте");
            MySQLConnection mconn = MySQLConnection.getInstance();
            if (mconn == null) {
                System.out.println("Отсутствует соединение с БД MySQL!");
                System.exit(0);
            } else {
                System.out.println("OK!");
            }

            System.out.println("Проверка основных модулей окончена. OK!");
            System.out.println("Начало процесса выгрузки:");
            Runs runs = new Runs();
            runs.r1();
            ftpUploader = new Main(prop.getProperty("ftp_host"), prop.getProperty("ftp_user"), prop.getProperty("ftp_passw"));
            ftpUploader.delFiles(prop.getProperty("ftp_documents"));
            ftpUploader.uploadFile("C:\\programm\\el_dost\\documents\\", prop.getProperty("ftp_documents"));
            ftpUploader.disconnect();
        }

        System.out.println("Выгрузка успешно завершена.");

    }
}
