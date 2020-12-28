package com.company;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWork {
    public FileWork() {
    }

    public static void write(File file, String text) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                out.print(text);
            } finally {
                out.close();
            }

        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }

    public static String read(File file) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));

            String s;
            try {
                while((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }

        return sb.toString();
    }
}

