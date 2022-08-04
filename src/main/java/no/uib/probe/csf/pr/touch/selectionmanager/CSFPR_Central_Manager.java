package no.uib.probe.csf.pr.touch.selectionmanager;

import com.vaadin.server.VaadinSession;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.BusyTaskProgressBar;

/**
 * This class represents the central manager that is responsible for handling
 * data across different visualizations and managing all users selections
 *
 * @author Yehia Farag
 *
 *
 */
public class CSFPR_Central_Manager implements Serializable {

    /**
     * Map for storing current listeners or filters.
     */
    private final LinkedHashMap<String, CSFListener> Listeners_Map;
    /**
     *
     * List of selected comparisons to be updated based on user selection for
     * comparisons across the system.
     */
    private Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    /**
     * List of selected proteins to be updated based on user selection for
     * comparisons across the system.
     */
    private Set<QuantComparisonProtein> selectedProteinsList;
    /**
     * List of equal comparison map to avoid double selection from heat map.
     */
    private Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap;
    /**
     * Main selected protein accession from the protein table that is used to
     * update peptides component table.
     */
    private String selectedProteinAccession;
    /**
     * System is doing long processing task to push the the system to show
     * progress bar.
     */
    private final BusyTaskProgressBar busyTask;
    /**
     * List of full publication information that is used by the resource
     * overview and in publication information overview.
     */
    private Map<String,Object[]>  fullPublicationList;
    /**
     * Main selected protein trend based on user customized data to show the
     * selected user protein trend in the peptide component.
     */
    private int custProteinSelectionTrend;
    /**
     * Main searching results object that contain the searching results
     * information.
     */
    private QuantSearchSelection quantSearchSelection;

    /**
     * Get list of selected proteins to be updated based on user selection for
     * comparisons across the system
     *
     * @return selectedProteinsList List of selected proteins
     */
    public Set<QuantComparisonProtein> getSelectedProteinsList() {
        return selectedProteinsList;
    }

    /**
     * *
     * Get list of full publication information that is used by the resource
     * overview and in publication information overview
     *
     * @return fullPublicationList List of full object arrays for publication
     * information
     */
    public Map<String,Object[]>  getFullPublicationList() {
        return fullPublicationList;
    }

    /**
     * *
     * Set list of full publication information that is used by the resource
     * overview and in publication information overview
     *
     * @param fullPublicationList List of full object arrays for publication
     * information
     */
    public void setFullPublicationList(Map<String,Object[]>  fullPublicationList) {
        this.fullPublicationList = fullPublicationList;
    }

    /**
     * Constructor to initialize the main attributes
     *
     * @param busyTask progress task manager to show progress bar on long
     * processing tasks
     */
    public CSFPR_Central_Manager(BusyTaskProgressBar busyTask) {
        this.busyTask = busyTask;
        Listeners_Map = new LinkedHashMap<>();
    }

    /**
     * Set list of equal comparison map to avoid double selection from heat map
     *
     * @param equalComparisonMap List of equal comparison map
     */
    public void setEqualComparisonMap(Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap) {
        this.equalComparisonMap = equalComparisonMap;
    }

    /**
     * Register new listener in the system
     *
     * @param listener listener component
     */
    public void registerListener(CSFListener listener) {
        if (Listeners_Map.containsKey(listener.getListenerId())) {
            Listeners_Map.remove(listener.getListenerId());
        }
        Listeners_Map.put(listener.getListenerId(), listener);

    }

    /**
     * Get list of equal comparison map to avoid double selection from heat map
     *
     * @return equalComparisonMap List of equal comparison map
     */
    public Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> getEqualComparisonMap() {
        return equalComparisonMap;
    }

    /**
     * Remove registered listener
     *
     * @param listener listener component
     */
    public void unregisterListener(CSFListener listener) {
        Listeners_Map.remove(listener.getListenerId(), listener);

    }

    /**
     * Get main selected protein accession from the protein table that is used
     * to update peptides component table
     *
     * @return selectedProteinAccession Main selected protein accession
     */
    public String getSelectedProteinAccession() {
        return selectedProteinAccession;
    }

