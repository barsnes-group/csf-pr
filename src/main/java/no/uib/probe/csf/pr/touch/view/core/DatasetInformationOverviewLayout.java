package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;

/**
 * This class represents the dataset information fields container layout.
 *
 * @author Yehia Farag
 */
public class DatasetInformationOverviewLayout extends VerticalLayout {

    /**
     * Disease HTML Color code.
     */
    private final String diseaseHashedColor;
    /**
     * Dataset form container.
     */
    private final VerticalLayout datasetInfoForm;
    /**
     * Quantification basis comments field.
     */
//    private InformationField quantBasisComment;
    /**
     * Number of proteins found only in the dataset.
     */
//    private InformationField uniqueProtNumField;
    /**
     * Number of peptides found only in the dataset.
     */
//    private InformationField uQuantPeptidesNum;
    /**
     * Number of quantified proteins in the dataset .
     */
//    private InformationField quantProteinsNum;
    /**
     * Number of quantified peptides in the dataset.
     */
//    private InformationField quantPeptidesNum;
    /**
     * Disease category (MS,AD,PD...etc)field.
     */
    private InformationField diseaseCategory;
    /**
     * Publication PubMed id field.
     */
    private InformationField pubMedIdField;
    /**
     * Raw data available(Yes/No) field.
     */
    private InformationField rawData;
    /**
     * Analytical method field.
     */
    private InformationField analyticalMethod;
    /**
     * Study type field.
     */
    private InformationField typeOfStudy;
    /**
     * Shotgun or targeted dataset field.
     */
    private InformationField shotgunTargeted;
    /**
     * Enzyme used field.
     */
    private InformationField enzyme;
    /**
     * Sample type field.
     */
    private InformationField sampleType;
    /**
     * Technology field.
     */
    private InformationField technology;
    /**
     * Quantification basis field.
     */
    private InformationField quantificationBasis;
    /**
     * Number of patients in disease group 1 field.
     */
    private InformationField patientsGroup1Number;
    /**
     * Number of patients in disease group 2 field.
     */
    private InformationField patientsGroup2Number;
    /**
     * Disease group 1 field.
     */
    private InformationField patientsGroup1;
    /**
     * Disease group 2 field.
     */
    private InformationField patientsGroup2;
    /**
     * Disease sub group 1 field.
     */
    private InformationField patientssubGroup1;
    /**
     * Disease group 1 comments field.
     */
    private InformationField patientsCommGroup1;
    /**
     * Disease sub group 2 field.
     */
    private InformationField patientssubGroup2;
    /**
     * Disease group 2 comments field.
     */
    private InformationField patientsCommGroup2;
    /**
     * Number of identified proteins.
     */
    private InformationField identifiedProteinsNumber;
    /**
     * Number of quantified proteins field.
     */
    private InformationField quantifiedProteinsNumber;
    /**
     * Sample matching field.
     */
    private InformationField sampleMatching;
    /**
     * Analytical approach field.
     */
    private InformationField analyticalApproach;
    /**
     * Normalization strategy field.
     */
    private InformationField normalization_strategy;

    /**
     * Constructor to initialize the layout and main attributes.
     *
     * @param quantDs Quant dataset object.
     * @param smallScreen the screen is relatively small.
     */
    public DatasetInformationOverviewLayout(QuantDataset quantDs, boolean smallScreen) {
        this.diseaseHashedColor = quantDs.getDiseaseHashedColor();
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);
        this.addStyleName("scrollable");
        if (!smallScreen) {
            this.setSpacing(true);
        }
        datasetInfoForm = generateQuantDatasetInformationLayout();
        datasetInfoForm.setVisible(true);
        this.addComponent(datasetInfoForm);
        this.setComponentAlignment(datasetInfoForm, Alignment.TOP_LEFT);
        this.updateDatasetForm(quantDs);
    }

    /**
     * Generate and to initialize the dataset information form layout.
     *
     * @return dataset information from layout
     */
    private VerticalLayout generateQuantDatasetInformationLayout() {

        
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth(100, Unit.PERCENTAGE);
        mainContainer.setHeightUndefined();
        mainContainer.addStyleName("scrollable");
        int h = 60;
        mainContainer.setSpacing(true);

        HorizontalLayout rowI = new HorizontalLayout();
        rowI.setWidth(100, Unit.PERCENTAGE);
        rowI.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowI);

        pubMedIdField = new InformationField("PubMed ID");
        rowI.addComponent(pubMedIdField);
        rowI.setExpandRatio(pubMedIdField, 0.15f);

        typeOfStudy = new InformationField("Type of Study");
        rowI.addComponent(typeOfStudy);
        rowI.setExpandRatio(typeOfStudy, 0.35f);

        analyticalApproach = new InformationField("Analytical Approach");
        rowI.addComponent(analyticalApproach);
        rowI.setExpandRatio(analyticalApproach, 0.15f);

        shotgunTargeted = new InformationField("Shotgun/Targeted");
        rowI.addComponent(shotgunTargeted);
        rowI.setExpandRatio(shotgunTargeted, 0.35f);
        shotgunTargeted.addStyleName("floattoright");
        shotgunTargeted.setMargin(new MarginInfo(false, false, false, true));
        rowI.setComponentAlignment(shotgunTargeted,Alignment.TOP_RIGHT);

        HorizontalLayout rowII = new HorizontalLayout();
        rowII.setWidth(100, Unit.PERCENTAGE);
        rowII.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowII);

        analyticalMethod = new InformationField("Analytical Method");
        rowII.addComponent(analyticalMethod);
        rowII.setExpandRatio(analyticalMethod, 0.15f);

        technology = new InformationField("Technology");
        rowII.addComponent(technology);
        rowII.setExpandRatio(technology, 0.35f);
        sampleType = new InformationField("Sample Type");
        rowII.addComponent(sampleType);
        rowII.setExpandRatio(sampleType, 0.15f);
        enzyme = new InformationField("Enzyme");
        rowII.addComponent(enzyme);
        rowII.setExpandRatio(enzyme, 0.35f);
        enzyme.addStyleName("floattoright");
        enzyme.setMargin(new MarginInfo(false, false, false, true));
        rowII.setComponentAlignment(enzyme,Alignment.TOP_RIGHT);
        HorizontalLayout rowIII = new HorizontalLayout();
        rowIII.setWidth(100, Unit.PERCENTAGE);
        rowIII.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowIII);

        quantificationBasis = new InformationField("Quantification Basis");
        rowIII.addComponent(quantificationBasis);
        rowIII.setExpandRatio(quantificationBasis, 0.15f);
