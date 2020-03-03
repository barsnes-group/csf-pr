package no.uib.probe.csf.pr.touch.view;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class represents the header layout container (logo, titles)
 *
 * @author Yehia Farag
 */
public class LogoLayout extends HorizontalLayout {

    /**
     * Constructor to initialize the logo of CSF-PR 2.0 logo.
     */
    public LogoLayout() {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(40, Unit.PIXELS);
        this.setSpacing(true);
        //tile logo container
        HorizontalLayout logoTitleContainer = new HorizontalLayout();
        this.addComponent(logoTitleContainer);
        this.setComponentAlignment(logoTitleContainer, Alignment.TOP_LEFT);
        VerticalLayout logo = new VerticalLayout();
        logo.setWidth(100, Unit.PERCENTAGE);
        logo.setHeight(30, Unit.PIXELS);
        Label cLabel = new Label("<a href=''  align='left' style='color:#ffffff;background-color: #4d749f;border-radius:4px;width:35px !important;padding-top: 8px !important;padding-right: 3px !important;padding-left: 3px !important;padding-bottom: 1px;font-weight:bold;text-decoration:none;'>"
                + "<font  align='left' ; style='color:#ffffff; !important;font-weight:bold;text-decoration:none;word-wrap: break-word !important;font-size: 24px;'>"
                + "C"
                + "</font>"
                + "</a> "
                + "<a href='' align='left'  style='text-decoration:none;    margin-left: -2px;'>"
                + "<font  align='left' ; style='margin-left:0px;color:#4d749f;font-weight:bold;text-decoration:none;word-wrap: break-word !important;font-size: 24px; '>"
                + "SF Proteome Resource"
                + "<font size='2'>"
                + " v2.0"
                + "</font>"
                + "</font>"
                + "</a>");
        cLabel.setContentMode(ContentMode.HTML);
        cLabel.setStyleName(Reindeer.LABEL_SMALL);
        cLabel.setWidth(100, Unit.PERCENTAGE);
        cLabel.setHeight(100, Unit.PERCENTAGE);
        logo.addComponent(cLabel);
        logo.setComponentAlignment(cLabel, Alignment.TOP_CENTER);
        logoTitleContainer.addComponent(logo);
    }

}
