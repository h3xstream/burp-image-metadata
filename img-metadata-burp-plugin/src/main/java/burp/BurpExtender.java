package burp;

public class BurpExtender implements IBurpExtender,IMessageEditorTabFactory {

    public IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;


    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {

        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        this.callbacks.setExtensionName("Image Metadata");

        this.callbacks.registerMessageEditorTabFactory(this);
    }

    @Override
    public IMessageEditorTab createNewInstance(IMessageEditorController iMessageEditorController, boolean b) {
        return new MetadataEditorTab(this.callbacks,this.helpers);
    }
}
