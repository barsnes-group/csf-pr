package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFSelection;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFiltersComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.PooledSamplesFilter;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.RecombineDiseaseGroupsCombonent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.ReorderSelectGroupsComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.SerumCsfFilter;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatMapLayout;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatmapFiltersContainerResizeControl;
import no.uib.probe.csf.pr.touch.view.core.ResizableTextLabel;

/**
 * This class represent the disease comparisons heat map component the class
 * allow users to select different disease comparisons
 *
 * @author Yehia Farag
 *
 */
public abstract class DiseaseComparisonHeatmapComponent extends VerticalLayout implements CSFListener {

    /**
     * The main heat map layout container.
     */
    private final HeatMapLayout heatmapLayoutContainer;
    /**
     * The wrapper of dataset pie-chart filters component (represented as button
     * with pop up layout).
     */
    private final DatasetPieChartFiltersComponent datasetPieChartFiltersComponent;
    /**
     * The wrapper of dataset recombine disease groups component (represented as
     * button with pop up layout).
     */
    private final RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersComponent;
    /**
     * The wrapper of Serum/CSF datasets filter component (represented as 2
     * buttons).
     */
    private final SerumCsfFilter serumCsfFilter;
    /**
     * The pool sampling dataset filter button.
     */
    private final PooledSamplesFilter poolFilterBtn;
    /**
     * The wrapper of dataset Select/Re-order disease groups component
     * (represented as button with pop up layout).
     */
    private final ReorderSelectGroupsComponent reorderSelectComponent;
    /**
     * The wrapper of dataset counter label to show the number of current
     * datasets represented in the heat-map.
     */
    private final ResizableTextLabel datasetCounterLabel;
    /**
     * The disease comparison heat-map right side control buttons container.
     */
    private final VerticalLayout heatmapToolsContainer;
    /**
     * The set of heat-map headers cell information objects required for
     * initialize the row headers.
     */
    private final LinkedHashSet<HeatMapHeaderCellInformationBean> rowheadersSet;
    /**
     * The set of heat-map disease category names set.
     */
    private final LinkedHashSet<String> fullDiseaseCategoryNameSet;
    /**
     * The set of heat-map headers cell information objects required for
     * initialize the column headers.
     */
    private final LinkedHashSet<HeatMapHeaderCellInformationBean> colheadersSet;
    /**
     * The set of disease group comparison that has all comparison information.
     */
    private final Set<DiseaseGroupComparison> patientsGroupComparisonsSet;
    /**
     * The map of full quant dataset map no filters applied.
     */
    private final Map<Integer, QuantDataset> fullQuantDsMap;
    /**
     * The map of quant datasets after different filters applied.
     */
    private final Map<Integer, QuantDataset> filteredQuantDsMap;
    /**
     * The quantitative data handler to work as controller layer to interact
     * between visualization and logic layer.
     */
    private final Data_Handler Data_handler;
    /**
     * The central selection manager for handling data across different
     * visualizations and managing all users selections.
     */
    private final CSFPR_Central_Manager CSFPR_Central_Manager;

