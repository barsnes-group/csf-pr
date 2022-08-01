package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;

/**
 * This class represents a button layout for dataset and publication pop-up
 * information.
 *
 * @author Yehia Farag
 */
public class PopupWrapperBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main pop-up window layout.
     */
    private final PopupWindowFrame popupPanel;

    /**
     * Constructor to initialize the main attributes for dataset button.
     *
     * @param quantDS quantification dataset object.
     * @param btnName The caption of the button.
     * @param publicationAuthor The author of the publication name.
     */
    public PopupWrapperBtn(QuantDataset quantDS, String btnName, String publicationAuthor) {
        this.addLayoutClickListener(PopupWrapperBtn.this);
        this.setHeight(80, Unit.PIXELS);
        this.setWidth(200, Unit.PIXELS);
        this.setStyleName("tabbtn");
        this.addStyleName("margintop");
        this.addStyleName("marginbottom");
        this.setDescription("Click to view dataset information");
        Label btnLabel = new Label(btnName);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        this.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);

        DatasetInformationOverviewLayout infoLayout = new DatasetInformationOverviewLayout(quantDS, false);
        popupPanel = new PopupWindowFrame(publicationAuthor, new VerticalLayout(infoLayout));
        popupPanel.setFrameHeight(550);
        popupPanel.setFrameWidth(1000);

        this.setExpandRatio(btnLabel, 0.99f);

    }
    private String pubmidID;

    /**
     * Constructor to initialize the main attributes for publication button.
     *
     * @param btnName The caption of the button.
     * @param pubmedId The author of the publication name.
     */
    public PopupWrapperBtn(String btnName, String pubmedId) {
        this.addLayoutClickListener(PopupWrapperBtn.this);
        this.setDescription("Click to view publication information");
        this.setHeight(80, Unit.PIXELS);
        this.setWidth(200, Unit.PIXELS);
        Label btnLabel = new Label(btnName);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        this.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
        this.setStyleName("tabbtn");
        this.addStyleName("margintop");
        this.addStyleName("marginbottom");

        this.pubmidID = pubmedId;
        popupPanel = null;
        this.setExpandRatio(btnLabel, 0.99f);

    }

    /**
     * On click on button.
     *
     * @param event The user click event.
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (popupPanel == null) {
            Page.getCurrent().open("http://www.ncbi.nlm.nih.gov/pubmed/" + pubmidID, "_blank");
        } else {
            popupPanel.view();
        }
    }

    /**
     * Generate publication pup-up form layout.
     *
     * @return Publication form container layout
     */
    private VerticalLayout initPublicationLayout(Object[] publicationData) {

        VerticalLayout publicationlayout = new VerticalLayout();
        publicationlayout.setWidth(500, Unit.PIXELS);
        publicationlayout.setHeightUndefined();
        publicationlayout.setSpacing(true);
        publicationlayout.setStyleName("publicationstyle");

        Label pubmedIdLabel = new Label("<h5>PubMed ID: <a class='link' href='http://www.ncbi.nlm.nih.gov/pubmed/" + publicationData[0].toString() + "' target='_blank'  >" + publicationData[0].toString() + "</a></h5>");

        pubmedIdLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(pubmedIdLabel);
        pubmedIdLabel.setHeight(40, Unit.PIXELS);

        Label proteinsNumLabel = new Label("<h5>#Proteins: " + publicationData[5].toString() + "</h5>");
        proteinsNumLabel.setDescription("Number of publication proteins " + publicationData[4].toString());
        proteinsNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(proteinsNumLabel);
        proteinsNumLabel.setHeight(40, Unit.PIXELS);

        Label uproteinsNumLabel = new Label("<h5>#Publication Specific Proteins: " + publicationData[4].toString() + "</h5>");
        uproteinsNumLabel.setDescription("Number of publication specific proteins " + publicationData[4].toString());
        uproteinsNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(uproteinsNumLabel);
        uproteinsNumLabel.setHeight(40, Unit.PIXELS);

        Label PeptidesNumLabel = new Label("<h5>#Peptides: " + publicationData[7].toString() + "</h5>");
        PeptidesNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(PeptidesNumLabel);
        PeptidesNumLabel.setDescription("Number of publication peptides " + publicationData[7].toString());
        PeptidesNumLabel.setHeight(40, Unit.PIXELS);

        Label uPeptidesNumLabel = new Label("<h5>#Publication Specific Peptides: " + publicationData[6].toString() + "</h5>");
        uPeptidesNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(uPeptidesNumLabel);
        uPeptidesNumLabel.setDescription("Number of publication specific  peptides " + publicationData[6].toString());
        uPeptidesNumLabel.setHeight(40, Unit.PIXELS);

        Label titleLabel = new Label("<textarea rows='5' cols='72' readonly >" + publicationData[3].toString() + "</textarea>");
        titleLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(titleLabel);
        titleLabel.setHeight(100, Unit.PIXELS);

        return publicationlayout;

    }

}
