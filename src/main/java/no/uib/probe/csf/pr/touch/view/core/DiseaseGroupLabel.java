package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents label layout for disease groups (colored based on
 * disease category).
 *
 * @author Yehia Farag
 *
 */
public class DiseaseGroupLabel extends VerticalLayout {

    /**
     * Constructor to initialize main attributes
     *
     * @param diseaseSubGroupTitle Sub-group disease title
     * @param styleName Disease category CSS style name
     */
    public DiseaseGroupLabel(String diseaseSubGroupTitle, String styleName) {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(24, Unit.PIXELS);
        this.setStyleName(styleName);
        this.setMargin(new MarginInfo(false, true, false, false));
        this.setDescription(diseaseSubGroupTitle);
        Label label = new Label(diseaseSubGroupTitle);
        label.addStyleName("paddingleft20");
        this.addComponent(label);
        this.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
    }

}
