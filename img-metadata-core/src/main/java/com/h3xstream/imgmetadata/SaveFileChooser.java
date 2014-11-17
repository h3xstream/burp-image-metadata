package com.h3xstream.imgmetadata;


import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SaveFileChooser {

    public static String FILE_CHOOSER = "FILE_CHOOSER";

    public void saveScriptToFile(String code,String filename,Component parent,PropertyPanelController cont) {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Save to file");
        String currentDirectory = new File(".").getAbsolutePath();

        fileChooser.setSelectedFile(new File(currentDirectory,filename));
        fileChooser.setName(FILE_CHOOSER);

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                if(file.createNewFile()) {
                    OutputStream out = new FileOutputStream(file);
                    out.write(code.getBytes());
                    out.close();

                    cont.fileSaveSuccess(file.getAbsolutePath());
                }
            } catch (IOException e) {
                Log.error(e.getMessage(), e);

                cont.fileSaveError(file.getAbsolutePath());
            }
        }
    }
}
