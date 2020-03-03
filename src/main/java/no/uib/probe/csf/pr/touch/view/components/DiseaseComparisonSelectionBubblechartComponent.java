package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFSelection;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.GroupSwitchBtn;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.BubbleComponent;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

/**
 * This class represents disease comparison selection bubble chart component.
 *
 * @author Yehia Farag
 *
 */
public abstract class DiseaseComparisonSelectionBubblechartComponent extends VerticalLayout implements CSFListener, LayoutEvents.LayoutClickListener {

    /**
     * The central selection manager for handling data across different
     * visualizations and managing all users selections.
     */
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    /**
     * The main chart container.
     */
    private final AbsoluteLayout chartLayoutContainer;
    /**
     * The main chart data container (Vaadin bubble container).
     */
    private final AbsoluteLayout chartComponentLayout;
    /**
     * The main chart background image (to be updated using JFreechart).
     */
    private final Image chartImage;

    /**
     * The main component width.
     */
    private int width;
    /**
     * The main component height.
     */
    private int height;
    /**
     * Hide equal proteins data to clean the chart.
     */
    private boolean hideEqualProteins = false;

    /**
     * Generate new image (part of sittings for JFreeChart image generator).
     */
    private boolean isNewImge = true;

    /**
     * Color of equal proteins bubble (blue or gray in case of no data
     * available).
     */
    private Color stableColor;
    /**
     * Chart rendering information that has the all information required for
     * drawing Vaadin bubbles in the absolute layout.
     */
    private final ChartRenderingInfo chartRenderingInfo;

    /**
     * JFreeChart used to generate thumb image and default chart image
     * background.
     */
    private JFreeChart chart;
    /**
     * Default chart image into url link generated from JFreechart for the main
     * chart layout.
     */
    private String defaultImgURL = "";
    /**
     * Thumb chart image into url link generated from JFreechart for the main
     * chart layout.
     */
    private String thumbImgUrl = "";
    /**
     * Map of comparisons name to number of proteins in each category (increased
     * 100%,equal...etc).
     */
    private final Map<String, double[]> tooltipsProtNumberMap;
    /**
     * List of selected comparisons to be updated based on user selection for
     * comparisons across the system.
     */
    private Set<QuantDiseaseGroupsComparison> selectedComparisonList;
    /**
     * Customized comparison based on user input data in quant comparison
     * layout.
     */
    private QuantDiseaseGroupsComparison userCustomizedComparison;
    /**
     * A marker for user data disease group comparison in the bubble chart.
     */
    private int userDataCounter;
    /**
     * List of selected bubbles in the bubble plot.
     */
    private final Set<BubbleComponent> lastselectedComponents;
    /**
     * Array of tool-tip text identical to comparisons order.
     */
    private final String[] tooltipLabels;
    /**
     * Array of trend HTML/CSS style names for different bubbles (increased
     * 100%, equal...etc).
     */
    private final String[] trendStyles;

    /**
     * Allow multi select for bubbles in the chart.
     */
    private boolean allowMultiSelect = true;

    /**
     * Map of comparison title and number of proteins included in each trend
     * (increased 100%, equal...etc).
     */
    private final Map<String, Integer[]> comparisonValuesMap = new LinkedHashMap<>();

    /**
     * The disease comparison bubble chart right side control buttons container.
     */
    private final VerticalLayout bubblechartToolsContainer;

    /**
     * Get side buttons container that has all the bubble chart control buttons.
     *
     * @return bubblechartToolsContainer
     */
    public VerticalLayout getBubblechartToolsContainer() {
        return bubblechartToolsContainer;
    }

