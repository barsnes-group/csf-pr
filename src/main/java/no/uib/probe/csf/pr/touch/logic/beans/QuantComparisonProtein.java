package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * This class contains all quant comparison protein information
 *
 * @author Yehia Farag
 */
public class QuantComparisonProtein implements Serializable {

    /**
     * Protein accession number.
     */
    private String proteinAccession;
    /**
     * Protein name.
     */
    private String proteinName;
    /**
     * Protein sequence imported from UniProt.
     */
    private String sequence;

    /**
     * Protein peptides list.
     */
    private Set<QuantPeptide> quantPeptidesList;

    /**
     * Patients number in different datasets mapped to protein accession.
     */
    private final Map<String, List<Integer>> patientsNumToTrindMap = new HashMap<>();
    /**
     * Patients number in different datasets mapped to dataset key.
     */
    private final Map<String, List<Integer>> patientsNumToDSIDMap = new HashMap<>();

    /**
     * Protein objects (from different dataset with same accession) mapped to
     * dataset id.
     */
    private Map<String, QuantProtein> dsQuantProteinsMap;
    /**
     * Overall increase significant number for the protein in different
     * datasets.
     */
    private Integer significantlyIncreasedNumber = 0;
    /**
     * Overall decrease significant number for the protein in different
     * datasets.
     */
    private Integer significantlyDecreasedNumber = 0;

    /**
     * Overall no value number for the protein in different datasets.
     */
    private int noValueprovided = 0;
    /**
     * Overall equal number for the protein in different datasets.
     */
    private int stable = 0;

    /**
     * The penalty value used for sorting proteins in the protein table equal,
     * increased-decreased non significant =0.5.
     */
    private double penalty = 0.0;

    /**
     * The trend value used for plotting the protein location in bubble charts.
     */
    private Double trendValue = 0.0;

    /**
     * The trend value used for sorting the protein location in protein table.
     */
    private double cellValue;

    /**
     * protein link in UniProt.
     */
    private String url;
    /**
     * The trend value used for plotting the protein location in line charts.
     */
    private double overallCellPercentValue;
    /**
     * The final value used for significant trend category 0 : 100% decreased 1:
     * less than 100% decrease 2: proteins are equal or not significantly
     * changed 3: less than 100% increased 4:100% increased 5:no value available
     * (proteins quantified on peptides level).
     */
    private int significantTrindCategory;

    /**
     * Get patients number in different datasets mapped to dataset key
     *
     * @return patientsNumToDSIDMap Patients number in different datasets mapped
     * to dataset key.
     */
    public Map<String, List<Integer>> getPatientsNumToDSIDMap() {
        return patientsNumToDSIDMap;
    }

    /**
     * Get protein name
     *
     * @return proteinName Protein name
     */
    public String getProteinName() {
        return proteinName;
    }

    /**
     * Get protein sequence
     *
     * @return sequence Protein sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Get protein link to UniProt
     *
     * @return URL link to UniProt
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get protein peptides list
     *
     * @return quantPeptidesList Protein peptides list
     */
    public Set<QuantPeptide> getQuantPeptidesList() {
        return quantPeptidesList;
    }

    /**
     * Set protein peptides list
     *
     * @param quantPeptidesList Protein peptides list
     */
    public void setQuantPeptidesList(Set<QuantPeptide> quantPeptidesList) {
        this.quantPeptidesList = quantPeptidesList;
    }

    /**
     * Set protein objects (from different dataset with same accession) mapped
     * to dataset id
     *
     * @param dsQuantProteinsMap Protein objects (from different dataset with
     * same accession) mapped to dataset id.
     */
    public void setDsQuantProteinsMap(Map<String, QuantProtein> dsQuantProteinsMap) {
        this.dsQuantProteinsMap = dsQuantProteinsMap;
    }

    /**
     * Get protein objects (from different dataset with same accession) mapped
     * to dataset id
     *
     * @return dsQuantProteinsMap Protein objects (from different dataset with
     * same accession) mapped to dataset id.
     */
    public Map<String, QuantProtein> getDsQuantProteinsMap() {
        return dsQuantProteinsMap;
    }

