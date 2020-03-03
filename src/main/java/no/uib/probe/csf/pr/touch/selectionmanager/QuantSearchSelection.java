package no.uib.probe.csf.pr.touch.selectionmanager;

import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 * This class represents quant searching selection object, the class has all the
 * information required for view selection in quant data layout
 *
 * @author Yehia Farag
 */
public class QuantSearchSelection {

    /**
     * Disease category name (MS, AD, PD..etc).
     */
    private Set<String> diseaseCategories;
    /**
     * Set of the found dataset indexes in the database.
     */
    private Set<Integer> quantDatasetIndexes;
    /**
     * Set of the keywords used for the searching query.
     */
    private Set<String> keyWords;
    /**
     * List of selected comparisons to be updated based on user selection for
     * comparisons across the system.
     */
    private Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    /**
     * List of selected proteins to be updated based on user selection for
     * comparisons across the system.
     */
    private Set<String> selectedProteinsList;
    /**
     * Map of disease category to found dataset ids.
     */
    private Map<String, Set<Integer>> diseaseCategoriesIdMap;
    /**
     * Customized comparison based on user input data in quant comparison
     * layout.
     */
    private QuantDiseaseGroupsComparison userCustomizedComparison;

    /**
     * Get customized comparison based on user input data in quant comparison
     * layout
     *
     * @return userCustomizedComparison Customized comparison based on user
     * input data
     */
    public QuantDiseaseGroupsComparison getUserCustomizedComparison() {
        return userCustomizedComparison;
    }

    /**
     * Set customized comparison based on user input data in quant comparison
     * layout
     *
     * @param userCustomizedComparison Customized comparison based on user input
     * data
     */
    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.userCustomizedComparison = userCustomizedComparison;
    }

    /**
     * Get set of the keywords used for the searching query
     *
     * @return keyWords Set of the keywords
     */
    public Set<String> getKeyWords() {
        return keyWords;
    }

    /**
     * Set set of the keywords used for the searching query
     *
     * @param keyWords Set of the keywords
     */
    public void setKeyWords(Set<String> keyWords) {
        this.keyWords = keyWords;
    }

    /**
     * Get disease category name (MS,AD,PD...etc)
     *
     * @return diseaseCategory Disease category name
     */
    public Set<String> getDiseaseCategories() {
        return diseaseCategories;
    }

    /**
     * Set disease category name (MS,AD,PD...etc)
     *
     * @param diseaseCategories Disease category name
     */
    public void setDiseaseCategories(Set<String> diseaseCategories) {
        this.diseaseCategories = diseaseCategories;
    }

    /**
     * Get set of the found dataset indexes in the database
     *
     * @return quantDatasetIndexes Set of dataset indexes
     */
    public Set<Integer> getQuantDatasetIndexes() {
        return quantDatasetIndexes;
    }

    /**
     * Set set of the found dataset indexes in the database
     *
     * @param quantDatasetIndexes Set of dataset indexes
     */
    public void setQuantDatasetIndexes(Set<Integer> quantDatasetIndexes) {
        this.quantDatasetIndexes = quantDatasetIndexes;
    }

    /**
     * Get list of selected comparisons to be updated based on user selection
     * for comparisons across the system
     *
     * @return selectedComparisonsList List of selected comparisons
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    /**
     * Set list of selected comparisons to be updated based on user selection
     * for comparisons across the system
     *
     * @param selectedComparisonsList selected comparisons list
     */
    public void setSelectedComparisonsList(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        this.selectedComparisonsList = selectedComparisonsList;
    }

    /**
     * Get list of selected proteins to be updated based on user selection for
     * comparisons across the system
     *
     * @return selectedProteinsList List of selected proteins
     */
    public Set<String> getSelectedProteinsList() {
        return selectedProteinsList;
    }

    /**
     * Set list of selected proteins to be updated based on user selection for
     * comparisons across the system
     *
     * @param selectedProteinsList List of selected proteins
     */
    public void setSelectedProteinsList(Set<String> selectedProteinsList) {
        this.selectedProteinsList = selectedProteinsList;
    }

    /**
     * Get map of dataset indexes mapped to disease category
     *
     * @return diseaseCategoriesIdMap Map of dataset indexes mapped to disease
     * category
     */
    public Map<String, Set<Integer>> getDiseaseCategoriesIdMap() {
        return diseaseCategoriesIdMap;
    }

    /**
     * Set map of dataset indexes mapped to disease category
     *
     * @param diseaseCategoriesIdMap Map of dataset indexes mapped to disease
     * category
     */
    public void setDiseaseCategoriesIdMap(Map<String, Set<Integer>> diseaseCategoriesIdMap) {
        this.diseaseCategoriesIdMap = diseaseCategoriesIdMap;
    }

}
