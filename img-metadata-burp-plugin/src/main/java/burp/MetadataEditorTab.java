package burp;

import com.drew.imaging.ImageProcessingException;
import com.h3xstream.imgmetadata.MetadataExtractor;
import com.h3xstream.imgmetadata.PropertyPanel;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class MetadataEditorTab implements IMessageEditorTab {

    private byte[] message;
    private PropertyPanel propertyPanel;

    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks callbacks;

    MetadataEditorTab(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        this.helpers = helpers;
        this.callbacks = callbacks;

        propertyPanel = new PropertyPanel();

        callbacks.customizeUiComponent(propertyPanel.getComponent());
    }


    @Override
    public String getTabCaption() {
        return "Metadata";
    }

    @Override
    public Component getUiComponent() {
        return propertyPanel.getComponent();
    }

    @Override
    public boolean isEnabled(byte[] respBytes, boolean isRequest) {
        if (isRequest) {
            return false;
        } else { //The tab will appears if the response is a JPG or PNG image
            IResponseInfo responseInfo = helpers.analyzeResponse(respBytes);
            int bodyOffset = responseInfo.getBodyOffset();
            return MetadataExtractor.isJpgFile(respBytes, bodyOffset) || MetadataExtractor.isPngFile(respBytes,bodyOffset);
        }
    }

    @Override
    public void setMessage(byte[] respBytes, boolean b) {
        this.message = respBytes;
        propertyPanel.clearProperties();

        try {
            IResponseInfo responseInfo = helpers.analyzeResponse(respBytes);
            int bodyOffset = responseInfo.getBodyOffset();

            //Extract properties
            Map<String,String> tags = new MetadataExtractor().readMetadata(respBytes,bodyOffset);

            //Update the table
            Set<Map.Entry<String,String>> entrySet = tags.entrySet();

            int i=0;
            for(Map.Entry<String,String> tag : entrySet) {
                propertyPanel.addProperty(tag.getKey(), tag.getValue());
            }

        } catch (ImageProcessingException e) {
            propertyPanel.displayErrorMessage(e.getMessage());
        } catch (IOException e) {
            propertyPanel.displayErrorMessage(e.getMessage());
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
        return this.message;
    }
}
