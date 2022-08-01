package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the publication study object that has all publication
 * information.
 *
 * @author Yehia Farag
 */
public class QuantDataset implements Serializable, Comparable {

    /**
     * Location x used for plotting the dataset.
     */
    private int x;
    /**
     * Location y used for plotting the dataset.
     */
    private int y;
    /**
     * *
     * Disease HTML Color code.
     */
    private String diseaseHashedColor;
    /**
     * Disease Style name at CSS file.
     */
    private String diseaseStyleName;
    /**
     * The Disease sub group I (currently used in the system)
     */
    private String activeDiseaseSubGroupI;
    /**
     * The Disease sub group II (currently used in the system)
     */
    private String activeDiseaseSubGroupII;

    /**
     * The dataset index in the database.
     */
    private int quantDatasetIndex;

    /**
     * The array of values for the included attributes.
     */
    private final Object[] values = new Object[29];

    /**
     * Map of values to attribute name.
     */
    private final Map<String, Object> valuesMap = new HashMap<>();

    /**
     * Number of included proteins in the dataset.
     */
    private int totalProtNum;

    /**
     * Number of proteins found only in the dataset.
     */
    private int uniqueProtNum;
    /**
     * Number of included peptides in the dataset.
     */
    private int totalPepNum;
    /**
     * Number of peptides found only in the dataset.
     */
    private int uniqePepNum;
    /**
     * The dataset is pool samples dataset.
     */
    private boolean pooledSamples;
     /**
     * The dataset cross disease.
     */
    private boolean crossDisease;

    /**
     *Is the dataset is based on pool sample
     * @return the dataset is pool sample based
     */
    public boolean isPooledSamples() {
        return pooledSamples;
    }

    /**
     *Set the dataset to be based on pool sample
     * @param pooledSamples the dataset is pool sampled
     */
    public void setPooledSamples(boolean pooledSamples) {
        this.pooledSamples = pooledSamples;
    }

    /**
     * *
     * Get Disease Style name at CSS file
     *
     * @return diseaseStyleName Disease Style name at CSS file.
     */
    public String getDiseaseStyleName() {
        return diseaseStyleName;
    }

    /**
     * *
     * Get the Disease sub group I (currently used in the system)
     *
     * @return activeDiseaseSubGroupI Disease sub group I
     */
    public String getActiveDiseaseSubGroupI() {
        if (activeDiseaseSubGroupI == null) {
            return this.getDiseaseSubGroup1();
        }
        return activeDiseaseSubGroupI;
    }

    /**
     * *
     * Set the Disease sub group I (currently used in the system)
     *
     * @param activeDiseaseSubGroupI Disease sub group I
     */
    public void setActiveDiseaseSubGroupI(String activeDiseaseSubGroupI) {
        this.activeDiseaseSubGroupI = activeDiseaseSubGroupI;
    }

    /**
     * *
     * Get the Disease sub group II (currently used in the system)
     *
     * @return activeDiseaseSubGroupII Disease sub group II
     */
    public String getActiveDiseaseSubGroupII() {
        if (activeDiseaseSubGroupII == null) {
            return this.getDiseaseSubGroup2();
        }
        return activeDiseaseSubGroupII;
    }

    /**
     * *
     * Set the disease main group II (not used in the current CSF-PR 2.0)
     *
     * @param activeDiseaseSubGroupII Disease sub group II
     */
    public void setActiveDiseaseSubGroupII(String activeDiseaseSubGroupII) {
        this.activeDiseaseSubGroupII = activeDiseaseSubGroupII;
    }

    /**
     * Set Disease Style name at CSS file
     *
     * @param diseaseStyleName Disease Style name at CSS file.
     */
    public void setDiseaseStyleName(String diseaseStyleName) {
        this.diseaseStyleName = diseaseStyleName;
    }

    /**
     * Get disease category HTML color code
     *
     * @return diseaseHashedColor Disease category HTML color code.
     */
    public String getDiseaseHashedColor() {
        return diseaseHashedColor;
    }