//        quantBasisComment = new InformationField("Quantification Basis Comment");
//        rowIII.addComponent(quantBasisComment);
//        rowIII.setExpandRatio(quantBasisComment, 0.35f);
//        identifiedProteinsNumber = new InformationField("#Identified Proteins");
//        rowIII.addComponent(identifiedProteinsNumber);
//        rowIII.setExpandRatio(identifiedProteinsNumber, 0.15f);

        sampleMatching = new InformationField("Sample Matching");
        rowIII.addComponent(sampleMatching);
        rowIII.setExpandRatio(sampleMatching, 0.35f);
        rawData = new InformationField("Raw Data");
        rowIII.addComponent(rawData);
        rowIII.setExpandRatio(rawData, 0.15f);

        quantifiedProteinsNumber = new InformationField("#Quantified Proteins");
        rowIII.addComponent(quantifiedProteinsNumber);
        rowIII.setExpandRatio(quantifiedProteinsNumber, 0.35f);
        quantifiedProteinsNumber.addStyleName("floattoright");
        quantifiedProteinsNumber.setMargin(new MarginInfo(false, false, false, true));
        rowIII.setComponentAlignment(quantifiedProteinsNumber,Alignment.TOP_RIGHT);
        h = 80;
        HorizontalLayout rowIV = new HorizontalLayout();
        rowIV.setWidth(100, Unit.PERCENTAGE);
        rowIV.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowIV);
        patientsGroup1 = new InformationField("Patients Gr.I");
        rowIV.addComponent(patientsGroup1);
        rowIV.setExpandRatio(patientsGroup1, 0.15f);
        patientssubGroup1 = new InformationField("Patients Sub Gr.I");
        rowIV.addComponent(patientssubGroup1);
        rowIV.setExpandRatio(patientssubGroup1, 0.35f);
        patientsGroup1Number = new InformationField("#Patients Gr.I");
        rowIV.addComponent(patientsGroup1Number);
        rowIV.setExpandRatio(patientsGroup1Number, 0.15f);
        patientsCommGroup1 = new InformationField("Patients Gr.I Comm.");
        rowIV.addComponent(patientsCommGroup1);
        rowIV.setExpandRatio(patientsCommGroup1, 0.35f);

        HorizontalLayout rowV = new HorizontalLayout();
        rowV.setWidth(100, Unit.PERCENTAGE);
        rowV.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowV);
        patientsGroup2 = new InformationField("Patients Gr.II");
        rowV.addComponent(patientsGroup2);
        rowV.setExpandRatio(patientsGroup2, 0.15f);

        patientssubGroup2 = new InformationField("Patients Sub Gr.II");
        rowV.addComponent(patientssubGroup2);
        rowV.setExpandRatio(patientssubGroup2, 0.35f);
        patientsGroup2Number = new InformationField("#Patients Gr.II");
        rowV.addComponent(patientsGroup2Number);
        rowV.setExpandRatio(patientsGroup2Number, 0.15f);
        patientsCommGroup2 = new InformationField("Patients Gr.II Comm.");
        rowV.addComponent(patientsCommGroup2);
        rowV.setExpandRatio(patientsCommGroup2, 0.35f);
        patientsCommGroup1.addStyleName("floattoright");
        patientsCommGroup1.setMargin(new MarginInfo(false, false, false, true));
        patientsCommGroup2.setMargin(new MarginInfo(false, false, false, true));
        patientsCommGroup2.addStyleName("floattoright");
        rowIV.setComponentAlignment(patientsCommGroup1,Alignment.TOP_RIGHT);
        rowV.setComponentAlignment(patientsCommGroup2,Alignment.TOP_RIGHT);
        HorizontalLayout rowVI = new HorizontalLayout();
        rowVI.setWidth(100, Unit.PERCENTAGE);
        rowVI.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowVI);

        diseaseCategory = new InformationField("Disease Category");
        rowVI.addComponent(diseaseCategory);
         rowVI.setComponentAlignment(diseaseCategory,Alignment.TOP_RIGHT);
        rowVI.setExpandRatio(diseaseCategory, 0.15f);
        normalization_strategy = new InformationField("Normalization Strategy");
        rowVI.addComponent(normalization_strategy);
        rowVI.setExpandRatio(normalization_strategy, 0.35f);
        
        InformationField empty = new InformationField("");
        rowVI.addComponent(empty);
        rowVI.setExpandRatio(empty, 0.15f);
       InformationField empty2 = new InformationField("");
        rowVI.addComponent(empty2);
        rowVI.setExpandRatio(empty2, 0.35f);
        
