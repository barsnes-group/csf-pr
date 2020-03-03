package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;

/**
 * This class represent protein ID data object currently not used in CSF-PR 2.0
 * for searching results overview
 *
 * @author Yehia Farag
 */
public class IdentificationProteinBean implements Serializable, Comparable<IdentificationProteinBean> {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Protein accession number
     */
    private String accession;

    /**
     * Datasetset id (dataset index in the database)
     */
    private int expId;

    /**
     * Protein name
     */
    private String Description;

    /**
     * Protein is valid
     */
    private boolean validated;

    /**
     * Protein is decoy
     */
    private int decoy;

    /**
     * The protein gene name
     */
    private String geneName;

    /**
     * The protein chromosome number
     */
    private String chromosomeNumber;

    /**
     * The protein group identification number
     */
    private int protGroupId;

    /**
     * The fraction number that has the protein
     */
    private int frcationId;

    /**
     * The other related proteins
     */
    private String otherProteins;

    /**
     * The protein inference class
     */
    private String proteinInferenceClass;

    /**
     * The protein sequence coverage
     */
    private double sequenceCoverage;

    /**
     * The protein observable coverage
     */
    private double observableCoverage;
    /**
     * The protein confident ptm sites
     */
    private String confidentPtmSites;

    /**
     * The protein number confident
     */
    private String numberConfident;

    /**
     * The protein other ptm sites
     */
    private String otherPtmSites;

    /**
     * The number of others proteins
     */
    private String numberOfOther;

    /**
     * The protein validated peptides number
     */
    private int numberValidatedPeptides;

    /**
     * The protein validated spectra number
     */
    private int numberValidatedSpectra;

    /**
     * The protein total spectra number
     */
    private int numberSpectra;

    /**
     * The protein total peptides number
     */
    private int numberPeptides;

    /**
     * The protein em pai
     */
    private double emPai;

    /**
     * The protein NSAF.
     */
    private double nsaf;

    /**
     * The protein MW in kDa.
     */
    private double mw_kDa;

    /**
     * The protein score.
     */
    private double score;

    /**
     * The protein confidence.
     */
    private double confidence;

    /**
     * The protein Starred.
     */
    private boolean Starred;

    /**
     * The protein peptide fraction spread lower range in kDa.
     */
    private String peptideFractionSpread_lower_range_kDa;

    /**
     * The protein peptide fraction spread upper range in kDa.
     */
    private String peptideFractionSpread_upper_range_kDa;

    /**
     * The protein spectrum fractionSpread lower range in kDa.
     */
    private String spectrumFractionSpread_lower_range_kDa;

    /**
     * The protein spectrum fraction spread upper range in kDa.
     */
    private String spectrumFractionSpread_upper_range_kDa;

    /**
     * The protein has non enzymatic peptides.
     */
    private boolean nonEnzymaticPeptides;

    /**
     * The protein number of peptides per fraction.
     */
    private int numberOfPeptidePerFraction;

    /**
     * The protein number of spectra per fraction .
     */
    private int numberOfSpectraPerFraction;

    /**
     * The protein average precursor intensity per fraction.
     */
    private double AveragePrecursorIntensityPerFraction;

    /**
     * *
     * The Default Constructor.
     */
    public IdentificationProteinBean() {
    }