    /**
     * Constructor to initialize the main attributes (Data handler, and
     * selection manage ..etc)
     *
     * @param CSFPR_Central_Manager The central selection manager
     * @param Data_Handler The quantitative data handler
     * @param diseaseCategorySet Set of disease category names
     * @param mainbodyLayoutWidth main body layout width (the container)
     * @param mainbodyLayoutHeight main body layout height (the container)
     * @param activeColumnHeaders boolean array of active columns for dataset
     * table export
     */
    public DiseaseComparisonHeatmapComponent(final CSFPR_Central_Manager CSFPR_Central_Manager, Data_Handler Data_Handler, Collection<DiseaseCategoryObject> diseaseCategorySet, int mainbodyLayoutWidth, int mainbodyLayoutHeight, boolean[] activeColumnHeaders) {

        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.Data_handler = Data_Handler;

        DiseaseComparisonHeatmapComponent.this.setWidth(mainbodyLayoutWidth, Unit.PIXELS);
        DiseaseComparisonHeatmapComponent.this.setHeight(mainbodyLayoutHeight, Unit.PIXELS);

        //init data structure
        this.rowheadersSet = new LinkedHashSet<>();
        this.colheadersSet = new LinkedHashSet<>();
        this.filteredQuantDsMap = new LinkedHashMap<>();
        this.fullQuantDsMap = new LinkedHashMap<>();
        this.patientsGroupComparisonsSet = new LinkedHashSet<>();
        this.fullDiseaseCategoryNameSet = new LinkedHashSet<>();

        VerticalLayout bodyLayoutWrapper = new VerticalLayout();
        bodyLayoutWrapper.setWidth(100, Unit.PERCENTAGE);
        bodyLayoutWrapper.setHeight(100, Unit.PERCENTAGE);

        DiseaseComparisonHeatmapComponent.this.addComponent(bodyLayoutWrapper);
        DiseaseComparisonHeatmapComponent.this.setComponentAlignment(bodyLayoutWrapper, Alignment.TOP_LEFT);

        GridLayout btnsWrapper = new GridLayout(3, 3);
        btnsWrapper.setColumnExpandRatio(0, 25);
        btnsWrapper.setColumnExpandRatio(1, 50);
        btnsWrapper.setColumnExpandRatio(2, 25);
        btnsWrapper.setSpacing(false);

        datasetPieChartFiltersComponent = new DatasetPieChartFiltersComponent() {
            @Override
            public void updateSystem(Set<Integer> selectedDatasetIds) {
                updateSystemComponents(selectedDatasetIds, null, false, true);
            }

            @Override
            public Map<Integer, QuantDataset> updatedDatasets() {
                if (CSFPR_Central_Manager.getQuantSearchSelection() != null) {
                    Map<Integer, QuantDataset> tempFilterMap = new LinkedHashMap<>();
                    filteredQuantDsMap.keySet().stream().filter((id) -> (heatmapLayoutContainer.getCurrentDsIds().contains(id))).forEachOrdered((id) -> {
                        tempFilterMap.put(id, filteredQuantDsMap.get(id));
                    });
                    return tempFilterMap;
                }
                return filteredQuantDsMap;
            }

        };
        btnsWrapper.addComponent(datasetPieChartFiltersComponent, 0, 1);
        btnsWrapper.setComponentAlignment(datasetPieChartFiltersComponent, Alignment.MIDDLE_LEFT);

        reconbineDiseaseGroupsFiltersComponent = new RecombineDiseaseGroupsCombonent(diseaseCategorySet) {

            @Override
            public void updateSystem(Map<String, Map<String, String>> updatedGroupsNamesMap) {
                updateCombinedGroups(updatedGroupsNamesMap);
                reorderSelectComponent.initialiseData(rowheadersSet, colheadersSet, patientsGroupComparisonsSet);
            }

        };
        HorizontalLayout bottomBtnContainer = new HorizontalLayout();
        bottomBtnContainer.setSpacing(false);

        bottomBtnContainer.addComponent(reconbineDiseaseGroupsFiltersComponent);
        bottomBtnContainer.setComponentAlignment(reconbineDiseaseGroupsFiltersComponent, Alignment.TOP_LEFT);

        reorderSelectComponent = new ReorderSelectGroupsComponent() {

            @Override
            public void updateSystem(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders, boolean isSelectionMode) {
                if (isSelectionMode) {
//                    update heatmap / hide show datasets
                } else {
                    datasetPieChartFiltersComponent.resetFilters();
                    heatmapLayoutContainer.reorderHeatmap(rowHeaders, colHeaders);
                }

            }

        };
        bottomBtnContainer.addComponent(reorderSelectComponent);
        bottomBtnContainer.setComponentAlignment(reorderSelectComponent, Alignment.TOP_LEFT);

        btnsWrapper.addComponent(bottomBtnContainer, 1, 2);
        btnsWrapper.setComponentAlignment(bottomBtnContainer, Alignment.TOP_CENTER);

        serumCsfFilter = new SerumCsfFilter() {
            boolean init = false;

            @Override
            public void updateSystem(boolean serumApplied, boolean csfApplied) {
                if (!init) {
                    init = true;
                    return;
                }
                reDrawHeatMap();
            }
        };

        btnsWrapper.addComponent(serumCsfFilter, 1, 0);
        btnsWrapper.setComponentAlignment(serumCsfFilter, Alignment.BOTTOM_CENTER);
        poolFilterBtn = new PooledSamplesFilter() {
            boolean init = false;

            @Override
            public void updateSystem(boolean hidePoolSamplesDatasets) {

                if (!init) {
                    init = true;
                    return;
                }
                reDrawHeatMap();
            }

        };
        btnsWrapper.addComponent(poolFilterBtn, 2, 1);
        btnsWrapper.setComponentAlignment(poolFilterBtn, Alignment.MIDDLE_RIGHT);

        datasetCounterLabel = new ResizableTextLabel();
        datasetCounterLabel.setDescription("#Datasets");

        btnsWrapper.addComponent(datasetCounterLabel, 1, 1);
        btnsWrapper.setComponentAlignment(datasetCounterLabel, Alignment.MIDDLE_CENTER);
        datasetCounterLabel.setStyleName("filterbtn");
        datasetCounterLabel.addStyleName("defaultcursor");

        HeatmapFiltersContainerResizeControl filterSizeController = new HeatmapFiltersContainerResizeControl(btnsWrapper, datasetPieChartFiltersComponent, reconbineDiseaseGroupsFiltersComponent, reorderSelectComponent, bottomBtnContainer, serumCsfFilter, poolFilterBtn, datasetCounterLabel);
        int availableHMHeight = mainbodyLayoutHeight;

        heatmapLayoutContainer = new HeatMapLayout(mainbodyLayoutWidth, availableHMHeight, activeColumnHeaders, filterSizeController, CSFPR_Central_Manager.getFullPublicationList()) {
            @Override
            public void updateSelectionManager(Set<QuantDiseaseGroupsComparison> selectedQuantComparisonsList) {

                if (selectedQuantComparisonsList != null && !selectedQuantComparisonsList.isEmpty()) {
                    while (true) {
                        Data_Handler.updateComparisonQuantProteins(selectedQuantComparisonsList);
                        boolean valid = true;
                        for (QuantDiseaseGroupsComparison comparsion : selectedQuantComparisonsList) {
                            if (comparsion.getQuantComparisonProteinMap() == null) {
                                valid = false;
                                break;
                            }

                        }
                        if (valid) {
                            break;
                        }
                    }

                }

                DiseaseComparisonHeatmapComponent.this.selfselection = true;
                CSFSelection selection = new CSFSelection("comparisons_selection", getListenerId(), selectedQuantComparisonsList, null);
                CSFPR_Central_Manager.setSelection(selection);
            }

            @Override
            public void updateHMThumb(String imgUrl, int datasetNumber, int deactivated, Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap, String selectedDiseaseCategory, boolean expandCollapsAction) {
                this.unselectAll();
                datasetCounterLabel.setValue(datasetNumber + "/" + fullQuantDsMap.size());
                if (deactivated > 0) {
                    datasetCounterLabel.setDescription("#Datasets<br/>#Not active datasets: " + deactivated);
                } else {
                    datasetCounterLabel.setDescription("#Datasets");
                }
                if (expandCollapsAction) {
                    filteredQuantDsMap.clear();
                    for (int dsId : heatmapLayoutContainer.getCurrentDsIds()) {
                        filteredQuantDsMap.put(dsId, fullQuantDsMap.get(dsId));
                    }
                }
                DiseaseComparisonHeatmapComponent.this.updateIcon(imgUrl, datasetNumber, selectedDiseaseCategory, expandCollapsAction);
                if (expandCollapsAction) {
                    datasetPieChartFiltersComponent.resetFilterIcon();
                }

                CSFPR_Central_Manager.setEqualComparisonMap(equalComparisonMap);
                reorderSelectComponent.updateSelectedDiseaseCategory(heatmapLayoutContainer.getUpdatedExpandedList());
            }

        };
        bodyLayoutWrapper.addComponent(heatmapLayoutContainer);
        bodyLayoutWrapper.setComponentAlignment(heatmapLayoutContainer, Alignment.MIDDLE_CENTER);

        this.heatmapToolsContainer = heatmapLayoutContainer.getHeatmapToolsContainer();
        heatmapLayoutContainer.getClearFilterBtn().addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            filteredQuantDsMap.clear();
            filteredQuantDsMap.putAll(fullQuantDsMap);
//            heatmapLayoutContainer.initialiseHeatMapData(fullDiseaseCategoryNameSet, rowheadersSet, colheadersSet, patientsGroupComparisonsSet, fullQuantDsMap);
            serumCsfFilter.resetFilter();
            poolFilterBtn.resetFilter();
        });
        CSFPR_Central_Manager.registerListener(DiseaseComparisonHeatmapComponent.this);

    }

    /**
     * Update the heat map layout
     *
     * @param rowheaders The set of heat-map headers cell information objects
     * required for initialize the row headers
     * @param colheaders The set of heat-map headers cell information objects
     * required for initialize the column headers
     * @param patientsGroupComparisonsSet The set of disease group comparison
     * that has all comparison information
     * @param fullQuantDsMap The map of full quant dataset map no filters
     * applied
     */
    public void updateData(LinkedHashMap<String, HeatMapHeaderCellInformationBean> rowheaders, LinkedHashMap<String, HeatMapHeaderCellInformationBean> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDataset> fullQuantDsMap) {

        int waiting = fullQuantDsMap.size() * 10;
        try {
            Thread.sleep(waiting);
        } catch (InterruptedException e) {
        }
        this.fullQuantDsMap.clear();
        this.fullQuantDsMap.putAll(fullQuantDsMap);

        this.rowheadersSet.clear();
        this.rowheadersSet.addAll(rowheaders.values());
        this.colheadersSet.clear();
        this.colheadersSet.addAll(colheaders.values());
        this.patientsGroupComparisonsSet.clear();
        this.fullDiseaseCategoryNameSet.clear();
        this.patientsGroupComparisonsSet.addAll(patientsGroupComparisonsSet);
        this.colheadersSet.forEach((hmheaderInfo) -> {
            fullDiseaseCategoryNameSet.add(hmheaderInfo.getDiseaseCategory());
        });
        this.filteredQuantDsMap.clear();
        this.filteredQuantDsMap.putAll(fullQuantDsMap);
        serumCsfFilter.resetFilter();
        poolFilterBtn.resetFilter();
        reorderSelectComponent.initialiseData(rowheadersSet, colheadersSet, patientsGroupComparisonsSet);
        heatmapLayoutContainer.initialiseHeatMapData(fullDiseaseCategoryNameSet, rowheadersSet, colheadersSet, patientsGroupComparisonsSet, fullQuantDsMap);

    }

    /**
     * Invoke search results mode to update (expand/collapse) disease category
     * heat-map
     *
     * @param datasetIds data-set ids for search results 
     * @param diseaseCategories set of selected disease categories
     */
    public void invokeSearchMode(Set<Integer> datasetIds, Set<String> diseaseCategories) {
        // update piechart filters
        updateSystemComponents(datasetIds, diseaseCategories, false, false);
//        Map<Integer, QuantDataset> updatedDsIds = unitFilteringDataset();
//        updateSystemComponents(updatedDsIds.keySet(), diseaseCategories, true, false);
//        heatmapLayoutContainer.selectDiseaseCategory(diseaseCategories, false, false);
//        Map<Integer, QuantDataset> heatMapQuantDsMap = new LinkedHashMap<>();
//        for (int dsId : heatmapLayoutContainer.getCurrentDsIds()) {
//            heatMapQuantDsMap.put(dsId, fullQuantDsMap.get(dsId));
//        }
//        datasetPieChartFiltersComponent.checkAndFilter(heatMapQuantDsMap);
//        reorderSelectComponent.updateSelectedDiseaseCategory(diseaseCategories);

    }

    /**
     * Select disease categories to update (expand/collapse) disease category
     * heat-map
     *
     * @param diseaseCategories set of selected disease categories
     */
    public void selectDiseaseCategory(Set<String> diseaseCategories) {
        // update piechart filters
        Map<Integer, QuantDataset> updatedDsIds = unitFilteringDataset();
        updateSystemComponents(updatedDsIds.keySet(), diseaseCategories, true, false);
        heatmapLayoutContainer.selectDiseaseCategory(diseaseCategories, false, false);
        Map<Integer, QuantDataset> heatMapQuantDsMap = new LinkedHashMap<>();
        for (int dsId : heatmapLayoutContainer.getCurrentDsIds()) {
            heatMapQuantDsMap.put(dsId, fullQuantDsMap.get(dsId));
        }
        datasetPieChartFiltersComponent.checkAndFilter(heatMapQuantDsMap);
        reorderSelectComponent.updateSelectedDiseaseCategory(diseaseCategories);

    }

    /**
     * Update the CSF-PR system based on user selection
     *
     * @param datasetIds selected dataset indexes
     */
    private void updateSystemComponents(Set<Integer> datasetIds, Set<String> diseaseCategories, boolean clearFilterList, boolean pieChartFilterAction) {
        if (filteredQuantDsMap.size() == datasetIds.size() && filteredQuantDsMap.keySet().containsAll(datasetIds)) {
            return;
        }
        filteredQuantDsMap.clear();
        if (clearFilterList) {
            fullQuantDsMap.keySet().stream().filter((datasetId) -> (datasetIds.contains(datasetId))).forEach((datasetId) -> {
                if (diseaseCategories != null && (diseaseCategories.contains(fullQuantDsMap.get(datasetId).getDiseaseCategoryI()) || diseaseCategories.contains(fullQuantDsMap.get(datasetId).getDiseaseCategoryII()))) {
                    filteredQuantDsMap.put(datasetId, fullQuantDsMap.get(datasetId));
                }
            });
        } else {
            fullQuantDsMap.keySet().stream().filter((datasetId) -> (datasetIds.contains(datasetId))).forEach((datasetId) -> {
                filteredQuantDsMap.put(datasetId, fullQuantDsMap.get(datasetId));
            });
        }

        Set<HeatMapHeaderCellInformationBean> localRows = new LinkedHashSet<>(), localColumns = new LinkedHashSet<>();
        Set<DiseaseGroupComparison> filteredPatientsGroupComparisonsSet = new LinkedHashSet<>();

        for (HeatMapHeaderCellInformationBean rowHeader : rowheadersSet) {
            for (int key : filteredQuantDsMap.keySet()) {
                QuantDataset dataset = filteredQuantDsMap.get(key);
                if ((dataset.getActiveDiseaseSubGroupI().equalsIgnoreCase(rowHeader.getDiseaseGroupName()) || dataset.getActiveDiseaseSubGroupII().equalsIgnoreCase(rowHeader.getDiseaseGroupName())) && (rowHeader.getDiseaseCategory().equalsIgnoreCase(dataset.getDiseaseCategoryI()))) {
                    localRows.add(rowHeader);

                }
            }

        }

        colheadersSet.stream().forEach((rowHeader) -> {
            for (Iterator<Integer> it = filteredQuantDsMap.keySet().iterator(); it.hasNext();) {
                int key = it.next();
                QuantDataset dataset = filteredQuantDsMap.get(key);
                if (((dataset.getActiveDiseaseSubGroupI().equalsIgnoreCase(rowHeader.getDiseaseGroupName()) || dataset.getActiveDiseaseSubGroupII().equalsIgnoreCase(rowHeader.getDiseaseGroupName()))) && (rowHeader.getDiseaseCategory().equalsIgnoreCase(dataset.getDiseaseCategoryI()))) {
                    localColumns.add(rowHeader);

                }
            }
        });
        Map<Integer, QuantDataset> tempFilteredHeatmapDatasetMap;
        Set<String> finalDiseaseCategories;
        if (pieChartFilterAction && datasetPieChartFiltersComponent.isApplied()) {
            tempFilteredHeatmapDatasetMap = filteredQuantDsMap;
            finalDiseaseCategories = new LinkedHashSet<>();
            for (QuantDataset ds : filteredQuantDsMap.values()) {
                finalDiseaseCategories.add(ds.getDiseaseCategoryI());
                finalDiseaseCategories.add(ds.getDiseaseCategoryII());
            }

        } else {
            finalDiseaseCategories = diseaseCategories;
            tempFilteredHeatmapDatasetMap = unitFilteringDataset();
            datasetPieChartFiltersComponent.resetFilterIcon();
        }

        for (int key : tempFilteredHeatmapDatasetMap.keySet()) {
            patientsGroupComparisonsSet.stream().filter((patientsGroupComparisons) -> (patientsGroupComparisons.getQuantDatasetIndex() == key)).forEach((patientsGroupComparisons) -> {
                filteredPatientsGroupComparisonsSet.add(patientsGroupComparisons);
            });
        }
        heatmapLayoutContainer.updateHeatMapFilterSelection(filteredPatientsGroupComparisonsSet, tempFilteredHeatmapDatasetMap, finalDiseaseCategories);

    }

    /**
     * Re organise / paint the disease category heat-map
     */
    public void reDrawHeatMap() {
        Map<Integer, QuantDataset> updatedDsIds = unitFilteringDataset();
        updateSystemComponents(updatedDsIds.keySet(), heatmapLayoutContainer.getUpdatedExpandedList(), true, false);
        heatmapLayoutContainer.selectDiseaseCategory(null, false, false);

    }

    private boolean selfselection = false;

    /**
     * Selection changed in the selection manager
     *
     * @param type type of selection
     */
    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("comparisons_selection_update")) {
            Set<QuantDiseaseGroupsComparison> selectedQuantComparisonsList = CSFPR_Central_Manager.getSelectedComparisonsList();
            Data_handler.updateComparisonQuantProteins(selectedQuantComparisonsList);
            selfselection = true;
            CSFSelection selection = new CSFSelection("comparisons_selection", getListenerId(), selectedQuantComparisonsList, null);
            CSFPR_Central_Manager.setSelection(selection);
        }

        if (type.equalsIgnoreCase("comparisons_selection")) {
            if (selfselection) {
                selfselection = false;
                return;

            }

            Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();

            if (compList == null || compList.isEmpty()) {
                heatmapLayoutContainer.clearSelection();

            } else {
                heatmapLayoutContainer.selectComparisons(compList);
            }
        }

    }

    /**
     * Show serum datasets (unselected by default).
     */
    public void showSerumDs() {
        serumCsfFilter.getNoSerumOptionBtn().removeStyleName("unapplied");
        serumCsfFilter.updateSystem(true, true);

    }

    /**
     * Get the listener id.
     *
     * @return listener id
     */
    @Override
    public String getListenerId() {
        return this.getClass().getName();
    }

    /**
     * Un-select all datasets in the heat-map.
     */
    public void unselectAll() {
        this.heatmapLayoutContainer.unselectAll();
    }

    /**
     * Select all datasets in the heat-map.
     */
    public void selectAll() {
        this.heatmapLayoutContainer.selectAll();
    }

    /**
     * This method to update and combine disease sub groups based on user
     * selection
     *
     *
     * @param updatedGroupsNamesMap updated disease sub group names
     */
    public abstract void updateCombinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap);

    /**
     * This method allow users to filter the datasets based on sample type (CSF
     * / Serum)
     *
     *
     * @param serumApplied show Serum datasets
     * @param csfApplied show CSF datasets
     */
    public abstract void updateCSFSerumDatasets(boolean serumApplied, boolean csfApplied);

    /**
     * Update the main button image with the current heat map image
     *
     * @param imageUrl URL for image encoded as Base64 string
     * @param number nuber of available datasets
     * @param selectedDiseaseCategory selected disease category
     * @param expandCollapsAction the action from heatmap expand or collaps
     * disease category
     */
    public abstract void updateIcon(String imageUrl, int number, String selectedDiseaseCategory, boolean expandCollapsAction);

    /**
     * Blinking the frame of the main button.
     */
    public abstract void blinkIcon();

    /**
     * Get side buttons container that has all the heat map control buttons
     *
     * @return heatmapToolsContainer the side buttons container
     */
    public VerticalLayout getHeatmapToolsContainer() {
        return heatmapToolsContainer;
    }

    private Map<Integer, QuantDataset> unitFilteringDataset() {

        Map<Integer, QuantDataset> updatedDsIds = new LinkedHashMap<>();
        for (int id : fullQuantDsMap.keySet()) {
            if (serumCsfFilter.isSerumApplied() && fullQuantDsMap.get(id).getSampleType().equalsIgnoreCase("Serum")) {
                updatedDsIds.put(id, fullQuantDsMap.get(id));
            } else if (serumCsfFilter.isCsfApplied() && fullQuantDsMap.get(id).getSampleType().equalsIgnoreCase("CSf")) {
                updatedDsIds.put(id, fullQuantDsMap.get(id));
            }
        }
        Map<Integer, QuantDataset> finalUpdatedDsIds = new LinkedHashMap<>();
        if (poolFilterBtn.isHidePoolSamplesDatasets()) {
            for (int id : updatedDsIds.keySet()) {
                if (!updatedDsIds.get(id).isPooledSamples()) {
                    finalUpdatedDsIds.put(id, updatedDsIds.get(id));
                }
            }

        } else {
            finalUpdatedDsIds.putAll(updatedDsIds);
        }
        return finalUpdatedDsIds;

    }

}
