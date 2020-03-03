package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 * This class represents comparison column header button that allow the users to
 * Sort/filter the proteins table.
 *
 * @author Yehia Farag
 */
public abstract class ColumnHeaderLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main sorting button.
     */
    private final VerticalLayout sortingBtn;
    /**
     * The main filter button.
     */
    private final ColumnFilterPopupBtn filterBtn;
    /**
     * The button index (same as comparison index).
     */
    private final int index;

    private final QuantDiseaseGroupsComparison comparison;

    /**
     * Get the index of the comparison
     *
     * @return index of the comparison
     */
    public int getComparisonIndex() {
        return index;
    }

    /**
     * get the comparison object
     *
     * @return comparison object
     */
    public QuantDiseaseGroupsComparison getComparison() {
        return comparison;
    }

    /**
     * Constructor to initialise the main attributes.
     *
     * @param comparison The main quant comparison.
     * @param index The comparison index.
     */
    public ColumnHeaderLayout(QuantDiseaseGroupsComparison comparison, int index) {
        ColumnHeaderLayout.this.setWidth(15, Unit.PIXELS);
        ColumnHeaderLayout.this.setHeight(15, Unit.PIXELS);
        this.index = index;
        VerticalLayout headerFrame = new VerticalLayout();
        headerFrame.setHeight(100, Unit.PERCENTAGE);
        headerFrame.setWidthUndefined();
        ColumnHeaderLayout.this.addComponent(headerFrame);
        ColumnHeaderLayout.this.setComponentAlignment(headerFrame, Alignment.TOP_CENTER);
        this.comparison = comparison;

        filterBtn = new ColumnFilterPopupBtn((index == -1)) {
            @Override
            public void dropComparison() {
                ColumnHeaderLayout.this.dropComparison(comparison);
            }

            @Override
            public void filterTable(Set<Object> filtersSet, boolean unselectfilter) {
                ColumnHeaderLayout.this.filterTable(comparison, index, filtersSet, unselectfilter);
            }

            @Override
            public void filterTable(double min, double max, boolean unselectfilter) {
                ColumnHeaderLayout.this.filterTable(comparison, index, min, max, unselectfilter);
            }

            @Override
            public void filterTable(long minStudiesNumber, long maxStudiesNumber, boolean unselectfilter) {
                ColumnHeaderLayout.this.filterTable(comparison, index, minStudiesNumber, maxStudiesNumber, unselectfilter);
            }

            @Override
            public void filterTable(double min, double max, double secMin, double secMax, boolean unselectfilter) {
                ColumnHeaderLayout.this.filterTable(comparison, index, min, max, secMin, secMax, unselectfilter);
            }

        };

        filterBtn.setWidth(20, Unit.PIXELS);
        filterBtn.setHeight(100, Unit.PERCENTAGE);
        filterBtn.setStyleName(comparison.getDiseaseCategoryStyle());
        filterBtn.addStyleName("unselectedfilter");
        filterBtn.setVisible(false);
        String[] gr = comparison.getComparisonFullName().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
        String updatedHeader = ("Numerator: " + gr[0] + "<br/>Denominator: " + gr[1] + "<br/>Disease: " + comparison.getDiseaseCategory());
        filterBtn.setDescription(updatedHeader);
        headerFrame.addComponent(filterBtn);
        headerFrame.setComponentAlignment(filterBtn, Alignment.TOP_CENTER);

        sortingBtn = new VerticalLayout();
        sortingBtn.setWidth(20, Unit.PIXELS);
        sortingBtn.setHeight(100, Unit.PERCENTAGE);
        sortingBtn.setStyleName(comparison.getDiseaseCategoryStyle());
        sortingBtn.setDescription(updatedHeader);
        sortingBtn.addLayoutClickListener(ColumnHeaderLayout.this);
        sortingBtn.addStyleName("sortdown");
        sortingBtn.addStyleName("unselected");
        headerFrame.addComponent(sortingBtn);
        headerFrame.setComponentAlignment(sortingBtn, Alignment.TOP_CENTER);
        ColumnHeaderLayout.this.updateHeaderData(null, null, -1);

    }

    /**
     *
     * @param filteredData
     * @param comparison
     * @param filterIndex
     */
    public void updateHeaderData(Set<String> filteredData, QuantDiseaseGroupsComparison comparison, int filterIndex) {
        if (filteredData == null || comparison != this.comparison) {
            filterBtn.updateChartsData(initRangeData(filteredData), initStudeisNumberData(filteredData));
        } else {
            switch (filterIndex) {
                case 0:
                    filterBtn.updateChartsData(initRangeData(filteredData), initStudeisNumberData(filteredData));
                    filterBtn.applyChartChanges(true, true);
                    break;
                case 1:
                    filterBtn.updateChartsData(null, initStudeisNumberData(filteredData));
                    filterBtn.applyChartChanges(false, true);
                    break;
                case 2:
                    filterBtn.updateChartsData(initRangeData(filteredData), null);
                    filterBtn.applyChartChanges(true, false);
                    break;
            }

        }

    }

    private double[][] initRangeData(Set<String> filteredData) {
        Map<Double, Integer> dataMap = new TreeMap<>();
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (String key : comparison.getQuantComparisonProteinMap().keySet()) {
            if (filteredData != null && !filteredData.contains(key.split("_")[1])) {
                continue;
            }
            QuantComparisonProtein quant = comparison.getQuantComparisonProteinMap().get(key);
            double value = quant.getOverallCellPercentValue();
            if (!dataMap.containsKey(value)) {
                dataMap.put(value, 0);
            }
            dataMap.put(value, (dataMap.get(value) + 1));
            if (dataMap.get(value) >= max) {
                max = dataMap.get(value);
            }
            if (dataMap.get(value) <= min) {
                min = dataMap.get(value);
            }

        }
        double[][] data = new double[dataMap.size()][2];
        int i = 0;
        for (double key : dataMap.keySet()) {
            data[i] = new double[]{key, scaleValues((double) dataMap.get(key), max, min)};
            i++;
        }
        return data;
    }

    private double[][] initStudeisNumberData(Set<String> filteredData) {
        Map<Integer, Double> dataMap = new TreeMap<>();
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (String key : comparison.getQuantComparisonProteinMap().keySet()) {
            if (filteredData != null && !filteredData.contains(key.split("_")[1])) {
                continue;
            }
            QuantComparisonProtein quant = comparison.getQuantComparisonProteinMap().get(key);
            int value = quant.getDsQuantProteinsMap().size();
            if (!dataMap.containsKey(value)) {
                dataMap.put(value, 0.0);
            }
            dataMap.put(value, dataMap.get(value) + 1.0);
            if (dataMap.get(value) >= max) {
                max = dataMap.get(value);
            }
            if (dataMap.get(value) <= min) {
                min = dataMap.get(value);
            }

        }
        double[][] data = new double[dataMap.size()][2];
        int i = 0;
        for (int key : dataMap.keySet()) {
            data[i] = new double[]{key, scaleValues((double) dataMap.get(key), max, min)};
            i++;
        }
        return data;
    }

    /**
     * Converts the value from linear scale to log scale; The log scale numbers
     * are limited by the range of the type float; The linear scale numbers can
     * be any double value.
     *
     * @param linearValue the value to be converted to log scale
     * @param max The upper limit number for the input numbers
     * @param lowerLimit the lower limit for the input numbers
     * @return the value in log scale
     */
    private double scaleValues(double linearValue, double max, double lowerLimit) {
        double logMax = (Math.log(max) / Math.log(2));
        double logValue = (Math.log(linearValue + 1) / Math.log(2));
        logValue = (logValue * 2 / logMax) + lowerLimit;
        if (logValue == Double.POSITIVE_INFINITY) {
            return max;
        }
        return logValue;
    }

    /**
     * Sort ascending.
     */
    private Boolean sortedUp;

    /**
     * Reset the Sorting and Filtering buttons.
     */
    public void reset() {
        filterBtn.reset();
        filterBtn.setVisible(false);
        sortingBtn.setVisible(true);
        if (sortingBtn.getStyleName().contains("blinkII")) {
            sortingBtn.removeStyleName("blinkII");
            sortingBtn.addStyleName("blink");
        } else {
            sortingBtn.removeStyleName("blinkI");
            sortingBtn.addStyleName("blinkII");
        }

    }

    /**
     * Switch between Sorting / Filtering buttons.
     */
    public void swichBtns() {
        filterBtn.setVisible(!filterBtn.isVisible());
        sortingBtn.setVisible(!sortingBtn.isVisible());
        if (sortingBtn.getStyleName().contains("blinkII")) {
            sortingBtn.removeStyleName("blinkII");
            sortingBtn.addStyleName("blink");
        } else {
            sortingBtn.removeStyleName("blinkI");
            sortingBtn.addStyleName("blinkII");
        }

        if (filterBtn.getStyleName().contains("blinkII")) {
            filterBtn.removeStyleName("blinkII");
            filterBtn.addStyleName("blink");
        } else {
            filterBtn.removeStyleName("blinkI");
            filterBtn.addStyleName("blinkII");
        }

    }

    /**
     * Set this component as default sorting component.
     */
    public void setAsDefault() {
        sortingBtn.removeStyleName("unselected");
        sortedUp = false;
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (sortedUp == null || sortedUp) {
            sortingBtn.addStyleName("sortdown");
            sortedUp = false;

        } else if (!sortedUp) {
            sortingBtn.removeStyleName("sortdown");
            sortingBtn.addStyleName("sortup");
            sortedUp = true;
        }
        sortingBtn.removeStyleName("unselected");
        sort(sortedUp, index);
    }

    /**
     * Reset sorting layout.
     */
    public void noSort() {
        sortingBtn.addStyleName("sortdown");
        sortingBtn.removeStyleName("sortup");
        sortingBtn.addStyleName("unselected");
        sortedUp = null;
    }

    /**
     * Remove all applied filters and reset filters selection option.
     */
    public void noFilter() {
        filterBtn.reset();
        filterBtn.addStyleName("unselectedfilter");
        filterBtn.removeStyleName("selectedfilter");
    }

    /**
     * Sort protein table based on the selected comparison and the selected
     * direction.
     *
     * @param up Sort ascending.
     * @param index Comparison index
     */
    public abstract void sort(boolean up, int index);

    /**
     * Un-select this comparison.
     *
     * @param comparison Quant disease comparison
     */
    public abstract void dropComparison(QuantDiseaseGroupsComparison comparison);

    /**
     * Filter table based on selected filters.
     *
     * @param comparison The quant disease comparison.
     * @param comparisonIndex The comparison index.
     * @param filterSet The applied filters set.
     * @param appliedFilter the filter is applied
     */
    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filterSet, boolean appliedFilter);

    /**
     * Filter table based on range filters.
     *
     * @param comparison The quant disease comparison.
     * @param comparisonIndex The comparison index.
     * @param min minimum range
     * @param max maximum range
     * @param appliedFilter the filter applied
     */
    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, double min, double max, boolean appliedFilter);

    /**
     * Filter table based on range filters.
     *
     * @param comparison The quant disease comparison.
     * @param comparisonIndex The comparison index.
     ** @param min first minimum range
     * @param max first maximum range
     * @param secMin second minimum range
     * @param secMax second maximum range
     * @param appliedFilter the filter applied
     *
     */
    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, double min, double max, double secMin, double secMax, boolean appliedFilter);

    /**
     * Filter table based on range filters.
     *
     * @param comparison The quant disease comparison.
     * @param comparisonIndex index of the comparison
     *@param min minimum range
     * @param max maximum range
     * @param appliedFilter the filter applied
     */
    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, long min, long max, boolean appliedFilter);

}
