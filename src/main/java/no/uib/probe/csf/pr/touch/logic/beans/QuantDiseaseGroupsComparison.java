package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents disease comparison with all its information
 *
 * @author Yehia Farag
 */
public class QuantDiseaseGroupsComparison implements Serializable, Comparable<QuantDiseaseGroupsComparison> {

    /**
     * Current comparison title.
     */
    private String comparisonHeader;
    /**
     * Publication comparison title.
     */
    private String oreginalComparisonHeader;
    /**
     * Map of datasets belong to the comparison.
     */
    private Map<Integer, QuantDataset> datasetMap;
    /**
     * HTML hashed color code for the disease.
     */
    private String diseaseCategoryColor;
    /**
     * User can flip the comparison.
     */
    private boolean switchable = true;
    /**
     * The CSS style for the disease category.
     */
    private String diseaseCategoryStyle;
    /**
     * Sort selection on the heat-map based on rows input data (if user select
     * disease group A from drop down list) group A will be Numerator by
     * default.
     */
    private boolean sortRows;
    /**
     * Sort selection on the heat-map based on column input data (if user select
     * disease group B from drop down list) group B will be Denominator by
     * default.
     */
    private boolean sortColumns;
    /**
     * Current comparison full (long) name for sub-disease categories.
     */
    private String comparisonFullName;
    /**
     * Disease category name (MS,AD,PD...etc).
     */
    private String diseaseCategory;
    /**
     * Map of quant comparison proteins belong to the comparison.
     */
    private Map<String, QuantComparisonProtein> quantComparisonProteinMap;
    /**
     * Map of quant comparison proteins belong to the comparison and divided
     * based on their trend.
     */
    private Map<Integer, Set<QuantComparisonProtein>> proteinsByTrendMap;

    /**
     * Cloning the comparison by creating identical comparison object.
     *
     * @return QuantDiseaseGroupsComparison New clonaid comparison.
     */
    public QuantDiseaseGroupsComparison cloning() {
        QuantDiseaseGroupsComparison updatedQuantComp = new QuantDiseaseGroupsComparison();
        updatedQuantComp.setComparisonFullName(getComparisonFullName());
        updatedQuantComp.setComparisonHeader(getComparisonHeader());
        updatedQuantComp.setDatasetMap(getDatasetMap());
        updatedQuantComp.setDiseaseCategory(getDiseaseCategory());
        updatedQuantComp.setDiseaseCategoryColor(diseaseCategoryColor);
        updatedQuantComp.setDiseaseCategoryStyle(diseaseCategoryStyle);
        updatedQuantComp.setProteinsByTrendMap(proteinsByTrendMap);
        updatedQuantComp.setOreginalComparisonHeader(oreginalComparisonHeader);
        updatedQuantComp.setQuantComparisonProteinMap(quantComparisonProteinMap);
        updatedQuantComp.setSwitchable(switchable);
        updatedQuantComp.setSortColumns(sortColumns);
        updatedQuantComp.setSortRows(sortRows);
        return updatedQuantComp;

    }

    /**
     * Get the CSS style for the disease category.
     *
     * @return diseaseCategoryStyle The CSS style for the disease category.
     */
    public String getDiseaseCategoryStyle() {
        return diseaseCategoryStyle;
    }

    /**
     * Set the CSS style for the disease category.
     *
     * @param diseaseCategoryStyle The CSS style for the disease category.
     */
    public void setDiseaseCategoryStyle(String diseaseCategoryStyle) {
        this.diseaseCategoryStyle = diseaseCategoryStyle;
    }

    /**
     * Get map of datasets belong to the comparison.
     *
     * @return datasetMap Map of datasets belong to the comparison.
     */
    public Map<Integer, QuantDataset> getDatasetMap() {
        return datasetMap;
    }

    /**
     * Set map of datasets belong to the comparison.
     *
     * @param datasetMap Map of datasets belong to the comparison.
     */
    public void setDatasetMap(Map<Integer, QuantDataset> datasetMap) {
        this.datasetMap = datasetMap;
    }

    /**
     * Get map of quant comparison proteins belong to the comparison and divided
     * based on their trend.
     *
     * @return proteinsByTrendMap Map of quant comparison proteins belong to the
     * comparison and divided based on their trend.
     */
    public Map<Integer, Set<QuantComparisonProtein>> getProteinsByTrendMap() {
        return proteinsByTrendMap;
    }

    /**
     * Set map of quant comparison proteins belong to the comparison and divided
     * based on their trend.
     *
     * @param proteinsByTrendMap Map of quant comparison proteins belong to the
     * comparison and divided based on their trend.
     */
    public void setProteinsByTrendMap(Map<Integer, Set<QuantComparisonProtein>> proteinsByTrendMap) {
        this.proteinsByTrendMap = proteinsByTrendMap;
    }

