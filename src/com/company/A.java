package com.company;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class A {
    private String pathAttach;
    private String idCourt;
    private String doc_type;

    public String getPathAttach() {
        return this.pathAttach;
    }

    public String getIdCourt() {
        return this.idCourt;
    }

    public String getDocType() {
        return this.doc_type;
    }

    public A(String doc_type) throws IOException {
        Setting setting = new Setting();
        Properties proper = setting.properties();
        this.pathAttach = proper.getProperty("path_attach");
        this.idCourt = proper.getProperty("court_id");
        this.doc_type = doc_type;
    }

    public A(String idCourt, String path) {
        this.pathAttach = path;
        this.idCourt = idCourt;
    }


    public ArrayList<Integer> getId(String dateTime) {
        ArrayList<Integer> resultat = new ArrayList();
        DBConnection dc = DBConnection.getInstance();
        String sql = "select a.CAUSEID from cause a,DBUSER b, CAUSEMEMBER c, FIRM d, CAUSESTATE e, TYPEPART f where a.ARBITRATORID=b.USERID and a.DRECEIVE > cast('" + dateTime + "' as date)" + " and a.ORGID = " + this.getIdCourt() + " and b.ORGID = " + this.getIdCourt() + " and c.ORGID = " + this.getIdCourt() + " and a.ARBITRATORORGID = " + this.getIdCourt() + " " + "and a.CAUSESTATEID = e.CAUSESTATEID and a.CAUSEID = c.CAUSEID and c.FIRMID = d.FIRMID and c.MEMBERTYPE = f.PARTID group by 1";
        Connection conn = dc.getConnection();
        if (conn == null) {
            System.out.println("Отсутствует соединение с БД!");
        }

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                resultat.add(rs.getInt(1));
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

        return resultat;
    }

    public ArrayList<Integer> getId(String dateTime, String dateTimeEnd) {
        ArrayList<Integer> resultat = new ArrayList();
        DBConnection dc = DBConnection.getInstance();
        String sql = "select a.CAUSEID from cause a,DBUSER b, CAUSEMEMBER c, FIRM d, CAUSESTATE e, TYPEPART f where a.ARBITRATORID=b.USERID and a.DRECEIVE > cast('" + dateTime + "' as date)" + " and (a.SIGN_END_REVIEW_DATE is null or a.SIGN_END_REVIEW_DATE > cast('" + dateTimeEnd + "' as date))" + " and a.ORGID = " + this.getIdCourt() + " and b.ORGID = " + this.getIdCourt() + " and c.ORGID = " + this.getIdCourt() + " and a.ARBITRATORORGID = " + this.getIdCourt() + " " + "and a.CAUSESTATEID = e.CAUSESTATEID and a.CAUSEID = c.CAUSEID and c.FIRMID = d.FIRMID and c.MEMBERTYPE = f.PARTID group by 1";
        Connection conn = dc.getConnection();
        if (conn == null) {
            System.out.println("Отсутствует соединение с БД!");
        }

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                resultat.add(rs.getInt(1));

            }
        } catch (SQLException var9) {
            var9.printStackTrace();
        }

        return resultat;
    }

    public ArrayList<Integer> getIdEmail(ArrayList<Integer> causeID) {
        ArrayList<Integer> resultat = new ArrayList();
        String idCauseSql = "";
        int i = 0;

        for(Iterator var6 = causeID.iterator(); var6.hasNext(); ++i) {
            int id = (Integer)var6.next();
            if (i == causeID.size() - 1) {
                idCauseSql = idCauseSql + id;
            } else {
                idCauseSql = idCauseSql + id + ",";
            }
        }
        String go = idCauseSql.substring(0,1);
        int start = 0;
        int stop = 3000;
        System.out.println(idCauseSql.length());
        while(stop > 0) {

            go = idCauseSql.substring(start, idCauseSql.length());
            start = stop;
            stop = idCauseSql.indexOf(',', idCauseSql.length());



            DBConnection dc = DBConnection.getInstance();
            String sql = "select a.CAUSEID from cause a,DBUSER b, CAUSEMEMBER c, FIRM d, CAUSESTATE e, TYPEPART f where a.ARBITRATORID=b.USERID and a.CAUSEID in (" + go + ") and a.ORGID = " + this.getIdCourt() + " and b.ORGID = " + this.getIdCourt() + " and c.ORGID = " + this.getIdCourt() + " and d.ORGID = " + this.getIdCourt() + " and a.ARBITRATORORGID = " + this.getIdCourt() + " " + "and a.CAUSESTATEID = e.CAUSESTATEID and a.CAUSEID = c.CAUSEID and d.email is not null and c.FIRMID = d.FIRMID and c.MEMBERTYPE = f.PARTID group by 1";
            String sql_2 = "SELECT a.CAUSEID from CAUSE a where CAUSEID in (" + go + ")";
            Connection conn = dc.getConnection();
            if (conn == null) {
                System.out.println("Отсутствует соединение с БД!");
            }

            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    resultat.add(rs.getInt(1));
                }
            } catch (SQLException var10) {
                var10.printStackTrace();
            }
        }

        return resultat;
    }

    public ArrayList<Integer> getIdDocAll(ArrayList<Integer> causeID) {
        ArrayList<Integer> resultat = new ArrayList();
        String idCauseSql = "";
        int i = 0;

        for(Iterator var6 = causeID.iterator(); var6.hasNext(); ++i) {
            int id = (Integer)var6.next();
            if (i == causeID.size() - 1) {
                idCauseSql = idCauseSql + id;
            } else {
                idCauseSql = idCauseSql + id + ",";
            }
        }

        DBConnection dc = DBConnection.getInstance();
        String sql = "select b.DOCID from DOCUMENT b where b.ORGID = " + this.getIdCourt() + " and b.ISDELETED = 'F' " + "and b.DRAFTCOPY = 'F' and b.CAUSEID in (" + idCauseSql + ") group by b.DOCID order by 1";
        Connection conn = dc.getConnection();
        if (conn == null) {
            System.out.println("Отсутствует соединение с БД!");
        }

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                resultat.add(rs.getInt(1));
            }
        } catch (SQLException var10) {
            var10.printStackTrace();
        }

        return resultat;
    }

    public ArrayList<Integer> getIdDocAttach(ArrayList<Integer> causeID) {
        ArrayList<Integer> resultat = new ArrayList();
        String idCauseSql = "";
        int i = 0;

        for(Iterator var6 = causeID.iterator(); var6.hasNext(); ++i) {
            int id = (Integer)var6.next();
            if (i == causeID.size() - 1) {
                idCauseSql = idCauseSql + id;
            } else {
                idCauseSql = idCauseSql + id + ",";
            }
        }



        DBConnection dc = DBConnection.getInstance();
        String sql = "select b.DOCID from ATTACHED_FILES a, DOCUMENT b where a.DOCORGID = " + this.getIdCourt() + " and b.ORGID = " + this.getIdCourt() + " and a.DELETED = 0 and b.DOCID = a.DOC_ID and b.ISDELETED = 'F' and " + "b.DRAFTCOPY = 'F' and b.DOCTYPEID in (" + this.getDocType() + ") and b.CAUSEID in (" + idCauseSql + ")   group by b.DOCID, a.DOC_ID, a.FILENAME order by 1";
        Connection conn = dc.getConnection();
        if (conn == null) {
            System.out.println("Отсутствует соединение с БД!");
        }

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                resultat.add(rs.getInt(1));
            }
        } catch (SQLException var10) {
            var10.printStackTrace();
        }

        return resultat;
    }

    public ArrayList<Integer> getIdDocShablon(ArrayList<Integer> causeID) {
        ArrayList<Integer> resultat = new ArrayList();
        String idCauseSql = "";
        int i = 0;

        for(Iterator var6 = causeID.iterator(); var6.hasNext(); ++i) {
            int id = (Integer)var6.next();
            if (i == causeID.size() - 1) {
                idCauseSql = idCauseSql + id;
            } else {
                idCauseSql = idCauseSql + id + ",";
            }
        }

        DBConnection dc = DBConnection.getInstance();
        String sql = "select a.DOCID from DOCUMENT_SIGNED a, DOCUMENT b, DOCTYPE c where b.ORGID = " + this.getIdCourt() + " and c.ORGID = b.DOCTYPEORGID and b.ISDELETED = 'F' " + "and b.DRAFTCOPY = 'F' and b.DOCID=a.DOCID and b.DOCTYPEID not in(280)  and b.DOCTYPEID = c.DOCTYPEID and b.CAUSEID in (" + idCauseSql + ") group by a.DOCID, b.CAUSEID, c.TYPENAME";
        Connection conn = dc.getConnection();
        if (conn == null) {
            System.out.println("Отсутствует соединение с БД!");
        }

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                resultat.add(rs.getInt(1));
            }
        } catch (SQLException var10) {
            var10.printStackTrace();
        }

        return resultat;
    }

    public void getInsertCause(ArrayList<Integer> causeID) throws IOException {
        int c = 0;
        StringBuilder resultat = new StringBuilder();
        String sqlInsert1 = "INSERT INTO cause (CAUSEID,CAUSEGNUM,DRECEIVE,judge,STATENAME,LISTENINGDATE,FIRMNAME,MEMBERTYPE,part,EMAIL) VALUES (";
        String result = "";
        String idCauseSql = "";
        int i = 0;

        for(Iterator var5 = causeID.iterator(); var5.hasNext(); ++i) {
            int id = (Integer)var5.next();
            if (i == causeID.size() - 1) {
                idCauseSql = idCauseSql + id;
            } else {
                idCauseSql = idCauseSql + id + ",";
            }
        }
        String go = idCauseSql.substring(0,3);
        int start = 0;
        int stop = 3000;
        while(stop > 0) {

            go = idCauseSql.substring(start,idCauseSql.length());
            start = stop;
            stop = idCauseSql.indexOf(',', idCauseSql.length());
            DBConnection dc = DBConnection.getInstance();
            String sql = "select a.CAUSEID,a.CAUSEGNUM,a.DRECEIVE,b.REALNAME, e.STATENAME, a.LISTENINGDATE, c.FIRMNAME,c.MEMBERTYPE, c.MEMBERTYPE_NAME, c.EMAIL from cause a,DBUSER b, CAUSEMEMBER_FULL c, CAUSESTATE e where a.ARBITRATORID=b.USERID and a.CAUSEID in (" + go + ") and a.ORGID = " + this.getIdCourt() + " and b.ORGID = " + this.getIdCourt() + " and c.ORGID = " + this.getIdCourt() + " and a.ARBITRATORORGID = " + this.getIdCourt() + " and a.isdeleted<>'T' and c.deldate is NULL and a.CAUSESTATEID = e.CAUSESTATEID and a.CAUSEID = c.CAUSEID order by 1";


            try {
                Connection conn = dc.getConnection();
                if (conn == null) {
                    System.out.println("Отсутствует соединение с БД!");
                }

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);


                while (rs.next()) {
                    int z_1 = rs.getInt(1);
                    String z_2 = rs.getString(2);
                    String z_3 = rs.getString(3);
                    String z_4 = rs.getString(4);
                    String z_5 = rs.getString(5);
                    String z_6 = rs.getString(6);
                    String z_7 = rs.getString(7);
                    String z_8 = rs.getString(8);
                    String z_9 = rs.getString(9);
                    String z_10 = rs.getString(10);
                    if (z_1 == 0){
                        z_1 = 1;
                    }
                    if(z_2 == null){
                        z_2 = " ";
                    }else{
                        z_2 = z_2.trim().replace("'", "");
                    }
                    if (z_3 == null){
                        z_3 = "0000-00-00";
                    }
                    if (z_4 == null){
                        z_4 = "n";
                    }
                    if (z_5 == null){
                        z_5 = "n";
                    }
                    if (z_6 == null){
                        z_6 = "0000-00-00 00:00:00";
                    }
                    if (z_7 == null){
                        z_7 = "n";
                    }
                    if (z_8 == null){
                        z_8 = "1";
                    }
                    if (z_9 == null){
                        z_9 = "n";
                    }
                    if (z_10 == null){
                        z_10 = "n";
                    }
                    resultat.append(sqlInsert1);
                    result += sqlInsert1;
                    int id = z_1;
                    resultat.append(id);
                    result += id;
                    resultat.append(",");
                    result += ",";
                    resultat.append("'");
                    result += "'";
                    resultat.append(z_2);
                    result += z_2;
                    resultat.append("'");
                    result += "'";
                    resultat.append(",");
                    result += ",";
                    resultat.append("'");
                    result += "'";
                    resultat.append(z_3.substring(0, 10));
                    result += z_3.substring(0,10);
                    resultat.append("'");
                    result += "'";
                    resultat.append(",");
                    result += ",";
                    resultat.append("'");
                    result += "'";
                    resultat.append(z_4.trim().replace("'", ""));
                    result += z_4.trim().replace("'", "");
                    resultat.append("'");
                    result += "'";
                    resultat.append(",");
                    result += ",";
                    resultat.append("'");
                    result += "'";
                    resultat.append(z_5.trim().replace("'", ""));
                    result += z_5.trim().replace("'", "");
                    resultat.append("'");
                    result += "'";
                    resultat.append(",");
                    result += ",";
                    resultat.append("'");
                    result += "'";
                    resultat.append(z_6);
                    result += z_6;
                    resultat.append("'");
                    result += "'";
                    resultat.append(",");
                    result += ",";
                    resultat.append("'");
                    result += "'";
                    resultat.append(z_7.trim().replace("'", ""));
                    result += z_7.trim().replace("'", "");
                    resultat.append("'");
                    result += "'";
                    resultat.append(",");
                    result += ",";
                    resultat.append(z_8);
                    result += z_8;
                    resultat.append(",");
                    result += ",";
                    resultat.append("'");
                    result += "'";
                    resultat.append(z_9);
                    result += z_9;
                    resultat.append("'");
                    result += "'";
                    resultat.append(",");
                    result += ",";
                    String email = z_10;
                    if (email == null) {
                        resultat.append("''");
                        result += "''";
                    } else {
                        resultat.append("'");
                        result += "'";
                        resultat.append(email.trim());
                        result += email.trim();
                        resultat.append("'");
                        result += "'";
                    }

                    resultat.append(");\n");
                    result += ");";
                    System.out.print(".");
                    ++c;
                    if (c % 50 == 0) {
                        System.out.println();
                    }

                }



                conn.close();
            } catch (SQLException var14) {
                var14.printStackTrace();
            }
        }
        System.out.println();
        System.out.println("Добавлено " + c + " записей");
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("C:\\programm\\el_dost\\sql\\insertCause.sql"), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pw.println(resultat);
        pw.close();
        System.out.println("Очистка таблици cause:");
        System.out.println(clear("cause"));
        System.out.println("Заполнение таблици cause:");
        try {
            System.out.println(last(result));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void getInsertDocumentShablon(ArrayList<Integer> docID) {
        StringBuilder resultat = new StringBuilder("");
        String sqlInsert1 = "INSERT INTO document (DOCID, CAUSEID, TYPENAME, filename) VALUES (";
        String idDocSql = "";
        int i = 0;

        for(Iterator var7 = docID.iterator(); var7.hasNext(); ++i) {
            int id = (Integer)var7.next();
            if (i == docID.size() - 1) {
                idDocSql = idDocSql + id;
            } else {
                idDocSql = idDocSql + id + ",";
            }
        }

        DBConnection dc = DBConnection.getInstance();
        String docT = "";
        if (this.getDocType().length() > 0) {
            docT = "and b.DOCTYPEID not in (" + this.getDocType() + ")";
        }

        String sql = "select a.DOCID, b.CAUSEID, c.TYPENAME from DOCUMENT_SIGNED a, DOCUMENT b, DOCTYPE c where b.ORGID = " + this.getIdCourt() + " and c.ORGID = b.DOCTYPEORGID and b.ISDELETED = 'F' " + "and b.DRAFTCOPY = 'F' " + docT + " and b.DOCID=a.DOCID and b.DOCTYPEID = c.DOCTYPEID and a.DOCID in (" + idDocSql + ") order by 2,1";

        try {
            Connection conn = dc.getConnection();
            if (conn == null) {
                System.out.println("Отсутствует соединение с БД!");
            }

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                int did = rs.getInt(1);
                resultat.append(sqlInsert1);
                resultat.append(did);
                resultat.append(",");
                resultat.append("'");
                resultat.append(rs.getString(2).trim().replace("'", ""));
                resultat.append("'");
                resultat.append(",");
                resultat.append("'");
                resultat.append(rs.getString(3).trim().replace("'", ""));
                resultat.append("'");
                resultat.append(",");
                resultat.append("'");
                resultat.append(did + ".doc");
                resultat.append("'");
                resultat.append(");\n");
                System.out.print(".");
            }

            FileWriter sw = new FileWriter("C:\\programm\\el_dost\\sql\\insertDocShablon.sql", true);
            sw.write(resultat + "\n");
            sw.close();
            conn.close();
        } catch (SQLException var13) {
            var13.printStackTrace();
        } catch (UnsupportedEncodingException var14) {
            var14.printStackTrace();
        } catch (FileNotFoundException var15) {
            var15.printStackTrace();
        } catch (IOException var16) {
            var16.printStackTrace();
        }

    }

    public void getInsertDocumentAttach(ArrayList<Integer> docID) {
        StringBuilder resultat = new StringBuilder("");
        String sqlInsert1 = "INSERT INTO document (DOCID, CAUSEID, TYPENAME, filename) VALUES (";
        String idDocSql = "";
        int i = 0;
        String result = "";

        for(Iterator var7 = docID.iterator(); var7.hasNext(); ++i) {
            int id = (Integer)var7.next();
            if (i == docID.size() - 1) {
                idDocSql = idDocSql + id;
            } else {
                idDocSql = idDocSql + id + ",";
            }
        }

        DBConnection dc = DBConnection.getInstance();
        String sql = "select b.DOCID, b.CAUSEID, c.TYPENAME, a.FILENAME from ATTACHED_FILES a, DOCUMENT b, DOCTYPE c where a.DOCORGID = " + this.getIdCourt() + " and b.ORGID = " + this.getIdCourt() + " and c.ORGID = b.DOCTYPEORGID and a.DELETED = 0 and b.DOCID = a.DOC_ID and b.ISDELETED = 'F' and " + "b.DRAFTCOPY = 'F' and a.DOC_ID in (" + idDocSql + ") and b.DOCTYPEID = c.DOCTYPEID and  b.DOCTYPEID in (" + this.getDocType() + ") order by 1";

        try {
            Connection conn = dc.getConnection();
            if (conn == null) {
                System.out.println("Отсутствует соединение с БД!");
            }

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int c = 0;

            while(rs.next()) {
                int did = rs.getInt(1);
                String fileName = rs.getString(4);
                resultat.append(sqlInsert1);
                result += sqlInsert1;
                resultat.append(did);
                result += did;
                resultat.append(",");
                result += ",";
                resultat.append("'");
                result += "'";
                resultat.append(rs.getString(2).trim().replace("'", ""));
                result += rs.getString(2).trim().replace("'", "");
                resultat.append("'");
                result += "'";
                resultat.append(",");
                result += ",";
                resultat.append("'");
                result += "'";
                resultat.append(rs.getString(3).trim().replace("'", ""));
                result += rs.getString(3).trim().replace("'", "");
                resultat.append("'");
                result += "'";
                resultat.append(",");
                result += ",";
                resultat.append("'");
                result += "'";
                resultat.append(fileName);
                result += fileName;
                resultat.append("'");
                result += "'";
                resultat.append(");\n");
                result += ");";
                System.out.print(".");
                ++c;
                if (c % 50 == 0) {
                    System.out.println();
                }
            }

            System.out.println();
            System.out.println("Добавлено " + c + " записей");
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("C:\\programm\\el_dost\\sql\\insertDocAttach.sql"), "utf-8"));
            pw.println(resultat);
            pw.close();
            conn.close();
        } catch (SQLException var14) {
            var14.printStackTrace();
        } catch (UnsupportedEncodingException var15) {
            var15.printStackTrace();
        } catch (FileNotFoundException var16) {
            var16.printStackTrace();
        }
        System.out.println("Очистка таблици document:");
        System.out.println(clear("document"));
        System.out.println("Заполнение таблици document:");
        try {
            System.out.println(last(result));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getFileNameAttache(ArrayList<Integer> docID) {
        ArrayList<String> resultat = new ArrayList();
        String idDocSql = "";
        int i = 0;

        for(Iterator var6 = docID.iterator(); var6.hasNext(); ++i) {
            int id = (Integer)var6.next();
            if (i == docID.size() - 1) {
                idDocSql = idDocSql + id;
            } else {
                idDocSql = idDocSql + id + ",";
            }
        }



        DBConnection dc = DBConnection.getInstance();
        String sql = "select a.FILENAME from ATTACHED_FILES a where a.DOC_ID in ("+idDocSql+")";

        try {
            Connection conn = dc.getConnection();
            if (conn == null) {
                System.out.println("Отсутствует соединение с БД!");
            }

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                resultat.add(rs.getString(1));
            }

            conn.close();
        } catch (SQLException var10) {
            var10.printStackTrace();
        }

        return resultat;
    }

    public String realPath(String docName) {
        String resultat = "";
        int id = 0;

        DBConnection dc = DBConnection.getInstance();
        String sql = "select DOC_ID from ATTACHED_FILES  where FILENAME = " + docName+"";

        try {
            Connection conn = dc.getConnection();
            if (conn == null) {
                System.out.println("Отсутствует соединение с БД!");
            }

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                id = Integer.parseInt(rs.getString(1));

            }

            conn.close();
        } catch (SQLException var10) {
            var10.printStackTrace();
        }
        sql = "select DOCCREATEDATE from DOCUMENT  where DOCID = "+id+"";

        try {
            Connection conn = dc.getConnection();
            if (conn == null) {
                System.out.println("Отсутствует соединение с БД!");
            }

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                resultat = rs.getString(1);

            }

            conn.close();
        } catch (SQLException var10) {
            var10.printStackTrace();
        }

        return resultat;
    }

    public void getLoadDocument(int docID) {
        String resultat = "";
        DBConnection dc = DBConnection.getInstance();
        String sql = "select a.DOCID, a.documenthtml from DOCUMENT_SIGNED a where a.docid = " + docID;

        try {
            Connection conn = dc.getConnection();
            if (conn == null) {
                System.out.println("Отсутствует соединение с БД!");
            }

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            boolean var9 = false;

            while(true) {
                Blob bl;
                int docIdFile;
                do {
                    if (!rs.next()) {
                        conn.close();
                        return;
                    }

                    docIdFile = rs.getInt(1);
                    bl = rs.getBlob(2);
                } while(bl == null);

                InputStream is = bl.getBinaryStream();
//                int b = false;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                int b;
                while((b = is.read()) != -1) {
                    baos.write(b);
                }

                resultat = baos.toString();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("documents/" + docIdFile + ".doc"), "utf-8"));
                pw.println(resultat);
                pw.close();
                is.close();
                baos.close();
            }
        } catch (SQLException var14) {
            var14.printStackTrace();
        } catch (IOException var15) {
            var15.printStackTrace();
        }

    }

    public void getLoadAttach(ArrayList<String> fileName) {
        int c = 0;
        Iterator var6 = fileName.iterator();
        int csh = 1;

        while(var6.hasNext()) {
            String string = (String)var6.next();
            String dt = realPath("'"+string+"'");
            File file = new File("Q:\\"+dt.substring(0,4)+"\\"+dt.substring(5,7)+"\\"+dt.substring(8,10)+"\\" + string);
            File file_finish = new File("C:\\programm\\el_dost\\documents\\" + string);
            if (c % 50 == 0) {
                System.out.println();
            }

            System.out.print(".");
            ++c;

            try {
                Files.copy(file.toPath(), file_finish.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (NoSuchFileException var8) {
                System.out.println("Файл " + file.getName() + " отсутствует в файловом хранилище");
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

        System.out.println("Выгружено " + c + " файлов.");
    }

    public boolean clear(String name_table){
        MySQLConnection mysql = MySQLConnection.getInstance();
        try (Connection connection = mysql.getConnection();
             Statement statement = connection.createStatement()) {
            String sql_code = "TRUNCATE " + name_table;
            PreparedStatement preparedStatement_inf = connection.prepareStatement(sql_code);
            preparedStatement_inf.execute();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean last(String sql) throws SQLException, ClassNotFoundException, IOException {
        Setting setting = new Setting();
        Properties prop = setting.properties();
        String url = "jdbc:mysql://" + prop.getProperty("mysql_server") + ":3306" + "/" + prop.getProperty("mysql_db") + "?useTimezone=true&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");
        int start = 0;
        int stop = sql.indexOf(";");
        int c = 0;
        try (Connection connection = DriverManager.getConnection(url, prop.getProperty("mysql_user"), prop.getProperty("mysql_password"));
             Statement statement = connection.createStatement()) {
            while (true){
                String sql_code = sql.substring(start,stop);
                PreparedStatement preparedStatement = connection.prepareStatement(sql_code);
                preparedStatement.execute();
                System.out.print("*");
                start = stop + 1;
                stop = sql.indexOf(";",start);
                ++c;
                if (c % 50 == 0) {
                    System.out.println();
                }
                if (stop <= sql.length() && stop < 0){
                    return true;
                }
            }
        }



    }




}

