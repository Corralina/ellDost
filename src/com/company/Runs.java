package com.company;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;

public class Runs {
    private A a;
    private String doc_type;
    private String dt;
    private String dt_end;

    public Runs(String idCourt, String path) {
        this.a = new A(idCourt, path);
    }

    public String getDocType() {
        return this.doc_type;
    }

    public String getDateEnd(String day_count) {
        if (this.dt_end.length() == 0) {
            return "";
        } else {
            int countDay = Integer.parseInt(this.dt_end);
            String dateFormat = "dd.MM.yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Calendar cal1 = Calendar.getInstance();
            cal1.add(5, -14);
            System.out.println("----------" + sdf.format(cal1.getTime()));
            return sdf.format(cal1.getTime());
        }
    }

    public Runs() throws IOException {
        FileInputStream w = null;
        Properties proper = new Properties();

        try {
            w = new FileInputStream("C:\\programm\\el_dost\\conf\\options.ini");
            proper.load(w);
            w.close();
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        this.dt_end = proper.getProperty("count_day_stop");
        this.dt = proper.getProperty("date_start");
        this.doc_type = proper.getProperty("doc_type");
        this.a = new A(this.doc_type);
    }

    public void r1() throws IOException {
        Properties proper = new Properties();
        File fileDateUpd = new File("C:\\programm\\el_dost\\upd.dat");
        new ArrayList();
        new ArrayList();
        ArrayList allCauseId;
        if (this.getDateEnd(proper.getProperty("count_day_stop")).length() == 0) {
            allCauseId = this.a.getId(this.getDateEnd(proper.getProperty("count_day_stop")));
        } else {
            allCauseId = this.a.getId(this.getDateEnd(proper.getProperty("count_day_stop")), this.getDateEnd(proper.getProperty("count_day_stop")));
            this.getDateEnd(proper.getProperty("count_day_stop"));
        }

        if (!allCauseId.isEmpty()) {
            System.out.println("Начинаем формировать файл для вставки (INSERT) дел в таблицу CAUSE:");
            this.a.getInsertCause(allCauseId);
            System.out.println("Файл для вставки (INSERT) дел в таблицу CAUSE сформирован успешно!");
            System.out.println();
            ArrayList<Integer> causeIdEmail = this.a.getIdEmail(allCauseId);

            if (!causeIdEmail.isEmpty()) {

                ArrayList<String> fileNameAttach = new ArrayList();
                ArrayList<Integer> documentIdAttach = new ArrayList();
                if (this.getDocType().length() > 0) {

                    documentIdAttach = this.a.getIdDocAttach(causeIdEmail);
                    fileNameAttach = this.a.getFileNameAttache(documentIdAttach);
                }

                ArrayList<Integer> documentIdShablon = this.a.getIdDocShablon(causeIdEmail);
                if (!documentIdAttach.isEmpty()) {
                    System.out.println("Начинаем формировать файл для вставки (INSERT) прикреплённых файлов в таблицу DOCUMENTS:");
                    this.a.getInsertDocumentAttach(documentIdAttach);
                    System.out.println("Файл для вставки (INSERT) прикреплённых файлов в таблицу DOCUMENTS сформирован успешно!");
                    System.out.println();
                }

                System.out.println("Начинаем формировать файл для вставки (INSERT) файлов-шаблонов в таблицу DOCUMENTS:");
                int countDocShablon = documentIdShablon.size();
                int z = 50;
                int operacion = (int)Math.ceil((double)countDocShablon / (double)z);

                int fId;
                for(fId = 0; fId < operacion; ++fId) {
                    ArrayList<Integer> document123 = new ArrayList();
                    if (countDocShablon >= z) {
                        document123.addAll(documentIdShablon.subList(countDocShablon - z, countDocShablon - 1));
                        countDocShablon -= 50;
                    } else {
                        document123.addAll(documentIdShablon.subList(0, countDocShablon - 1));
                    }

                    this.a.getInsertDocumentShablon(document123);
                    System.out.println();
                }

                System.out.println("Файл для вставки (INSERT) файлов-шаблонов в таблицу DOCUMENTS сформирован успешно!");
                System.out.println();
                System.out.println("Начинаем выгружать файлы-шаблоны из БД");
                fId = 0;

                for(Iterator var12 = documentIdShablon.iterator(); var12.hasNext(); ++fId) {
                    int docId = (Integer)var12.next();
                    this.a.getLoadDocument(docId);
                    if (fId % 50 == 0) {
                        System.out.println();
                    }

                    System.out.print(".");
                }

                System.out.println();
                System.out.println("Выгружено " + fId + " файлов.");
                System.out.println("Файлы-шаблоны из БД выгружены успешно!");
                System.out.println();
                if (!fileNameAttach.isEmpty()) {
                    System.out.println("Начинаем выгружать прикреплённые файлы");
                    this.a.getLoadAttach(fileNameAttach);
                    System.out.println("Прикреплённые файлы выгружены успешно!");
                    System.out.println();
                }

                Calendar c = new GregorianCalendar();
                String dataDay = String.valueOf(c.get(5));
                if (dataDay.length() == 1) {
                    dataDay = "0" + dataDay;
                }

                String dataMonth = String.valueOf(c.get(2) + 1);
                if (dataMonth.length() == 1) {
                    dataMonth = "0" + dataMonth;
                }

                String dataYear = String.valueOf(c.get(1));
                String dataH = String.valueOf(c.get(11));
                if (dataH.length() == 1) {
                    dataH = "0" + dataH;
                }

                String dataM = String.valueOf(c.get(12));
                if (dataM.length() == 1) {
                    dataM = "0" + dataM;
                }

                StringBuilder sb = new StringBuilder();
                sb.append(dataDay);
                sb.append(".");
                sb.append(dataMonth);
                sb.append(".");
                sb.append(dataYear);
                sb.append(" ");
                sb.append(dataH);
                sb.append(":");
                sb.append(dataM);

                try {
                    fileDateUpd.createNewFile();
                    FileWork.write(fileDateUpd, sb.toString());
                } catch (IOException var19) {
                    var19.printStackTrace();
                }
            } else {
                System.out.println("Дела с указаным EMAIL не найдены");
            }
        } else {
            System.out.println("Не выбрано ни одного из всех дел!");
        }

    }
}

