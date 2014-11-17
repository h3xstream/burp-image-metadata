package com.h3xstream.imgmetadata;

import javax.swing.*;
import java.awt.*;

/**
 * Not an automated test..
 */
public class PropertyPanelTest {

    public static void main(String[] args) {
        //panelEmpty();
        panelWithProps();
    }

    private static void panelEmpty() {
        PropertyPanel panel = new PropertyPanel(new PropertyPanelController());
        display(panel.getComponent());
    }

    private static void panelWithProps() {
        PropertyPanel panel = new PropertyPanel(new PropertyPanelController());

        panel.clearProperties();
        panel.addProperty("Property 1", "ABC");
        panel.addProperty("Property 2", "DEF");

        display(panel.getComponent());
    }

    private static void display(Component comp) {

        JFrame window = new JFrame();

        window.add(comp);

        window.setTitle("Mock Window (Test)");
        window.pack();
        window.setSize(600, 400);
        window.setVisible(true);
    }
}
