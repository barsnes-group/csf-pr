package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.view.core.DatasetButtonsContainerLayout;
import no.uib.probe.csf.pr.touch.view.core.PopupWindowFrame;

/**
 * This class represents dataset information container that contain 2 taps one
 * for dataset buttons and second for publication buttons every button is a
 * pop-up panel that has the dataset or publication information
 *
 * @author Yehia Farag
 */
public abstract class DatasetInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * Dataset buttons container that is used for dataset tab.
     */
    private final DatasetButtonsContainerLayout datasetsPopupLayout;
    /**
     * Publication buttons container that is used for dataset tab.
     */
    private final DatasetButtonsContainerLayout publicationPopupLayout;
    /**
     * List of arrays of objects, every array contains one publication
     * information .
     */
    private Map<String,Object[]> publicationList;
    /**
     * Main tab-sheet that has 2 tabs one for dataset and one for publication.
     */
    private final TabSheet tab;
    /**
     * Main pop-up container body
     */
    private final VerticalLayout popupBody;
    /**
     * Main Window layout.
     */
    private final PopupWindowFrame popupWindowFrame;

    /**
     * Constructor to initialize the main attributes
     *
     * @param publicationList List of publication object arrays.
     */
    public DatasetInformationWindow(Map<String,Object[]> publicationList) {

        popupBody = new VerticalLayout();
        popupWindowFrame = new PopupWindowFrame("Datasets and Publications", popupBody);

        this.addLayoutClickListener(DatasetInformationWindow.this);
        this.setHeight(10, Unit.PIXELS);

        tab = new TabSheet();
        tab.setHeight(100.0f, Unit.PERCENTAGE);
        tab.setWidth(100.0f, Unit.PERCENTAGE);
        tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tab.addStyleName("transparentframe");

        popupBody.addComponent(tab);
        popupBody.setComponentAlignment(tab, Alignment.TOP_CENTER);

        datasetsPopupLayout = new DatasetButtonsContainerLayout((int) popupWindowFrame.getFrameWidth());
        if (datasetsPopupLayout.getColNumber() == 1) {
            popupWindowFrame.setFrameWidth(260);
        }

        tab.addTab(datasetsPopupLayout, "Datasets");
        publicationPopupLayout = new DatasetButtonsContainerLayout((int) popupWindowFrame.getFrameWidth());
        tab.addTab(publicationPopupLayout, "Publications");
        tab.addSelectedTabChangeListener((TabSheet.SelectedTabChangeEvent event) -> {
            if (event.getTabSheet().getTabPosition((tab.getTab(tab.getSelectedTab()))) == 0) {
                popupWindowFrame.setFrameHeight(193 + (datasetsPopupLayout.getRowcounter() * 100));
            } else {
                popupWindowFrame.setFrameHeight(193 + (publicationPopupLayout.getRowcounter() * 100));
            }
        });

        if (publicationList == null) {
            this.publicationList = publicationList;
            return;
        }
        publicationPopupLayout.setPublicationData(publicationList);

    }

    /**
     * Update the dataset buttons container
     *
     * @param dsObjects Set of dataset information objects
     */
    public void updateData(Collection<QuantDataset> dsObjects) {
        datasetsPopupLayout.setInformationData(dsObjects);

        if (publicationList == null) {
            Set<String> publicationMap = new LinkedHashSet<>();
            for (QuantDataset quantDS : dsObjects) {
                publicationMap.add(quantDS.getPubMedId());

            }
            publicationPopupLayout.setPublicationData(getPublicationsInformation(publicationMap));
        }
        popupWindowFrame.setFrameHeight(173 + (datasetsPopupLayout.getRowcounter() * 100));

    }

    /**
     * Get the publication information required to update the publication
     * buttons data
     *
     * @param pumedIdSet Set of publication PubMed id
     * @return publicationList List of Publication information object arrays
     */
    public abstract Map<String,Object[]>  getPublicationsInformation(Set<String> pumedIdSet);

    /**
     * View/hide the dataset individual buttons window.
     */
    public void view() {
        popupWindowFrame.view();
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        view();
    }
}