    /**
     * Constructor to initialize the main attributes ( selection manage ..etc).
     *
     * @param CSFPR_Central_Manager The central selection manager
     * @param width main body layout width (the container)
     * @param height main body layout height (the container)
     *
     */
    public DiseaseComparisonSelectionBubblechartComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height) {
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;

        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);

        VerticalLayout bodyContainer = new VerticalLayout();
        bodyContainer.setWidth(100, Unit.PERCENTAGE);
        bodyContainer.setHeightUndefined();
        bodyContainer.setSpacing(true);
        this.addComponent(bodyContainer);

        //init toplayout
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setHeight(25, Unit.PIXELS);
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.setSpacing(true);
        topLayout.setMargin(new MarginInfo(false, false, false, true));
        bodyContainer.addComponent(topLayout);
        bodyContainer.setComponentAlignment(topLayout, Alignment.TOP_CENTER);

        HorizontalLayout titleLayoutWrapper = new HorizontalLayout();
        titleLayoutWrapper.setHeight(25, Unit.PIXELS);
        titleLayoutWrapper.setWidthUndefined();
        titleLayoutWrapper.setSpacing(true);
        titleLayoutWrapper.setMargin(false);
        titleLayoutWrapper.addStyleName("margintop7");
        topLayout.addComponent(titleLayoutWrapper);
        topLayout.setExpandRatio(titleLayoutWrapper, 10);

        Label overviewLabel = new Label("Overview");
        overviewLabel.setStyleName(ValoTheme.LABEL_BOLD);
        overviewLabel.addStyleName(ValoTheme.LABEL_SMALL);
        overviewLabel.addStyleName(ValoTheme.LABEL_TINY);
        overviewLabel.setWidth(75, Unit.PIXELS);
        titleLayoutWrapper.addComponent(overviewLabel);
        titleLayoutWrapper.setComponentAlignment(overviewLabel, Alignment.TOP_LEFT);
        TrendLegend legendLayout = new TrendLegend("bubblechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(24, Unit.PIXELS);
        legendLayout.addStyleName("margintop7");
        legendLayout.addStyleName("floatright");
        topLayout.addComponent(legendLayout);
        topLayout.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);
        topLayout.setExpandRatio(legendLayout, 90);

        /**
         * end of top layout.
         */
        /**
         * start chart layout.
         */
        VerticalLayout chartLayoutFrame = new VerticalLayout();
        height = height - 44;

        width = width - 50;
        chartLayoutFrame.setWidth(width, Unit.PIXELS);
        chartLayoutFrame.setHeightUndefined();
        chartLayoutFrame.addStyleName("roundedborder");
        chartLayoutFrame.addStyleName("padding20");
        chartLayoutFrame.addStyleName("whitelayout");
        bodyContainer.addComponent(chartLayoutFrame);
        bodyContainer.setComponentAlignment(chartLayoutFrame, Alignment.MIDDLE_CENTER);

        height = height - 70;
        width = width - 40;
        this.height = height;
        this.width = width;
        chartLayoutContainer = new AbsoluteLayout();
        chartLayoutContainer.setWidth(width, Unit.PIXELS);
        chartLayoutContainer.setHeight(this.height, Unit.PIXELS);
        chartLayoutFrame.addComponent(chartLayoutContainer);

        HorizontalLayout controlsLayout = new HorizontalLayout();
        controlsLayout.setWidth(100, Unit.PERCENTAGE);
        controlsLayout.setHeight(20, Unit.PIXELS);

        Label clickcommentLabel = new Label("Click in the chart to select data");
        clickcommentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        clickcommentLabel.addStyleName(ValoTheme.LABEL_TINY);
        clickcommentLabel.addStyleName("italictext");
        clickcommentLabel.setWidth(182, Unit.PIXELS);

        controlsLayout.addComponent(clickcommentLabel);
        controlsLayout.setComponentAlignment(clickcommentLabel, Alignment.BOTTOM_RIGHT);
        chartLayoutFrame.addComponent(controlsLayout);

        chartImage = new Image();
        chartImage.setWidth(100, Unit.PERCENTAGE);
        chartImage.setHeight(100, Unit.PERCENTAGE);
        chartLayoutContainer.addComponent(chartImage, "left: " + 0 + "px; top: " + 0 + "px;");

        chartComponentLayout = new AbsoluteLayout();
        chartComponentLayout.setWidth(100, Unit.PERCENTAGE);
        chartComponentLayout.setHeight(100, Unit.PERCENTAGE);
        chartComponentLayout.addLayoutClickListener(DiseaseComparisonSelectionBubblechartComponent.this);
        chartLayoutContainer.addComponent(chartComponentLayout, "left: " + 0 + "px; top: " + 0 + "px;");
        this.chartRenderingInfo = new ChartRenderingInfo();

        //init data structure
        tooltipsProtNumberMap = new HashMap<>();
        lastselectedComponents = new HashSet<>();
        tooltipLabels = new String[]{"", "  Decreased" + " ", "  Decreased" + " ", "  Equal" + " ", "  Increased" + " ", "  Increased" + " ", ""};
        trendStyles = new String[]{"", "decreased100", "decreasedless100", "stable", "increasedless100", "increased100", ""};

        this.CSFPR_Central_Manager.registerListener(DiseaseComparisonSelectionBubblechartComponent.this);

        //init side control btns layout 
        bubblechartToolsContainer = new VerticalLayout();
        bubblechartToolsContainer.setHeightUndefined();
        bubblechartToolsContainer.setWidthUndefined();
        bubblechartToolsContainer.setSpacing(true);

        GroupSwitchBtn groupSwichBtn = new GroupSwitchBtn() {

            @Override
            public Set<QuantDiseaseGroupsComparison> getUpdatedComparsionList() {
                return CSFPR_Central_Manager.getSelectedComparisonsList();
            }

            @Override
            public void updateComparisons(LinkedHashSet<QuantDiseaseGroupsComparison> updatedComparisonList) {

                CSFSelection selection = new CSFSelection("comparisons_selection_update", getListenerId(), updatedComparisonList, null);
                CSFPR_Central_Manager.setSelection(selection);

            }

            @Override
            public Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> getEqualComparsionMap() {
                return CSFPR_Central_Manager.getEqualComparisonMap();
            }

        };

        bubblechartToolsContainer.addComponent(groupSwichBtn);
        bubblechartToolsContainer.setComponentAlignment(groupSwichBtn, Alignment.MIDDLE_CENTER);

        ThemeResource scatterplotApplied = new ThemeResource("img/scatter_plot_applied_updated.png");
        ThemeResource scatterplotUnapplied = new ThemeResource("img/scatter_plot_unapplied.png");
        final ImageContainerBtn hideStableBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.getDescription().equalsIgnoreCase("Hide equal proteins")) {
                    this.updateIcon(scatterplotUnapplied);
                    hideEqualProteins = true;
                    this.setDescription("Show equal proteins");
                } else {
                    this.updateIcon(scatterplotApplied);

                    hideEqualProteins = false;
                    this.setDescription("Hide equal proteins");

                }
                updateChart();

            }
        };

        hideStableBtn.setHeight(40, Unit.PIXELS);
        hideStableBtn.setWidth(40, Unit.PIXELS);

        hideStableBtn.updateIcon(scatterplotApplied);
        hideStableBtn.setEnabled(true);
        bubblechartToolsContainer.addComponent(hideStableBtn);
        bubblechartToolsContainer.setComponentAlignment(hideStableBtn, Alignment.MIDDLE_CENTER);
        hideStableBtn.setDescription("Hide equal proteins");

        ImageContainerBtn unselectAllBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (lastselectedComponents.isEmpty()) {
                    return;
                }
                lastselectedComponents.clear();
                rePaintChart();
                updateSelectionManager();
            }

        };
        unselectAllBtn.updateIcon(new ThemeResource("img/grid-small-o.png"));
        unselectAllBtn.setEnabled(true);

        unselectAllBtn.setWidth(40, Unit.PIXELS);
        unselectAllBtn.setHeight(40, Unit.PIXELS);
        unselectAllBtn.addStyleName("smallimg");

        bubblechartToolsContainer.addComponent(unselectAllBtn);
        bubblechartToolsContainer.setComponentAlignment(unselectAllBtn, Alignment.MIDDLE_CENTER);
        unselectAllBtn.setDescription("Unselect all disease group comparisons");

        final ImageContainerBtn selectMultiBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.getStyleName().contains("selectmultiselectedbtn")) {
                    allowMultiSelect = false;
                    this.removeStyleName("selectmultiselectedbtn");

                } else {
                    allowMultiSelect = true;
                    this.addStyleName("selectmultiselectedbtn");

                }
            }

        };
        selectMultiBtn.addStyleName("selectmultiselectedbtn");
        selectMultiBtn.addStyleName("smallimg");
        selectMultiBtn.setDescription("Multiple selection");
        selectMultiBtn.updateIcon(new ThemeResource("img/grid-small-multi.png"));
        selectMultiBtn.setEnabled(true);

        selectMultiBtn.setWidth(40, Unit.PIXELS);
        selectMultiBtn.setHeight(40, Unit.PIXELS);

        bubblechartToolsContainer.addComponent(selectMultiBtn);
        bubblechartToolsContainer.setComponentAlignment(selectMultiBtn, Alignment.MIDDLE_CENTER);

        ImageContainerBtn exportPdfBtn = new ImageContainerBtn() {
            private Table t;

            @Override
            public void onClick() {
                if (t != null) {
                    this.removeComponent(t);
                }

                t = new Table();
                t.addContainerProperty("Index", Integer.class, null, "Index", null, Table.Align.RIGHT);
                t.addContainerProperty("Disease Comparisons", String.class, null, "Disease Comparisons", null, Table.Align.LEFT);
                t.addContainerProperty("NoQuantInfo", Integer.class, null, "#No Quant. Info", null, Table.Align.RIGHT);
                t.addContainerProperty("100Decreased", Integer.class, null, "#100% Decreased", null, Table.Align.RIGHT);
                t.addContainerProperty("less100Decreased", Integer.class, null, "#<100% Decreased", null, Table.Align.RIGHT);
                t.addContainerProperty("Stable", Integer.class, null, "#Stable", null, Table.Align.RIGHT);
                t.addContainerProperty("less100Increased", Integer.class, null, "#<100% Increased", null, Table.Align.RIGHT);
                t.addContainerProperty("100Increased", Integer.class, null, "#100% Increased", null, Table.Align.RIGHT);
                t.setVisible(false);
                this.addComponent(t);

                int index = 0;
                for (String compTitile : comparisonValuesMap.keySet()) {
                    Integer[] values = comparisonValuesMap.get(compTitile);
                    t.addItem(new Object[]{(index + 1), compTitile, values[0], values[1], values[2], values[3], values[4], values[5]}, index++);

                }

                ExcelExport csvExport = new ExcelExport(t, "CSF-PR  Quant Protein Overview");
                csvExport.setReportTitle("CSF-PR / Quant Protein Overview ");
                csvExport.setExportFileName("CSF-PR - Quant Protein Overview" + ".xls");
                csvExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.setExcelFormatOfProperty("Index", "0");

                csvExport.export();
            }

        };

        exportPdfBtn.setHeight(40, Unit.PIXELS);
        exportPdfBtn.setWidth(40, Unit.PIXELS);

        exportPdfBtn.updateIcon(new ThemeResource("img/xls-text-o-2.png"));
        exportPdfBtn.setEnabled(true);
        bubblechartToolsContainer.addComponent(exportPdfBtn);
        bubblechartToolsContainer.setComponentAlignment(exportPdfBtn, Alignment.MIDDLE_CENTER);
        exportPdfBtn.setDescription("Export data");
        bubblechartToolsContainer.addComponent(exportPdfBtn);

        InformationButton info = new InformationButton("The bubble chart provides an overview of all the proteins found in the currently selected disease group comparisons. The size of each bubble represents the number of proteins in the given comparison and the color represents the trend. To change the order of the groups in a given comparison please click the \"Switch disease groups\" icon in the lower right corner. Select one or more bubbles to display the corresponding proteins.", false);
        bubblechartToolsContainer.addComponent(info);

    }

    /**
     * Generate bubble chart (JFreechart) that is used to generate the thumb
     * image and default background image the development based on DiVA concept
     *
     * @param selectedComparisonList List of selected comparison objects
     */
    private JFreeChart generateBubbleChart(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        tooltipsProtNumberMap.clear();
        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
        int counter = 0;
        int upper = -1;
        Set<QuantDiseaseGroupsComparison> tselectedComparisonList = new LinkedHashSet<>();
        comparisonValuesMap.clear();
        if (userCustomizedComparison != null) {
            tselectedComparisonList.add(userCustomizedComparison);
            if (userCustomizedComparison.getQuantComparisonProteinMap().size() > upper) {
                upper = userCustomizedComparison.getQuantComparisonProteinMap().size();
            }

        }
        tselectedComparisonList.addAll(selectedComparisonList);

        for (QuantDiseaseGroupsComparison qc : tselectedComparisonList) {
            if (hideEqualProteins) {
                int upperCounter = 0;
                upperCounter = qc.getQuantComparisonProteinMap().values().stream().filter((quantComparisonProtein) -> !(quantComparisonProtein == null)).filter((quantComparisonProtein) -> !(quantComparisonProtein.getSignificantTrindCategory() == 2 || quantComparisonProtein.getSignificantTrindCategory() == 5)).map((_item) -> 1).reduce(upperCounter, Integer::sum);
                if (upperCounter > upper) {
                    upper = upperCounter;
                }

            } else {
                if (qc.getQuantComparisonProteinMap() == null) {
                    System.out.println("null qc " + qc.getComparisonHeader());
                }
                if (qc.getQuantComparisonProteinMap().size() > upper) {
                    upper = qc.getQuantComparisonProteinMap().size();
                }
            }

        }

        final Map<Integer, Color[]> seriousColorMap = new HashMap<>();

        Color[] dataColor;

        dataColor = new Color[]{Color.WHITE, new Color(0, 153, 0), new Color(0, 229, 132), stableColor, new Color(247, 119, 119), new Color(204, 0, 0), Color.WHITE};

        double[] yAxisValueI = new double[]{0, 0, 0, 0, 0, 0, 0};
        double[] xAxisValueI = new double[]{0, 0, 0, 0, 0, 0, 0};
        double[] widthValueI = new double[]{0, 0, 0, 0, 0, 0, 0};
        double[][] seriesValuesI = {yAxisValueI, xAxisValueI, widthValueI};
        seriousColorMap.put(0, new Color[]{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE});
        defaultxyzdataset.addSeries("   ", seriesValuesI);

        for (QuantDiseaseGroupsComparison quantComparison : tselectedComparisonList) {
            Integer[] values = initValuesArr();

            double[] tempWidthValue = new double[8];
            if (quantComparison.getQuantComparisonProteinMap() == null) {
                continue;
            }

//            System.out.println("at ----------------------- >>      error "+ quantComparison.getQuantComparisonProteinMap().keySet());
            quantComparison.getQuantComparisonProteinMap().keySet().stream().forEach((key) -> {
//
                QuantComparisonProtein quantComparisonProtein = quantComparison.getQuantComparisonProteinMap().get(key);
                quantComparisonProtein.finalizeQuantData();
//
                if (hideEqualProteins && (quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() == 2 || quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() == 5)) {
                    tempWidthValue[3] = 0;
                    tempWidthValue[6] = 0;
                    values[2] = 0;
                    values[5] = 0;
                } else {
                    values[quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() + 1] = values[quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() + 1] + 1;
                    tempWidthValue[quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() + 1] = tempWidthValue[quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() + 1] + 1;
                }
            });
            comparisonValuesMap.put(quantComparison.getComparisonFullName(), values);
            if (tempWidthValue[3] > 0 && tempWidthValue[6] >= 0) {
                stableColor = new Color(1, 141, 244);
                trendStyles[3] = "stable";
            } else if (tempWidthValue[3] == 0 && tempWidthValue[6] > 0) {
                stableColor = Color.decode("#b5babb");
                trendStyles[3] = "nodata";
            }

            tempWidthValue[3] = tempWidthValue[3] + tempWidthValue[6];
            tempWidthValue[6] = 0;
            dataColor[3] = stableColor;

            int length = 0;
            if (upper < 10) {
                upper = 10;
            }

            double[] tooltipNumbess = new double[tempWidthValue.length];
            System.arraycopy(tempWidthValue, 0, tooltipNumbess, 0, tempWidthValue.length);
            this.tooltipsProtNumberMap.put(quantComparison.getComparisonHeader(), tooltipNumbess);
            for (int x = 0; x < tempWidthValue.length; x++) {
                if (tempWidthValue[x] > 0) {
                    tempWidthValue[x] = scaleValues(tempWidthValue[x], upper, 0.05);
                    length++;
                }

            }
            double[] yAxisValue = new double[length];
            double[] xAxisValue = new double[length];
            double[] widthValue = new double[length];
            Color[] serColorArr = new Color[length];
            length = 0;

            for (int x = 0; x < tempWidthValue.length; x++) {
                if (tempWidthValue[x] > 0) {
                    xAxisValue[length] = x;
                    yAxisValue[length] = counter + 1;
                    widthValue[length] = tempWidthValue[x];
                    serColorArr[length] = dataColor[x];
                    length++;
                }

            }

            if (length == 1 && tselectedComparisonList.size() == 1) {
                widthValue[0] = 1;
            }
            seriousColorMap.put(counter + 1, serColorArr);

            double[][] seriesValues = {yAxisValue, xAxisValue, widthValue};
            defaultxyzdataset.addSeries(quantComparison.getComparisonHeader(), seriesValues);
            counter++;
        }
        double[] yAxisValueII = new double[0];
        double[] xAxisValueII = new double[0];
        double[] widthValueII = new double[0];
        seriousColorMap.put(counter + 1, new Color[]{});
        double[][] seriesValuesII = {yAxisValueII, xAxisValueII, widthValueII};
        defaultxyzdataset.addSeries(" ", seriesValuesII);

        final Color[] labelsColor = new Color[]{Color.LIGHT_GRAY, new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, new Color(204, 0, 0), Color.LIGHT_GRAY};
        Font font;

        font = new Font("Helvetica Neue", Font.PLAIN, 13);

        SymbolAxis yAxis = new SymbolAxis(null, new String[]{"  ", "Decreased", " ", "Equal", " ", "Increased", "  "}) {
            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= labelsColor.length) {
                    x = 0;
                }
                return labelsColor[x++];
            }
        };
        yAxis.setAutoRangeStickyZero(true);
        yAxis.setFixedAutoRange(8);
        yAxis.setTickLabelFont(font);
        yAxis.setGridBandsVisible(false);
        yAxis.setAxisLinePaint(Color.LIGHT_GRAY);
        yAxis.setTickMarksVisible(false);
        yAxis.setUpperBound(6);

        String[] xAxisLabels = new String[tselectedComparisonList.size() + 2];
        int x = 0;
        xAxisLabels[x] = "";
        int maxLength = -1;
        //init labels color

        final Color[] diseaseGroupslabelsColor = new Color[tselectedComparisonList.size() + 2];
        diseaseGroupslabelsColor[x] = Color.WHITE;
        x++;

        for (QuantDiseaseGroupsComparison comp : tselectedComparisonList) {
            String header = comp.getComparisonHeader();
            String updatedHeader = header.split(" / ")[0].split("__")[0] + " / " + header.split(" / ")[1].split("__")[0] + "";

            xAxisLabels[x] = updatedHeader;
            if (xAxisLabels[x].length() > maxLength) {
                maxLength = xAxisLabels[x].length();
            }
            diseaseGroupslabelsColor[x] = Color.decode(comp.getDiseaseCategoryColor());
            x++;

        }
        xAxisLabels[x] = "";
        diseaseGroupslabelsColor[x] = Color.WHITE;

        SymbolAxis xAxis;
        final boolean finalNum;
        finalNum = maxLength > 50 && tselectedComparisonList.size() > 4;

        xAxis = new SymbolAxis(null, xAxisLabels) {

            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= diseaseGroupslabelsColor.length) {
                    x = 0;
                }
                return diseaseGroupslabelsColor[x++];
            }

            private final boolean localfinal = finalNum;

            @Override
            protected List refreshTicksHorizontal(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {

                if (localfinal) {
                    setVerticalTickLabels(localfinal);
                    return super.refreshTicksHorizontal(g2, dataArea, edge);
                }
                List ticks = new java.util.ArrayList();
                Font tickLabelFont = getTickLabelFont();
                g2.setFont(tickLabelFont);
                double size = getTickUnit().getSize();
                int count = calculateVisibleTickCount();
                double lowestTickValue = calculateLowestVisibleTickValue();
                double previousDrawnTickLabelPos = 0.0;
                double previousDrawnTickLabelLength = 0.0;
                if (count <= ValueAxis.MAXIMUM_TICK_COUNT) {
                    for (int i = 0; i < count; i++) {
                        double currentTickValue = lowestTickValue + (i * size);
                        double xx = valueToJava2D(currentTickValue, dataArea, edge);
                        String tickLabel;
                        NumberFormat formatter = getNumberFormatOverride();
                        if (formatter != null) {
                            tickLabel = formatter.format(currentTickValue) + "  ";
                        } else {
                            tickLabel = valueToString(currentTickValue) + "  ";
                        }
                        // avoid to draw overlapping tick labels
                        Rectangle2D bounds = TextUtilities.getTextBounds(tickLabel, g2,
                                g2.getFontMetrics());
                        double tickLabelLength = isVerticalTickLabels()
                                ? bounds.getHeight() : bounds.getWidth();
                        boolean tickLabelsOverlapping = false;
                        if (i > 0) {
                            double avgTickLabelLength = (previousDrawnTickLabelLength
                                    + tickLabelLength) / 2.0;
                            if (Math.abs(xx - previousDrawnTickLabelPos)
                                    < avgTickLabelLength) {
                                tickLabelsOverlapping = true;
                            }
                        }
                        if (tickLabelsOverlapping) {
                            setVerticalTickLabels(true);
                        } else {
                            // remember these values for next comparison
                            previousDrawnTickLabelPos = xx;
                            previousDrawnTickLabelLength = tickLabelLength;
                        }
                        TextAnchor anchor;
                        TextAnchor rotationAnchor;
                        double angle = 0.0;
                        if (isVerticalTickLabels()) {
                            anchor = TextAnchor.CENTER_RIGHT;
                            rotationAnchor = TextAnchor.CENTER_RIGHT;
                            if (edge == RectangleEdge.TOP) {
                                angle = 76.5;
                            } else {
                                angle = -76.5;
                            }
                        } else {
                            if (edge == RectangleEdge.TOP) {
                                anchor = TextAnchor.BOTTOM_CENTER;
                                rotationAnchor = TextAnchor.BOTTOM_CENTER;
                            } else {
                                anchor = TextAnchor.TOP_CENTER;
                                rotationAnchor = TextAnchor.TOP_CENTER;
                            }
                        }
                        Tick tick = new NumberTick(new Double(currentTickValue),
                                tickLabel, anchor, rotationAnchor, angle);

                        ticks.add(tick);
                    }
                }
                return ticks;
            }
        };
        xAxis.setTickLabelFont(font);
        xAxis.setTickLabelInsets(new RectangleInsets(2, 20, 2, 20));
        xAxis.setAutoRangeStickyZero(true);
        xAxis.setTickMarksVisible(false);
        xAxis.setUpperBound(diseaseGroupslabelsColor.length - 1);

        xAxis.setGridBandsVisible(false);
        xAxis.setAxisLinePaint(Color.LIGHT_GRAY);
        int scale = XYBubbleRenderer.SCALE_ON_RANGE_AXIS;

        XYItemRenderer xyitemrenderer = new XYBubbleRenderer(scale) {
            private int counter = 0;
            private int localSerious = -1;
            private final Map<Integer, Color[]> localSeriousColorMap = seriousColorMap;

            @Override
            public Paint getSeriesPaint(int series) {
                if (series != localSerious || isNewImge || localSeriousColorMap.get(series).length == counter) {
                    counter = 0;
                    isNewImge = false;
                }
                localSerious = series;
                Color c = localSeriousColorMap.get(series)[counter];
                counter++;

                return c;
            }

        };

        XYPlot xyplot = new XYPlot(defaultxyzdataset, xAxis, yAxis, xyitemrenderer) {

            @Override
            protected void drawRangeGridlines(Graphics2D g2, Rectangle2D area, List ticks) {
                try {
                    if (!ticks.isEmpty()) {
                        ticks.remove(0);
                    }
                    if (!ticks.isEmpty()) {
                        ticks.remove(ticks.size() - 1);
                    }
                } catch (Exception e) {
                }
                super.drawRangeGridlines(g2, area, ticks);
            }

        };

        JFreeChart generatedChart = new JFreeChart(xyplot) {

        };
        xyplot.setOutlineVisible(false);
        LegendTitle legend = generatedChart.getLegend();
        legend.setVisible(false);
        xyplot.setForegroundAlpha(0.5F);

        xyplot.setBackgroundPaint(Color.WHITE);
        generatedChart.setBackgroundPaint(Color.WHITE);
        generatedChart.setPadding(new RectangleInsets(0, 0, 0, 0));
        return generatedChart;

    }

    /**
     * Converts the value from linear scale to log scale. The log scale numbers
     * are limited by the range of the type float. The linear scale numbers can
     * be any double value.
     *
     * @param linearValue the value to be converted to log scale
     * @param max The upper limit number for the input numbers
     * @param lowerLimit the lower limit for the input numbers
     * @return the value in log scale
     */
    private double scaleValues(double linearValue, int max, double lowerLimit) {
        double logMax = (Math.log(max) / Math.log(2));
        double logValue = (Math.log(linearValue + 1) / Math.log(2));
        logValue = (logValue * 2 / logMax) + lowerLimit;
        return logValue;
    }

    /**
     * Selection changed in the selection manager
     *
     * @param type type of selection
     */
    @Override
    public void selectionChanged(String type) {

        if (type.equalsIgnoreCase("comparisons_selection")) {
            lastselectedComponents.clear();
            this.selectedComparisonList = CSFPR_Central_Manager.getSelectedComparisonsList();
            if (selectedComparisonList.isEmpty()) {
                chartComponentLayout.removeAllComponents();
                chartImage.setSource(null);
                updateIcon(null);
                return;

            }

            updateChart();
            updateSelectionManager();

        }

    }

    /**
     * Update JFree chart based on new comparison selection
     *
     * @param type type of selection
     */
    private void updateChart() {
        chart = generateBubbleChart(selectedComparisonList);
        updateChartLayoutComponents(chart, width, height);
        DefaultXYZDataset emptyxyzdataset = new DefaultXYZDataset();
        DefaultXYZDataset dataset = ((DefaultXYZDataset) chart.getXYPlot().getDataset());
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            emptyxyzdataset.addSeries(dataset.getSeriesKey(i), new double[][]{{}, {}, {}});
        }
        chart.getXYPlot().setNoDataMessagePaint(Color.WHITE);
        chart.getXYPlot().setDataset(emptyxyzdataset);
        defaultImgURL = getChartImage(chart, width, height);

        chartImage.setSource(new ExternalResource(defaultImgURL));
        XYPlot xyplot = chart.getXYPlot();
        xyplot.getDomainAxis().setVisible(false);
        xyplot.getRangeAxis().setVisible(false);
        chart.setBorderVisible(true);
        chart.setBorderPaint(Color.LIGHT_GRAY);
        chart.getXYPlot().setDataset(dataset);
        thumbImgUrl = getChartImage(chart, 200, 200);

        xyplot.getDomainAxis().setVisible(true);
        xyplot.getRangeAxis().setVisible(true);
        chart.setBorderVisible(false);
        chart.setBorderPaint(Color.WHITE);
        xyplot.setForegroundAlpha(0.8F);

        updateIcon(thumbImgUrl);

    }

    /**
     * Convert JFree chart based into string url for image
     *
     * @param chart JFreechart
     * @param width generated image width
     * @param height generated image height
     * @return URL for generated image
     */
    private String getChartImage(JFreeChart chart, int width, int height) {
        if (chart == null) {
            return null;
        }

        String base64 = "";
        try {
            base64 = "data:image/png;base64," + Base64.encodeBase64String(ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo)));

        } catch (IOException ex) {
            System.err.println("at error " + this.getClass() + " line 536 " + ex.getLocalizedMessage());
        }
        return base64;

    }

    /**
     * Update Vaadin bubble components on the chart layout data container
     *
     * @param chart JFree chart object
     * @param width chart data width
     * @param height chart data heights
     */
    private void updateChartLayoutComponents(final JFreeChart chart, final double width, final double height) {
        chart.getXYPlot().setNoDataMessage((int) width + "," + (int) height);

        if (width < 1 || height < 1) {
            return;
        }
        try {
            ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
        } catch (IOException ex) {
            Logger.getLogger(DiseaseComparisonSelectionBubblechartComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        isNewImge = true;
        Set<BubbleComponent> set = new TreeSet<>();
        Set<BubbleComponent> updatedselectedComponents = new HashSet<>();
        chartComponentLayout.removeAllComponents();
        for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity) {
                XYItemEntity catEnt = (XYItemEntity) entity;
                BubbleComponent square = new BubbleComponent();
                square.setStyleName("bubblechart");
                String[] coords = catEnt.getShapeCoords().split(",");
                int smallX = Integer.MAX_VALUE;
                int largeX = Integer.MIN_VALUE;
                int smallY = Integer.MAX_VALUE;
                int largeY = Integer.MIN_VALUE;
                for (int x = 0; x < coords.length; x++) {
                    String coorX = coords[x++];
                    if (Integer.valueOf(coorX) < smallX) {
                        smallX = Integer.valueOf(coorX);
                    }
                    if (Integer.valueOf(coorX) > largeX) {
                        largeX = Integer.valueOf(coorX);
                    }

                    String coorY = coords[x];
                    if (Integer.valueOf(coorY) < smallY) {
                        smallY = Integer.valueOf(coorY);

                    }
                    if (Integer.valueOf(coorY) > largeY) {
                        largeY = Integer.valueOf(coorY);
                    }

                }
                int sqheight = (largeY - smallY);
                if (sqheight < 2) {
                    continue;
                } else if (sqheight < 14) {
                    smallY = smallY - (14 - sqheight);
                }

                int sqwidth = (largeX - smallX);
                int finalWidth;
                if (sqwidth < 20) {
                    finalWidth = 20;
                    smallX = smallX - ((finalWidth - sqwidth) / 2);

                } else {
                    finalWidth = sqwidth;
                }
                int finalHeight;

                if (sqheight < 20) {
                    finalHeight = 20;
                    if (sqheight < 14) {
                        smallY = smallY - (((finalHeight - sqheight) / 2) - (14 - sqheight));
                    } else {
                        smallY = smallY - ((finalHeight - sqheight) / 2);
                    }

                } else {
                    finalHeight = sqheight;
                }
                square.setWidth((finalWidth + 2), Unit.PIXELS);
                square.setHeight((finalHeight + 2), Unit.PIXELS);
                if (selectedComparisonList == null || selectedComparisonList.isEmpty()) {
                    return;
                }
                QuantDiseaseGroupsComparison comparison;
                if (userCustomizedComparison != null && catEnt.getSeriesIndex() == 0) {
                    continue;
                } else if (userCustomizedComparison != null && catEnt.getSeriesIndex() == 1) {
                    comparison = userCustomizedComparison;
                } else {
                    comparison = ((QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[catEnt.getSeriesIndex() - 1 - userDataCounter]);
                }

                String header = comparison.getComparisonHeader();
                int itemNumber = (int) ((XYItemEntity) entity).getDataset().getYValue(((XYItemEntity) entity).getSeriesIndex(), ((XYItemEntity) entity).getItem());
                String[] gr = comparison.getComparisonFullName().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
                String updatedHeader = ("Numerator: " + gr[0] + "<br/>Denominator: " + gr[1] + "<br/>Disease: " + comparison.getDiseaseCategory());
                square.addStyleName(trendStyles[itemNumber]);
                square.setDescription(updatedHeader + "<br/>Category: " + tooltipLabels[itemNumber] + "<br/>#Proteins: " + (int) tooltipsProtNumberMap.get(header)[itemNumber]);
                double categIndex = (double) itemNumber;
                int seriesIndex = ((XYItemEntity) entity).getSeriesIndex();
                square.setParam("seriesIndex", seriesIndex);
                square.setParam("categIndex", categIndex);

                if (!lastselectedComponents.isEmpty()) {
                    square.select(false);
                    for (BubbleComponent lastselectedComponent : lastselectedComponents) {
                        if (lastselectedComponent != null && categIndex == (Double) lastselectedComponent.getParam("categIndex") && seriesIndex == (Integer) lastselectedComponent.getParam("seriesIndex")) {
                            square.select(true);
                            updatedselectedComponents.add(square);
                            break;
                        }
                    }

                }
                square.setParam("position", "left: " + (smallX - 1) + "px; top: " + (smallY - 1) + "px;");
                square.setParam("proteinList", comparison.getProteinsByTrendMap().get((itemNumber - 1)));
                set.add(square);
            }

        }
        lastselectedComponents.clear();
        lastselectedComponents.addAll(updatedselectedComponents);
        set.stream().forEach((square) -> {
            chartComponentLayout.addComponent(square, square.getParam("position").toString());
        });

    }

    /**
     * Get registered listener id
     *
     * @return listener id
     */
    @Override
    public String getListenerId() {
        return "bubble_chart_listener";
    }

    /**
     * On chart click (selection from bubble or chart layout)
     *
     * @param event User selection event
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        BubbleComponent selectedComponent = (BubbleComponent) event.getClickedComponent();
        updateSelectionList(selectedComponent);
        updateSelectionManager();

    }

    /**
     * Update the thumb button icon
     *
     * @param imageUrl URL for image encoded as Base64 string
     */
    public abstract void updateIcon(String imageUrl);

    /**
     * Update the selection proteins list based on user selection on bubble
     * chart to update the chart layout and update the selection list that is
     * used for updating the selection manager
     *
     * @param imageUrl URL for image encoded as Base64 string
     */
    private void updateSelectionList(BubbleComponent selectedComponent) {
        if (selectedComponent == null) {
            lastselectedComponents.clear();
        } else if (this.allowMultiSelect) {
            if (lastselectedComponents.contains(selectedComponent)) {
                lastselectedComponents.remove(selectedComponent);
            } else {
                lastselectedComponents.add(selectedComponent);
            }

        } else {
            if (lastselectedComponents.contains(selectedComponent)) {
                lastselectedComponents.clear();
            } else {
                lastselectedComponents.clear();
                lastselectedComponents.add(selectedComponent);
            }
        }
        rePaintChart();

    }

    /**
     * Update the chart layout upon user selection on the bubble plot (highlight
     * or blur bubbles).
     */
    private void rePaintChart() {
        Iterator<Component> itr = chartComponentLayout.iterator();
        boolean selectAction = false;
        if (lastselectedComponents.isEmpty()) {
            selectAction = true;
        }

        while (itr.hasNext()) {
            Component component = itr.next();
            if (lastselectedComponents.contains((BubbleComponent) component)) {
                ((BubbleComponent) component).select(true);

            } else {
                ((BubbleComponent) component).select(selectAction);
            }

        }

    }

    /**
     * Update the selection manager using selection proteins list.
     */
    private void updateSelectionManager() {
        Set<QuantComparisonProtein> selectedProteinsList;
        if (lastselectedComponents.isEmpty()) {
            selectedProteinsList = null;

        } else {
            Map<String, QuantComparisonProtein> selectedProteinsMap = new LinkedHashMap<>();
            selectedProteinsList = new LinkedHashSet<>();
            lastselectedComponents.stream().forEach((component) -> {
                for (QuantComparisonProtein quantProt : (Set<QuantComparisonProtein>) component.getParam("proteinList")) {
                    if (userCustomizedComparison != null && userCustomizedComparison.getQuantComparisonProteinMap().containsKey(quantProt.getProteinAccession())) {
                        selectedProteinsMap.put(quantProt.getProteinAccession(), quantProt);
                    } else {
                        selectedProteinsMap.put(quantProt.getProteinAccession(), quantProt);
                    }
                }
                selectedProteinsList.addAll(selectedProteinsMap.values());
            });

        }

        CSFSelection selection = new CSFSelection("protein_selection", getListenerId(), selectedComparisonList, selectedProteinsList);
        CSFPR_Central_Manager.setSelection(selection);
    }

    /**
     * Create array of integers and initialize it with 0 values.
     */
    private Integer[] initValuesArr() {
        Integer[] values = new Integer[]{0, 0, 0, 0, 0, 0,0};
        return values;

    }

    /**
     * Add User Customized Comparison to the system (activating quant compare
     * mode)
     *
     * @param userCustomizedComparison user input data in quant compare mode
     */
    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.userCustomizedComparison = userCustomizedComparison;
        if (userCustomizedComparison == null) {
            userDataCounter = 0;
        } else {
            userDataCounter = 1;
        }

    }
}