    /**
     * Get Protein accession number
     *
     * @return proteinAccession Protein accession number.
     */
    public String getProteinAccession() {
        return proteinAccession;
    }

    /**
     * Set protein sequence (imported from UniProt)
     *
     * @param sequence Protein sequence.
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * The final value used for significant trend category 0 : 100% decreased 1:
     * less than 100% decrease 2: proteins are equal or not significantly
     * changed 3: less than 100% increased 4:100% increased 5:no value available
     * (proteins quantified on peptides level)
     *
     * @return significantTrindCategory The final value used for significant
     * trend category.
     */
    public int getSignificantTrindCategory() {
        return significantTrindCategory;
    }

    /**
     * Constructor (initialize data required for quant comparison protein).
     */
    public QuantComparisonProtein() {
        this.dsQuantProteinsMap = new HashMap<>();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        patientsNumToTrindMap.put("up", new ArrayList<>());
        patientsNumToTrindMap.put("equal", new ArrayList<>());
        patientsNumToTrindMap.put("down", new ArrayList<>());
        patientsNumToDSIDMap.put("up", new ArrayList<>());
        patientsNumToDSIDMap.put("equal", new ArrayList<>());
        patientsNumToDSIDMap.put("down", new ArrayList<>());
        patientsNumToTrindMap.put("noValueProvided", new ArrayList<>());
        patientsNumToDSIDMap.put("noValueProvided", new ArrayList<>());
    }

    /**
     * Add +1 for number of decreased protein in comparison (protein value
     * decreased in this comparison)
     *
     * @param patientsNumber Total number of patients in this dataset.
     * @param dsID Dataset index in database
     * @param significant The pValue is significant
     */
    public void addDecreasedProtein(int patientsNumber, int dsID, boolean significant) {

        if (significant) {
            trendValue -= (double) 1;
            this.significantlyDecreasedNumber += 1;
            List<Integer> downList = this.patientsNumToTrindMap.get("down");
            downList.add(patientsNumber);
            this.patientsNumToTrindMap.put("down", downList);

            List<Integer> downDsList = this.patientsNumToDSIDMap.get("down");
            downDsList.add(dsID);
            this.patientsNumToDSIDMap.put("down", downDsList);

        } else {

            addEqualProtein(patientsNumber, dsID);
        }
    }

    /**
     * Add +1 for number of equal protein in comparison (protein value didn't
     * changed)
     *
     * @param patientsNumber Total number of patients in this dataset.
     * @param dsID Dataset index in database
     */
    public void addEqualProtein(int patientsNumber, int dsID) {
        penalty += 0.5;
        this.stable += 1;
        List<Integer> notRegList = this.patientsNumToTrindMap.get("equal");
        notRegList.add(patientsNumber);
        this.patientsNumToTrindMap.put("equal", notRegList);
        List<Integer> notRegDsList = this.patientsNumToDSIDMap.get("equal");
        notRegDsList.add(dsID);
        this.patientsNumToDSIDMap.put("equal", notRegDsList);
    }

    /**
     * Add +1 for number of increased protein in comparison (protein value
     * decreased in this comparison)
     *
     * @param patientsNumber Total number of patients in this dataset.
     * @param dsID Dataset index in database
     * @param significant The pValue is significant
     */
    public void addIncreasedProtein(int patientsNumber, int dsID, boolean significant) {

        if (significant) {
            trendValue += (double) 1;
            this.significantlyIncreasedNumber += 1;
            List<Integer> upList = this.patientsNumToTrindMap.get("up");
            upList.add(patientsNumber);
            this.patientsNumToTrindMap.put("up", upList);

            List<Integer> upDsList = this.patientsNumToDSIDMap.get("up");
            upDsList.add(dsID);
            this.patientsNumToDSIDMap.put("up", upDsList);

        } else {
            addEqualProtein(patientsNumber, dsID);
        }

    }