    /**
     * Constructor allow cloning the data.
     *
     * @param identificationProteinBean identification protein bean
     */
    public IdentificationProteinBean(IdentificationProteinBean identificationProteinBean) {
        this.accession = identificationProteinBean.getAccession();
        this.Description = identificationProteinBean.getDescription();
        this.validated = identificationProteinBean.isValidated();
        this.otherProteins = identificationProteinBean.getOtherProteins();
        this.proteinInferenceClass = identificationProteinBean.getProteinInferenceClass();
        this.sequenceCoverage = identificationProteinBean.getSequenceCoverage();
        this.observableCoverage = identificationProteinBean.getObservableCoverage();
        this.confidentPtmSites = identificationProteinBean.getConfidentPtmSites();
        this.numberConfident = identificationProteinBean.getNumberConfident();
        this.otherPtmSites = identificationProteinBean.getOtherPtmSites();
        this.numberOfOther = identificationProteinBean.getNumberOfOther();
        this.numberValidatedPeptides = identificationProteinBean.getNumberValidatedPeptides();
        this.numberValidatedSpectra = identificationProteinBean.getNumberValidatedSpectra();
        this.emPai = identificationProteinBean.getEmPai();
        this.nsaf = identificationProteinBean.getNsaf();
        this.mw_kDa = identificationProteinBean.getMw_kDa();
        this.score = identificationProteinBean.getScore();
        this.confidence = identificationProteinBean.getConfidence();
        this.Starred = identificationProteinBean.isStarred();
        this.peptideFractionSpread_lower_range_kDa = identificationProteinBean.getPeptideFractionSpread_lower_range_kDa();
        this.peptideFractionSpread_upper_range_kDa = identificationProteinBean.getPeptideFractionSpread_upper_range_kDa();
        this.spectrumFractionSpread_lower_range_kDa = identificationProteinBean.getSpectrumFractionSpread_lower_range_kDa();
        this.spectrumFractionSpread_upper_range_kDa = identificationProteinBean.getSpectrumFractionSpread_upper_range_kDa();
        this.nonEnzymaticPeptides = identificationProteinBean.isNonEnzymaticPeptides();
        this.geneName = identificationProteinBean.getGeneName();
        this.chromosomeNumber = identificationProteinBean.getChromosomeNumber();
        this.numberPeptides = identificationProteinBean.getNumberPeptides();
        this.numberSpectra = identificationProteinBean.getNumberSpectra();
        this.expId = identificationProteinBean.getDatasetId();
        this.decoy = identificationProteinBean.getDecoy();
        this.protGroupId = identificationProteinBean.getProtGroupId();

    }

    /**
     * Get dataset id (index in dataset table in the database)
     *
     * @return expId dataset id
     */
    public int getExpId() {
        return expId;
    }

    /**
     * Set dataset id (index in dataset table in the database)
     *
     * @param expId Dataset id.
     */
    public void setExpId(int expId) {
        this.expId = expId;
    }

    /**
     * Get the protein fraction id (index in fraction table in the database)
     *
     * @return frcationId fraction id .
     */
    public int getFrcationId() {
        return frcationId;
    }

    /**
     * Set the protein fraction id (index in fraction table in the database)
     *
     * @param frcationId Fraction id.
     */
    public void setFrcationId(int frcationId) {
        this.frcationId = frcationId;
    }

    /**
     * Get the protein gene name
     *
     * @return gene name
     */
    public String getGeneName() {
        return geneName;
    }

    /**
     * Set the protein gene name
     *
     * @param geneName Gene name.
     */
    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    /**
     * Get the protein chromosome number
     *
     * @return chromosomeNumber chromosome number
     */
    public String getChromosomeNumber() {
        return chromosomeNumber;
    }

    /**
     * Set the protein chromosome number
     *
     * @param chromosomeNumber Chromosome number
     */
    public void setChromosomeNumber(String chromosomeNumber) {
        this.chromosomeNumber = chromosomeNumber;
    }

    /**
     * Protein is validated
     *
     * @return validated protein.
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     * Set protein is validated or not
     *
     * @param validated Validated protein.
     */
    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    /**
     * Set protein accession number
     *
     * @param accession protein accession number.
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * Get protein accession number
     *
     * @return accession Protein accession number.
     */
    public String getAccession() {
        return accession;
    }

    /**
     * Set protein name
     *
     * @param description Protein name.
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * Get protein name
     *
     * @return protein name
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Set Other proteins accession numbers in same protein group
     *
     * @param otherProteins Other proteins accessions belong to same protein
     * group.
     */
    public void setOtherProteins(String otherProteins) {
        this.otherProteins = otherProteins;
    }

    /**
     * Get Other proteins accession numbers in same protein group
     *
     * @return other proteins accessions belong to same protein group.
     */
    public String getOtherProteins() {
        return otherProteins;
    }

    /**
     * Set protein inference class
     *
     * @param proteinInferenceClass Protein inference class
     */
    public void setProteinInferenceClass(String proteinInferenceClass) {
        this.proteinInferenceClass = proteinInferenceClass;
    }

