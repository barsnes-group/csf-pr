package no.uib.probe.csf.pr.touch.logic.beans;

/**
 ** This class represents quant protein object that contain all the protein
 * information
 *
 * @author Yehia Farag
 */

public class QuantProtein implements Comparable<QuantProtein> {

    /**
     * Publication PubMed id
     */
    private String pubMedId;

    /**
     * Protein UniProt accession number.
     */
    private String uniprotAccessionNumber;
    /**
     * Protein UniProt name.
     */
    private String uniprotProteinName;
    /**
     * Protein publication accession number.
     */
    private String publicationAccessionNumber;
    /**
     * Protein publication name.
     */
    private String publicationProteinName;
    /**
     * Raw data available(available/not available).
     */
    private String rawDataAvailable;
    /**
     * Study type.
     */
    private String typeOfStudy;
    /**
     * Sample type.
     */
    private String sampleType;
    /**
     * The Disease main group I (not used in the current CSF-PR 2.0).
     */
    private String diseaseMainGroupI;
    /**
     * The Disease sub group I (publication name).
     */
    private String originalDiseaseSubGroupI;
    /**
     * The Disease main group I comments.
     */
    private String diseaseMainGroupIComment;
    /**
     * The Disease main group II (not used in the current CSF-PR 2.0).
     */
    private String diseaseMainGroupII;
    /**
     * The Disease sub group II (publication name).
     */
    private String originalDiseaseSubGroupII;
    /**
     * The Disease main group II comments.
     */
    private String diseaseMainGroupIIComment;
    /**
     * Sample matching.
     */
    private String sampleMatching;
    /**
     * Normalization strategy.
     */
    private String normalizationStrategy;
    /**
     * Technology.
     */
    private String technology;
    /**
     * Analytical approach.
     */
    private String analyticalApproach;
    /**
     * Enzyme.
     */
    private String enzyme;
    /**
     * Shotgun or targetedQquant study.
     */
    private String shotgunOrTargetedQquant;
    /**
     * Protein quantification basis.
     */
    private String quantificationBasis;
    /**
     * Protein quantification basis comments.
     */
    private String quantBasisComment;
    /**
     * Protein additional comments.
     */
    private String additionalComments;

    /**
     * Number of identified peptides.
     */
    private int peptideIdNumb;
    /**
     * number of quantified peptides.
     */
    private int quantifiedPeptidesNumber;
    /**
     * Disease main group I patients number.
     */
    private int diseaseGroupIPatientsNumber;
    /**
     * Disease main group II patients number.
     */
    private int diseaseGroupIIPatientsNumber;
    /**
     * Publication publishing year.
     */
    private int year;

    /**
     * Unique parent dataset id (dataset index in database).
     */
    private int quantDatasetIndex;
    /**
     * Unique protein id (protein index in database).
     */
    private int protIndex;

    /**
     * Fold change as text (increased, decreased or equal).
     */
    private String string_fc_value;
    /**
     * Quantification p_value (Significant/not significant).
     */
    private String string_p_value;
    /**
     * The publication author name.
     */
    private String author;
    /**
     * Peptide p_value comments.
     */
    private String p_value_comments;
    /**
     * Quantification p_value significance threshold.
     */
    private String pvalueSignificanceThreshold;
    /**
     * Disease category (MS,AD,PD...etc).
     */
    private String diseaseCategoryI;
      /**
     * Disease category (MS,AD,PD...etc).
     */
    private String diseaseCategoryII;
    /**
     * Link to protein in UniProt.
     */
    private String URL;
    /**
     * The final accession (UniProt or publication) will be used in the protein
     * table.
     */
    private String finalAccession;

    /**
     * The protein sequence imported from UniProt.
     */
    private String sequence;
    /**
     * Quantification pValue.
     */
    private double p_value;

    /**
     * Receiver operating characteristic.
     */
    private double roc_auc;
    /**
     * Fold change value (log2 value).
     */
    private double fc_value;

    /**
     * Protein has peptides data.
     */
    private boolean peptideProt;

    /**
     * Get the final accession (UniProt or publication-if UniProt accession not
     * available use protein publication accession number) will be used in the
     * protein table
     *
     * @return finalAccession The final selected protein accession
     */
    public String getFinalAccession() {
        return finalAccession;
    }

