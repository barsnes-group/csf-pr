package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.absolutelayout.AbsoluteLayoutState;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 * This class represents protein trend line chart(spark line ) required for
 * quant protein table.
 *
 * @author Yehia Farag
 */
public abstract class ProteinTrendLayout extends AbsoluteLayout implements Comparable<ProteinTrendLayout>, LayoutEvents.LayoutClickListener {

    /**
     * Component protein key.
     */
    private final String proteinKey;
    /**
     * List of selected comparisons to be updated based on user selection for
     * comparisons across the system.
     */
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    /**
     * The main chart data container (VaadinLayout bubble container).
     */
    private AbsoluteLayout chartComponentsLayout;
    /**
     * This is counter used to check when to draw the chart.
     */
    private int initSparkline = 0;
    /**
     * The width of the component layout.
     */
    private final int width;
    /**
     * The protein value used for sorting the table.
     */
    private QuantComparisonProtein sortableProtein;
    /**
     * The chart expanding/minimizing button.
     */
    private final VerticalLayout maxMinBtn;
    /**
     * The chart container.
     */
    private final VerticalLayout sparkLineContainer;
    /**
     * The container table row item id.
     */
    private final Object itemId;
    /**
     * Customized user data trend based on user input data in quant comparison
     * layout.
     */
    private int custTrend = -1;
    /**
     * Chart is in the expanding mode.
     */
    private boolean expandChartMode = false;
    /**
     * Main component line-chart.
     */
    private LineChart sparkLine;
    /**
     * Main component sorting value.
     */
    private Double sortingValue = 0.0;

    /**
     * Get current Vaadin layout for the chart, this method is used to get the
     * location on x-access for different comparisons to set the top layout
     * sorting buttons location.
     *
     * @return chartComponentsLayout the chart components container layout.
     */
    public AbsoluteLayout getChartComponentsLayout() {
        return chartComponentsLayout;
    }

    /**
     * Get the chart component width.
     *
     * @return current chart width.
     */
    public int getChartWidth() {
        return width - 10;
    }

    /**
     * Get the line-chart component.
     *
     * @return sparkLine (the line chart component).
     */
    public LineChart getSparkLine() {
        return sparkLine;
    }