    /**
     * Get protein inference class
     *
     * @return proteinInferenceClass Protein inference class
     */
    public String getProteinInferenceClass() {
        return proteinInferenceClass;
    }

    /**
     * Set protein sequence coverage
     *
     * @param sequenceCoverage protein sequence coverage.
     */
    public void setSequenceCoverage(double sequenceCoverage) {
        this.sequenceCoverage = sequenceCoverage;
    }

    /**
     * Get protein sequence coverage
     *
     * @return sequenceCoverage protein sequence coverage.
     */
    public double getSequenceCoverage() {
        return sequenceCoverage;
    }

    /**
     * Set protein observable Coverage
     *
     * @param observableCoverage Protein observable Coverage
     */
    public void setObservableCoverage(double observableCoverage) {
        this.observableCoverage = observableCoverage;
    }

    /**
     * Get protein observable Coverage
     *
     * @return observableCoverage Protein observable Coverage
     */
    public double getObservableCoverage() {
        return observableCoverage;
    }

    /**
     * Set protein confident PTM sites
     *
     * @param confidentPtmSites Protein confident PTM sites
     */
    public void setConfidentPtmSites(String confidentPtmSites) {
        this.confidentPtmSites = confidentPtmSites;
    }

    /**
     * Get protein confident ptm sites
     *
     * @return confidentPtmSites Protein confident PTM sites
     */
    public String getConfidentPtmSites() {
        return confidentPtmSites;
    }

    /**
     * Set protein confident number
     *
     * @param numberConfident Protein confident number
     */
    public void setNumberConfident(String numberConfident) {
        this.numberConfident = numberConfident;
    }

    /**
     * Get protein confident number
     *
     * @return numberConfident Protein confident number
     */
    public String getNumberConfident() {
        return numberConfident;
    }

    /**
     * Set protein other PTM sites
     *
     * @param otherPtmSites Protein other PTM sites
     */
    public void setOtherPtmSites(String otherPtmSites) {
        this.otherPtmSites = otherPtmSites;
    }

    /**
     * Get protein other PTM sites
     *
     * @return otherPtmSites Protein other PTM sites
     */
    public String getOtherPtmSites() {
        return otherPtmSites;
    }

    /**
     * Set the number of others proteins in the same protein group.
     *
     * @param numberOfOther Number of others proteins in the same protein group.
     */
    public void setNumberOfOther(String numberOfOther) {
        this.numberOfOther = numberOfOther;
    }

    /**
     * Get the number of others proteins
     *
     * @return numberOfOther Number of others proteins in the same protein
     * group.
     */
    public String getNumberOfOther() {
        return numberOfOther;
    }

    /**
     * Set the validated peptides number for the protein
     *
     * @param numberValidatedPeptides Validated peptides number for the protein.
     */
    public void setNumberValidatedPeptides(int numberValidatedPeptides) {
        this.numberValidatedPeptides = numberValidatedPeptides;
    }

    /**
     * Get the validated peptides number for the protein
     *
     * @return numberValidatedPeptides Validated peptides number for the
     * protein.
     */
    public int getNumberValidatedPeptides() {
        return numberValidatedPeptides;
    }

    /**
     * Set the validated spectra number for the protein
     *
     * @param numberValidatedSpectra Validated spectra number for the protein.
     */
    public void setNumberValidatedSpectra(int numberValidatedSpectra) {
        this.numberValidatedSpectra = numberValidatedSpectra;
    }

    /**
     * Get the validated spectra number for the protein
     *
     * @return numberValidatedSpectra Validated spectra number for the protein.
     */
    public int getNumberValidatedSpectra() {
        return numberValidatedSpectra;
    }

    /**
     * Set protein emPai
     *
     * @param emPai protein emPai
     */
    public void setEmPai(double emPai) {
        this.emPai = emPai;
    }

    /**
     * GetemPai
     *
     * @return emPai Protein emPai
     */
    public double getEmPai() {
        return emPai;
    }

    /**
     * Set protein NSAF
     *
     * @param nsaf Protein NSAF
     */
    public void setNsaf(double nsaf) {
        this.nsaf = nsaf;
    }

    /**
     * Get protein NSAF
     *
     * @return NSAF Protein NSAF
     */
    public double getNsaf() {
        return nsaf;
    }

