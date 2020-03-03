package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFiltersComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.RecombineDiseaseGroupsCombonent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.ReorderSelectGroupsComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.SerumCsfFilter;
import no.uib.probe.csf.pr.touch.view.core.ResizableTextLabel;

/**
 * This class represents the corner cell in the heat map that contain the
 * dataset filters
 *
 * @author Yehia Farag
 */
public class HeatmapFiltersContainerResizeControl {

    /**
     * The dataset filter container with the interactive management system for
     * different interactive pie-chart filters.
     *
     */
    private final DatasetPieChartFiltersComponent datasetPieChartFiltersBtn;
    /**
     * The disease sub group rename and re-combine into new customized disease
     * sub-groups.
     *
     */
    private final RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersBtn;
    /**
     * This button is responsible for updating heat-map rows and columns order
     * as well as allowing user to hide disease groups.
     *
     */
    private final ReorderSelectGroupsComponent reorderSelectBtn;
    /**
     * This layout contains 2 filters CSF filter allows user to view CSF
     * datasets Serum filter allows user to view Serum datasets users need to
     * select at least on of the filters.
     *
     */
    private final SerumCsfFilter serumCsfFilter;
    /**
     * This button is responsible for remove and reset all applied filters.
     *
     */
    private final VerticalLayout clearFilterBtn;
    /**
     * This Label for number of datasets included in the heat map.
     *
     */
    private final ResizableTextLabel datasetCounterLabel;
    /**
     * This is the bottom container for recombine group button and sort and select
     * button.
     *
     */
    private final HorizontalLayout bottomBtnContainer;
    /**
     * The main container layout.
     *
     */
    private final GridLayout btnsWrapper;

    /**
     * Constructor to initialize the main attributes
     *
     * @param btnsWrapper Container layout for the filters buttons
     * @param datasetPieChartFiltersBtn Dataset pie-chart filter button
     * @param reconbineDiseaseGroupsFiltersBtn Re-combine disease sub group button
     * @param reorderSelectBtn Re-order/Select disease sub group button 
     * @param bottomBtnContainer Under label button container
     * @param serumCsfFilter Serum/CSF filter button
     * @param clearFilterBtn Clear all filters button
     * @param datasetCounterLabel Dataset counter label  
     */
    public HeatmapFiltersContainerResizeControl(GridLayout btnsWrapper, DatasetPieChartFiltersComponent datasetPieChartFiltersBtn, RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersBtn, ReorderSelectGroupsComponent reorderSelectBtn, HorizontalLayout bottomBtnContainer, SerumCsfFilter serumCsfFilter, VerticalLayout clearFilterBtn, ResizableTextLabel datasetCounterLabel) {
        this.btnsWrapper = btnsWrapper;
        this.datasetPieChartFiltersBtn = datasetPieChartFiltersBtn;
        this.reconbineDiseaseGroupsFiltersBtn = reconbineDiseaseGroupsFiltersBtn;
        this.reorderSelectBtn = reorderSelectBtn;
        this.bottomBtnContainer = bottomBtnContainer;
        this.serumCsfFilter = serumCsfFilter;
        this.clearFilterBtn = clearFilterBtn;
        this.datasetCounterLabel = datasetCounterLabel;

    }

    /**
     * Get the main layout container that is used inside the corner cell
     *
     * @return btnsWrapper The main container layout.
     */
    public GridLayout getFilterContainerLayout() {
        return this.btnsWrapper;
    }

    /**
     * Resize the container and sub components (the filters buttons) based on
     * the parent container size
     *
     * @param resizeFactor resize factor the resize the sub components buttons
     */
    public void resizeFilters(double resizeFactor) {
        btnsWrapper.setWidth((int) (115 * resizeFactor), Unit.PIXELS);
        btnsWrapper.setHeight((int) (100 * resizeFactor), Unit.PIXELS);
        this.datasetPieChartFiltersBtn.resizeFilter(resizeFactor);
        this.datasetCounterLabel.setHeight((int) (25 * resizeFactor), Unit.PIXELS);
        this.clearFilterBtn.setWidth((int) (25 * resizeFactor), Unit.PIXELS);
        this.clearFilterBtn.setHeight((int) (25 * resizeFactor), Unit.PIXELS);
        this.serumCsfFilter.resizeFilter(resizeFactor);

        bottomBtnContainer.setWidth((int) (53 * resizeFactor), Unit.PIXELS);
        this.reconbineDiseaseGroupsFiltersBtn.resizeFilter(resizeFactor);
        this.reorderSelectBtn.resizeFilter(resizeFactor);
    }

}
