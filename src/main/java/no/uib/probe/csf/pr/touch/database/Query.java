package no.uib.probe.csf.pr.touch.database;

import java.io.Serializable;
import java.util.Set;

/**
 * This class represents a query bean that has all query information
 *
 * @author Yehia Farag
 */
public class Query implements Serializable {

    /**
     * Search data type (id/quant).
     */
    private String searchDataType;
    /**
     * Search dataset name.
     */
    private String searchDataset;
    /**
     * Searching method (accession,name or peptide sequence).
     */
    private String searchBy;
    /**
     * Search keyword.
     */
    private String searchKeyWords;
    /**
     * Show validated proteins only.
     */
    private boolean validatedProteins;
    /**
     * Disease categories.
     */
    private Set<Object> diseaseCategorys;

    /**
     * Return the requested dataset name
     *
     * @return searching specific dataset name.
     */
    public String getSearchDataset() {
        return searchDataset;
    }

    /**
     * Set the dataset id
     *
     * @param searchDataset dataset identifier.
     */
    public void setSearchDataset(String searchDataset) {
        this.searchDataset = searchDataset;
    }

    /**
     * Get searching by method (accession, peptide sequence..etc)
     *
     * @return searching method name.
     */
    public String getSearchBy() {
        return searchBy;
    }

    /**
     * Set searching by method (accession, peptide sequence..etc)
     *
     * @param searchBy searching by method (accession, peptide sequence..etc)
     */
    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    /**
     * Get searching data type (Id data, quant data)
     *
     * @return searching data type.
     */
    public String getSearchDataType() {
        return searchDataType;
    }

    /**
     * Set searching data type (Id data, quant data)
     *
     * @param searchDataType searching data type.
     */
    public void setSearchDataType(String searchDataType) {
        this.searchDataType = searchDataType;
    }

    /**
     * Get searching keywords
     *
     * @return the searching keyword.
     */
    public String getSearchKeyWords() {
        return searchKeyWords;
    }

    /**
     * Set searching keywords
     *
     * @param searchKeyWords searching keywords.
     */
    public void setSearchKeyWords(String searchKeyWords) {
        this.searchKeyWords = searchKeyWords;
    }

    /**
     * Is searching for validated proteins only
     *
     * @return search for only validated proteins.
     */
    public boolean isValidatedProteins() {
        return validatedProteins;
    }

    /**
     * Set searching for validated proteins only
     *
     * @param validatedProteins only validated proteins
     */
    public void setValidatedProteins(boolean validatedProteins) {
        this.validatedProteins = validatedProteins;
    }

    /**
     * Get searching disease category query
     *
     * @return diseaseCategorys set of disease categories
     */
    public Set<Object> getDiseaseCategorys() {
        return diseaseCategorys;
    }

    /**
     * Set searching disease category query
     *
     * @param diseaseCategorys set of disease categories
     */
    public void setDiseaseCategorys(Set<Object> diseaseCategorys) {
        this.diseaseCategorys = diseaseCategorys;
    }

}
