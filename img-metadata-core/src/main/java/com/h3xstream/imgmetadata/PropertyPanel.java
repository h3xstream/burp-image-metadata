package com.h3xstream.imgmetadata;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PropertyPanel {

    private JPanel panel;
    private JTable table;
    private static String[] columns = {"Property","Value"};
    private DefaultTableModel tableModel;

    public PropertyPanel() {
        buildPropertyTable();
    }

    /**
     * This method must be called ASAP after its instantiation
     */
    private void buildPropertyTable() {

        //Table grid
        String[][] dataValues = {};
        tableModel = new DefaultTableModel(new String[][] {}, columns) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int col, int row) {
                return false;
            }
        };
        this.table = new JTable( tableModel );

        //Container (Panel)
        this.panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(0).setMaxWidth(300);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    }

    public JPanel getComponent() {
        return panel;
    }

    public void clearProperties() {
        tableModel.setRowCount(0);
    }

    public void addProperty(String key, String value) {
        tableModel.addRow(new String[]{key, value});
    }

    public void displayErrorMessage(String errorMessage) {
        //TODO: Display error message
    }
}
