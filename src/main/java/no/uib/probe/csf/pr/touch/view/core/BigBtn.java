package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * This class represents the welcome page and left side panels buttons.
 *
 * @author Yehia Farag
 */
public abstract class BigBtn extends HorizontalLayout implements LayoutEvents.LayoutClickListener, MouseEvents.ClickListener {

    /**
     * Main icon image for the button.
     */
    private final Image buttonIcon;
    /**
     * Main icon container.
     */
    private final ImageContainerBtn thumbContainer;

    /**
     * Get the main icon container layout
     *
     * @return thumbContainer The icon container.
     */
    public ImageContainerBtn getThumbBtn() {
        return thumbContainer;
    }

    /**
     * Constructor to initialize the main attributes.
     *
     * @param header main header title.
     * @param text The button text.
     * @param imgUrl icon URL.
     */
    public BigBtn(String header, String text, String imgUrl) {
        this.setWidthUndefined();
        this.setMargin(false);
        int lineHeight = 35;
        this.addLayoutClickListener(BigBtn.this);
        this.setStyleName("bigbtn");

        buttonIcon = new Image();
        buttonIcon.setSource(new ThemeResource(imgUrl));

        buttonIcon.addStyleName("blink");
        this.addComponent(buttonIcon);
        String labelText = "<b>" + header + "</b><br/><font size='1'>" + text + "</font>";

        Label btnLabel = new Label(labelText);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        this.setComponentAlignment(btnLabel, Alignment.MIDDLE_LEFT);
        btnLabel.setWidth(240, Unit.PIXELS);
        btnLabel.setHeight(lineHeight, Unit.PIXELS);

        thumbContainer = new ImageContainerBtn() {

            @Override
            public void onClick() {
                BigBtn.this.onClick();
            }

        };

        thumbContainer.updateIcon(new ThemeResource(imgUrl));
        thumbContainer.setEnabled(true);
        thumbContainer.setReadOnly(false);

        buttonIcon.setWidth(70, Unit.PIXELS);
        buttonIcon.setHeight(70, Unit.PIXELS);
        thumbContainer.setWidth(40, Unit.PIXELS);
        thumbContainer.setHeight(40, Unit.PIXELS);

    }

    /**
     * Layout clicked listener implementation.
     *
     * @param event Click the button event.
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        onClick();
    }

    /**
     * Do the implemented action. 
     * @param event Click the button event.
     */
    @Override
    public void click(MouseEvents.ClickEvent event) {
        onClick();
    }

    /**
     * On click do the button function (to be implemented).
     */
    public abstract void onClick();

}
