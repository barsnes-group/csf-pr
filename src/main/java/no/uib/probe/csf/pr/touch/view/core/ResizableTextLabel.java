package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.Label;

/**
 * This class represents label with scalable font size based on label size.
 *
 * @author Yehia Farag
 */
public class ResizableTextLabel extends Label {

    /**
     * Constructor to initialize the main attributes.
     *
     * @param textValue the label text value
     */
    public ResizableTextLabel(String textValue) {
        super(textValue);
        this.addStyleName("linehight150");
    }

    /**
     * Default constructor to initialize the main attributes.
     */
    public ResizableTextLabel() {
        super();
        this.addStyleName("linehight150");
    }

    /**
     * Resize the font on changing the label container height.
     * @param height the hight of the container label
     * @param unit the height unit
     */
    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);
        if (unit.equals(Unit.PIXELS)) {
            this.removeStyleName("xxsmallfont");
            this.removeStyleName("xsmallfont");
            this.removeStyleName("smallfont");
            if (height < 15) {
                this.addStyleName("xxsmallfont");
            } else if (height >= 15 && height < 20) {
                this.addStyleName("xsmallfont");
            } else {
                this.addStyleName("smallfont");
            }

        }
    }

}
