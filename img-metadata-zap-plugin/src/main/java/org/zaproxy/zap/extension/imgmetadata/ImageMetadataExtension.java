package org.zaproxy.zap.extension.imgmetadata;

import com.esotericsoftware.minlog.Log;
import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.zaproxy.zap.extension.httppanel.component.split.response.ResponseSplitComponent;
import org.zaproxy.zap.view.HttpPanelManager;

import java.util.ResourceBundle;

public class ImageMetadataExtension extends ExtensionAdaptor {

    public static final String NAME = ImageMetadataExtension.class.getName();
    private ResourceBundle messages = null;

    private static final Logger LOGGER = Logger.getLogger(ImageMetadataExtension.class.getName());

    public ImageMetadataExtension() {
        super(NAME);
        initExtension();
    }

    private void initExtension() {
        messages = ResourceBundle.getBundle(this.getClass().getPackage().getName() + ".Messages", Constant.getLocale());

        Log.setLogger(new Log.Logger() {
            @Override
            protected void print(String message) {
                LOGGER.info(message);
            }
        });
        Log.DEBUG();
    }

    @Override
    public String getAuthor() {
        return "Philippe Arteau";
    }

    @Override
    public void hook(ExtensionHook extensionHook) {

        if (getView() != null) {
            HttpPanelManager panelManager = HttpPanelManager.getInstance();
            panelManager.addResponseViewFactory(ResponseSplitComponent.NAME, new ResponseImageMetadataViewFactory());
            panelManager.addResponseDefaultViewSelectorFactory(ResponseSplitComponent.NAME, new ResponseImageMetadataViewSelectorFactory());
        }
    }

    @Override
    public void unload() {
        if (getView() != null) {
            HttpPanelManager panelManager = HttpPanelManager.getInstance();
            panelManager.removeResponseViewFactory(ResponseSplitComponent.NAME,
                    ResponseImageMetadataViewFactory.NAME);
            panelManager.removeResponseViews(
                    ResponseSplitComponent.NAME,
                    ResponseImageMetadataView.NAME,
                    ResponseSplitComponent.ViewComponent.BODY);

            panelManager.removeResponseDefaultViewSelectorFactory(
                    ResponseSplitComponent.NAME,
                    ResponseImageMetadataViewFactory.NAME);
            panelManager.removeResponseDefaultViewSelectors(ResponseSplitComponent.NAME,
                    ResponseImageMetadataViewSelector.NAME,
                    ResponseSplitComponent.ViewComponent.BODY);
        }
    }

    public String getMessage(String msgId) {
        return messages.getString(msgId);
    }


}
