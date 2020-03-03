package no.uib.probe.csf.pr.touch;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.database.Query;
import no.uib.probe.csf.pr.touch.logic.CoreLogic;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.logic.dataset.DatasetUtility;
import no.uib.probe.csf.pr.touch.selectionmanager.QuantSearchSelection;
import no.uib.probe.csf.pr.touch.view.core.OverviewInfoBean;

/**
 * This class is responsible for handling the datasets information across the
 * system, The handler interact with both visualization and logic layer.
 *
 * @author Yehia Farag
 */
public class Data_Handler implements Serializable {

    /**
     * This is the main logic layer.
     */
    private final CoreLogic coreLogic;
    /**
     * This is the dataset utility class that has all active dataset
     * information.
     */
    private final DatasetUtility Dataset_Util;

    /**
     * Constructor to initialize the main Data Handler.
     *
     * @param url database URL
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param password database password
     */
    public Data_Handler(String url, String dbName, String driver, String userName, String password) {
        this.coreLogic = new CoreLogic(url, dbName, driver, userName, password);
        Dataset_Util = new DatasetUtility(coreLogic);
    }

    /**
     * Activate searching layout sub-data handler.
     *
     * @param searchSelection search selection data.
     */
    public void switchToSearchingMode(QuantSearchSelection searchSelection) {
        this.Dataset_Util.switchToSearchingMode(searchSelection);
    }

    /**
     * Get CSF-PR resource overview information.
     *
     * @return OverviewInfoBean resource information bean.
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        return this.coreLogic.getResourceOverviewInformation();

    }

    /**
     * Get initial publication information.
     *
     * @return list of publications available in the the resource.
     */
    public List<Object[]> getPublicationList() {
        return this.coreLogic.getPublicationList();
    }

    /**
     * this method responsible for getting initial datasets information.
     *
     * @return set of datasets information available in the the resource.
     */
    public Set<QuantDataset> getQuantDatasetList() {
        return this.coreLogic.getQuantDatasetList();

    }
    /**
     * Get the current available disease category list.
     *
     * @return set of disease category objects that has all disease category
     * information and styling information.
     */
    public Set<String> getActiveDiseaseCategorySet() {
        return Dataset_Util.getActiveDiseaseCategorySet();
    }

    /**
     *Reset to default data (removing search compare data)
     */
    public void resetToDefault(){
    Dataset_Util.resetToDefault();
    
    }

    /**
     * Get the current available disease category list.
     *
     * @return set of disease category objects that has all disease category
     * information and styling information.
     */
    public Collection<DiseaseCategoryObject> getDiseaseCategorySet() {
        return Dataset_Util.getDiseaseCategorySet();
    }

    /**
     * Get full disease sub groups name list.
     *
     * @return set of disease sub group name.
     */
    public Set<String> getFullDiseaseGroupNameSet() {
        return Dataset_Util.getFullDiseaseGroupNameSet();
    }

    /**
     * Load and initialize all information for the selected disease category.
     *
     * @param diseaseCategory Disease category name (MS, AD, PD..etc)
     */
    public void loadDiseaseCategory(Set<String> diseaseCategory) {
        Dataset_Util.setMainDiseaseCategory(diseaseCategory);
    }

    /**
     * Get active quantification pie charts filters (to hide them if they are
     * empty).
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes.
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
        return coreLogic.getActivePieChartQuantFilters();

    }

    /**
     * get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty)
     *
     * @param searchQuantificationProtList List of quant proteins.
     * @return boolean array for the active and not active pie chart filters
     * indexes.
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
        return coreLogic.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    /**
     * Get disease group row labels for the current active disease category.
     *
     * @return active row labels category set
     */
    public LinkedHashMap<String,HeatMapHeaderCellInformationBean> getRowLabels() {
        return Dataset_Util.getRowLabels();

    }

    /**
     * Get the disease group column labels for the current active disease
     * category
     *
     * @return active column labels category set
     */
    public LinkedHashMap<String,HeatMapHeaderCellInformationBean> getColumnLabels() {
        return Dataset_Util.getColumnLabels();

    }

    /**
     * Get the disease group comparisons for the current active disease
     * category.
     *
     * @return active disease group comparisons
     */
    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {        
        return Dataset_Util.getDiseaseGroupComparisonsSet();

    }

