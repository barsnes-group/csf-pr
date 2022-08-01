package no.uib.probe.csf.pr.touch.view.components;

import com.itextpdf.text.pdf.codec.Base64;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents.ProteinDatasetsTable;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents.ProteinDatasetDetailsLineChart;
import no.uib.probe.csf.pr.touch.view.core.CloseButton;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

/**
 * This class represents the peptide information layout the layout show overall
 * trend line-chart and detailed studies line chart for the selected protein the
 * second component consist of table of comparisons and peptides sequences for
 * each protein.
 *
 * @author Yehia Farag
 */
public abstract class PeptideViewComponent extends VerticalLayout implements CSFListener {

    /**
     * The central manager for handling data across different visualizations and
     * managing all users selections.
     */
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    /**
     * The peptide component right side control buttons container.
     */
    private final VerticalLayout peptideTableToolsContainer;
    /**
     * Is individual datasets for each disease group comparison.
     */
    private boolean showIndividualDatasets = true;
    /**
     * The peptide table (detailed protein table) that has peptides information.
     */
    private final ProteinDatasetsTable peptideSequenceTableLayout;
    /**
     * The top protein name label.
     */
    private final Label proteinNameLabel;
    /**
     * The studies number label.
     */
    private final Label studiesNumberLabel;
    /**
     * Is default order or decreased to increased order.
     */
    private boolean defaultTrend = true;

    /**
     * Switch the comparisons button between default order or decreased to
     * increased order.
     */
    private final ImageContainerBtn orderByTrendBtn;
    /**
     * Show individual datasets for each disease group comparison.
     */
    private final ImageContainerBtn individualToTotalComparisonsDatasetsSwichBtn;
    /**
     * Resize individual datasets symbol based on patients number.
     */
//    private final ImageContainerBtn resizeDsOnPatientNumbersBtn;
    /**
     * Hide not significant peptides data from the protein coverage to clean the
     * chart.
     */
    private final ImageContainerBtn showSigOnlyBtn;
    /**
     * Is individual datasets symbol resized based on patients number.
     */
//    private boolean resizeDatasetOnPateintsNumber = false;
    /**
     * Top legend layout container.
     */
    private final VerticalLayout legendLayout;
    /**
     * Show individual datasets button icon.
     */
    private final Resource comparisonDsRes = new ThemeResource("img/comparisons-ds.png");
    /**
     * Hide individual datasets button icon.
     */
    private final Resource dsComparisonRes = new ThemeResource("img/ds-comparisons.png");
    /**
     * Detailed protein line chart component (developed using JFree chart and
     * DiVA).
     */
    private ProteinDatasetDetailsLineChart lineChart;
    /**
     * The total number of included datasets.
     */
    private int totatlDatasetsNumber;
    /**
     * The selected protein key to find the peptides belong to the protein.
     */
    private String proteinKey;
    /**
     * List of selected disease group comparisons.
     */
    private Set<QuantDiseaseGroupsComparison> selectedComparisonsList;

    /**
     * Notification message alert.
     */
//    private final Notification notification = new Notification(null, "<span style='position:fixed;top:0;left:0;width:100%;height:100%'><center>To view dataset details use dataset details mode - double click on data point or click on <br/> <br/> <img style='width:40px; height:80px;border:2px solid lightgray; border-radius:4px;background-color: whitesmoke;padding:2px;' src='VAADIN/themes/mytheme/img/comparisons-ds.png' alt='Show/hide individual datasets'></center></span>", Notification.Type.TRAY_NOTIFICATION, true) {
//    };

    /**
     * Get side buttons container that has all the peptides component control
     * buttons
     *
     * @return peptideTableToolsContainer Side buttons container
     */
    public VerticalLayout getPeptideTableToolsContainer() {
        return peptideTableToolsContainer;
    }

