package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;

/**
 * This class represents the click-able label component for comparison name in
 * protein datasets table.
 *
 * @author Yehia Farag
 */
public class ComparisonLable extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main pop-up window layout.
     */
    private final PopupWindowFrame popupWindow;

    /**
     * Constructor to initialize the main attributes.
     *
     * @param comparison The quant comparison object
     * @param itemId The quant comparison index.
     * @param quantProtein The main selected protein
     * @param dataset The quant dataset object.
     */
    public ComparisonLable(QuantDiseaseGroupsComparison comparison, Object itemId, QuantProtein quantProtein, QuantDataset dataset) {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        this.setSpacing(false);
        this.addStyleName("pointer");
        this.addLayoutClickListener(ComparisonLable.this);
        String[] headerI = comparison.getComparisonHeader().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
        String diseaseColor = comparison.getDiseaseCategoryColor();

        Label labelI = new Label("<font  style=' color:" + diseaseColor + "'>" + headerI[0] + "</font>");
        labelI.setWidth(100, Unit.PERCENTAGE);
        labelI.addStyleName(ValoTheme.LABEL_SMALL);
        labelI.addStyleName(ValoTheme.LABEL_TINY);
        labelI.addStyleName("overflowtext");
        labelI.addStyleName("nomargin");
        labelI.setContentMode(ContentMode.HTML);
        this.addComponent(labelI);
        this.setComponentAlignment(labelI, Alignment.BOTTOM_CENTER);

        VerticalLayout spacer = new VerticalLayout();
        spacer.setStyleName(ValoTheme.LAYOUT_WELL);
        spacer.setWidth(100, Unit.PERCENTAGE);
        spacer.setHeight(2, Unit.PIXELS);
        spacer.setMargin(new MarginInfo(false, true, false, true));
        spacer.addStyleName("nomargin");
        spacer.addStyleName("alignmiddle");
        spacer.addStyleName("margintop-5");
        this.addComponent(spacer);
        this.setComponentAlignment(spacer, Alignment.MIDDLE_CENTER);

        Label labelII = new Label("<font  style='color:" + diseaseColor + "'>" + headerI[1] + "</font>");
        labelII.setWidth(100, Unit.PERCENTAGE);
        labelII.addStyleName("overflowtext");
        labelII.addStyleName("nomargin");
        labelII.addStyleName(ValoTheme.LABEL_SMALL);
        labelII.addStyleName(ValoTheme.LABEL_TINY);
        labelII.setContentMode(ContentMode.HTML);
        this.addComponent(labelII);
        this.setComponentAlignment(labelII, Alignment.TOP_CENTER);

        String[] gr = comparison.getComparisonFullName().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
        String updatedHeader = ("Numerator: " + gr[0] + "<br/>Denominator: " + gr[1] + "<br/>Disease: " + comparison.getDiseaseCategory());
        this.setDescription("View comparison details <br/>" + updatedHeader);

        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth(100, Unit.PERCENTAGE);
        popupBody.setHeight(98, Unit.PERCENTAGE);
        String protName = quantProtein.getUniprotProteinName().trim();
        if (protName.equalsIgnoreCase("")) {
            protName = quantProtein.getPublicationProteinName();
        }
        String title = "Dataset and Protein Information (" + protName + ")";
        popupWindow = new PopupWindowFrame(title, popupBody);
        popupWindow.setFrameHeight(490);
        popupWindow.setFrameWidth(1500);
        TabSheet tab = new TabSheet();
        tab.setHeight(97.0f, Unit.PERCENTAGE);
        tab.setWidth(100.0f, Unit.PERCENTAGE);
        tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tab.addStyleName("transparentframe");
        popupBody.addComponent(tab);
        popupBody.setComponentAlignment(tab, Alignment.TOP_CENTER);
        ProteinsInformationOverviewLayout proteinInfoLayout = new ProteinsInformationOverviewLayout();
        VerticalLayout protInfoPopup = generatePopupLayout(proteinInfoLayout);
        proteinInfoLayout.updateProteinsForm(quantProtein, quantProtein.getUniprotAccessionNumber(), quantProtein.getURL(), quantProtein.getUniprotProteinName());
        tab.addTab(protInfoPopup, "Protein");
        DatasetInformationOverviewLayout dsOverview = new DatasetInformationOverviewLayout(dataset, false);
        VerticalLayout infoPopup = generatePopupLayout(dsOverview);
        tab.addTab(infoPopup, "Dataset");
        tab.addSelectedTabChangeListener((TabSheet.SelectedTabChangeEvent event) -> {
            if (event.getTabSheet().getTabPosition((tab.getTab(tab.getSelectedTab()))) == 0) {
                popupWindow.setFrameHeight(490);
            } else {
                popupWindow.setFrameHeight(780);
            }
        });
    }

    /**
     * Generate the main pop-up layout wrapper for the comparison information
     * body.
     *
     * @param infoLayout the main body layout.
     */
    private VerticalLayout generatePopupLayout(VerticalLayout infoLayout) {
        VerticalLayout popupBodyLayout = new VerticalLayout();
        popupBodyLayout.setWidth(99, Unit.PERCENTAGE);
        popupBodyLayout.setHeight(99, Unit.PERCENTAGE);
        popupBodyLayout.addStyleName("padding20");
        popupBodyLayout.setSpacing(true);
        popupBodyLayout.setStyleName("pupupbody");
        popupBodyLayout.addComponent(infoLayout);
        return popupBodyLayout;
    }

    /**
     * On click view pop-up window.
     *
     * @param event User click action.
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.view();
    }

    /**
     * View pop-up window.
     */
    public void openComparisonPopup() {
        popupWindow.view();

    }

}
