package burp;

import com.drew.imaging.ImageProcessingException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ExifEditorTab implements IMessageEditorTab {

    private byte[] message;
    private JPanel panel;
    private JTable table;
    private static String[] columns = {"Property","Value"};
    private DefaultTableModel tableModel;

    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks callbacks;

    ExifEditorTab(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        this.helpers = helpers;
        this.callbacks = callbacks;

        panel = buildPropertyTable();
    }

    private JPanel buildPropertyTable() {

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
        callbacks.customizeUiComponent(panel);

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(0).setMaxWidth(300);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        return panel;
    }

    @Override
    public String getTabCaption() {
        return "Metadata";
    }

    @Override
    public Component getUiComponent() {
        return panel;
    }

    @Override
    public boolean isEnabled(byte[] respBytes, boolean isRequest) {
        if (isRequest) {
            return false;
        } else { //The tab will appears if the response is a JPG or PNG image
            IResponseInfo responseInfo = helpers.analyzeResponse(respBytes);
            int bodyOffset = responseInfo.getBodyOffset();
            return MetadataExtractor.isJpgFile(respBytes,bodyOffset) || MetadataExtractor.isPngFile(respBytes,bodyOffset);
        }
    }

    @Override
    public void setMessage(byte[] respBytes, boolean b) {
        this.message = respBytes;

        try {
            Map<String,String> tags = new MetadataExtractor().readMetadata(respBytes,helpers);

            //Update the table
            Set<Map.Entry<String,String>> entrySet = tags.entrySet();
            tableModel.setRowCount(0);
            int i=0;
            for(Map.Entry<String,String> tag : entrySet) {
                tableModel.addRow(new String[]{tag.getKey(), tag.getValue()});
            }


        } catch (ImageProcessingException e) {
            tableModel.addRow(new String[]{"Error",e.getMessage()});
        } catch (IOException e) {
            tableModel.addRow(new String[]{"Error",e.getMessage()});
        }
    }

    @Override
    public byte[] getMessage() {
        return message;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public byte[] getSelectedData() {
        return null;
    }
}
