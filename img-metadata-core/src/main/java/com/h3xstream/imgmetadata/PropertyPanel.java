package com.h3xstream.imgmetadata;

import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PropertyPanel {

    private JPanel propertiesPanel;
    private JPanel messagePanel;
    private JPanel activePanel; //Containing either the messagePanel or the propertiesPanel

    //Components of the Properties Panel
    private JTable table;
    private static String[] columns = {"Property","Value"};
    private DefaultTableModel tableModel;

    //Components of the Message Panel
    private JLabel labelMessage;
    private static final String NO_METADATA_FOUND = "No metadata found";

    private PropertyPanelController controller;

    public PropertyPanel(PropertyPanelController controller) {
        buildPropertyTable();
        this.controller = controller;
    }

    /**
     * This method must be called ASAP after its instantiation
     */
    private void buildPropertyTable() {

        //Table grid
        tableModel = new DefaultTableModel(new String[][] {}, columns) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int col, int row) {
                return false;
            }
        };
        this.table = new JTable( tableModel );

        //Properties Panel
        this.propertiesPanel = new JPanel();
        propertiesPanel.setName("Properties Panel");
        propertiesPanel.setLayout(new BorderLayout());
        propertiesPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(0).setMaxWidth(300);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        buildSaveOptions(propertiesPanel);

        //Message Panel
        this.messagePanel = new JPanel();
        messagePanel.setName("Message Panel");
        this.labelMessage = new JLabel(NO_METADATA_FOUND);
        messagePanel.add(labelMessage);

        //Container
        this.activePanel = new JPanel();
        activePanel.setLayout(new BorderLayout());

        setActivePanel(messagePanel);
    }

    private void buildSaveOptions(Container container) {
        JPanel buttonContainer = new JPanel(new FlowLayout());

        JButton buttonCopy = new JButton("Copy to clipboard");
        JButton buttonSave = new JButton("Save to file");
        buttonCopy.addActionListener(new CopyScriptToClipboard());
        buttonSave.addActionListener(new SaveScriptToFile());

        buttonContainer.add(buttonCopy);
        buttonContainer.add(buttonSave);

        container.add(buttonContainer,BorderLayout.SOUTH);
    }

    private void setActivePanel(JPanel panel) {
        activePanel.removeAll();
        activePanel.add(panel, BorderLayout.CENTER);
        //Update UI
        activePanel.validate();
        activePanel.repaint();
    }

    public JPanel getComponent() {
        Log.debug("Active panel : "+activePanel.getName());
        return activePanel;
    }

    public void clearProperties() {
        setActivePanel(messagePanel); //Switch to message panel
        labelMessage.setText(NO_METADATA_FOUND); //Replace potential error message (previously displayed)
        tableModel.setRowCount(0);
    }

    public void addProperty(String key, String value) {
        setActivePanel(propertiesPanel); //Switch to properties panel
        tableModel.addRow(new String[]{key, value});
    }

    public void displayErrorMessage(String errorMessage) {
        if(tableModel.getRowCount() == 0)
            setActivePanel(messagePanel); //Switch to message panel
        labelMessage.setText(errorMessage);
    }


    /// Actions display at the bottom of the panel

    private class CopyScriptToClipboard implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.copyToClipboard(getPropertiesToString());
        }
    }

    private class SaveScriptToFile implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.saveToFile(getPropertiesToString(), PropertyPanel.this.getComponent());
        }
    }

    /**
     * Expose to the save option a text representation
     * @return
     */
    protected String getPropertiesToString() {
        int columns = tableModel.getColumnCount();
        StringBuilder buffer = new StringBuilder();
        for(int i=0;i<tableModel.getRowCount();i++) {
            for(int j=0;j<columns;j++) {
                String value = (String) tableModel.getValueAt(i,j);
                buffer.append(value);
                if(j == 0) {
                    buffer.append('\t');
                }
            }
            buffer.append('\n');
        }
        return buffer.toString();
    }
}