    /**
     * Set Selection in the system to update other registered listeners
     *
     * @param selection quant search selection object
     */
    public void setSelection(CSFSelection selection) {
        if (selection.getType().equalsIgnoreCase("peptide_selection")) {
            selectedProteinAccession = selection.getSelectedProteinAccession();
            custProteinSelectionTrend = selection.getCustProteinSelectionTrend();
        } else {
            this.selectedComparisonsList = selection.getSelectedComparisonsList();
            this.selectedProteinsList = selection.getSelectedProteinsList();

            if (selectedComparisonsList.size() > 5) {
                busyTask.setVisible(true);
            }
        }
        SelectionChanged(selection.getType());

    }

    /**
     * Get list of selected comparisons to be updated based on user selection
     * for comparisons across the system
     *
     * @return selectedComparisonsList Selected comparisons list
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        Set<QuantDiseaseGroupsComparison> tempProteinsList = new LinkedHashSet<>();
        if (quantSearchSelection != null && selectedComparisonsList != null) {
            for (QuantDiseaseGroupsComparison quantComp : selectedComparisonsList) {
                Map<String, QuantComparisonProtein> map = new LinkedHashMap<>();

                for (String protien : quantSearchSelection.getSelectedProteinsList()) {
                    String key = "";
                    if (quantComp.getQuantComparisonProteinMap().containsKey("0_" + protien)) {
                        key = "0_" + protien;
                    } else if (quantComp.getQuantComparisonProteinMap().containsKey("1_" + protien)) {
                        key = "1_" + protien;
                    } else if (quantComp.getQuantComparisonProteinMap().containsKey("2_" + protien)) {
                        key = "2_" + protien;

                    } else if (quantComp.getQuantComparisonProteinMap().containsKey("3_" + protien)) {

                        key = "3_" + protien;
                    } else if (quantComp.getQuantComparisonProteinMap().containsKey("4_" + protien)) //                                    if (quantComp.getQuantComparisonProteinMap().containsValue(protien.getProtKey())) {
                    {
                        key = "4_" + protien;

                    }

                    if (quantComp.getQuantComparisonProteinMap().containsKey(key)) {
                        map.put(key, quantComp.getQuantComparisonProteinMap().get(key));
                    }

                }
                QuantDiseaseGroupsComparison updatedQuantComp = quantComp.cloning();
                updatedQuantComp.setQuantComparisonProteinMap(map);
                tempProteinsList.add(updatedQuantComp);
            }
            return tempProteinsList;

        }

        return selectedComparisonsList;
    }

    /**
     * Loop responsible for updating all registered listeners
     *
     * @param type selection type
     */
    private void SelectionChanged(String type) {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            Listeners_Map.values().stream().forEach((filter) -> {
                filter.selectionChanged(type);
            });
        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
            busyTask.setVisible(false);
        }

    }

    /**
     * Quant Search Selection in registered component invoke searching mode in
     * the system and set the searching results information object
     *
     * @param selection Quant search selection object
     */
    public void searchSelectionAction(QuantSearchSelection selection) {
        this.quantSearchSelection = selection;
        SelectionChanged("quant_searching");

    }

    /**
     * Reset Quant Search Selection in the system back to normal quant explorer
     * mode and remove searching results information from the system.
     */
    public void resetSearchSelection() {
        this.quantSearchSelection = null;
        SelectionChanged("reset_quant_searching");
    }

    /**
     * Quant Search Selection in registered component invoke comparing mode in
     * the system and searching results information object in the system.
     *
     * @param selection Quant search selection
     */
    public void compareSelectionAction(QuantSearchSelection selection) {
        this.quantSearchSelection = selection;
        SelectionChanged("quant_compare");

    }

    /**
     * Get main searching results object that contain the searching results
     * information
     *
     * @return quantSearchSelection Quant search selection
     */
    public QuantSearchSelection getQuantSearchSelection() {
        return quantSearchSelection;
    }

    /**
     * Get main selected protein trend based on user customized data to show the
     * selected user protein trend in the peptide component
     *
     * @return custProteinSelectionTrend Main selected protein trend based on
     * user customized data
     */
    public int getCustProteinSelectionTrend() {
        return custProteinSelectionTrend;
    }

}