//        HorizontalLayout rowVII = new HorizontalLayout();
//        rowVII.setWidth(100, Unit.PERCENTAGE);
//        rowVII.setHeight(h, Unit.PIXELS);
//        quantProteinsNum = new InformationField("#Proteins");
//        rowVII.addComponent(quantProteinsNum);
////        mainContainer.addComponent(rowVII);
//
//        quantPeptidesNum = new InformationField("#Peptides");
//        rowVII.addComponent(quantPeptidesNum);
//
//        uniqueProtNumField = new InformationField("#Dataset Specific Proteins");
//        rowVII.addComponent(uniqueProtNumField);
//
//        uQuantPeptidesNum = new InformationField("#Dataset Specific Peptides");
//        rowVII.addComponent(uQuantPeptidesNum);
        return mainContainer;
    }

    /**
     * Update and fill dataset form (dataset information fields).
     *
     * @param quantDs Quant dataset object.
     */
    private void updateDatasetForm(QuantDataset quantDs) {
        if (quantDs == null) {
            return;
        }
        pubMedIdField.setValue(quantDs.getPubMedId(), "http://www.ncbi.nlm.nih.gov/pubmed/" + quantDs.getPubMedId());
        if (quantDs.getRawDataUrl().equalsIgnoreCase("false")) {
            rawData.setValue("Not Available", null);
        } else {
            rawData.setValue("Available", null);
        }
        analyticalMethod.setValue(quantDs.getAnalyticalMethod(), null);
        typeOfStudy.setValue(quantDs.getTypeOfStudy(), null);
        shotgunTargeted.setValue(quantDs.getShotgunTargeted(), null);
        enzyme.setValue(quantDs.getEnzyme(), null);

        sampleType.setValue(quantDs.getSampleType(), null);
        technology.setValue(quantDs.getTechnology(), null);

        quantificationBasis.setValue(quantDs.getQuantificationBasis(), null);
        patientsGroup1Number.setValue(quantDs.getDiseaseMainGroup1Number(), null);
        patientsGroup2Number.setValue(quantDs.getDiseaseMainGroup2Number(), null);
        patientsGroup1.setValue(quantDs.getDiseaseMainGroupI(), null);
        patientsGroup2.setValue(quantDs.getDiseaseMainGroup2() + "", null);

        patientssubGroup1.setValue(quantDs.getDiseaseSubGroup1(), null);
        patientsCommGroup1.setValue(quantDs.getDiseaseMainGroup1Comm(), null);

        patientssubGroup2.setValue(quantDs.getDiseaseSubGroup2(), null);
        patientsCommGroup2.setValue(quantDs.getDiseaseMainGroup2Comm(), null);

//        identifiedProteinsNumber.setValue(quantDs.getIdentifiedProteinsNumber() + "", null);
        quantifiedProteinsNumber.setValue(quantDs.getQuantifiedProteinsNumber() + "", null);

        sampleMatching.setValue(quantDs.getSampleMatching() + "", null);
//        quantBasisComment.setValue(quantDs.getQuantBasisComment() + "", null);
        analyticalApproach.setValue(quantDs.getAnalyticalApproach() + "", null);
        normalization_strategy.setValue(quantDs.getNormalizationStrategy(), null);

        diseaseCategory.setValue("<font color='" + diseaseHashedColor + "' style='font-weight: bold;'>" + quantDs.getDiseaseCategoryI() + "</font>", null);

        quantifiedProteinsNumber.setValue("" + quantDs.getQuantifiedProteinsNumber(), null);

//        quantPeptidesNum.setValue("" + quantDs.getTotalPepNum() + "", null);
//        quantProteinsNum.setValue("" + quantDs.getTotalProtNum() + "", null);
//        uQuantPeptidesNum.setValue(quantDs.getUniqePepNum(), null);
//        uniqueProtNumField.setValue(quantDs.getUniqueProtNum(), null);
        this.datasetInfoForm.setVisible(true);
    }

}
