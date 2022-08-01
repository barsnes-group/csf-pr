package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.server.ThemeResource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;

/**
 * This class represents the dataset information button that is used in the
 * heat-map right control buttons layout
 *
 * @author Yehia Farag
 */
public abstract class DatasetInformationBtn extends ImageContainerBtn {

    /**
     * Datasets container pop-up window that contains the individual datasets
     * buttons.
     */
    private final DatasetInformationWindow studiesInformationWindow;

    /**
     * Default constructor to initialize the main attributes.
     */
    public DatasetInformationBtn() {
        this.updateIcon(new ThemeResource("img/file-text-o-1.png"));
        this.setWidth(40, Unit.PIXELS);
        this.setHeight(40, Unit.PIXELS);
        this.setEnabled(true);

        this.setDescription("Show dataset information");
        this.studiesInformationWindow = new DatasetInformationWindow(null) {

            @Override
            public Map<String,Object[]>  getPublicationsInformation(Set<String> pumedId) {
                return updatePublicationsData(pumedId);
            }

        };
    }

    /**
     * Update the dataset information input data to update the individual
     * buttons inside the pop up panel
     *
     * @param quantDatasetSet Set of quant dataset objects.
     */
    public void updateData(Collection<QuantDataset> quantDatasetSet) {
        studiesInformationWindow.updateData(quantDatasetSet);
    }

    /**
     * View/hide the dataset individual buttons window.
     */
    public void view() {
        studiesInformationWindow.view();
    }

    @Override
    public void onClick() {
    }

    /**
     * Update the publication data using pubmedID for the selected publication
     *
     * @param pumedIdSet  Set of publication PubMed id.
     * @return List of Publication information object arrays
     */
    public abstract Map<String,Object[]>  updatePublicationsData(Set<String> pumedIdSet);

}
