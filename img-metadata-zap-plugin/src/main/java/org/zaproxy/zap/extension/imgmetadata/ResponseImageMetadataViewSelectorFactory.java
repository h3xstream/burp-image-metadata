package org.zaproxy.zap.extension.imgmetadata;

import org.zaproxy.zap.extension.httppanel.component.split.response.ResponseSplitComponent;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelDefaultViewSelector;
import org.zaproxy.zap.view.HttpPanelManager;

public class ResponseImageMetadataViewSelectorFactory implements HttpPanelManager.HttpPanelDefaultViewSelectorFactory {

    public static final String NAME = ResponseImageMetadataViewSelectorFactory.class.getName();

    private static HttpPanelDefaultViewSelector defaultViewSelector = null;

    private HttpPanelDefaultViewSelector getDefaultViewSelector() {
        if (defaultViewSelector == null) {
            createViewSelector();
        }
        return defaultViewSelector;
    }

    private synchronized void createViewSelector() {
        if (defaultViewSelector == null) {
            defaultViewSelector = new ResponseImageMetadataViewSelector();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public HttpPanelDefaultViewSelector getNewDefaultViewSelector() {
        return getDefaultViewSelector();
    }

    @Override
    public Object getOptions() {
        return ResponseSplitComponent.ViewComponent.BODY;
    }
}