    /**
     * Set protein MW in kDa
     *
     * @param mw_kDa Protein MW in kDa
     */
    public void setMw_kDa(double mw_kDa) {
        this.mw_kDa = mw_kDa;
    }

    /**
     * Get protein MW in kDa
     *
     * @return Protein MW in kDa
     */
    public double getMw_kDa() {
        return mw_kDa;
    }

    /**
     * Set protein score
     *
     * @param score Protein score
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Get protein score
     *
     * @return Protein score
     */
    public double getScore() {
        return score;
    }

    /**
     * Set protein confidence
     *
     * @param confidence Protein confidence.
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    /**
     * Get protein confidence
     *
     * @return confidence Protein confidence.
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Set protein starred
     *
     * @param starred Protein starred
     */
    public void setStarred(boolean starred) {
        Starred = starred;
    }

    /**
     * Get protein starred
     *
     * @return Starred Protein starred
     */
    public boolean isStarred() {
        return Starred;
    }

    /**
     * Set protein number of peptides per fraction
     *
     * @param numberOfPeptidePerFraction Protein number of peptides per fraction
     */
    public void setNumberOfPeptidePerFraction(int numberOfPeptidePerFraction) {
        this.numberOfPeptidePerFraction = numberOfPeptidePerFraction;
    }

    /**
     * Get protein number of peptides per fraction
     *
     * @return numberOfPeptidePerFraction Protein number of peptides per
     * fraction
     */
    public int getNumberOfPeptidePerFraction() {
        return numberOfPeptidePerFraction;
    }

    /**
     * Set protein number of spectra per fraction
     *
     * @param numberOfSpectraPerFraction Protein number of spectra per fraction
     */
    public void setNumberOfSpectraPerFraction(int numberOfSpectraPerFraction) {
        this.numberOfSpectraPerFraction = numberOfSpectraPerFraction;
    }

    /**
     * Get protein number of spectra per fraction
     *
     * @return numberOfSpectraPerFraction Protein number of spectra per fraction
     */
    public int getNumberOfSpectraPerFraction() {
        return numberOfSpectraPerFraction;
    }

    /**
     * Set protein average precursor intensity per fraction
     *
     * @param averagePrecursorIntensityPerFraction Protein average precursor
     * intensity per fraction
     */
    public void setAveragePrecursorIntensityPerFraction(
            double averagePrecursorIntensityPerFraction) {
        AveragePrecursorIntensityPerFraction = averagePrecursorIntensityPerFraction;
    }

    /**
     * Get protein average precursor intensity per fraction
     *
     * @return AveragePrecursorIntensityPerFraction Protein average precursor
     * intensity per fraction
     */
    public double getAveragePrecursorIntensityPerFraction() {
        return AveragePrecursorIntensityPerFraction;
    }

    /**
     * Get protein has non enzymatic peptides
     *
     * @return nonEnzymaticPeptides Protein has non enzymatic peptides
     */
    public boolean isNonEnzymaticPeptides() {
        return nonEnzymaticPeptides;
    }

    /**
     * Set protein has non enzymatic peptides
     *
     * @param nonEnzymaticPeptides Protein has non enzymatic peptides
     */
    public void setNonEnzymaticPeptides(boolean nonEnzymaticPeptides) {
        this.nonEnzymaticPeptides = nonEnzymaticPeptides;
    }

    /**
     * Get Protein peptide Fraction Spread lower range kDa
     *
     * @return peptideFractionSpread_lower_range_kDa Protein peptide Fraction
     * Spread lower range kDa
     */
    public String getPeptideFractionSpread_lower_range_kDa() {
        return peptideFractionSpread_lower_range_kDa;
    }

    /**
     * Set Protein peptide Fraction Spread lower range kDa
     *
     * @param peptideFractionSpread_lower_range_kDa Protein peptide Fraction
     * Spread lower range kDa
     */
    public void setPeptideFractionSpread_lower_range_kDa(
            String peptideFractionSpread_lower_range_kDa) {
        this.peptideFractionSpread_lower_range_kDa = peptideFractionSpread_lower_range_kDa;
    }

