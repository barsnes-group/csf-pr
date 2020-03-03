package no.uib.probe.csf.pr.touch.logic.beans;

import java.util.Map;
import java.util.Set;

/**
 *This class represents initial information object that have an overview of the
 * resource data
 * @author Yehia Farag
 */
public class InitialInformationObject {

    /**
     *Map of quant datasets object map to its index in the database.
     */
    private Map<Integer, QuantDataset> quantDatasetsMap;
    /**
     *Array of active dataset pie charts filters  in the system using their index
     *0:Year
     *1:Study Type
     *2:Sample Matching
     *3:Technology
     *4:Analytical Approach
     *5:Shotgun/Targeted.
     */
    private boolean[] activeDatasetPieChartsFilters;

    /**
     *Disease category name (MS, AD, PD..etc).
     */
    private Set<String> diseaseCategories;

    /**
     * Get disease categories list (MS,AD,PD...etc).
     *
     * @return diseaseCategories Disease categories list (MS,AD,PD...etc)
     */
    public Set<String> getDiseaseCategories() {
        return diseaseCategories;
    }

    /**
     * Set disease categories list (MS,AD,PD...etc)
     *
     * @param diseaseCategories  Disease categories list (MS,AD,PD...etc)
     */
    public void setDiseaseCategories(Set<String> diseaseCategories) {
        this.diseaseCategories = diseaseCategories;
    }

    /**
     * Get map of quant datasets object map to its index in the database
     *
     * @return quantDatasetsMap  Map of quant datasets object map to its index in the database.
     */
    public Map<Integer, QuantDataset> getQuantDatasetsMap() {
        return quantDatasetsMap;
    }

    /**
     * Set map of quant datasets object map to its index in the database
     *
     * @param quantDatasetsMap Map of quant datasets object map to its index in the database.
     */
    public void setQuantDatasetsMap(Map<Integer, QuantDataset> quantDatasetsMap) {
        this.quantDatasetsMap = quantDatasetsMap;
    }

    /**
     * Get array of active dataset pie charts filters in the system using their
     * index 0:Year 1:Study Type 2:Sample Matching 3:Technology 4:Analytical
     * Approach 5:Shotgun/Targeted
     *
     * @return activeDatasetPieChartsFilters Array of active dataset pie charts filters
     */
    public boolean[] getActiveDatasetPieChartsFilters() {
        return activeDatasetPieChartsFilters;
    }

    /**
     * Set array of active dataset pie charts filters in the system using their
     * index 0:Year 1:Study Type 2:Sample Matching 3:Technology 4:Analytical
     * Approach 5:Shotgun/Targeted
     *
     * @param activeDatasetPieChartsFilters Array of active dataset pie charts filters
     */
    public void setActiveDatasetPieChartsFilters(boolean[] activeDatasetPieChartsFilters) {
        this.activeDatasetPieChartsFilters = activeDatasetPieChartsFilters;
    }
}