    /**
     * Generate thumb image to update the left side button icon.
     *
     * @return iconResource The resource is used to update the left side button
     * icon
     *
     */
    private Resource generateThumbImg() {
        String seq = peptideSequenceTableLayout.getSequence();
        Set<String> peptides = peptideSequenceTableLayout.getPeptidesSequenceSet();
        final Border fullBorder = new LineBorder(Color.GRAY);
        Border peptideBorder = new LineBorder(Color.decode("#1d69b4").darker());
        JPanel proteinSequencePanel = new JPanel();
        proteinSequencePanel.setLayout(null);
        proteinSequencePanel.setSize(100, 100);
        proteinSequencePanel.setBackground(Color.WHITE);

        ChartPanel lineChartPanel = new ChartPanel(lineChart.generateThumbChart());
        lineChartPanel.setSize(100, 50);
        lineChartPanel.setLocation(0, 0);
        lineChartPanel.setOpaque(true);
        proteinSequencePanel.add(lineChartPanel);

        JPanel coveragePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        coveragePanel.setSize(100, 20);
        coveragePanel.setBackground(new Color(242, 242, 242));
        coveragePanel.setLocation(0, 65);

        coveragePanel.setBorder(fullBorder);
        coveragePanel.setOpaque(true);
        proteinSequencePanel.add(coveragePanel);

        if (seq != null && !seq.equalsIgnoreCase("") && peptides != null && !peptides.isEmpty()) {
            double charSize = 100.0 / (double) seq.length();
            for (String pep : peptides) {
                JPanel peptide = new JPanel();
                int w = (int) Math.max((pep.length() * charSize), 2);
                peptide.setSize(w, 18);
                peptide.setBackground(Color.decode("#1d69b4"));
                peptide.setBorder(peptideBorder);
                peptide.setLocation((int) (seq.split(pep)[0].length() * charSize), 1);
                peptide.setOpaque(true);
                coveragePanel.add(peptide);

            }

        }

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(Color.WHITE);
        graphics.setBackground(Color.WHITE);

        proteinSequencePanel.paint(graphics);
        byte[] imageData = null;

        try {

            ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, 1);
            imageData = in.encode(image);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = Base64.encodeBytes(imageData);
        base64 = "data:image/png;base64," + base64;

        return new ExternalResource(base64);
    }

    /**
     * Update thumb button icon for the left side button
     *
     * @param iconResource The resource is used to update the left side button
     * icon
     *
     */
    public abstract void updateIcon(Resource iconResource);

