package org.zaproxy.zap.extension.imgmetadata;

import org.zaproxy.zap.extension.httppanel.Message;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelDefaultViewSelector;

public class ResponseImageMetadataViewSelector implements HttpPanelDefaultViewSelector {

    public static final String NAME = ResponseImageMetadataViewSelector.class.getName();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean matchToDefaultView(Message aMessage) {
        return ResponseImageMetadataView.isImage(aMessage);
    }

    @Override
    public String getViewName() {
        return ResponseImageMetadataView.NAME;
    }

    @Override
    public int getOrder() {
        return 30;
    }
}
