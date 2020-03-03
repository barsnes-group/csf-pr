package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;

/**
 * This class represents the protein information fields container layout.
 *
 * @author Yehia Farag
 */
public class ProteinsInformationOverviewLayout extends VerticalLayout {

    /**
     * Double data formatter.
     */
    private DecimalFormat df = null;
    /**
     * Protein form container.
     */
    private final VerticalLayout proteinsForm;
    /**
     * Quantification basis field.
     */
    private InformationField quantBasis;
    /**
     * Quantification basis comments field.
     */
    private InformationField quantBasisComment;
    /**
     * Protein accession (from UniProt or publication) field.
     */
    private InformationField accsession;
    /**
     * Protein name (from UniProt or publication) field.
     */
    private InformationField name;
    /**
     * Quantified peptides number field.
     */
    private InformationField quantPeptidesNumber;
    /**
     * Identified peptides number field.
     */
    private InformationField idPeptidesNumber;
    /**
     * pValue field (Significant/not Significant).
     */
    private InformationField pValue;
    /**
     * pValue comments (statistical comments) field.
     */
    private InformationField pValueComm;
    /**
     * Protein quantification fold change value (Increased, Decreased,Equal)
     * field.
     */
    private InformationField foldChange;
    /**
     * Receiver operating characteristic value field.
     */
    private InformationField roc;
    /**
     * Quantification additional comments field.
     */
    private InformationField additionalComments;
    /**
     * pValue significance threshold field.
     */
    private InformationField pvalueSignificanceThreshold;

    /**
     * Constructor to initialize main attributes.
     */
    public ProteinsInformationOverviewLayout() {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.###", otherSymbols);
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setMargin(false);
        this.setStyleName("whitelayout");
        proteinsForm = generateProteinsForm();
        proteinsForm.setVisible(false);
        this.addComponent(proteinsForm);
    }

    /**
     * Generate and initialize main protein form fields.
     *
     * @return Protein information form container layout
     */
    private VerticalLayout generateProteinsForm() {
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth(100, Unit.PERCENTAGE);
        mainContainer.setHeightUndefined();
        mainContainer.setSpacing(true);

        HorizontalLayout rowI = new HorizontalLayout();
        rowI.setWidth(100, Unit.PERCENTAGE);
        rowI.setHeight(80, Unit.PIXELS);
        mainContainer.addComponent(rowI);
        accsession = new InformationField("Accession");
        rowI.addComponent(accsession);

        name = new InformationField("Protein Name");
        rowI.addComponent(name);

        quantPeptidesNumber = new InformationField("#Quant Peptides");
        rowI.addComponent(quantPeptidesNumber);

        idPeptidesNumber = new InformationField("#Identified Peptides");
        rowI.addComponent(idPeptidesNumber);

        HorizontalLayout rowII = new HorizontalLayout();
        rowII.setWidth(100, Unit.PERCENTAGE);
        rowII.setHeight(80, Unit.PIXELS);
        mainContainer.addComponent(rowII);

        foldChange = new InformationField("Fold Change");
        rowII.addComponent(foldChange);
        pValue = new InformationField("<i>p</i>-value");
        rowII.addComponent(pValue);

        pvalueSignificanceThreshold = new InformationField("<i>p</i>-value Threshold");
        rowII.addComponent(pvalueSignificanceThreshold);

        pValueComm = new InformationField("Statistical Comments");
        rowII.addComponent(pValueComm);

        HorizontalLayout rowIII = new HorizontalLayout();
        rowIII.setWidth(100, Unit.PERCENTAGE);
        rowIII.setHeight(80, Unit.PIXELS);
        mainContainer.addComponent(rowIII);

        roc = new InformationField("ROC AUC");
        rowIII.addComponent(roc);

        quantBasis = new InformationField("Quantification Basis");
        rowIII.addComponent(quantBasis);

        quantBasisComment = new InformationField("Quantification Basis Comment");
        rowIII.addComponent(quantBasisComment);

        additionalComments = new InformationField("Additional Comments");
        rowIII.addComponent(additionalComments);
        return mainContainer;
    }

    /**
     * Updated protein form fields.
     *
     * @param quantProtein Quant protein object.
     * @param accession protein accession
     * @param url URL to UniProt if exist
     * @param protName Final protein name(from uniProt or publication)
     */
    public void updateProteinsForm(QuantProtein quantProtein, String accession, String url, String protName) {
        idPeptidesNumber.setValue(quantProtein.getQuantifiedPeptidesNumber(), null);
        accsession.setValue(accession, url);
        name.setValue(protName, null);
        pvalueSignificanceThreshold.setVisible(true);
        additionalComments.setVisible(true);
        pvalueSignificanceThreshold.setValue(quantProtein.getPvalueSignificanceThreshold(), null);
        additionalComments.setValue(quantProtein.getAdditionalComments(), null);
        String pval;
        if (quantProtein.getString_p_value() != null && !quantProtein.getString_p_value().equalsIgnoreCase("") && !quantProtein.getString_p_value().equalsIgnoreCase("Not Available")) {
            pval = quantProtein.getString_p_value() + " ";
        } else {
            pval = "          ";
        }
        if (quantProtein.getP_value() != -1000000000) {
            pval += "(" + df.format(quantProtein.getP_value()) + ")";
        }
        pValue.setValue(pval, null);
        pValueComm.setValue(quantProtein.getP_value_comments(), null);

        String strFoldChange;
        if (quantProtein.getString_fc_value() != null && !quantProtein.getString_fc_value().equalsIgnoreCase("") && !quantProtein.getString_fc_value().equalsIgnoreCase("Not Available")) {
            strFoldChange = quantProtein.getString_fc_value() + " ";
            if (strFoldChange.trim().equalsIgnoreCase("Not regulated")) {
                strFoldChange = "No Change ";
            }
        } else {
            strFoldChange = "          ";
        }
        if (quantProtein.getFc_value() != -1000000000) {
            strFoldChange += "(" + df.format(quantProtein.getFc_value()) + ")";
        }
        foldChange.setValue(strFoldChange, null);
        String rocv;
        if (quantProtein.getRoc_auc() != -1000000000) {
            rocv = df.format(quantProtein.getRoc_auc()) + "";
        } else {
            rocv = "";
        }
        this.roc.setValue(rocv, null);

        int quantPepNumber = quantProtein.getQuantifiedPeptidesNumber();
        quantPeptidesNumber.setValue(quantPepNumber, null);
        quantBasisComment.setValue(quantProtein.getQuantBasisComment() + "", null);
        quantBasis.setValue(quantProtein.getQuantificationBasis() + "", null);
        this.proteinsForm.setVisible(true);
    }

}