    /**
     * Get the full quant dataset map the current active disease category
     *
     * @return map of quant dataset objects
     */
    public Map<Integer, QuantDataset> getActiveQuantDsMap() {       
        return Dataset_Util.getActiveQuantDsMap();
    }

    /**
     * Get the active data columns for the current active disease category
     *
     * @return boolean array of active column
     */
    public boolean[] getActiveDataColumns() {

        return Dataset_Util.getActiveDataColumns();
    }

    /**
     * Update and combine disease sub groups based on user selection
     *
     *
     * @param updatedGroupsNamesMap updated disease sub group names
     */
    public void updateCombinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap) {
        Dataset_Util.updateCobinedGroups(updatedGroupsNamesMap);

    }

    /**
     * Update quant comparison proteins map for each comparison.
     *
     * @param selectedQuantComparisonsList selected comparisons.
     * @return updated quant comparisons list.
     */
    public Set<QuantDiseaseGroupsComparison> updateComparisonQuantProteins(Set<QuantDiseaseGroupsComparison> selectedQuantComparisonsList) {
        return coreLogic.updateComparisonQuantProteins(selectedQuantComparisonsList, Dataset_Util.getInUse_DiseaseCat_DiseaseGroupMap());

    }

    /**
     * Search for quantification proteins.
     *
     * @param query query object that has all query information.
     * @param toCompare quant comparison mode.
     * @return quant proteins list.
     */
    public List<QuantProtein> searchQuantificationProtein(Query query, boolean toCompare) {
        return coreLogic.searchQuantificationProteins(query, toCompare);

    }
      /**
     * Store query to share with csf-pr ID.
     *
     * @param query query object that has all query information.
     * @return successful db transaction
     */
    public int  storeQuery(Query query) {
        return  coreLogic.storeQuery(query);

    }

    /**
     * Filter the quant search results based on keywords and detect the not
     * found keywords.
     *
     * @param quantProteinstList list of found proteins.
     * @param SearchingKeys keyword used for searching.
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence).
     * @return not found keywords within the searching list.
     */
    public String filterQuantSearchingKeywords(List<QuantProtein> quantProteinstList, String SearchingKeys, String searchBy) {
        return coreLogic.filterQuantSearchingKeywords(quantProteinstList, SearchingKeys, searchBy);
    }

    /**
     * Get the quant hits list from the searching results and group the common
     * proteins in separated lists.
     *
     * @param quantProteinsList list of found proteins.
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence).
     * @return list of quant hits results.
     */
    public Map<String, Integer[]> getQuantHitsList(List<QuantProtein> quantProteinsList, String searchBy) {
        return coreLogic.getQuantHitsList(quantProteinsList, searchBy);
    }

    /**
     * Get the quant compare list from the searching results and group the
     * common proteins in separated lists.
     *
     * @param quantProteinsList list of found proteins.
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence).
     * @return list of quant hits results.
     */
    public Integer[] getQuantComparisonHitsList(List<QuantProtein> quantProteinsList, String searchBy) {
        return coreLogic.getQuantComparisonHitsList(quantProteinsList, searchBy);

    }

    /**
     * Get the quant compare list from the searching results and group the
     * common proteins in separated lists.
     *
     * @param quantProteinsList list of found proteins.
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence).
     * @return list of quant hits results.
     */
    public Integer[] getQuantComparisonProteinList(List<QuantProtein> quantProteinsList, String searchBy) {
        return coreLogic.getQuantComparisonProteinList(quantProteinsList, searchBy);
    }

    /**
     * Search for identification proteins.
     *
     * @param query query object that has all query information.
     * @return identificationProtein list.
     */
    public String searchIdentificationProtein(Query query) {
        return coreLogic.searchIdentficationProtein(query);
    }

    /**
     * Export accession list to CSV file.
     *
     * @param proteinsList list of protein accessions.
     * @return byte[] of the exported file.
     */
    public byte[] exportProteinsListToCSV(Set<String> proteinsList) {
        return coreLogic.exportProteinsListToCSV(proteinsList);
    }

    /**
     * Get unmapped peptide set (the current version of the protein sequence in
     * UniProt has changed) for current active dataset.
     *
     * @return Set of current unmapped peptides for the active dataset.
     */
    public Set<QuantPeptide> getUnmappedPeptideSet() {
        return coreLogic.getUnmappedPeptideSet();
    }

}
