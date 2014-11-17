package com.h3xstream.imgmetadata;

import com.esotericsoftware.minlog.Log;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Action that are trigger by the panel.
 */
public class PropertyPanelController {

    public void copyToClipboard(String code) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard ();
        clip.setContents(new StringSelection(code), null);
    }

    public void saveToFile(String code, Component parent) {
        new SaveFileChooser().saveScriptToFile(code,"",parent,this);
    }

    public void fileSaveSuccess(String fileName) {
        Log.debug(String.format("Script '%s' saved with success!", fileName));
    }

    public void fileSaveError(String fileName) {
        Log.debug(String.format("Unable to save '%s'", fileName));
    }
}
