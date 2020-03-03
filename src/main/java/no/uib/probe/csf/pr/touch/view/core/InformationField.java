package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents information field for Datasets, Proteins, and peptides
 * that is used in different forms
 *
 * @author Yehia Farag
 */
public class InformationField extends VerticalLayout {

    /**
     * The value label.
     */
    private final Link valueLabel;

    /**
     * The title label.
     */
    private final Label titleLabel;

    /**
     * Construction to initialize the main attributes.
     *
     * @param title the field title.
     */
    public InformationField(String title) {
        this.setHeight(100, Unit.PERCENTAGE);
        this.setWidth(100, Unit.PERCENTAGE);
        this.setSpacing(false);
        this.setMargin(new MarginInfo(false, false, false, false));
        titleLabel = new Label(title);
        this.addComponent(titleLabel);
        titleLabel.setContentMode(ContentMode.HTML);
        titleLabel.setStyleName("captionlabel");
        titleLabel.setHeight(100, Unit.PERCENTAGE);
        titleLabel.setWidth(200, Unit.PIXELS);
        titleLabel.addStyleName("minhight200");
        this.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);

        valueLabel = new Link();
        valueLabel.setStyleName("link");
        valueLabel.setTargetName("_blank");
        valueLabel.setCaptionAsHtml(true);
        valueLabel.setHeight(100, Unit.PERCENTAGE);
        valueLabel.setWidth(90, Unit.PERCENTAGE);
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.TOP_LEFT);

    }

    /**
     * Set the field value
     *
     * @param object The value could be String, Double, Integer values.
     * @param urlAddress If the value has external link URL is not null.
     */
    public void setValue(Object object, String urlAddress) {

      
        boolean isNumber = true;
        try {
            Integer.valueOf(object.toString().trim());
        } catch (NumberFormatException exp) {
            isNumber = false;
        }
        String stringValue = object.toString();          
         int rowNumber = Math.min(4,(stringValue.toCharArray().length/40)+1);
        if ((object instanceof Number || isNumber) && urlAddress == null) {
            if (object.toString().equalsIgnoreCase("-1")) {
                valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"' style='text-align: left ;width: 100px;' readonly>N/A</textarea>");
            } else {
                valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"' style='text-align: left ;width: 100px;' readonly>" + object + "</textarea>");
            }
            valueLabel.setCaptionAsHtml(true);
            valueLabel.setStyleName("valuelabel");
            valueLabel.setHeight(100, Unit.PERCENTAGE);

        } else if (object.toString().contains("/") && !object.toString().contains("</") && !object.toString().contains("/MS") && object.toString().toCharArray().length < 25) {            
            valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"' style='text-align: left ;width:140px;' readonly>" + object + "</textarea>");            
            valueLabel.setCaptionAsHtml(true);
            valueLabel.setStyleName("valuelabel");

        } else {
               
            if (stringValue == null || stringValue.trim().equalsIgnoreCase("") || stringValue.equalsIgnoreCase("-1") || stringValue.equalsIgnoreCase("Not Available")) {
                valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"' style='text-align: left;' readonly>N/A</textarea>");
                valueLabel.setStyleName("valuelabel");
                return;
            } else if (stringValue.contains("Increase")) {
                valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"'  readonly>" + stringValue + "</textarea>");
                valueLabel.addStyleName("redvaluelabel");
                return;

            } else if (stringValue.contains("Decrease")) {
                valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"'  readonly>" + stringValue + "</textarea>");
                valueLabel.addStyleName("greenvaluelabel");
                return;

            } else if (stringValue.contains("Significant (") && !stringValue.contains("Not Significant (")) {
                valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"'  readonly>" + stringValue + "</textarea>");
                valueLabel.addStyleName("bluevaluelabel");
                return;

            } else if (stringValue.contains("</font>")) {
                valueLabel.setCaption("" + stringValue + "");
            } else if (stringValue.toCharArray().length > 60) {

                valueLabel.setCaptionAsHtml(true);

                valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"'  readonly>" + stringValue + "</textarea>");
                int h = Math.min(((stringValue.toCharArray().length / 100) * 20), 70);
                valueLabel.setHeight(h, Unit.PIXELS);
                titleLabel.setHeight(100, Unit.PERCENTAGE);
                this.setSpacing(false);
                this.setHeightUndefined();
            } else {
                valueLabel.setCaption("<textarea cols='100' rows='"+rowNumber+"'  readonly>" + stringValue + "</textarea>");
            }
            if (urlAddress != null) {
                valueLabel.addStyleName("link");
                valueLabel.addStyleName("valuelink");
                valueLabel.addStyleName("pointer");
                valueLabel.setResource(new ExternalResource(urlAddress));
            } else {
                valueLabel.addStyleName("valuelabel");
            }
        }
    }
}
