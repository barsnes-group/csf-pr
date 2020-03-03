package no.uib.probe.csf.pr.touch.logic.dataset;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;

/**
 * This class contains all quant data information required in the visualization
 * layout.
 *
 * @author Yehia Farag
 */
public class QuantData implements Serializable {

    /**
     * List of active(current) heat map row header cells with all information
     * required for selecting and rendering the heat map.
     */
    private LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeRowIds;
    /**
     * List of original heat map row header cells with all information required
     * for selecting and rendering the heat map.
     */
    private LinkedHashMap<String, HeatMapHeaderCellInformationBean> oreginalRowIds;
    /**
     * List of active(current) heat map column header cells with all information
     * required for selecting and rendering the heat map.
     */
    private LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeColumnIds;
    /**
     * List of original heat map column header cells with all information
     * required for selecting and rendering the heat map.
     */
    private LinkedHashMap<String, HeatMapHeaderCellInformationBean> oreginalColumnIds;
    /**
     * Disease category name (MS, AD, PD..etc).
     */
    private String diseaseCategories;
    /**
     * List of disease group comparisons objects.
     */
    private Set<DiseaseGroupComparison> diseaseComparisonSet;

    private Map<Integer, DiseaseGroupComparison> indexToComparisons;
    /**
     * Array of active dataset pie charts filters in the system using their
     * index 0:Year 1:Study Type 2:Sample Matching 3:Technology 4:Analytical
     * Approach 5:Shotgun/Targeted.
     */
    private boolean[] activeDatasetPieChartsFilters;

    /**
     * Get list of original heat map row header cells with all information
     * required for selecting and rendering the heat map
     *
     * @return oreginalRowIds Heat map row header cells list
     */
    public LinkedHashMap<String, HeatMapHeaderCellInformationBean> getOreginalRowIds() {
        return oreginalRowIds;
    }

    /**
     * Set list of original heat map row header cells with all information
     * required for selecting and rendering the heat map
     *
     * @param oreginalRowIds Heat map row header cells list
     */
    public void setOreginalRowIds(LinkedHashMap<String, HeatMapHeaderCellInformationBean> oreginalRowIds) {
        this.oreginalRowIds = oreginalRowIds;
    }

    /**
     * Get list of original heat map column header cells with all information
     * required for selecting and rendering the heat map
     *
     * @return oreginalColumnIds Heat map column header cells list
     */
    public LinkedHashMap<String, HeatMapHeaderCellInformationBean> getOreginalColumnIds() {
        return oreginalColumnIds;
    }

    /**
     * Set list of original heat map column header cells with all information
     * required for selecting and rendering the heat map
     *
     * @param oreginalColumnIds Heat map column header cells list
     */
    public void setOreginalColumnIds(LinkedHashMap<String, HeatMapHeaderCellInformationBean> oreginalColumnIds) {
        this.oreginalColumnIds = oreginalColumnIds;
    }

    /**
     * Get list of active(current) heat map row header cells with all
     * information required for selecting and rendering the heat map
     *
     * @return activeRowIds List of active(current) heat map row header cells
     */
    public LinkedHashMap<String, HeatMapHeaderCellInformationBean> getActiveRowIds() {
        if (activeRowIds == null) {
            return oreginalRowIds;
        }
        return activeRowIds;
    }

    /**
     * Set list of active(current) heat map row header cells with all
     * information required for selecting and rendering the heat map
     *
     * @param activeRowIds List of active(current) heat map row header cells
     */
    public void setActiveRowIds(LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeRowIds) {
        this.activeRowIds = activeRowIds;
    }

    /**
     * Get list of active(current) heat map column header cells with all
     * information required for selecting and rendering the heat map
     *
     * @return activeColumnIds List of active(current) heat map column header
     * cells.
     */
    public LinkedHashMap<String, HeatMapHeaderCellInformationBean> getActiveColumnIds() {
        if (activeColumnIds == null) {
            return oreginalColumnIds;
        }
        return activeColumnIds;
    }

    /**
     * Set list of active(current) heat map column header cells with all
     * information required for selecting and rendering the heat map
     *
     * @param activeColumnIds List of active(current) heat map column header
     * cells
     */
    public void setActiveColumnIds(LinkedHashMap<String, HeatMapHeaderCellInformationBean> activeColumnIds) {
        this.activeColumnIds = activeColumnIds;
    }

    /**
     * Get list of disease group comparisons objects
     *
     * @return diseaseComparisonSet List of disease group comparisons objects
     */
    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        return diseaseComparisonSet;
    }

    /**
     * Get map of quant dataset index to comparisons
     *
     * @return map of quant dataset index mapped to disease comparisons
     */
    public Map<Integer, DiseaseGroupComparison> getIndexToComparisons() {
        return indexToComparisons;
    }

    /**
     * Set list of disease group comparisons objects
     *
     * @param diseaseComparisonSet List of disease group comparisons objects
     */
    public void setDiseaseComparisonSet(Set<DiseaseGroupComparison> diseaseComparisonSet) {
        this.diseaseComparisonSet = diseaseComparisonSet;
        indexToComparisons = new HashMap<>();
        for (DiseaseGroupComparison dgcomp : diseaseComparisonSet) {
            indexToComparisons.put(dgcomp.getQuantDatasetIndex(), dgcomp);
        }
    }

    /**
     * Get disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategories Disease category name
     */
    public String getDiseaseCategories() {
        return diseaseCategories;
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategories Disease category name
     */
    public void setDiseaseCategories(String diseaseCategories) {
        this.diseaseCategories = diseaseCategories;
    }

    /**
     * Get array of active dataset pie charts filters in the system using their
     * index 0:Year 1:Study Type 2:Sample Matching 3:Technology 4:Analytical
     * Approach 5:Shotgun/Targeted
     *
     * @return activeDatasetPieChartsFilters Array of active dataset pie charts
     * filters
     */
    public boolean[] getActiveDatasetPieChartsFilters() {
        return activeDatasetPieChartsFilters;
    }

    /**
     * Set array of active dataset pie charts filters in the system using their
     * index 0:Year 1:Study Type 2:Sample Matching 3:Technology 4:Analytical
     * Approach 5:Shotgun/Targeted
     *
     * @param activeDatasetPieChartsFilters Array of active dataset pie charts
     * filters
     */
    public void setActiveDatasetPieChartsFilters(boolean[] activeDatasetPieChartsFilters) {
        this.activeDatasetPieChartsFilters = activeDatasetPieChartsFilters;
    }

}