    /**
     * Add +1 for number of protein with no value provided in comparison
     *
     * @param patientsNumber Total number of patients in this dataset.
     * @param dsID Dataset index in database
     */
    public void addNoValueProvided(int patientsNumber, int dsID) {

        this.noValueprovided += 1;
        List<Integer> noValueProvidedList = this.patientsNumToTrindMap.get("noValueProvided");
        noValueProvidedList.add(patientsNumber);
        this.patientsNumToTrindMap.put("noValueProvided", noValueProvidedList);

        List<Integer> noValueProvidedDsList = this.patientsNumToDSIDMap.get("noValueProvided");
        noValueProvidedDsList.add(dsID);
        this.patientsNumToDSIDMap.put("noValueProvided", noValueProvidedDsList);

    }

    /**
     * Set protein accession number
     *
     * @param proteinAccession Protein accession number
     */
    public void setProteinAccession(String proteinAccession) {
        this.proteinAccession = proteinAccession;
    }

    /**
     * Set Protein name
     *
     * @param proteinName Protein name
     */
    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    /**
     * Set UniProt protein link
     *
     * @param url UniProt protein link
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get trend value used for plotting the protein location in bubble charts
     *
     * @return trendValue Trend value
     */
    public Double getTrendValue() {
        return trendValue;
    }

    /**
     * Get overall increase significant number for the protein in different
     * datasets
     *
     * @return significantlyIncreasedNumber Overall increase significant number.
     */
    public int getSignificantlyIncreasedNumber() {
        return significantlyIncreasedNumber;
    }

    /**
     * Get overall decrease significant number for the protein in different
     * datasets
     *
     * @return significantlyDecreasedNumber Overall decrease significant number
     * for the protein
     */
    public int getSignificantlyDecreasedNumber() {
        return significantlyDecreasedNumber;
    }

    /**
     * Calculate the final values for protein trend across different datasets.
     */
    public void finalizeQuantData() {

        Double v1;
        if (significantlyIncreasedNumber == significantlyDecreasedNumber.intValue()) {
            v1 = trendValue;
        } else if (trendValue > 0) {
            double factor = penalty;
            v1 = trendValue - factor;
            v1 = Math.max(v1, 0) + ((double) (significantlyIncreasedNumber - significantlyDecreasedNumber) / 10.0);
        } else {
            double factor = penalty;
            v1 = trendValue + factor;
            v1 = Math.min(v1, 0) + ((double) (significantlyIncreasedNumber - significantlyDecreasedNumber) / 10.0);
        }
        if (v1 > 0) {
            cellValue = Math.min(v1, 1);
        } else if (v1 < 0) {
            cellValue = Math.max(v1, -1);
        }
        int existStudiesNumber = significantlyDecreasedNumber + significantlyIncreasedNumber + stable;
        if (cellValue > 0) {
            if (stable > 0 || significantlyDecreasedNumber > 0) {
                significantTrindCategory = 3;
                overallCellPercentValue = Math.max((((double) (significantlyIncreasedNumber - significantlyDecreasedNumber)) / (double) existStudiesNumber) * 100.0, 5.0);

            } else {
                significantTrindCategory = 4;
                overallCellPercentValue = 100;
            }

        } else if (cellValue == 0) {
            significantTrindCategory = 2;
            overallCellPercentValue = 0;

        } else {
            if (stable > 0 || significantlyIncreasedNumber > 0) {
                significantTrindCategory = 1;
                overallCellPercentValue = Math.max((((double) (significantlyDecreasedNumber - significantlyIncreasedNumber)) / (double) existStudiesNumber) * 100.0, 5.0);
                overallCellPercentValue = -1 * overallCellPercentValue;
            } else {
                significantTrindCategory = 0;
                overallCellPercentValue = -100;
            }
        }
        if (stable == 0 && significantlyIncreasedNumber == 0 && significantlyDecreasedNumber == 0 && noValueprovided > 0) {
            significantTrindCategory = 5;
            overallCellPercentValue = 0;
        }
    }

    /**
     * Get trend value used for plotting the protein location in line charts
     *
     * @return overallCellPercentValue The trend value used for plotting the
     * protein location in line charts
     */
    public double getOverallCellPercentValue() {
        return overallCellPercentValue;
    }

}
