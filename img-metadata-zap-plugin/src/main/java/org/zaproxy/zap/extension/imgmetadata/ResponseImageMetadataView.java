package org.zaproxy.zap.extension.imgmetadata;

import com.drew.imaging.ImageProcessingException;
import com.h3xstream.imgmetadata.MetadataExtractor;
import com.h3xstream.imgmetadata.PropertyPanel;
import org.apache.commons.configuration.FileConfiguration;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.httppanel.Message;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelView;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModel;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModelEvent;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModelListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ResponseImageMetadataView implements HttpPanelView, HttpPanelViewModelListener {

    public static final String NAME = ResponseImageMetadataView.class.getName();
    private HttpPanelViewModel model;

    private PropertyPanel propertyPanel;

    public ResponseImageMetadataView(HttpPanelViewModel model) {
        this.model = model;


        propertyPanel = new PropertyPanel();

        this.model.addHttpPanelViewModelListener(this);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getCaptionName() {
        return "Metadata";
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
        return propertyPanel.getComponent();
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
    public void dataChanged(HttpPanelViewModelEvent event) {
        HttpMessage httpMessage = (HttpMessage) model.getMessage();

        if (isImage(httpMessage)) {
            byte[] respBytes = httpMessage.getResponseBody().getBytes();

            try {
                //Extract properties
                Map<String,String> tags = new MetadataExtractor().readMetadata(respBytes, 0);

                //Update the table
                Set<Map.Entry<String,String>> entrySet = tags.entrySet();

                propertyPanel.clearProperties();
                int i=0;
                for(Map.Entry<String,String> tag : entrySet) {
                    propertyPanel.addProperty(tag.getKey(), tag.getValue());
                }

            } catch (ImageProcessingException e) {
                propertyPanel.displayErrorMessage(e.getMessage());
            } catch (IOException e) {
                propertyPanel.displayErrorMessage(e.getMessage());
            }
        } else {
            propertyPanel.clearProperties();
        }
    }
}
