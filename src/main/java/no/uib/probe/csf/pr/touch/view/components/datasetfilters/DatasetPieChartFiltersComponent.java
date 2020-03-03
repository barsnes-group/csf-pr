package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PopupWindowFrameWithFunctionsBtns;

/**
 * This class represents the dataset filter container with the interactive
 * management system for different interactive pie-chart filters
 *
 * @author Yehia Farag
 */
public abstract class DatasetPieChartFiltersComponent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * Main pop-up window frame with buttons control.
     */
    private final PopupWindowFrameWithFunctionsBtns popupWindow;
    /**
     * Main pie-charts filter container.
     */
    private final GridLayout popupBody;
    /**
     * Map of filter title and map of sup-filters slices map.
     */
    private final Map<String, Map<Comparable, PieChartSlice>> fullFiltersData = new LinkedHashMap<>();
    /**
     * Map of filter title and pie-chart filter component.
     */
    private final Map<String, DatasetPieChartFilter> filtersSet = new LinkedHashMap<>();
    /**
     * Map current datasets indexes and boolean is active (or filtered).
     */
    private final Map<Integer, Boolean> activeDatasetMap;
    /**
     * Map of datasets indexes and dataset objects.
     */
    private Map<Integer, QuantDataset> quantDatasetMap;
    /**
     * Only one filter applied.
     */
    private boolean singlefilter;
    /**
     * Main pop-up window width .
     */
    private final int screenWidth = Math.min(Page.getCurrent().getBrowserWindowWidth(), 980);
    /**
     * Main pop-up window height .
     */
    private final int screenHeight = Math.max(Math.min(Page.getCurrent().getBrowserWindowHeight(), 800), 435);
    private final Image icon;
    private final ThemeResource defaultTheam = new ThemeResource("img/pie-chart.png");
    private final ThemeResource selectedTheam = new ThemeResource("img/pie-chart-selected.png");

    /**
     * Constructor to initialize the main attributes.
     */
    public DatasetPieChartFiltersComponent() {

        //init icon
        this.setStyleName("filterbtn");
        this.addStyleName("unselectedpiechart");
        icon = new Image();
        icon.setSource(defaultTheam);
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(DatasetPieChartFiltersComponent.this);
        this.setDescription("Dataset filters");

        popupBody = new GridLayout(4, 1000);

        HorizontalLayout btnsFrame = new HorizontalLayout();
        popupWindow = new PopupWindowFrameWithFunctionsBtns("Dataset Explorer", new VerticalLayout(popupBody), btnsFrame);
        popupWindow.setFrameWidth(screenWidth);
        popupWindow.setFrameHeight(screenHeight - 24);
        //init datasetructure
        activeDatasetMap = new HashMap<>();
        //init pie-chart filters
        this.initIntercativeDatasetFilters();
        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        btnsFrame.addComponent(leftsideWrapper);
        btnsFrame.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        leftsideWrapper.setSpacing(true);

        InformationButton info = new InformationButton("The provided filters allow for the selection of subsets of the available data for the currently selected disease categories. A selection in one chart will result in the other charts updating to show only the remaining options. Click \"Apply\" to use the filters.", true);
        leftsideWrapper.addComponent(info);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setWidthUndefined();
        btnLayout.setSpacing(true);
        btnsFrame.addComponent(btnLayout);
        btnsFrame.setComponentAlignment(btnLayout, Alignment.MIDDLE_RIGHT);

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener((Button.ClickEvent event) -> {
            Set<Integer> filteredSelectionSet = filterSelectionUnit();
            DatasetPieChartFiltersComponent.this.updateSystem(filteredSelectionSet);
            if (selectedDatasetNumbers.size() > 1) {
                applyFilterIcon();
            } else {
                resetFilterIcon();
            }
            popupWindow.view();

        });

        Button unselectAllBtn = new Button("Clear");
        unselectAllBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(unselectAllBtn);
        btnLayout.setComponentAlignment(unselectAllBtn, Alignment.TOP_LEFT);
        unselectAllBtn.setDescription("Clear all selections");
        unselectAllBtn.addClickListener((Button.ClickEvent event) -> {
            singlefilter = false;
            DatasetPieChartFiltersComponent.this.resetFilters();

        });

    }

    /**
     * Initialize the different interactive pie-chart filters
     */
    private void initIntercativeDatasetFilters() {

        int filterHeight = Math.max(((screenHeight - 200) / 2), 300);
        int filterWidth = filterHeight;

        int colNumber = Math.max(screenWidth / 310, 1);
        popupBody.setColumns(colNumber);
        popupBody.setHideEmptyRowsAndColumns(true);
        if (colNumber == 1) {
            popupWindow.setFrameWidth(370);

        }

        filtersSet.clear();
        DatasetPieChartFilter yearFilter = initPieChartFilter("Year", "yearFilter", 0, filterWidth, filterHeight);

        filtersSet.put("yearFilter", yearFilter);

        DatasetPieChartFilter studyTypeFilter = initPieChartFilter("Study Type", "studyTypeFilter", 1, filterWidth, filterHeight);

        filtersSet.put("studyTypeFilter", studyTypeFilter);

        DatasetPieChartFilter sampleMatchingFilter = initPieChartFilter("Sample Matching", "sampleMatchingFilter", 1, filterWidth, filterHeight);

        filtersSet.put("sampleMatchingFilter", sampleMatchingFilter);

        DatasetPieChartFilter technologyFilter = initPieChartFilter("Technology", "technologyFilter", 1, filterWidth, filterHeight);

        filtersSet.put("technologyFilter", technologyFilter);

        DatasetPieChartFilter analyticalApproachFilter = initPieChartFilter("Analytical Approach", "analyticalApproachFilter", 1, filterWidth, filterHeight);

        filtersSet.put("analyticalApproachFilter", analyticalApproachFilter);

        DatasetPieChartFilter shotgunTargetedFilter = initPieChartFilter("Shotgun/Targeted", "shotgunTargetedFilter", 1, filterWidth, filterHeight);

        filtersSet.put("shotgunTargetedFilter", shotgunTargetedFilter);
        int col = 0, row = 0;

        for (DatasetPieChartFilter filter : filtersSet.values()) {
            this.popupBody.addComponent(filter, col++, row);
            this.popupBody.setComponentAlignment(filter, Alignment.MIDDLE_CENTER);
            if (col == colNumber) {
                row++;
                col = 0;
            }
        }

    }

    /**
     * This method responsible for updating the main datasets across all
     * pie-charts the update happened mainly on changing disease category
     *
     * @param quantDatasetMap Map of quant datasets and its indexes
     */
    private void updateQuantDatasetMap(Map<Integer, QuantDataset> quantDatasetMap) {

        Set<Integer> finalSelectedIds = filterSelectionUnit();
        if (finalSelectedIds.size() == quantDatasetMap.size() && finalSelectedIds.containsAll(quantDatasetMap.keySet())) {
            return;
        }
        this.quantDatasetMap = quantDatasetMap;
        updatePieChartSliceSet();

    }

    /**
     * This method responsible for updating the pie-chart filters sub categories
     * (slices).
     */
    private void updatePieChartSliceSet() {
        fullFiltersData.clear();
        activeDatasetMap.clear();
        Map<Comparable, PieChartSlice> yearMap = new TreeMap<>(Collections.reverseOrder());
        Map<Comparable, PieChartSlice> studyTypeMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> sampleMatchingMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> technologyMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> analyticalApproachMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> shotgunTargetedMap = new TreeMap<>();

        quantDatasetMap.values().stream().map((dataset) -> {
            activeDatasetMap.put(dataset.getQuantDatasetIndex(), Boolean.TRUE);
            return dataset;
        }).map((dataset) -> {
            if (!yearMap.containsKey(dataset.getYear())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getYear());
                yearMap.put(dataset.getYear(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice yearSlice = yearMap.get(dataset.getYear());
            yearSlice.addDatasetId(dataset.getQuantDatasetIndex());
            yearMap.put(dataset.getYear(), yearSlice);
            return dataset;
        }).map((dataset) -> {
            if (!studyTypeMap.containsKey(dataset.getTypeOfStudy())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getTypeOfStudy());
                studyTypeMap.put(dataset.getTypeOfStudy(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice studyTypeSlice = studyTypeMap.get(dataset.getTypeOfStudy());
            studyTypeSlice.addDatasetId(dataset.getQuantDatasetIndex());
            studyTypeMap.put(dataset.getTypeOfStudy(), studyTypeSlice);
            return dataset;
        }).map((dataset) -> {
            if (!sampleMatchingMap.containsKey(dataset.getSampleMatching())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getSampleMatching());
                sampleMatchingMap.put(dataset.getSampleMatching(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice sampleMachingSlice = sampleMatchingMap.get(dataset.getSampleMatching());
            sampleMachingSlice.addDatasetId(dataset.getQuantDatasetIndex());
            sampleMatchingMap.put(dataset.getSampleMatching(), sampleMachingSlice);
            return dataset;
        }).map((dataset) -> {
            if (!technologyMap.containsKey(dataset.getTechnology())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getTechnology());
                technologyMap.put(dataset.getTechnology(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice technologySlice = technologyMap.get(dataset.getTechnology());
            technologySlice.addDatasetId(dataset.getQuantDatasetIndex());
            technologyMap.put(dataset.getTechnology(), technologySlice);
            return dataset;
        }).map((dataset) -> {
            if (!analyticalApproachMap.containsKey(dataset.getAnalyticalApproach())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getAnalyticalApproach());
                analyticalApproachMap.put(dataset.getAnalyticalApproach(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice analyticalApproachSlice = analyticalApproachMap.get(dataset.getAnalyticalApproach());
            analyticalApproachSlice.addDatasetId(dataset.getQuantDatasetIndex());
            analyticalApproachMap.put(dataset.getAnalyticalApproach(), analyticalApproachSlice);
            return dataset;
        }).map((dataset) -> {
            if (!shotgunTargetedMap.containsKey(dataset.getShotgunTargeted())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getShotgunTargeted());
                shotgunTargetedMap.put(dataset.getShotgunTargeted(), slice);
            }
            return dataset;
        }).forEach((dataset) -> {
            PieChartSlice shotgunTargetedSlice = shotgunTargetedMap.get(dataset.getShotgunTargeted());
            shotgunTargetedSlice.addDatasetId(dataset.getQuantDatasetIndex());
            shotgunTargetedMap.put(dataset.getShotgunTargeted(), shotgunTargetedSlice);
        });

        filtersSet.get("yearFilter").initializeFilterData(yearMap);
        fullFiltersData.put("yearFilter", yearMap);

        filtersSet.get("studyTypeFilter").initializeFilterData(studyTypeMap);
        fullFiltersData.put("studyTypeFilter", studyTypeMap);

        filtersSet.get("sampleMatchingFilter").initializeFilterData(sampleMatchingMap);
        fullFiltersData.put("sampleMatchingFilter", sampleMatchingMap);

        filtersSet.get("technologyFilter").initializeFilterData(technologyMap);
        fullFiltersData.put("technologyFilter", technologyMap);

        filtersSet.get("analyticalApproachFilter").initializeFilterData(analyticalApproachMap);
        fullFiltersData.put("analyticalApproachFilter", analyticalApproachMap);

        filtersSet.get("shotgunTargetedFilter").initializeFilterData(shotgunTargetedMap);
        fullFiltersData.put("shotgunTargetedFilter", shotgunTargetedMap);

    }

    /**
     * This method responsible for initializing each pie-chart filter
     *
     * @param title filter title
     * @param filterId filter id
     * @param index the filter index
     * @param width the filter width
     * @param height the filter height
     * @return initialized pie-chart filter
     */
    private DatasetPieChartFilter initPieChartFilter(String title, String filterId, int index, int width, int height) {
        DatasetPieChartFilter filter = new DatasetPieChartFilter(title, filterId, index, width, height) {
            @Override
            public void selectDatasets(boolean noselection) {
                Set<Integer> finalSelectionIds = filterSelectionUnit();
                if (noselection) {
                    updateFilters(finalSelectionIds, "");
                } else {
                    updateFilters(finalSelectionIds, filterId);
                }
            }
        };
        return filter;
    }

    /**
     * This method responsible for calculate datasets required for synchronizing
     * the different pie-chart filters.
     *
     * @return set of selected dataset indexes
     */
    private Set<Integer> filterSelectionUnit() {
        singlefilter = false;
        Map<Integer, Integer> dsCounter = new HashMap<>();
        int counter = 0;
        for (DatasetPieChartFilter filter : filtersSet.values()) {
            if (filter.isActiveFilter()) {
                counter++;
            }

            filter.getSelectedDsIds().stream().map((id) -> {
                if (!dsCounter.containsKey(id)) {
                    dsCounter.put(id, 0);
                }
                return id;
            }).forEach((id) -> {
                dsCounter.put(id, dsCounter.get(id) + 1);
            });

        }
        Set<Integer> finalSelectionIds = new HashSet<>();
        dsCounter.keySet().stream().filter((dsId) -> (dsCounter.get(dsId) == 6)).forEach((dsId) -> {
            finalSelectionIds.add(dsId);
        });
        if (counter == 1) {
            singlefilter = true;
        }

        return finalSelectionIds;

    }
    private final Set<Integer> selectedDatasetNumbers = new HashSet<>();

    /**
     * This method responsible for synchronize the different pie-chart filters.
     *
     * @param datasetIds Set of dataset indexes
     * @param filterId The applied filter id
     */
    private void updateFilters(Collection<Integer> datasetIds, String filterId) {
        selectedDatasetNumbers.clear();
        filtersSet.keySet().stream().map((keyFilterId) -> {
            DatasetPieChartFilter filter = filtersSet.get(keyFilterId);
            if (!keyFilterId.equalsIgnoreCase(filterId)) {
                filter.localUpdate(datasetIds, singlefilter);
            }
            return filter;
        }).forEachOrdered((filter) -> {
            selectedDatasetNumbers.add(filter.getSelectedDsIds().size());
        });

    }

    /**
     * Reset the pie-chart filters to its initial state.
     */
    public void resetFilters() {
        filtersSet.values().stream().forEach((filter) -> {
            filter.reset();
        });
        DatasetPieChartFiltersComponent.this.updateSystem(filterSelectionUnit());
        resetFilterIcon();
    }

    /**
     * Update the system with the current selected dataset ids
     *
     * @param selectedDatasetIds Set of dataset indexes
     */
    public abstract void updateSystem(Set<Integer> selectedDatasetIds);

    /**
     * Update the system with the current selected dataset ids
     *
     * @return get the set of filtered datasets
     */
    public abstract Map<Integer, QuantDataset> updatedDatasets();

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        this.updateQuantDatasetMap(this.updatedDatasets());

        popupWindow.view();
    }

    /**
     * Remove the highlight from filter button icon.
     */
    public void resetFilterIcon() {
        removeStyleName("appliedfilter");
        icon.setSource(defaultTheam);

    }

    /**
     * Highlight filter button icon.
     */
    private void applyFilterIcon() {
        addStyleName("appliedfilter");
        icon.setSource(selectedTheam);
    }

    /**
     * Check if filter applied
     *
     * @return filter is applied
     */
    public boolean isApplied() {
        return selectedDatasetNumbers.size() > 1;
    }

    /**
     * Check current selected filters to update the system
     *
     * @param quantDatasetToFilter Map of quant dataset mapped to datasets
     * indexes
     * @return finalSelectionIds set of dataset indexes
     */
    public Set<Integer> checkAndFilter(Map<Integer, QuantDataset> quantDatasetToFilter) {
        this.updateQuantDatasetMap(quantDatasetToFilter);
        filtersSet.values().stream().forEach((filter) -> {
            filter.reset();
        });
        return filterSelectionUnit();

    }

    /**
     * Update the main icon button for the filters based on the container size
     *
     * @param resizeFactor the resize factor to resize the filter component
     */
    public void resizeFilter(double resizeFactor) {
        this.setWidth((int) (25 * resizeFactor), Unit.PIXELS);
        this.setHeight(this.getWidth(), Unit.PIXELS);
    }
}