    /**
     * Get Protein peptide Fraction Spread upper range kDa
     *
     * @return peptideFractionSpread_upper_range_kDa Protein peptide Fraction
     * Spread upper range kDa
     */
    public String getPeptideFractionSpread_upper_range_kDa() {
        return peptideFractionSpread_upper_range_kDa;
    }

    /**
     * Set Protein peptide Fraction Spread upper range kDa
     *
     * @param peptideFractionSpread_upper_range_kDa Protein peptide Fraction
     * Spread upper range kDa
     */
    public void setPeptideFractionSpread_upper_range_kDa(
            String peptideFractionSpread_upper_range_kDa) {
        this.peptideFractionSpread_upper_range_kDa = peptideFractionSpread_upper_range_kDa;
    }

    /**
     * Get Protein spectrum Fraction Spread lower range kDa
     *
     * @return Protein spectrum Fraction Spread lower range kDa
     */
    public String getSpectrumFractionSpread_lower_range_kDa() {
        return spectrumFractionSpread_lower_range_kDa;
    }

    /**
     * Set Protein spectrum Fraction Spread lower range kDa
     *
     * @param spectrumFractionSpread_lower_range_kDa Protein spectrum Fraction
     * Spread lower range kDa
     */
    public void setSpectrumFractionSpread_lower_range_kDa(
            String spectrumFractionSpread_lower_range_kDa) {
        this.spectrumFractionSpread_lower_range_kDa = spectrumFractionSpread_lower_range_kDa;
    }

    /**
     * Get Protein spectrum Fraction Spread upper range kDa
     *
     * @return spectrumFractionSpread_upper_range_kDa Protein spectrum Fraction
     * Spread upper range kDa
     */
    public String getSpectrumFractionSpread_upper_range_kDa() {
        return spectrumFractionSpread_upper_range_kDa;
    }

    /**
     * Set Protein spectrum Fraction Spread upper range kDa
     *
     * @param spectrumFractionSpread_upper_range_kDa Protein spectrum Fraction
     * Spread upper range kDa
     */
    public void setSpectrumFractionSpread_upper_range_kDa(
            String spectrumFractionSpread_upper_range_kDa) {
        this.spectrumFractionSpread_upper_range_kDa = spectrumFractionSpread_upper_range_kDa;
    }

    /**
     * Get protein number of spectra
     *
     * @return numberSpectra Protein number of spectra
     */
    public int getNumberSpectra() {
        return numberSpectra;
    }

    /**
     * Set protein number of spectra
     *
     * @param numberSpectra Protein number of spectra
     */
    public void setNumberSpectra(int numberSpectra) {
        this.numberSpectra = numberSpectra;
    }

    /**
     * Get protein number of peptides
     *
     * @return numberPeptides Protein number of peptides
     */
    public int getNumberPeptides() {
        return numberPeptides;
    }

    /**
     * Set protein number of peptides
     *
     * @param numberPeptides Protein number of peptides
     */
    public void setNumberPeptides(int numberPeptides) {
        this.numberPeptides = numberPeptides;
    }

    /**
     * Is protein decoy
     *
     * @return protein decoy
     */
    public int getDecoy() {
        return decoy;
    }

    /**
     * set if protein decoy or not
     *
     * @param decoy Protein decoy
     */
    public void setDecoy(int decoy) {
        this.decoy = decoy;
    }

    /**
     * Get dataset id (index in the database)
     *
     * @return expId Dataset id.
     */
    public int getDatasetId() {
        return expId;
    }

    /**
     * Set dataset id (index in the database)
     *
     * @param expId Dataset id.
     */
    public void setDatasetId(int expId) {
        this.expId = expId;
    }

    /**
     * Get protein group id (index in the database)
     *
     * @return protein group id (index in the database)
     */
    public int getProtGroupId() {
        return protGroupId;
    }

    /**
     * Set protein group id (index in the database)
     *
     * @param protGroupId Protein group id (index in the database)
     */
    public void setProtGroupId(int protGroupId) {
        this.protGroupId = protGroupId;
    }

    /**
     *Override compare to method
     * @param o object to compare
     * @return comparison results
     */
    @Override
    public int compareTo(IdentificationProteinBean o) {
        if (this.nsaf > o.getNsaf()) {
            return 1;
        } else {
            return -1;
        }

    }
}
