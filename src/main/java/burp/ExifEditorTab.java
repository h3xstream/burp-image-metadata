package burp;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ExifEditorTab implements IMessageEditorTab {

    private byte[] message;
    private ITextEditor editor;
    private IExtensionHelpers helpers;

    ExifEditorTab(IBurpExtenderCallbacks callbacks,IExtensionHelpers helpers) {
        this.helpers = helpers;
        this.editor = callbacks.createTextEditor();
        editor.setEditable(false);

        this.setMessage("Loading...".getBytes(), false);
    }

    @Override
    public String getTabCaption() {
        return "Exif";
    }

    @Override
    public Component getUiComponent() {
        return editor.getComponent();
    }

    @Override
    public boolean isEnabled(byte[] bytes, boolean isRequest) {
        if(isRequest) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void setMessage(byte[] respBytes, boolean b) {
        this.message = respBytes;

        IResponseInfo responseInfo = helpers.analyzeResponse(respBytes);
        int bodyOffset = responseInfo.getBodyOffset();

        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(respBytes,bodyOffset, respBytes.length - bodyOffset));
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(in,false);

            StringBuilder buffer = new StringBuilder();
            for(Directory dir : metadata.getDirectories()) {
                for (Tag tag : dir.getTags()) {
                    buffer.append(tag.getTagName() +": "+tag.getDescription()+"\n");
                }
            }

            editor.setText(buffer.toString().getBytes());
        } catch (ImageProcessingException e) {
            editor.setText(e.getMessage().getBytes());
        } catch (IOException e) {
            editor.setText(e.getMessage().getBytes());
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