    /**
     * Set disease category HTML color code
     *
     * @param diseaseHashedColor Disease category HTML color code.
     */
    public void setDiseaseHashedColor(String diseaseHashedColor) {
        this.diseaseHashedColor = diseaseHashedColor;
    }

    /**
     * Get main disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategory Disease category name(MS,AD,PD...etc)
     */
    public String getDiseaseCategoryI() {
        return values[27].toString();
    }

    /**
     * Get second disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategory Disease category name(MS,AD,PD...etc)
     */
    public String getDiseaseCategoryII() {
        return values[28].toString();
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategoryI Disease category name(MS,AD,PD...etc)
     * @param diseaseCategoryII Second disease category name(MS,AD,PD...etc)
     */
    public void setDiseaseCategory(String diseaseCategoryI, String diseaseCategoryII) {
        values[27] = diseaseCategoryI;
        valuesMap.put("diseaseCategoryI", diseaseCategoryI);
        values[28] = diseaseCategoryII;
        valuesMap.put("diseaseCategoryII", diseaseCategoryII);
       
    }

    /**
     * Get the publication author name
     *
     * @return author Publication author name
     */
    public String getAuthor() {
        return values[0].toString();
    }

    /**
     * Set the publication author name
     *
     * @param author Publication author name
     */
    public void setAuthor(String author) {
        values[0] = author;
        valuesMap.put("author", author);
    }

    /**
     * Get the publication publishing year
     *
     * @return year Publication year
     */
    public int getYear() {
        return (Integer) values[1];
    }

    /**
     * Set the publication publishing year
     *
     * @param year Publication year
     */
    public void setYear(int year) {
        values[1] = year;
        valuesMap.put("year", year);
    }

    /**
     * Get dataset identified proteins number
     *
     * @return identifiedProteinsNumber Dataset identified proteins number
     */
//    public int getIdentifiedProteinsNumber() {
//        return Math.max((Integer) values[2], 0);
//    }

//    /**
//     * Set dataset identified proteins number
//     *
//     * @param identifiedProteinsNumber Dataset identified proteins number
//     */
//    public void setIdentifiedProteinsNumber(int identifiedProteinsNumber) {
//        values[2] = identifiedProteinsNumber;
//        valuesMap.put("identifiedProteinsNumber", identifiedProteinsNumber);
//    }

    /**
     * Set dataset quantified proteins number
     *
     * @return quantifiedProteinsNumber Dataset quantified proteins number
     */
    public int getQuantifiedProteinsNumber() {
        return Math.max((Integer) values[3], 0);
    }

    /**
     * Get dataset quantified proteins number
     *
     * @param quantifiedProteinsNumber Dataset quantified proteins number
     */
    public void setQuantifiedProteinsNumber(int quantifiedProteinsNumber) {
        values[3] = quantifiedProteinsNumber;
        valuesMap.put("quantifiedProteinsNumber", quantifiedProteinsNumber);
    }

    /**
     * Get analytical method
     *
     * @return analyticalMethod Analytical method
     */
    public String getAnalyticalMethod() {
        return values[4].toString();
    }

    /**
     * Set analytical method
     *
     * @param analyticalMethod Analytical method
     */
    public void setAnalyticalMethod(String analyticalMethod) {
        values[4] = analyticalMethod;
        valuesMap.put("analytical_method", analyticalMethod);
    }

    /**
     * Get raw data available(available/not available)
     *
     * @return rawDataAvailable Raw data is available
     */
    public String getRawDataUrl() {
        return values[5].toString();
    }

    /**
     * Set raw data available
     *
     * @param rawDataAvailable Raw data is available
     */
    public void setRawDataUrl(String rawDataAvailable) {
        values[5] = rawDataAvailable;
        valuesMap.put("rawDataUrl", rawDataAvailable);
    }

    /**
     * Get study type
     *
     * @return typeOfStudy Study type
     */
    public String getTypeOfStudy() {
        return values[7].toString();
    }

    /**
     * Set study type
     *
     * @param typeOfStudy Study type
     */
    public void setTypeOfStudy(String typeOfStudy) {
        values[7] = typeOfStudy;
        valuesMap.put("typeOfStudy", typeOfStudy);
    }

    /**
     * Get sample type
     *
     * @return sampleType Sample type
     */
    public String getSampleType() {
        return values[8].toString();
    }

    /**
     * Set sample type
     *
     * @param sampleType Sample type
     */
    public void setSampleType(String sampleType) {
        values[8] = sampleType;
        valuesMap.put("sampleType", sampleType);
    }

    /**
     * Get sample matching
     *
     * @return sampleMatching Sample matching
     */
    public String getSampleMatching() {
        return values[9].toString();
    }

    /**
     * Set sample matching
     *
     * @param sampleMatching Sample matching
     */
    public void setSampleMatching(String sampleMatching) {
        values[9] = sampleMatching;
        valuesMap.put("sampleMatching", sampleMatching);
    }

    /**
     * Get Shotgun / Targeted
     *
     * @return shotgunTargeted Shotgun or Targeted
     */
    public String getShotgunTargeted() {
        return values[10].toString();
    }

    /**
     * Set Shotgun / Targeted
     *
     * @param shotgunTargeted Shotgun or Targeted
     */
    public void setShotgunTargeted(String shotgunTargeted) {
        values[10] = shotgunTargeted;
        valuesMap.put("shotgunTargeted", shotgunTargeted);
    }

    /**
     * Get technology
     *
     * @return technology
     */
    public String getTechnology() {
        return values[11].toString();
    }

    /**
     * Set technology used
     *
     * @param technology Technology used
     */
    public void setTechnology(String technology) {
        values[11] = technology;
        valuesMap.put("technology", technology);
    }

    /**
     * Get analytical approach
     *
     * @return analyticalApproach Analytical approach
     */
    public String getAnalyticalApproach() {
        return values[12].toString();
    }

    /**
     * Set analytical approach
     *
     * @param analyticalApproach Analytical approach
     */
    public void setAnalyticalApproach(String analyticalApproach) {
        values[12] = analyticalApproach;
        valuesMap.put("analyticalApproach", analyticalApproach);
    }

    /**
     * Get the enzyme used
     *
     * @return enzyme The used enzyme
     */
    public String getEnzyme() {
        return values[13].toString();
    }

    /**
     * Set enzyme used
     *
     * @param enzyme The used enzyme
     */
    public void setEnzyme(String enzyme) {
        values[13] = enzyme;
        valuesMap.put("enzyme", enzyme);
    }

    /**
     * Get quantification basis
     *
     * @return quantificationBasis Quantification basis
     */
    public String getQuantificationBasis() {
        return values[14].toString();
    }

    /**
     * Set quantification basis
     *
     * @param quantificationBasis Quantification basis
     */
    public void setQuantificationBasis(String quantificationBasis) {
        values[14] = quantificationBasis;
        valuesMap.put("quantificationBasis", quantificationBasis);
    }

//    /**
//     * Get quantification basis comments (protein level) the quant bases
//     * comments are different in protein and peptides level
//     *
//     * @return quantBasisComment Quantification basis comments
//     */
//    public String getQuantBasisComment() {
//        return values[15].toString();
//    }
//
//    /**
//     * Set quantification basis comments (protein level) the quant bases
//     * comments are different in protein and peptides level
//     *
//     * @param quantBasisComment Quantification basis comments
//     */
//    public void setQuantBasisComment(String quantBasisComment) {
//        values[15] = quantBasisComment;
//        valuesMap.put("quantBasisComment", quantBasisComment);
//    }

    /**
     * Get normalization strategy
     *
     * @return normalizationStrategy Normalization strategy
     */
    public String getNormalizationStrategy() {
        return values[16].toString();
    }

    /**
     * Set normalization strategy
     *
     * @param normalizationStrategy Normalization strategy
     */
    public void setNormalizationStrategy(String normalizationStrategy) {

        values[16] = normalizationStrategy;
        valuesMap.put("normalizationStrategy", normalizationStrategy);
    }

    /**
     * Get publication PubMed id
     *
     * @return pubMedId Publication PubMed id
     */
    public String getPubMedId() {
        return values[17].toString();
    }

    /**
     * Set publication PubMed id
     *
     * @param pubMedId Publication PubMed id
     */
    public void setPubMedId(String pubMedId) {
        values[17] = pubMedId;
        valuesMap.put("pumedID", pubMedId);
    }

    /**
     * Get the disease main group I (not used in the current CSF-PR 2.0)
     *
     * @return diseaseMainGroupI The disease main group I
     */
    public String getDiseaseMainGroupI() {
        return (String) values[18];
    }

    /**
     * Set the disease main group I (not used in the current CSF-PR 2.0)
     *
     * @param patientsGroup1 The disease main group I
     */
    public void setDiseaseMainGroupI(String patientsGroup1) {
        values[18] = patientsGroup1;
        valuesMap.put("patientsGroup1", patientsGroup1);
    }

    /**
     * Get the disease main group I patients number
     *
     * @return patientsGroup1Number Patients number
     */
    public int getDiseaseMainGroup1Number() {
        return (Integer) values[19];
    }

    /**
     * Set the disease main group I patients number
     *
     * @param patientsGroup1Number Patients number
     */
    public void setDiseaseMainGroup1Number(int patientsGroup1Number) {
        values[19] = patientsGroup1Number;
        valuesMap.put("patientsGroup1Number", patientsGroup1Number);
    }

    /**
     * Get the disease group I comments
     *
     * @return patientsGroup1Comm Disease group I comments
     */
    public String getDiseaseMainGroup1Comm() {
        return (String) values[20];
    }

    /**
     * Set the disease group I comments
     *
     * @param patientsGroup1Comm Disease group I comments
     */
    public void setDiseaseMainGroup1Comm(String patientsGroup1Comm) {
        values[20] = patientsGroup1Comm;
        valuesMap.put("patientsGroup1Comm", patientsGroup1Comm);
    }

    /**
     * Get the disease sub group I (publication name)
     *
     * @return patientsSubGroup1 Disease sub group I
     */
    public String getDiseaseSubGroup1() {
        return (String) values[21];
    }

    /**
     * Set the disease sub group I (publication name)
     *
     * @param patientsSubGroup1 Disease sub group I
     */
    public void setDiseaseSubGroup1(String patientsSubGroup1) {
        values[21] = patientsSubGroup1;
        valuesMap.put("patientsSubGroup1", patientsSubGroup1);
    }

    /**
     * Get the disease main group II (not used in the current CSF-PR 2.0)
     *
     * @return diseaseMainGroupII Disease main group II
     */
    public String getDiseaseMainGroup2() {
        return (String) values[22];
    }

    /**
     * Set the disease main group II (not used in the current CSF-PR 2.0)
     *
     * @param patientsGroup2 Disease main group II
     */
    public void setDiseaseMainGroup2(String patientsGroup2) {
        values[22] = patientsGroup2;
        valuesMap.put("patientsGroup2", patientsGroup2);
    }

    /**
     * Get the disease main group II patients number
     *
     * @return patientsGroup2Number Patients number
     */
    public int getDiseaseMainGroup2Number() {
        return (Integer) values[23];
    }

    /**
     * Set the disease main group II patients number
     *
     * @param patientsGroup2Number Patients number
     */
    public void setDiseaseMainGroup2Number(int patientsGroup2Number) {
        values[23] = patientsGroup2Number;
        valuesMap.put("patientsGroup2Number", patientsGroup2Number);
    }

    /**
     * SSet the disease group II comments
     *
     * @return patientsGroup2Comm
     */
    public String getDiseaseMainGroup2Comm() {
        return (String) values[24];
    }

    /**
     * Set the disease group II comments
     *
     * @param patientsGroup2Comm Disease group II comments
     */
    public void setDiseaseMainGroup2Comm(String patientsGroup2Comm) {
        values[24] = patientsGroup2Comm;
        valuesMap.put("patientsGroup2Comm", patientsGroup2Comm);
    }

    /**
     * Get the disease sub group II (publication name)
     *
     * @return diseaseSubGroupII Disease sub group II
     */
    public String getDiseaseSubGroup2() {
        return (String) values[25];
    }

    /**
     * Set the disease sub group II (publication name)
     *
     * @param patientsSubGroup2 Disease sub group II
     */
    public void setDiseaseSubGroup2(String patientsSubGroup2) {
        values[25] = patientsSubGroup2;
        valuesMap.put("patientsSubGroup2", patientsSubGroup2);
    }

    /**
     * Set additional comments for the disease group comparison
     *
     * @param additionalComments Additional comments
     */
    public void setAdditionalcomments(String additionalComments) {
        values[26] = additionalComments;
        valuesMap.put("additionalComments", additionalComments);
    }

    /**
     * Get additional comments for the disease group comparison
     *
     * @return additionalComments Additional comments
     */
    public String getAdditionalcomments() {
        return (String) values[26];
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public int compareTo(Object t) {
        if ((Integer) ((QuantDataset) t).values[1] > (Integer) this.values[1]) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Get the dataset index in the database
     *
     * @return quantDatasetIndex Dataset index
     */
    public int getQuantDatasetIndex() {
        return quantDatasetIndex;
    }

    /**
     * Set the dataset index in the database
     *
     * @param quantDatasetIndex Dataset index
     */
    public void setQuantDatasetIndex(int quantDatasetIndex) {
        this.quantDatasetIndex = quantDatasetIndex;
    }

    /**
     * Get location x used for plotting the dataset
     *
     * @return x Location x
     */
    public int getX() {
        return x;
    }

    /**
     * Set location x used for plotting the dataset
     *
     * @param x Location x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get location y used for plotting the dataset
     *
     * @return y Location y
     */
    public int getY() {
        return y;
    }

    /**
     * Set location y used for plotting the dataset
     *
     * @param y Location y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get number of included proteins in the dataset
     *
     * @return totalProtNum
     */
    public int getTotalProtNum() {
        return totalProtNum;
    }

    /**
     * Set number of included proteins in the dataset
     *
     * @param totalProtNum Number of included proteins
     */
    public void setTotalProtNum(int totalProtNum) {
        this.totalProtNum = totalProtNum;
    }

    /**
     * Set number of proteins found only in the dataset
     *
     * @return uniqueProtNum Number of included proteins
     */
    public int getUniqueProtNum() {
        return uniqueProtNum;
    }

    /**
     * Get number of proteins found only in the dataset
     *
     * @param uniqueProtNum Number of proteins found only in the dataset
     */
    public void setUniqueProtNum(int uniqueProtNum) {
        this.uniqueProtNum = uniqueProtNum;
    }

    /**
     * Set number of included peptides in the dataset
     *
     * @return totalPepNum Number of peptides in the dataset
     */
    public int getTotalPepNum() {
        return totalPepNum;
    }

    /**
     * Get number of included peptides in the dataset
     *
     * @param totalPepNum Number of peptides in the dataset
     */
    public void setTotalPepNum(int totalPepNum) {
        this.totalPepNum = totalPepNum;
    }

    /**
     * Get number of peptides found only in the dataset
     *
     * @return uniqePepNum Number of peptides found only in the dataset
     */
    public int getUniqePepNum() {
        return uniqePepNum;
    }

    /**
     * Set number of peptides found only in the dataset
     *
     * @param uniqePepNum Number of peptides found only in the dataset
     */
    public void setUniqePepNum(int uniqePepNum) {
        this.uniqePepNum = uniqePepNum;
    }

    /**
     *Is the dataset represents comparisons between 2 different disease categories
     * @return the dataset represents comparisons between 2 different disease categories
     */
    public boolean isCrossDisease() {
        return crossDisease;
    }

    /**
     * Set the dataset to represents comparisons between 2 different disease categories
     *
     * @param crossDisease 2 different disease categories
     */
    public void setCrossDisease(boolean crossDisease) {
        this.crossDisease = crossDisease;
    }

}