    /**
     * Constructor to initialize the main attributes ( selection manage ..etc)
     *
     * @param CSFPR_Central_Manager Central selection manager
     * @param width main body layout width (the container)
     * @param height main body layout height (the container)
     */
    public PeptideViewComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height) {
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
//        this.notification.setDelayMsec(5000);
//        this.notification.setStyleName("custnotification");

        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        VerticalLayout mainBodyContainer = new VerticalLayout();
        mainBodyContainer.setSpacing(false);
        mainBodyContainer.setWidth(100, Unit.PERCENTAGE);
        mainBodyContainer.setHeightUndefined();
        this.addComponent(mainBodyContainer);

        HorizontalLayout topLayoutContainer = new HorizontalLayout();
        topLayoutContainer.setWidth(100, Unit.PERCENTAGE);
        topLayoutContainer.addStyleName("margintop7");
        mainBodyContainer.addComponent(topLayoutContainer);
        topLayoutContainer.setMargin(new MarginInfo(false, false, false, true));
        mainBodyContainer.setComponentAlignment(topLayoutContainer, Alignment.TOP_CENTER);

        proteinNameLabel = new Label();
        proteinNameLabel.setStyleName(ValoTheme.LABEL_BOLD);
        proteinNameLabel.addStyleName("overflowtext");
        proteinNameLabel.addStyleName("leftaligntext");
        proteinNameLabel.addStyleName(ValoTheme.LABEL_SMALL);
        proteinNameLabel.addStyleName(ValoTheme.LABEL_TINY);
        proteinNameLabel.setHeight(24, Unit.PIXELS);
        topLayoutContainer.addComponent(proteinNameLabel);
        topLayoutContainer.setComponentAlignment(proteinNameLabel, Alignment.MIDDLE_LEFT);
        topLayoutContainer.setExpandRatio(proteinNameLabel, 300);

        proteinNameLabel.setWidth(300, Unit.PIXELS);

        legendLayout = new VerticalLayout();
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(25, Unit.PIXELS);

        topLayoutContainer.addComponent(legendLayout);
        topLayoutContainer.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);
        topLayoutContainer.setExpandRatio(legendLayout, this.getWidth() - 200);

        int componentHeight = ((height - 80) / 2) - 5;

        VerticalLayout topLayout = new VerticalLayout();
        width = width - 50;
        topLayout.setWidth(width, Unit.PIXELS);
        topLayout.setHeight(componentHeight, Unit.PIXELS);
        topLayout.addStyleName("roundedborder");
        topLayout.addStyleName("padding20");
        topLayout.addStyleName("paddingbottom10");
        topLayout.addStyleName("whitelayout");

        mainBodyContainer.addComponent(topLayout);
        mainBodyContainer.setComponentAlignment(topLayout, Alignment.TOP_CENTER);
        lineChart = new ProteinDatasetDetailsLineChart(width - 50, (componentHeight - 40)) {

            @Override
            public void select(QuantDiseaseGroupsComparison comparison, int dsKey) {
                if (dsKey == -100) {
//                    notification.show(Page.getCurrent());
                    individualToTotalComparisonsDatasetsSwichBtn.blink();
//                } else if (dsKey == -100 && isDoubleClick) {
                    individualToTotalComparisonsDatasetsSwichBtn.onClick();
                } else {
                    peptideSequenceTableLayout.select(comparison, dsKey);
                    studiesNumberLabel.setValue("Datasets (" + peptideSequenceTableLayout.getRowsNumber() + "/" + totatlDatasetsNumber + ")");
                }
            }
        };

        topLayout.addComponent(lineChart);
        HorizontalLayout middlelayout = new HorizontalLayout();
        middlelayout.setWidth(100, Unit.PERCENTAGE);
        middlelayout.addStyleName("margintop7");
        mainBodyContainer.addComponent(middlelayout);
        mainBodyContainer.setComponentAlignment(middlelayout, Alignment.TOP_LEFT);
        middlelayout.setMargin(new MarginInfo(false, false, false, true));

        studiesNumberLabel = new Label();
        studiesNumberLabel.setStyleName(ValoTheme.LABEL_BOLD);
        studiesNumberLabel.addStyleName("overflowtext");
        studiesNumberLabel.addStyleName("leftaligntext");

        studiesNumberLabel.addStyleName(ValoTheme.LABEL_SMALL);
        studiesNumberLabel.addStyleName(ValoTheme.LABEL_TINY);
        studiesNumberLabel.setHeight(24, Unit.PIXELS);
        middlelayout.addComponent(studiesNumberLabel);
        middlelayout.setExpandRatio(studiesNumberLabel, 120);

        TrendLegend sequenceLegendLayout = new TrendLegend("ministackedpeptidessequence");
        sequenceLegendLayout.setWidthUndefined();

        if (this.getWidth() - 120 < 600) {
            CloseButton closeBtn = new CloseButton();
            VerticalLayout legendPopup = new VerticalLayout();
            legendPopup.addComponent(closeBtn);
            legendPopup.setExpandRatio(closeBtn, 1);
            Set<Component> set = new LinkedHashSet<>();
            VerticalLayout spacer = new VerticalLayout();
            spacer.setHeight(5, Unit.PIXELS);
            spacer.setWidth(20, Unit.PIXELS);
            set.add(spacer);
            Iterator<Component> itr = sequenceLegendLayout.iterator();
            while (itr.hasNext()) {
                set.add(itr.next());
            }

            for (Component c : set) {
                legendPopup.addComponent(c);
                legendPopup.setExpandRatio(c, c.getHeight() + 5);
            }

            legendPopup.setWidth(230, Unit.PIXELS);
            legendPopup.setHeight(150, Unit.PIXELS);
            final PopupView popup = new PopupView("Legend", legendPopup);
            legendPopup.addStyleName("compactlegend");
            popup.addStyleName("marginright20");
            popup.setHideOnMouseOut(false);
            closeBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                popup.setPopupVisible(false);

            });
            middlelayout.addComponent(popup);
            middlelayout.setComponentAlignment(popup, Alignment.TOP_RIGHT);
            middlelayout.setExpandRatio(popup, this.getWidth() - 120);
        } else {
            sequenceLegendLayout.setHeight(25, Unit.PIXELS);
            middlelayout.addComponent(sequenceLegendLayout);
            middlelayout.setComponentAlignment(sequenceLegendLayout, Alignment.TOP_RIGHT);
            middlelayout.setExpandRatio(sequenceLegendLayout, this.getWidth() - 120);
        }

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setWidth(width, Unit.PIXELS);
        bottomLayout.setHeight(componentHeight + 12, Unit.PIXELS);
        bottomLayout.addStyleName("roundedborder");
        bottomLayout.addStyleName("paddingtop20");
        bottomLayout.addStyleName("paddingleft10");
        bottomLayout.addStyleName("paddingbottom10");
        bottomLayout.addStyleName("whitelayout");

        mainBodyContainer.addComponent(bottomLayout);
        mainBodyContainer.setComponentAlignment(bottomLayout, Alignment.MIDDLE_CENTER);

        peptideSequenceTableLayout = new ProteinDatasetsTable(width - 20, (componentHeight - 13));
        bottomLayout.addComponent(peptideSequenceTableLayout);
        bottomLayout.setComponentAlignment(peptideSequenceTableLayout, Alignment.MIDDLE_CENTER);

        CSFPR_Central_Manager.registerListener(PeptideViewComponent.this);

        //init side control btns layout 
        peptideTableToolsContainer = new VerticalLayout();
        peptideTableToolsContainer.setHeightUndefined();
        peptideTableToolsContainer.setWidthUndefined();
        peptideTableToolsContainer.setSpacing(true);

        individualToTotalComparisonsDatasetsSwichBtn = new ImageContainerBtn() {
            @Override
            public void onClick() {
                if (showIndividualDatasets) {
                    showIndividualDatasets = false;
                    this.updateIcon(comparisonDsRes);
                } else {
                    showIndividualDatasets = true;
                    this.updateIcon(dsComparisonRes);
                }
                lineChart.setViewDetailedDatasets(showIndividualDatasets);
                if (showIndividualDatasets) {
                    lineChart.setResizeDetailedDatasets(showIndividualDatasets);
                }
            }

        };

        individualToTotalComparisonsDatasetsSwichBtn.setHeight(80, Unit.PIXELS);
        individualToTotalComparisonsDatasetsSwichBtn.setWidth(40, Unit.PIXELS);

        individualToTotalComparisonsDatasetsSwichBtn.updateIcon(comparisonDsRes);
        individualToTotalComparisonsDatasetsSwichBtn.setEnabled(true);
        individualToTotalComparisonsDatasetsSwichBtn.addStyleName("pointer");
        peptideTableToolsContainer.addComponent(individualToTotalComparisonsDatasetsSwichBtn);
        peptideTableToolsContainer.setComponentAlignment(individualToTotalComparisonsDatasetsSwichBtn, Alignment.MIDDLE_CENTER);
        individualToTotalComparisonsDatasetsSwichBtn.setDescription("Show/hide individual datasets");
        final Resource trendOrderRes = new ThemeResource("img/orderedtrend.png");
        orderByTrendBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                individualToTotalComparisonsDatasetsSwichBtn.updateIcon(comparisonDsRes);
               

                if (defaultTrend) {
                    defaultTrend = false;
                    this.addStyleName("selectmultiselectedbtn");

                } else {
                    defaultTrend = true;
                    this.removeStyleName("selectmultiselectedbtn");
                }
                lineChart.setSortDatasetOnTrendOrder(!defaultTrend);
                peptideSequenceTableLayout.sortTable(lineChart.getCurrentComparisonList(!defaultTrend));
                studiesNumberLabel.setValue("Datasets (" + peptideSequenceTableLayout.getRowsNumber() + "/" + totatlDatasetsNumber + ")");

                this.setEnabled(true);
                PeptideViewComponent.this.updateIcon(generateThumbImg());
                 showIndividualDatasets = !showIndividualDatasets;
                individualToTotalComparisonsDatasetsSwichBtn.onClick();

            }

        };

        orderByTrendBtn.setHeight(40, Unit.PIXELS);
        orderByTrendBtn.setWidth(40, Unit.PIXELS);

        orderByTrendBtn.addStyleName("pointer");

        orderByTrendBtn.updateIcon(trendOrderRes);
        orderByTrendBtn.setEnabled(true);
        peptideTableToolsContainer.addComponent(orderByTrendBtn);
        peptideTableToolsContainer.setComponentAlignment(orderByTrendBtn, Alignment.MIDDLE_CENTER);
        orderByTrendBtn.setDescription("Order dataset by trend");

        showSigOnlyBtn = new ImageContainerBtn() {

            private boolean showNotSigPeptides = true;

            @Override
            public void onClick() {
                if (showNotSigPeptides) {
                    showNotSigPeptides = false;
                    this.removeStyleName("selectmultiselectedbtn");

                } else {
                    showNotSigPeptides = true;
                    this.addStyleName("selectmultiselectedbtn");
                }

                peptideSequenceTableLayout.showNotSignificantPeptides(showNotSigPeptides);

            }

        };

        showSigOnlyBtn.setHeight(40, Unit.PIXELS);
        showSigOnlyBtn.setWidth(40, Unit.PIXELS);

        showSigOnlyBtn.addStyleName("pointer");
        showSigOnlyBtn.updateIcon(new ThemeResource("img/showSig.png"));
        showSigOnlyBtn.addStyleName("selectmultiselectedbtn");
        showSigOnlyBtn.setEnabled(true);
        peptideTableToolsContainer.addComponent(showSigOnlyBtn);
        peptideTableToolsContainer.setComponentAlignment(showSigOnlyBtn, Alignment.MIDDLE_CENTER);
        showSigOnlyBtn.setDescription("Show / hide not significant and stable peptides");
        peptideTableToolsContainer.setEnabled(true);

        ImageContainerBtn exportPdfBtn = new ImageContainerBtn() {

            private Table t1, t2;

            @Override
            public void onClick() {
                if (t1 != null) {
                    this.removeComponent(t1);
                    this.removeComponent(t2);
                }

                t1 = new Table();
                t1.addContainerProperty("Index", Integer.class, null, "Index", null, Table.Align.RIGHT);
                t1.addContainerProperty("Disease Category", String.class, null);
                t1.addContainerProperty("Disease Comparisons", String.class, null, "Disease Comparisons", null, Table.Align.LEFT);
                t1.addContainerProperty("Author", String.class, null);
                t1.addContainerProperty("PubMed ID", String.class, null);

                t1.addContainerProperty("Type of Study", String.class, null);
                t1.addContainerProperty("Analytical Approach", String.class, null);
                t1.addContainerProperty("Shotgun/Targeted", String.class, null);
                t1.addContainerProperty("Analytical Method", String.class, null);
                t1.addContainerProperty("Technology", String.class, null);
                t1.addContainerProperty("Sample Type", String.class, null);
                t1.addContainerProperty("Enzyme", String.class, null);

                t1.addContainerProperty("Quantification Basis", String.class, null);
                t1.addContainerProperty("Quantification Basis Comment", String.class, null);
                t1.addContainerProperty("#Identified Proteins", Integer.class, null, "#Identified Proteins", null, Table.Align.RIGHT);
                t1.addContainerProperty("#Quantified Proteins", Integer.class, null, "#Quantified Proteins", null, Table.Align.RIGHT);
                t1.addContainerProperty("Patients Gr.I", String.class, null);
                t1.addContainerProperty("Patients Sub Gr.I", String.class, null);
                t1.addContainerProperty("#Patients Gr.I", Integer.class, null, "#Patients Gr.I", null, Table.Align.RIGHT);
                t1.addContainerProperty("Patients Gr.I Comm.", String.class, null);

                t1.addContainerProperty("Patients Gr.II", String.class, null);
                t1.addContainerProperty("Patients Sub Gr.II", String.class, null);
                t1.addContainerProperty("#Patients Gr.II", Integer.class, null, "#Patients Gr.II", null, Table.Align.RIGHT);
                t1.addContainerProperty("Patients Gr.II Comm.", String.class, null);

                t1.addContainerProperty("Sample Matching", String.class, null);
                t1.addContainerProperty("Normalization Strategy", String.class, null);
                t1.addContainerProperty("Raw Data", String.class, null);

                t1.addContainerProperty("#Proteins", Integer.class, null, "#Proteins", null, Table.Align.RIGHT);
                t1.addContainerProperty("#Peptides", Integer.class, null, "Peptides", null, Table.Align.RIGHT);
                t1.addContainerProperty("#Dataset Specific Proteins", Integer.class, null, "#Dataset Specific Proteins", null, Table.Align.RIGHT);
                t1.addContainerProperty("#Dataset Specific Peptides", Integer.class, null, "#Dataset Specific Peptides", null, Table.Align.RIGHT);

                t1.addContainerProperty("Accession", String.class, null);
                t1.addContainerProperty("Fold Change", String.class, null);
                t1.addContainerProperty("p-value", String.class, null);
                t1.addContainerProperty("p-value Threshold", String.class, null);
                t1.addContainerProperty("Statistical Comments", String.class, null);
                t1.addContainerProperty("ROC AUC", String.class, null);

                t1.addContainerProperty("Additional Comments", String.class, null);

                t1.setVisible(false);
                this.addComponent(t1);

                String keyI = 0 + "_" + proteinKey;
                String keyII = 1 + "_" + proteinKey;
                String keyIII = 2 + "_" + proteinKey;

                String keyIV = 3 + "_" + proteinKey;
                String keyV = 4 + "_" + proteinKey;

                String key;
                int index = 0, index2 = 0;

                t2 = new Table();

                t2.addContainerProperty(
                        "Index2", Integer.class, null, "Index", null, Table.Align.RIGHT);
                t2.addContainerProperty(
                        "Protein Trend", String.class, null, "Protein Trend", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "Disease Category2", String.class, null, "Disease Category", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "Disease Comparisons2", String.class, null, "Disease Comparisons", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "Author2", String.class, null, "Author", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "PubMed Id2", String.class, null, "PubMed ID", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "#Patients2", Integer.class, null, "#Patients", null, Table.Align.RIGHT);
                t2.addContainerProperty(
                        "Peptide Sequence", String.class, null);
                t2.addContainerProperty(
                        "Sequence Annotated", String.class, null);
                t2.addContainerProperty(
                        "Peptide Modification", String.class, null);
                t2.addContainerProperty(
                        "Modification Comment", String.class, null);

                t2.addContainerProperty(
                        "Fold Change2", String.class, null, "Fold Change", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "p-value2", String.class, null, "p-value", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "p-value Threshold2", String.class, null, "p-value Threshold", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "Statistical Comments2", String.class, null, "Statistical Comments", null, Table.Align.LEFT);

                t2.addContainerProperty(
                        "Quantification Basis2", String.class, null, "Quantification Basis", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "Quantification Basis Comment2", String.class, null, "Quantification Basis Comment", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "Peptide Charge2", String.class, null, "Peptide Charge", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "ROC AUC2", String.class, null, "ROC AUC", null, Table.Align.LEFT);
                t2.addContainerProperty(
                        "Additional Comments2", String.class, null, "Additional Comments", null, Table.Align.LEFT);

                t2.setVisible(false);
                this.addComponent(t2);
                for (QuantDiseaseGroupsComparison groupComp : selectedComparisonsList) {
                    key = "";
                    if (groupComp.getQuantComparisonProteinMap().containsKey(keyI)) {
                        key = keyI;

                    } else if (groupComp.getQuantComparisonProteinMap().containsKey(keyII)) {
                        key = keyII;

                    } else if (groupComp.getQuantComparisonProteinMap().containsKey(keyIII)) {
                        key = keyIII;

                    } else if (groupComp.getQuantComparisonProteinMap().containsKey(keyIV)) {
                        key = keyIV;

                    } else if (groupComp.getQuantComparisonProteinMap().containsKey(keyV)) {
                        key = keyV;

                    }
                    if (!groupComp.getQuantComparisonProteinMap().containsKey(key)) {

                        continue;
                    }
                    QuantComparisonProtein protein = groupComp.getQuantComparisonProteinMap().get(key);
                    for (QuantProtein quantProt : protein.getDsQuantProteinsMap().values()) {
                        QuantDataset ds = groupComp.getDatasetMap().get(quantProt.getQuantDatasetIndex());
                        String roc = quantProt.getRoc_auc() + "";
                        if (quantProt.getRoc_auc() == -1000000000.0) {
                            roc = null;
                        }
                        t1.addItem(new Object[]{(index + 1), groupComp.getDiseaseCategory(), groupComp.getComparisonHeader().replace("__" + groupComp.getDiseaseCategory(), ""), ds.getAuthor(), ds.getPubMedId(), ds.getTypeOfStudy(), ds.getAnalyticalApproach(), ds.getShotgunTargeted(), ds.getAnalyticalMethod(), ds.getTechnology(), ds.getSampleType(), ds.getEnzyme(),
                            ds.getQuantificationBasis(), quantProt.getQuantBasisComment(), "", ds.getQuantifiedProteinsNumber(), ds.getDiseaseMainGroupI(), ds.getDiseaseSubGroup1(), ds.getDiseaseMainGroup1Number(), ds.getDiseaseMainGroup1Comm(), ds.getDiseaseMainGroup2(), ds.getDiseaseSubGroup2(), ds.getDiseaseMainGroup2Number(), ds.getDiseaseMainGroup2Comm(),
                            ds.getSampleMatching(), ds.getNormalizationStrategy(), ds.getRawDataUrl(), ds.getTotalProtNum(), ds.getTotalPepNum(), ds.getUniqueProtNum(), ds.getUniqePepNum(), quantProt.getUniprotAccessionNumber(), quantProt.getString_fc_value(), quantProt.getString_p_value(), quantProt.getPvalueSignificanceThreshold(), quantProt.getP_value_comments(), roc, quantProt.getAdditionalComments()}, index++);

                    }

                    for (QuantPeptide quantPeptide : protein.getQuantPeptidesList()) {
                        QuantDataset ds = groupComp.getDatasetMap().get(quantPeptide.getQuantDatasetIndex());
                        String protTrend;
                        if (protein.getOverallCellPercentValue() > 0) {
                            protTrend = "Increased";
                        } else if (protein.getOverallCellPercentValue() == 0) {
                            protTrend = "Equal";
                        } else {
                            protTrend = "Decreased";
                        }
                        t2.addItem(new Object[]{(index2 + 1), protTrend, ds.getDiseaseCategoryI(), groupComp.getComparisonHeader().replace("__" + groupComp.getDiseaseCategory(), ""), ds.getAuthor(), ds.getPubMedId(), (ds.getDiseaseMainGroup1Number() + ds.getDiseaseMainGroup2Number()), quantPeptide.getPeptideSequence(), quantPeptide.getSequenceAnnotated(), quantPeptide.getPeptideModification(), quantPeptide.getModification_comment(), quantPeptide.getString_fc_value(), quantPeptide.getString_p_value(), quantPeptide.getPvalueSignificanceThreshold(), quantPeptide.getP_value_comments(), ds.getQuantificationBasis(), quantPeptide.getQuantBasisComment(), quantPeptide.getPeptideCharge() + "", quantPeptide.getRoc_auc() + "", quantPeptide.getAdditionalComments()}, index2++);
                    }
                }

                ExcelExport csvExport = new ExcelExport(t1, proteinNameLabel.getValue() + " (" + proteinKey + ") Overview");

                csvExport.setReportTitle(
                        "CSF-PR / " + proteinNameLabel.getValue() + " (" + proteinKey + ") Overview");
                csvExport.setExportFileName(
                        "CSF-PR - Protein and Peptides information (" + proteinKey + ").xls");
                csvExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);

                csvExport.setDisplayTotals(
                        false);
                csvExport.setExcelFormatOfProperty(
                        "Index", "0");
                csvExport.convertTable();
                csvExport.setNextTable(t2, "Peptides Information");
                csvExport.export();

            }

        };

        exportPdfBtn.setHeight(40, Unit.PIXELS);
        exportPdfBtn.setWidth(40, Unit.PIXELS);

        exportPdfBtn.updateIcon(
                new ThemeResource("img/xls-text-o-2.png"));
        exportPdfBtn.setEnabled(
                true);
        peptideTableToolsContainer.addComponent(exportPdfBtn);

        peptideTableToolsContainer.setComponentAlignment(exportPdfBtn, Alignment.MIDDLE_CENTER);

        exportPdfBtn.setDescription(
                "Export protein overview and peptides information");

        InformationButton info = new InformationButton("The protein panel provides an overview of the available information for the currently selected protein. The chart at the top shows the quantitative information for the selected protein, classified into Increased, Decreased or Equal. If the quantitative data for a given comparison is not exclusively in the same direction an average value will be shown. \n"
                + "To show the individual datasets click the \"Show datasets\" button in the lower right corner. Clicking the \"Resize dataset symbols based on number of patients\" will then change the chart to indicate the number of patients in each dataset. The lower half of the panel shows the details for each dataset, including the sequence coverage (if available). Click a peptide or any of the other columns for further details.", false);

        peptideTableToolsContainer.addComponent(info);

    }

    /**
     * Selection changed in the selection manager
     *
     * @param type type of selection
     */
    @Override
    public void selectionChanged(String type) {

        if (type.equalsIgnoreCase("peptide_selection")) {
            if (CSFPR_Central_Manager.getSelectedProteinAccession() == null) {
                updateIcon(null);
            } else {
                updateData(CSFPR_Central_Manager.getSelectedComparisonsList(), CSFPR_Central_Manager.getSelectedProteinAccession(), CSFPR_Central_Manager.getCustProteinSelectionTrend());
            }

        } else {

            updateIcon(null);
        }
        defaultTrend = true;
        showSigOnlyBtn.addStyleName("selectmultiselectedbtn");
        showIndividualDatasets = false;
        orderByTrendBtn.removeStyleName("selectmultiselectedbtn");
        this.individualToTotalComparisonsDatasetsSwichBtn.updateIcon(comparisonDsRes);

//        resizeDsOnPatientNumbersBtn.setEnabled(false);
//        resizeDsOnPatientNumbersBtn.removeStyleName("selectmultiselectedbtn");
        lineChart.setResizeDetailedDatasets(true);
        individualToTotalComparisonsDatasetsSwichBtn.onClick();

    }

    /**
     * Update data input for the component to generate the protein line chart
     * and protein details table(peptide table)
     *
     * @param selectedComparisonsList Set of selected comparisons
     * @param proteinKey Main selected protein key
     * @param custTrend the customized user data trend -1 if no customized data
     * input and (0 to 4) in case of compare mode is activated
     *
     */
    private void updateData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey, int custTrend) {
        TrendLegend legendLayoutComponent;
        int legentWidth = 860;
        if (custTrend != -1) {
            legendLayout.removeAllComponents();
            legendLayoutComponent = new TrendLegend(custTrend);
            legendLayoutComponent.setWidthUndefined();
            legendLayoutComponent.setHeight(25, Unit.PIXELS);
            legentWidth = 900;
        } else {
            legendLayout.removeAllComponents();
            legendLayoutComponent = new TrendLegend("linechart");
        }
        if (this.getWidth() - 300 < legentWidth) {
            CloseButton closeBtn = new CloseButton();
            VerticalLayout legendPopup = new VerticalLayout();
            legendPopup.addComponent(closeBtn);
            legendPopup.setExpandRatio(closeBtn, 1);
            Set<Component> set = new LinkedHashSet<>();
            VerticalLayout spacer = new VerticalLayout();
            spacer.setHeight(5, Unit.PIXELS);
            spacer.setWidth(20, Unit.PIXELS);
            set.add(spacer);
            Iterator<Component> itr = legendLayoutComponent.iterator();
            while (itr.hasNext()) {
                set.add(itr.next());
            }

            for (Component c : set) {
                legendPopup.addComponent(c);
                legendPopup.setExpandRatio(c, c.getHeight() + 5);
            }

            legendPopup.setWidth(230, Unit.PIXELS);
            legendPopup.setHeight(150, Unit.PIXELS);
            final PopupView popup = new PopupView("Legend", legendPopup);
            legendPopup.addStyleName("compactlegend");
            popup.addStyleName("marginright20");
            popup.setHideOnMouseOut(false);
            closeBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                popup.setPopupVisible(false);

            });
            legendLayout.addComponent(popup);
            legendLayout.setComponentAlignment(popup, Alignment.TOP_RIGHT);
        } else {
            legendLayoutComponent.setWidthUndefined();
            legendLayoutComponent.setHeight(25, Unit.PIXELS);
            legendLayout.addComponent(legendLayoutComponent);
            legendLayout.setComponentAlignment(legendLayoutComponent, Alignment.TOP_RIGHT);

        }

        this.selectedComparisonsList = selectedComparisonsList;

        this.proteinKey = proteinKey;
        lineChart.updateData(selectedComparisonsList, this.proteinKey, custTrend);
        peptideSequenceTableLayout.updateTableData(selectedComparisonsList, this.proteinKey);
        totatlDatasetsNumber = peptideSequenceTableLayout.getRowsNumber();
        studiesNumberLabel.setValue("Datasets (" + totatlDatasetsNumber + "/" + totatlDatasetsNumber + ")");
        proteinNameLabel.setValue(lineChart.getProteinName());
        proteinNameLabel.setDescription(lineChart.getProteinName());
        updateIcon(generateThumbImg());

    }

    /**
     * Get registered listener id.
     *
     * @return listener id
     */
    @Override
    public String getListenerId() {
        return "peptideComponent";
    }

}
