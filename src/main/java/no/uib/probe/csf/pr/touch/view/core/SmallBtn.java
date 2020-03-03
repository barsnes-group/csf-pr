package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents small button with centered icon.
 *
 * @author Yehia Farag
 */
public class SmallBtn extends VerticalLayout {

    /**
     * Constructor to initialize the button layout.
     *
     * @param iconResource Image resource for the centered icon.
     */
    public SmallBtn(Resource iconResource) {
        this.setWidth(25, Unit.PIXELS);
        this.setHeight(25, Unit.PIXELS);
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setSource(iconResource);
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
    }

}
