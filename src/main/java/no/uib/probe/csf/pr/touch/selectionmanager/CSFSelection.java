package no.uib.probe.csf.pr.touch.selectionmanager;

import java.io.Serializable;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 * Selection class is made to work together with the csf-pr central selection
 * manager keeping information required for different types of selection across
 * the system
 *
 * @author Yehia Farag
 *
 *
 */
public class CSFSelection implements Serializable {

    /**
     * The listener id to define the listener in the system .
     */
    private final String listener_Id;
    /**
     * List of selected comparisons to be updated based on user selection for
     * comparisons across the system.
     */
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    /**
     * Selection type.
     */
    private final String type;
    /**
     * List of selected proteins to be updated based on user selection for
     * comparisons across the system.
     */
    private final Set<QuantComparisonProtein> selectedProteinsList;
    /**
     * Main selected protein accession from the protein table that is used to
     * update peptides component table.
     */
    private String selectedProteinAccession;
    /**
     * Main selected protein trend based on user customized data to show the
     * selected user protein trend in the peptide component.
     */
    private int custProteinSelectionTrend;

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
     * Set Main selected protein accession from the protein table that is used
     * to update peptides component table
     *
     * @param selectedProteinAccession Main selected protein accession
     */
    public void setSelectedProteinAccession(String selectedProteinAccession) {
        this.selectedProteinAccession = selectedProteinAccession;
    }

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
     * Constructor to initialize the main selection attributes
     *
     * @param type Selection type
     * @param listener_Id The listener id
     * @param selectedComparisonsList List of selected comparisons
     * @param selectedProteinsList List of selected proteins
     */
    public CSFSelection(String type, String listener_Id, Set<QuantDiseaseGroupsComparison> selectedComparisonsList, Set<QuantComparisonProtein> selectedProteinsList) {
        this.type = type;
        this.listener_Id = listener_Id;
        this.selectedComparisonsList = selectedComparisonsList;
        this.selectedProteinsList = selectedProteinsList;

    }

    /**
     * Get the listener id
     *
     * @return listener_Id Listener Id
     */
    public String getListener_Id() {
        return listener_Id;
    }

    /**
     * Get list of selected comparisons to be updated based on user selection
     * for comparisons across the system
     *
     * @return selectedComparisonsList Selected comparisons list
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    /**
     * Get type of selection
     *
     * @return type Selection type
     */
    public String getType() {
        return type;
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

    /**
     * Set main selected protein trend based on user customized data to show the
     * selected user protein trend in the peptide component
     *
     * @param custProteinSelectionTrend Main selected protein trend based on
     * user customized data
     */
    public void setCustProteinSelectionTrend(int custProteinSelectionTrend) {
        this.custProteinSelectionTrend = custProteinSelectionTrend;
    }

}
