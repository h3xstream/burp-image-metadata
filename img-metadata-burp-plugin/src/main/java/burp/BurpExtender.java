package burp;

import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class BurpExtender implements IBurpExtender,IMessageEditorTabFactory {

    public IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;


    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks) {

        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        this.callbacks.setExtensionName("Image Metadata");

        Log.setLogger(new Log.Logger() {
            @Override
            protected void print(String message) {
                try {
                    callbacks.getStdout().write(message.getBytes());
                    callbacks.getStdout().write('\n');
                } catch (IOException e) {
                    System.err.println("Error while printing the log : " + e.getMessage()); //Very unlikely
                }
            }
        });
        Log.DEBUG();

        this.callbacks.registerMessageEditorTabFactory(this);

    }

    @Override
    public IMessageEditorTab createNewInstance(IMessageEditorController iMessageEditorController, boolean b) {
        return new MetadataEditorTab(this.callbacks,this.helpers);
    }
}
