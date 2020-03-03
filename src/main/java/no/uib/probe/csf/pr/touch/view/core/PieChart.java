package no.uib.probe.csf.pr.touch.view.core;

import com.itextpdf.text.pdf.codec.Base64;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * This class represents interactive pie-chart the chart developed using JFree
 * chart and DiVA concept
 *
 * @author Yehia Farag
 */
public abstract class PieChart extends AbsoluteLayout implements LayoutEvents.LayoutClickListener {

    /**
     * JFree Components.
     */
    /**
     * JFreeChart used to generate thumb image and default chart image
     * background.
     */
    private JFreeChart chart;
    /**
     * Chart rendering information that has the all information required for
     * drawing Vaadin pie-chart in the absolute layout.
     */
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    /**
     * Chart dataset.
     */
    private DefaultPieDataset dataset;
    /**
     * Selected slice AWT color.
     */
    private final Color selectedColor = Color.decode("#197de1");
    /**
     * Map of each category in the chart and its default color (for JFree chart
     * reset coloring)
     */
    private final Map<Comparable, Color> defaultKeyColorMap;
    /**
     * Map of category and selected slice color (required by JFree chart)
     */
    private final Map<Comparable, Color> selectedKeyColorMap;
    /**
     * The main pie-chart JFree plot(required by JFree chart)
     */
    private PiePlot plot;
    /**
     * The main chart background image (to be updated using JFreechart).
     */
    private final Image chartBackgroundImg;
    /**
     * Map of category and value (number of items)
     */
    private final Map<Comparable, String> valuesMap = new HashMap<>();
    /**
     * A wite layout that has the label and turn pie-chart into donut chart.
     */
    private final VerticalLayout middleDonutLayout;

    /**
     * The width of the chart.
     */
    private final int width;
    /**
     * The height of the chart.
     */
    private final int height;

    /**
     * The chart label contain the total number of datasets.
     */
    private final Label selectAllLabel;
    /**
     * The set of selected items
     */
    private final Set<String> selectionSet;

    /**
     * Constructor to initialize the main attributes
     *
     * @param filterWidth The width of the filter
     * @param filterHeight The height of the filter
     * @param title Filter title
     * @param interactive The filter is support selection.
     */
    public PieChart(int filterWidth, int filterHeight, String title, boolean interactive) {

        this.setWidth(filterWidth, Unit.PIXELS);
        this.setHeight(filterHeight, Unit.PIXELS);

        this.width = filterWidth;
        this.height = filterHeight;

        this.defaultKeyColorMap = new HashMap<>();
        this.selectedKeyColorMap = new HashMap<>();

        this.chartBackgroundImg = new Image();
        if (interactive) {
            this.addLayoutClickListener(PieChart.this);
        }
        this.addStyleName("pointer");
        chartBackgroundImg.setWidth(100, Unit.PERCENTAGE);
        chartBackgroundImg.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(chartBackgroundImg);

        middleDonutLayout = new VerticalLayout();
        middleDonutLayout.setWidth(100, Unit.PIXELS);
        middleDonutLayout.setHeight(100, Unit.PIXELS);
        middleDonutLayout.setStyleName("middledountchart");

        selectAllLabel = new Label();
        selectAllLabel.setWidth(100, Unit.PERCENTAGE);
        selectAllLabel.setStyleName(ValoTheme.LABEL_TINY);
        selectAllLabel.addStyleName(ValoTheme.LABEL_SMALL);
        middleDonutLayout.addComponent(selectAllLabel);
        middleDonutLayout.setComponentAlignment(selectAllLabel, Alignment.MIDDLE_CENTER);

        this.initPieChart(title);
        selectionSet = new LinkedHashSet<>();

    }

    /**
     * Get the middle white layout (the middle donut component).
     *
     * @return the label container layout
     */
    public VerticalLayout getMiddleDonutLayout() {
        return middleDonutLayout;
    }

