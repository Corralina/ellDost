package com.company;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Setting {
    public Setting() {
    }

    public Properties properties() throws IOException {
        FileInputStream w = null;
        Properties proper = new Properties();

        try {
            w = new FileInputStream("C:\\programm\\el_dost\\conf\\options.ini");
            proper.load(w);
            w.close();
            return proper;
        } catch (FileNotFoundException var4) {
            System.out.println(var4.getMessage());
            return null;
        } catch (IOException var5) {
            System.out.println(var5.getMessage());
            return null;
        }
    }
}