    /**
     * Set the final accession (UniProt or publication-if UniProt accession not
     * available use protein publication accession number) will be used in the
     * protein table
     *
     * @param finalAccession The final selected protein accession
     */
    public void setFinalAccession(String finalAccession) {
        this.finalAccession = finalAccession;
    }

    /**
     * Get disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategory Disease category name
     */
    public String getDiseaseCategoryI() {
        return diseaseCategoryI;
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategory Disease category name
     */
    public void setDiseaseCategoryI(String diseaseCategory) {
        this.diseaseCategoryI = diseaseCategory;
    }

    /**
     * get protein sequence
     *
     * @return sequence Protein sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Set protein sequence
     *
     * @param sequence Protein sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
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
     * Get quantification p_value comments
     *
     * @return p_value_comments pValue comments
     */
    public String getP_value_comments() {
        return p_value_comments;
    }

    /**
     * Set quantification p_value comments
     *
     * @param p_value_comments pValue comments
     */
    public void setP_value_comments(String p_value_comments) {
        this.p_value_comments = p_value_comments;
    }

    /**
     * Get unique protein id (protein index in database)
     *
     * @return protIndex protein id
     */
    public int getProtIndex() {
        return protIndex;
    }

    /**
     * Set unique protein id (protein index in database)
     *
     * @param protIndex protein id
     */
    public void setProtIndex(int protIndex) {
        this.protIndex = protIndex;
    }

    /**
     * Set the dataset index in the database
     *
     * @param quantDatasetIndex dataset index
     */
    public void setQuantDatasetIndex(int quantDatasetIndex) {
        this.quantDatasetIndex = quantDatasetIndex;
    }

    /**
     * Get quantification pValue (Significant/not significant)
     *
     * @return string_p_value pValue (Significant/not significant)
     */
    public String getString_p_value() {
        return string_p_value;
    }

    /**
     * Set quantification pValue (Significant/not significant)
     *
     * @param string_p_value pValue (Significant/not significant)
     */
    public void setString_p_value(String string_p_value) {
        this.string_p_value = string_p_value;
    }

    /**
     * Get the publication publishing year
     *
     * @return year Publication year
     */
    public int getYear() {
        return year;
    }

    /**
     * Set the publication publishing year
     *
     * @param year Publication year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Get the publication author name
     *
     * @return author Publication author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the publication author name
     *
     * @param author Publication author name
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get fold change as text (increased, decreased or equal)
     *
     * @return string_fc_value Fold change as text
     */
    public String getString_fc_value() {
        return string_fc_value;
    }

    /**
     * Set fold change as text (increased, decreased or equal)
     *
     * @param string_fc_value Fold change as text
     */
    public void setString_fc_value(String string_fc_value) {
        this.string_fc_value = string_fc_value;
    }

    /**
     * Check if protein has peptides
     *
     * @return peptideProt Protein has peptides.
     */
    public boolean isPeptideProt() {
        return peptideProt;
    }

    /**
     * Set protein has peptides
     *
     * @param peptideProt Protein has peptides
     */
    public void setPeptideProt(boolean peptideProt) {
        this.peptideProt = peptideProt;
    }

    /**
     * Get publication PubMed id
     *
     * @return pubMedId Publication PubMed id
     */
    public String getPubMedId() {
        return pubMedId;
    }

    /**
     * Set publication PubMed id
     *
     * @param pubMedId Publication PubMed id
     */
    public void setPubMedId(String pubMedId) {
        this.pubMedId = pubMedId;
    }

    /**
     * Get UniProt protein accession number
     *
     * @return uniprotAccessionNumber Accession number
     */
    public String getUniprotAccessionNumber() {
        return uniprotAccessionNumber;
    }

    /**
     * Set UniProt protein accession number
     *
     * @param uniprotAccessionNumber Accession number
     */
    public void setUniprotAccessionNumber(String uniprotAccessionNumber) {
        this.uniprotAccessionNumber = uniprotAccessionNumber;
    }

    /**
     * Get UniProt protein name
     *
     * @return uniprotProteinName UniProt protein name
     */
    public String getUniprotProteinName() {
        return uniprotProteinName;
    }

    /**
     * Set UniProt protein name
     *
     * @param uniprotProteinName UniProt protein name
     */
    public void setUniprotProteinName(String uniprotProteinName) {
        this.uniprotProteinName = uniprotProteinName;
    }

    /**
     * Get publication protein accession number (IPI or UniProt..etc)
     *
     * @return publicationAccessionNumber Protein accession number
     */
    public String getPublicationAccessionNumber() {
        return publicationAccessionNumber;
    }

    /**
     * Set publication protein accession number (IPI or UniProt..etc)
     *
     * @param publicationAccessionNumber Protein accession number
     */
    public void setPublicationAccessionNumber(String publicationAccessionNumber) {
        this.publicationAccessionNumber = publicationAccessionNumber;
    }

    /**
     * Get publication protein name
     *
     * @return publicationProteinName Publication protein name
     */
    public String getPublicationProteinName() {
        return publicationProteinName;
    }

    /**
     * Set publication protein name
     *
     * @param publicationProteinName Publication protein name
     */
    public void setPublicationProteinName(String publicationProteinName) {
        this.publicationProteinName = publicationProteinName;
    }

    /**
     * Get raw data available(available/not available)
     *
     * @return rawDataAvailable Raw data is available
     */
    public String getRawDataAvailable() {
        return rawDataAvailable;
    }

    /**
     * Set raw data available
     *
     * @param rawDataAvailable Raw data is available
     */
    public void setRawDataAvailable(String rawDataAvailable) {
        this.rawDataAvailable = rawDataAvailable;
    }

    /**
     * Get number of identified peptides
     *
     * @return peptideIdNumb Number of identified peptides
     */
    public int getPeptideIdNumb() {
        return peptideIdNumb;
    }

    /**
     * Set number of identified peptides
     *
     * @param peptideIdNumb Number of identified peptides
     */
    public void setPeptideIdNumb(int peptideIdNumb) {
        this.peptideIdNumb = peptideIdNumb;
    }

    /**
     * Get number of quantified peptides
     *
     * @return quantifiedPeptidesNumber Number of quantified peptides
     */
    public int getQuantifiedPeptidesNumber() {
        return quantifiedPeptidesNumber;
    }

    /**
     * Set number of quantified peptides
     *
     * @param quantifiedPeptidesNumber Number of quantified peptides
     */
    public void setQuantifiedPeptidesNumber(int quantifiedPeptidesNumber) {
        this.quantifiedPeptidesNumber = quantifiedPeptidesNumber;
    }

    /**
     * Get fold change value (log2 value)
     *
     * @return fc_value Fold change value (log2 value)
     */
    public double getFc_value() {
        return fc_value;
    }

    /**
     * Set fold change value (log2 value)
     *
     * @param fc_value Fold change value (log2 value)
     */
    public void setFc_value(double fc_value) {
        this.fc_value = fc_value;
    }

    /**
     * Get study type
     *
     * @return typeOfStudy Study type
     */
    public String getTypeOfStudy() {
        return typeOfStudy;
    }

    /**
     * Set study type
     *
     * @param typeOfStudy Study type
     */
    public void setTypeOfStudy(String typeOfStudy) {
        this.typeOfStudy = typeOfStudy;
    }

    /**
     * Get sample type
     *
     * @return sampleType Sample type
     */
    public String getSampleType() {
        return sampleType;
    }

    /**
     * Set sample type
     *
     * @param sampleType Sample type
     */
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    /**
     * Get disease main group I patients number
     *
     * @return diseaseGroupIPatientsNumber Patients number
     */
    public int getDiseaseGroupIPatientsNumber() {
        return diseaseGroupIPatientsNumber;
    }

    /**
     * Set disease main group I patients number
     *
     * @param diseaseGroupIPatientsNumber Patients number
     */
    public void setDiseaseGroupIPatientsNumber(int diseaseGroupIPatientsNumber) {
        this.diseaseGroupIPatientsNumber = diseaseGroupIPatientsNumber;
    }

    /**
     * Get the disease main group I (not used in the current CSF-PR 2.0)
     *
     * @return diseaseMainGroupI Disease main group I
     */
    public String getDiseaseMainGroupI() {
        return diseaseMainGroupI;
    }

    /**
     * Set the disease main group I (not used in the current CSF-PR 2.0)
     *
     * @param diseaseMainGroupI Disease main group I
     */
    public void setDiseaseMainGroupI(String diseaseMainGroupI) {
        this.diseaseMainGroupI = diseaseMainGroupI;
    }

    /**
     * Get the disease sub group I (publication name)
     *
     * @return originalDiseaseSubGroupI Disease sub group I (publication name)
     */
    public String getOriginalDiseaseSubGroupI() {
        return originalDiseaseSubGroupI;
    }

    /**
     * Set the disease sub group I (publication name)
     *
     * @param originalDiseaseSubGroupI Disease sub group I (publication name)
     */
    public void setOriginalDiseaseSubGroupI(String originalDiseaseSubGroupI) {
        this.originalDiseaseSubGroupI = originalDiseaseSubGroupI;
    }

    /**
     * Get disease main group I comments
     *
     * @return diseaseMainGroupIComment Disease main group I comments
     */
    public String getDiseaseMainGroupIComment() {
        return diseaseMainGroupIComment;
    }

    /**
     * Set disease main group I comments
     *
     * @param diseaseMainGroupIComment Disease main group I comments
     */
    public void setDiseaseMainGroupIComment(String diseaseMainGroupIComment) {
        this.diseaseMainGroupIComment = diseaseMainGroupIComment;
    }

    /**
     * Set Disease main group II patients number
     *
     * @return diseaseGroupIIPatientsNumber Patients number
     */
    public int getDiseaseGroupIIPatientsNumber() {
        return diseaseGroupIIPatientsNumber;
    }

    /**
     * Set Disease main group II patients number
     *
     * @param diseaseGroupIIPatientsNumber Patients number
     */
    public void setDiseaseGroupIIPatientsNumber(int diseaseGroupIIPatientsNumber) {
        this.diseaseGroupIIPatientsNumber = diseaseGroupIIPatientsNumber;
    }

    /**
     * Get the disease main group II (not used in the current CSF-PR 2.0)
     *
     * @return diseaseMainGroupII Disease main group II
     */
    public String getDiseaseMainGroupII() {
        return diseaseMainGroupII;
    }

    /**
     * Set the disease main group II (not used in the current CSF-PR 2.0)
     *
     * @param diseaseMainGroupII Disease main group II
     */
    public void setDiseaseMainGroupII(String diseaseMainGroupII) {
        this.diseaseMainGroupII = diseaseMainGroupII;
    }

    /**
     * Get the disease sub group II (publication name)
     *
     * @return originalDiseaseSubGroupII Disease sub group II (publication name)
     */
    public String getOriginalDiseaseSubGroupII() {
        return originalDiseaseSubGroupII;
    }

    /**
     * Set the disease sub group II (publication name)
     *
     * @param originalDiseaseSubGroupII Disease sub group II (publication name)
     */
    public void setOriginalDiseaseSubGroupII(String originalDiseaseSubGroupII) {
        this.originalDiseaseSubGroupII = originalDiseaseSubGroupII;
    }

    /**
     * Get disease main group II comments
     *
     * @return diseaseMainGroupIIComment Disease main group II comments
     */
    public String getDiseaseMainGroupIIComment() {
        return diseaseMainGroupIIComment;
    }

    /**
     * Set Disease main group II comments
     *
     * @param diseaseMainGroupIIComment Disease main group II comments
     */
    public void setDiseaseMainGroupIIComment(String diseaseMainGroupIIComment) {
        this.diseaseMainGroupIIComment = diseaseMainGroupIIComment;
    }

    /**
     * Get sample matching
     *
     * @return sampleMatching Sample matching
     */
    public String getSampleMatching() {
        return sampleMatching;
    }

    /**
     * Set sample matching
     *
     * @param sampleMatching Sample matching
     */
    public void setSampleMatching(String sampleMatching) {
        this.sampleMatching = sampleMatching;
    }

    /**
     * Get normalization strategy Normalization strategy
     *
     * @return normalizationStrategy
     */
    public String getNormalizationStrategy() {
        return normalizationStrategy;
    }

    /**
     * Set normalization strategy
     *
     * @param normalizationStrategy Normalization strategy
     */
    public void setNormalizationStrategy(String normalizationStrategy) {
        this.normalizationStrategy = normalizationStrategy;
    }

    /**
     * Get quantification pValue(actual value if available)
     *
     * @return p_value pValue(actual value if available)
     */
    public double getP_value() {
        return p_value;
    }

    /**
     * Set quantification pValue(actual value if available)
     *
     * @param p_value pValue(actual value if available)
     */
    public void setP_value(double p_value) {
        this.p_value = p_value;
    }

    /**
     * Get receiver operating characteristic value
     *
     * @return roc_auc Receiver operating characteristic value
     */
    public double getRoc_auc() {
        return roc_auc;
    }

    /**
     * Set receiver operating characteristic value
     *
     * @param roc_auc Receiver operating characteristic value
     */
    public void setRoc_auc(double roc_auc) {
        this.roc_auc = roc_auc;
    }

    /**
     * Get technology
     *
     * @return technology The used technology
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Set technology
     *
     * @param technology The used technology
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Get analytical approach
     *
     * @return analyticalApproach Analytical approach
     */
    public String getAnalyticalApproach() {
        return analyticalApproach;
    }

    /**
     * Set analytical approach
     *
     * @param analyticalApproach Analytical approach
     */
    public void setAnalyticalApproach(String analyticalApproach) {
        this.analyticalApproach = analyticalApproach;
    }

    /**
     * Get enzyme used
     *
     * @return enzyme Used enzyme
     */
    public String getEnzyme() {
        return enzyme;
    }

    /**
     * Set enzyme used
     *
     * @param enzyme Used enzyme
     */
    public void setEnzyme(String enzyme) {
        this.enzyme = enzyme;
    }

    /**
     * Get Shotgun / Targeted
     *
     * @return shotgunTargeted Shotgun or Targeted
     */
    public String getShotgunOrTargetedQquant() {
        return shotgunOrTargetedQquant;
    }

    /**
     * Set Shotgun / Targeted
     *
     * @param shotgunOrTargetedQquant Shotgun or Targeted
     */
    public void setShotgunOrTargetedQquant(String shotgunOrTargetedQquant) {
        this.shotgunOrTargetedQquant = shotgunOrTargetedQquant;
    }

    /**
     * Get quantification basis
     *
     * @return quantificationBasis Quantification basis
     */
    public String getQuantificationBasis() {
        return quantificationBasis;
    }

    /**
     * Set quantification basis
     *
     * @param quantificationBasis Quantification basis
     */
    public void setQuantificationBasis(String quantificationBasis) {
        this.quantificationBasis = quantificationBasis;
    }

    /**
     * Get quantification basis comments (protein level) the quant bases
     * comments are different in protein and peptides level
     *
     * @return quantBasisComment Quantification basis comments
     */
    public String getQuantBasisComment() {
        return quantBasisComment;
    }

    /**
     * Set quantification basis comments (protein level) the quant bases
     * comments are different in protein and peptides level
     *
     * @param quantBasisComment Quantification basis comments
     */
    public void setQuantBasisComment(String quantBasisComment) {
        this.quantBasisComment = quantBasisComment;
    }

    /**
     * Get protein additional comments
     *
     * @return additionalComments Protein additional comments
     */
    public String getAdditionalComments() {
        return additionalComments;
    }

    /**
     * Set protein additional comments
     *
     * @param additionalComments Protein additional comments
     */
    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    /**
     * Get quantification pValue significance threshold
     *
     * @return pvalueSignificanceThreshold pValue significance threshold
     */
    public String getPvalueSignificanceThreshold() {
        return pvalueSignificanceThreshold;
    }

    /**
     * Set quantification pValue significance threshold
     *
     * @param pvalueSignificanceThreshold pValue significance threshold
     */
    public void setPvalueSignificanceThreshold(String pvalueSignificanceThreshold) {
        this.pvalueSignificanceThreshold = pvalueSignificanceThreshold;
    }

     /**
     * Override equals method
     *
     * @param o object to compare
     * @return object are equal
     */
    @Override
    public int compareTo(QuantProtein o) {
        if ((this.getDiseaseGroupIPatientsNumber() + this.getDiseaseGroupIIPatientsNumber()) > (o.getDiseaseGroupIPatientsNumber() + o.getDiseaseGroupIIPatientsNumber())) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Get link to protein in UniProt
     *
     * @return URL Link to protein in UniProt
     */
    public String getURL() {
        return URL;
    }

    /**
     * Set link to protein in UniProt
     *
     * @param URL Link to protein in UniProt
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * Get second disease category for second disease sub group 
     * @return second disease category
     */
    public String getDiseaseCategoryII() {
        return diseaseCategoryII;
    }

    /**
     *Set second disease category for second disease sub group 
     * @param diseaseCategoryII second disease category
     */
    public void setDiseaseCategoryII(String diseaseCategoryII) {
        this.diseaseCategoryII = diseaseCategoryII;
    }

    
    /**
     *Is the quant protein object come from comparisons between 2 different disease categories
     * @return the comparisons between 2 different disease categories
     */
    public boolean isCrossDiseases() {
        return !diseaseCategoryI.equalsIgnoreCase(diseaseCategoryII);
    }

}