    /**
     * Initialize the main JFree chart component.
     *
     * @param title The filter title.
     */
    private void initPieChart(String title) {
        dataset = new DefaultPieDataset();

        plot = new PiePlot(dataset);
        plot.setNoDataMessage("");
        plot.setCircular(true);
        plot.setLabelGap(0);
        plot.setLabelFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        plot.setLabelGenerator(new PieSectionLabelGenerator() {

            @Override
            public String generateSectionLabel(PieDataset pd, Comparable cmprbl) {
                return valuesMap.get(cmprbl);
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset pd, Comparable cmprbl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        plot.setSimpleLabels(false);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelPaint(Color.GRAY);
        plot.setLabelOutlinePaint(null);
        plot.setLabelLinksVisible(true);
        plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
        plot.setLabelLinkMargin(0);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setInteriorGap(0);
        plot.setShadowPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(1.2f));
        plot.setInteriorGap(0.05);
        plot.setIgnoreZeroValues(true);

        chart = new JFreeChart(plot);
        chart.setTitle(new TextTitle(title, new Font("Helvetica Neue", Font.PLAIN, 13)));
        chart.setBorderPaint(null);
        chart.setBackgroundPaint(null);
        chart.getLegend().setVisible(false);
        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.getLegend().setItemFont(new Font("Helvetica Neue", Font.PLAIN, 12));

    }

    /**
     * Convert JFree chart into image and encode it as base64 string to be used
     * as image link.
     *
     * @param chart JFree chart instance
     * @param width Image width
     * @param height Image height.
     */
    private String saveToFile(final JFreeChart chart, double width, double height) {
        byte imageData[];
        try {
            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBytes(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";
    }

    /**
     * This method is responsible for initializing chart dataset.
     *
     * @param items The items labels
     * @param values The items values
     * @param colors The items AWT colors
     */
    public void initializeFilterData(String[] items, Integer[] values, Color[] colors) {
        
        this.defaultKeyColorMap.clear();
        this.selectedKeyColorMap.clear();
        dataset.clear();
        if (values == null) {
            return;
        }
        for (int x = 0; x < items.length; x++) {
            if (values.length <= x || values[x] == null || values[x] == 0) {
                continue;
            }
            dataset.setValue(items[x], new Double(values[x]));
            valuesMap.put(items[x], values[x] + "");
            if (colors != null) {
                plot.setSectionPaint(items[x], colors[x]);
                this.selectedKeyColorMap.put(items[x], colors[x].darker());
                this.defaultKeyColorMap.put(items[x], colors[x]);
            }
        }
        if (values.length > 0) {
            selectAllLabel.setValue(values[values.length - 1] + "");
        } else {
            selectAllLabel.setValue(0 + "");
        }
    }

    /**
     * Get value for the input item label.
     *
     * @param key Item label
     * @return the item value.
     */
    public int getValue(String key) {
        if (valuesMap.containsKey(key)) {
            return Integer.valueOf(valuesMap.get(key));
        } else {
            return 0;
        }
    }

    /**
     *Re-draw chart image and Vaadin components.
     */
    public void redrawChart() {
        String imgUrl = saveToFile(chart, width, height);
        this.chartBackgroundImg.setSource(new ExternalResource(imgUrl));
        this.removeComponent(middleDonutLayout);
        this.addComponent(middleDonutLayout, "left: " + ((chartRenderingInfo.getPlotInfo().getDataArea().getCenterX()) - 50) + "px; top: " + (chartRenderingInfo.getPlotInfo().getDataArea().getCenterY() - 50) + "px");

    }

    /**
     * On chart click (selection on the pie-chart layout).
     *
     * @param event user click action
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (!chartRenderingInfo.getPlotInfo().getDataArea().contains(event.getRelativeX(), event.getRelativeY())) {
            return;
        }
        Comparable sliceName = "all";
        if (event.getClickedComponent() == null || event.getClickedComponent() instanceof Image) {
            ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(event.getRelativeX(), event.getRelativeY());
            if (entity instanceof PieSectionEntity) {
                PieSectionEntity pieEnt = (PieSectionEntity) entity;
                selectSlice(pieEnt.getSectionKey());
                sliceName = pieEnt.getSectionKey();
            }

        } else if (event.getClickedComponent() instanceof VerticalLayout || event.getClickedComponent() instanceof Label) {
            selectSlice("all");

        }

        sliceClicked(sliceName);
    }

    /**
     * Select slice action.
     *
     * @param sliceKey selected slice key.
     */
    public void selectSlice(Comparable sliceKey) {

        if (selectionSet.contains("all")) {
            selectionSet.clear();
        } else if (sliceKey.toString().equalsIgnoreCase("all")) {

            if (middleDonutLayout.getStyleName().contains("selected")) {
                middleDonutLayout.removeStyleName("selected");
                selectionSet.remove("all");
            } else {
                middleDonutLayout.addStyleName("selected");
                selectionSet.clear();
                selectionSet.add("all");
            }
        } else if (plot.getSectionOutlinePaint(sliceKey) == selectedColor) {
            plot.setSectionOutlinePaint(sliceKey, null);
            plot.setSectionPaint(sliceKey, defaultKeyColorMap.get(sliceKey));
            selectionSet.remove(sliceKey.toString());
        } else {
            plot.setSectionPaint(sliceKey, selectedKeyColorMap.get(sliceKey));
            plot.setSectionOutlinePaint(sliceKey, selectedColor);
//            plot.setSectionOutlineStroke(sliceKey, stroke);
            selectionSet.add(sliceKey.toString());
        }
        if (selectionSet.contains("all") || selectionSet.size() == valuesMap.size() || (!selectionSet.isEmpty() && valuesMap.size() == 1)) {
            middleDonutLayout.addStyleName("selected");
            selectionSet.clear();
            selectionSet.add("all");
            valuesMap.keySet().stream().map((keys) -> {
                plot.setSectionOutlinePaint(keys, selectedColor);
                return keys;
            }).forEach((keys) -> {
                plot.setSectionPaint(keys, selectedKeyColorMap.get(keys));
            });
        }
        if (selectionSet.isEmpty()) {
            resetChart();
        } else {
            redrawChart();
        }

    }

    /**
     * Get set of the selected slice items.
     *
     * @return set of selected items labels.
     */
    public Set<String> getSelectionSet() {
        if ((!selectionSet.isEmpty() && valuesMap.size() == 1)) {
            selectionSet.clear();
            selectionSet.add(valuesMap.keySet().iterator().next().toString());
        }
        return selectionSet;
    }

    /**
     * Reset the chart to initial state.
     */
    public void resetChart() {
        middleDonutLayout.removeStyleName("selected");
        valuesMap.keySet().stream().map((keys) -> {
            plot.setSectionOutlinePaint(keys, null);
            return keys;
        }).forEach((keys) -> {
            plot.setSectionPaint(keys, defaultKeyColorMap.get(keys));
        });
        redrawChart();
    }

    /**
     * Select slice action.
     *
     * @param sliceKey selected slice key
     */
    public abstract void sliceClicked(Comparable sliceKey);

}