    /**
     * Get disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategory Disease category name(MS,AD,PD...etc)
     */
    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategory Disease category name(MS,AD,PD...etc)
     */
    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    /**
     * Set Current comparison full (long) name for sub-disease categories.
     *
     * @param comparisonFullName Current comparison full (long) name for
     * sub-disease categories.
     */
    public void setComparisonFullName(String comparisonFullName) {
        this.comparisonFullName = comparisonFullName;
    }

    /**
     * Default constructor.
     */
    public QuantDiseaseGroupsComparison() {
    }

    /**
     * Get Current comparison full (long) name for sub-disease categories.
     *
     * @return comparisonFullName Current comparison full (long) name for
     * sub-disease categories.
     */
    public String getComparisonFullName() {
        return comparisonFullName;
    }

    /**
     * Get HTML hashed color code for the disease.
     *
     * @return diseaseCategoryColor HTML hashed color code for the disease.
     */
    public String getDiseaseCategoryColor() {
        return diseaseCategoryColor;
    }

    /**
     * Set HTML hashed color code for the disease.
     *
     * @param diseaseCategoryColor HTML hashed color code for the disease.
     */
    public void setDiseaseCategoryColor(String diseaseCategoryColor) {
        this.diseaseCategoryColor = diseaseCategoryColor;
    }

    /**
     * Get Current comparison title.
     *
     * @return comparisonHeader Current comparison title.
     */
    public String getComparisonHeader() {
        return comparisonHeader;
    }

    /**
     * Get Publication comparison title.
     *
     * @return oreginalComparisonHeader Publication comparison title.
     */
    public String getOreginalComparisonHeader() {

        return oreginalComparisonHeader;
    }

    /**
     * Reset comparison title to publication title.
     */
    public void resetComparisonHeader() {
        if (!comparisonHeader.equalsIgnoreCase(oreginalComparisonHeader)) {
            comparisonHeader = oreginalComparisonHeader;
        }

    }

    /**
     * Set Current comparison title.
     *
     * @param comparisonHeader Current comparison title.
     */
    public void setComparisonHeader(String comparisonHeader) {
        this.comparisonHeader = comparisonHeader;

    }

    /**
     * Set Publication comparison title.
     *
     * @param oreginalComparisonHeader Publication comparison title.
     */
    public void setOreginalComparisonHeader(String oreginalComparisonHeader) {
        this.oreginalComparisonHeader = oreginalComparisonHeader;

    }

    /**
     * Get map of quant comparison proteins belong to the comparison.
     *
     * @return quantComparisonProteinMap Map of quant comparison proteins belong
     * to the comparison.
     */
    public Map<String, QuantComparisonProtein> getQuantComparisonProteinMap() {
        return quantComparisonProteinMap;
    }

    /**
     * Set map of quant comparison proteins belong to the comparison.
     *
     * @param quantComparisonProteinMap Map of quant comparison proteins belong
     * to the comparison.
     */
    public void setQuantComparisonProteinMap(Map<String, QuantComparisonProtein> quantComparisonProteinMap) {
        this.quantComparisonProteinMap = quantComparisonProteinMap;
    }

    /**
     * Override compare to method
     *
     * @param t object to compare
     * @return comparison result
     */
    @Override
    public int compareTo(QuantDiseaseGroupsComparison t) {
        return this.comparisonHeader.compareTo(t.comparisonHeader);
    }

    /**
     * Check if sort selection on the heat-map based on rows input data (if user
     * select disease group A from drop down list) group A will be Numerator by
     * default.
     *
     * @return sortRows
     */
    public boolean isSortRows() {
        return sortRows;
    }

    /**
     * Set sort selection on the heat-map based on rows input data (if user
     * select disease group A from drop down list) group A will be Numerator by
     * default.
     *
     * @param sortRows Sort selection on row based
     */
    public void setSortRows(boolean sortRows) {
        this.sortRows = sortRows;
    }

    /**
     * Check if sort selection on the heat-map based on column input data (if
     * user select disease group B from drop down list) group B will be
     * Denominator by default.
     *
     * @return sortColumns Sort selection on column based
     */
    public boolean isSortColumns() {
        return sortColumns;
    }

    /**
     * Set sort selection on the heat-map based on column input data (if user
     * select disease group B from drop down list) group B will be Denominator
     * by default.
     *
     * @param sortColumns Sort selection on column based
     */
    public void setSortColumns(boolean sortColumns) {
        this.sortColumns = sortColumns;
    }

    /**
     * Check if user can flip the comparison.
     *
     * @return switchable User can flip the comparison.
     */
    public boolean isSwitchable() {
        return switchable;
    }

    /**
     * Set user can flip the comparison.
     *
     * @param switchable User can flip the comparison.
     */
    public void setSwitchable(boolean switchable) {
        this.switchable = switchable;
    }

    /**
     * Override equals method
     *
     * @param obj object to compare
     * @return object are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuantDiseaseGroupsComparison other = (QuantDiseaseGroupsComparison) obj;
        return Objects.equals(this.comparisonHeader, other.comparisonHeader);
    }
     /**
     *Override hash code method 
     * @return calculated hash code value
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
