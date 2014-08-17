package org.zaproxy.zap.extension.imgmetadata;

import org.apache.commons.configuration.FileConfiguration;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.httppanel.Message;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelView;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModel;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModelEvent;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModelListener;

import javax.swing.*;
import java.awt.*;

public class ResponseImageMetadataView implements HttpPanelView, HttpPanelViewModelListener {

    public static final String NAME = ResponseImageMetadataView.class.getName();
    private HttpPanelViewModel model;

    private JPanel mainPanel;

    public ResponseImageMetadataView(HttpPanelViewModel model) {
        this.model = model;

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(new JLabel("Hello!")));

        this.model.addHttpPanelViewModelListener(this);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getCaptionName() {
        return NAME;
    }

    @Override
    public String getTargetViewName() {
        return "";
    }

    @Override
    public int getPosition() {
        return 2;
    }

    @Override
    public JComponent getPane() {
        return mainPanel;
    }

    @Override
    public void setSelected(boolean b) {

    }

    @Override
    public void save() {
    }

    @Override
    public HttpPanelViewModel getModel() {
        return model;
    }

    @Override
    public boolean isEnabled(Message message) {
        return isImage(message);
    }

    @Override
    public boolean hasChanged() {
        return false;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean b) {
    }

    @Override
    public void setParentConfigurationKey(String s) {
    }

    @Override
    public void loadConfiguration(FileConfiguration fileConfiguration) {
    }

    @Override
    public void saveConfiguration(FileConfiguration fileConfiguration) {
    }

    static boolean isImage(final Message aMessage) {
        if (aMessage instanceof HttpMessage) {
            HttpMessage httpMessage = (HttpMessage) aMessage;

            if (httpMessage.getResponseBody() == null) {
                return false;
            }

            return httpMessage.getResponseHeader().isImage();
        }

        return false;
    }

    @Override
    public void dataChanged(HttpPanelViewModelEvent httpPanelViewModelEvent) {

    }
}
