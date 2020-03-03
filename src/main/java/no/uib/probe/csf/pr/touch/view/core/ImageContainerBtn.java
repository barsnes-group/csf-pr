package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents button with middle image icon the button supports
 * images updates.
 *
 * @author Yehia Farag
 */
public abstract class ImageContainerBtn extends AbsoluteLayout implements LayoutEvents.LayoutClickListener {

    /**
     * Image object that is used as middle icon.
     */
    private final Image img;
    /**
     * Side description for the button.
     */
    private final Label text;
    /**
     * Parent wrapper layout.
     */
    private boolean hasWrapper = false;

    /**
     * Constructor to initialize main attributes.
     */
    public ImageContainerBtn() {
        img = new Image();
        img.setWidth(100, Unit.PERCENTAGE);
        img.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(img, "left: " + (0) + "px; top: " + (0) + "px;");

        VerticalLayout labelcontainer = new VerticalLayout();
        labelcontainer.setWidth(100, Unit.PERCENTAGE);
        labelcontainer.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(labelcontainer, "left: " + (0) + "px; top: " + (0) + "px;");
        text = new Label() {

            @Override
            public void setValue(String newStringValue) {
                if (newStringValue == null || newStringValue.equalsIgnoreCase("")) {
                    this.setVisible(false);
                } else {
                    this.setVisible(true);
                }
                super.setValue(newStringValue); //To change body of generated methods, choose Tools | Templates.
            }

        };
        text.addStyleName("bubbleframe");
        labelcontainer.addComponent(text);
        text.setWidthUndefined();
        labelcontainer.setComponentAlignment(text, Alignment.MIDDLE_CENTER);
        text.setContentMode(ContentMode.HTML);

        this.setStyleName("bigbtn");
        this.addStyleName("pointer");
        this.addLayoutClickListener(ImageContainerBtn.this);
        this.setReadOnly(true);
        ImageContainerBtn.this.setEnabled(false);

    }

    /**
     *
     * This method responsible for updating button middle icon
     *
     * @param imgResource resource for the middle icon
     */
    public void updateIcon(Resource imgResource) {
        if (imgResource == null) {
            img.setVisible(false);
        } else {
            img.setSource(imgResource);
            img.setVisible(true);
        }
    }

    /**
     *
     * This method responsible for updating button description text.
     *
     * @param descriptionText button description text.
     */
    public void updateText(String descriptionText) {
        text.setValue(descriptionText);
    }

    /**
     * On button clicked.
     *
     * @param event user click event.
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        this.removeStyleName("orangeBorder");
        onClick();
    }

    /**
     * The button has parent wrapper layout.
     *
     * @param hasWrapper the component wrapped in parent container
     */
    public void setHasWrapper(boolean hasWrapper) {
        this.hasWrapper = hasWrapper;
    }

    /**
     * Blinking the button to notify users by changes.
     */
    public void blink() {
        this.addStyleName("orangeBorder");
        if (img.getStyleName().contains("blinkII")) {
            img.removeStyleName("blinkII");
            img.addStyleName("blink");
        } else {
            img.removeStyleName("blinkI");
            img.addStyleName("blinkII");
        }
    }

    /**
     * On click final action.
     */
    public abstract void onClick();

    /**
     * Override enable button to hide on disable.
     *
     * @param enabled enabled the actions on the component
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.removeStyleName("unapplied");
            this.blink();

        } else {
            this.addStyleName("unapplied");
            text.setValue("");
        }
        super.setEnabled(enabled); //To change body of generated methods, choose Tools | Templates.
        if (this.getParent() != null && hasWrapper) {
            this.getParent().setEnabled(enabled);
        }
        this.setVisible(enabled);
    }

    /**
     * Reset button to initial state. to be override
     */
    public void reset() {
    }
;

}