    /**
     * Set the main protein used for getting the sorting value for the
     * component.
     *
     * @param comparisonIndex the comparison index in the quant disease
     * comparison set.
     */
    public void setSortableColumnIndex(int comparisonIndex) {
        QuantDiseaseGroupsComparison comp = (QuantDiseaseGroupsComparison) selectedComparisonsList.toArray()[comparisonIndex];
        if (comp.getQuantComparisonProteinMap().containsKey("0_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("0_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("1_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("1_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("2_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("2_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("3_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("3_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("4_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("4_" + proteinKey);
        } else {
            sortableProtein = null;
        }
    }

    /**
     * Constructor to initialize the main attributes.
     *
     * @param selectedComparisonsList List of selected quant disease comparisons
     * @param selectedProtein The quant comparison main protein
     * @param width The available width for the component (protein comparison
     * column width).
     * @param itemId The row item id in the container table
     * @param draw draw the chart in the initializing state
     */
    public ProteinTrendLayout(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, QuantComparisonProtein selectedProtein, int width, Object itemId, boolean draw) {
        this.itemId = itemId;
        this.selectedComparisonsList = selectedComparisonsList;
        proteinKey = selectedProtein.getProteinAccession();
        QuantDiseaseGroupsComparison comp = (QuantDiseaseGroupsComparison) selectedComparisonsList.toArray()[selectedComparisonsList.size() - 1];
        if (comp.getQuantComparisonProteinMap().containsKey("0_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("0_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("1_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("1_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("2_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("2_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("3_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("3_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("4_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("4_" + proteinKey);
        }
        this.width = width;

        width = width - 10;
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(100, Unit.PIXELS);
        this.addStyleName("slowslide");
        this.setStyleName("proteintrendcell");

        sparkLineContainer = new VerticalLayout();
        sparkLineContainer.setSizeFull();
        this.addComponent(sparkLineContainer, "left: " + 0 + "px; top: " + 0 + "px;");
        sparkLineContainer.addLayoutClickListener(ProteinTrendLayout.this);

        VerticalLayout resizeIconLayout = new VerticalLayout();
        resizeIconLayout.setWidth(20, Unit.PERCENTAGE);
        resizeIconLayout.setHeight(20, Unit.PIXELS);
        this.addComponent(resizeIconLayout, "left: " + (width - 25) + "px; top: " + 0 + "px;");

        maxMinBtn = new VerticalLayout();
        maxMinBtn.setWidth(20, Unit.PIXELS);
        maxMinBtn.setHeight(20, Unit.PIXELS);
        maxMinBtn.setMargin(new MarginInfo(false, false, false, false));
        maxMinBtn.setStyleName("maxmizebtn");
        maxMinBtn.setDescription("Click to maximize");

        resizeIconLayout.addComponent(maxMinBtn);
        resizeIconLayout.setComponentAlignment(maxMinBtn, Alignment.TOP_RIGHT);
        maxMinBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            ProteinTrendLayout.this.expandChartLayout();
        });

        if (draw) {

            sparkLine = new LineChart(width, 500);
            sparkLine.updateData(selectedComparisonsList, proteinKey, custTrend);
            sparkLineContainer.addComponent(sparkLine);
            chartComponentsLayout = sparkLine.getChartComponentsLayout();
        }
        
    }

    /**
     * Set chart expanding mode to maximize.
     */
    public void expandChartLayout() {
        if (sparkLine == null) {
            return;
        }
        if (expandChartMode) {
            setHeight(100, Unit.PIXELS);
            sparkLine.minimize(false);
            maxMinBtn.setStyleName("maxmizebtn");
            expandChartMode = false;
        } else {
            maxMinBtn.setStyleName("minmizebtn");
            setHeight(500, Unit.PIXELS);
            sparkLine.maxmize();
            expandChartMode = true;

        }

    }

    /**
     * Add customized user trend if available (Quant comparison mode)
     *
     * @param userTrend Customized user input data (for the selected protein) in
     * case of quant comparison mode.
     */
    public void updateCustTrend(int userTrend) {
        if (sparkLine != null) {
            sparkLine.updateUrserTrend(userTrend);
        }
        this.custTrend = userTrend;

    }

    @Override
    protected AbsoluteLayoutState getState() {
        if (initSparkline == 6 && sparkLine == null) {
            initSparkline++;
            sparkLine = new LineChart(width, 500);
            sparkLine.updateData(selectedComparisonsList, proteinKey, custTrend);
            sparkLineContainer.addComponent(sparkLine);
        }
        initSparkline++;
        return super.getState();
    }

    /**
     * Override compareTo to allow different sorting value based on the sorting
     * comparison.
     *
     * @param combarableProteinTrendLayout The comparable component.
     * @return  comparison value 
     */
    @Override
    public int compareTo(ProteinTrendLayout combarableProteinTrendLayout) {
        Double v2;
        if (sortableProtein == null) {
            sortingValue = 0.0;
        } else {
            sortingValue = sortableProtein.getOverallCellPercentValue();
        }
        QuantComparisonProtein o = combarableProteinTrendLayout.sortableProtein;
        if (o == null) {
            v2 = 0.0;
        } else {
            v2 = combarableProteinTrendLayout.sortableProtein.getOverallCellPercentValue();
        }

        return (sortingValue).compareTo(v2);
    }

    /**
     * Select row in the table on click.
     *
     * @param event The user selection action.
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        selectTableItem(itemId);
    }

    /**
     * Select table item (row) in the containing table.
     *
     * @param itemId Row item id.
     */
    public abstract void selectTableItem(Object itemId);

}
