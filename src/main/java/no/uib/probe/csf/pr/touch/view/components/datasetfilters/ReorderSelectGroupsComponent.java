package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PopupWindowFrameWithFunctionsBtns;
import no.uib.probe.csf.pr.touch.view.core.SortableLayoutContainer;
import org.vaadin.teemu.switchui.Switch;

/**
 * This class is responsible for updating heat-map rows and columns order as
 * well as allowing user to hide disease groups
 *
 * @author Yehia Farag
 *
 */
public abstract class ReorderSelectGroupsComponent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * Main pop-up window frame with buttons control.
     */
    private final PopupWindowFrameWithFunctionsBtns popupWindow;
    /**
     * Main disease groups (comparisons labels) container.
     */
    private final HorizontalLayout diseaseGroupsContaioner;
    /**
     * Main window container layout.
     */
    private final VerticalLayout popupBody;
    /**
     * Set of datasets indexes available in the heat-map.
     */
    private LinkedHashSet<Integer> datasetsIndexes;
    /**
     * Left side sortable (drag and drop) layout container.
     */
    private final SortableLayoutContainer groupILayout;
    /**
     * Right side sortable (drag and drop) layout container.
     */
    private final SortableLayoutContainer groupIILayout;
    /**
     * Set of datasets sub-group comparisons.
     */
    private Set<DiseaseGroupComparison> fullDiseaseSubGroupComparisonsSet;
    /**
     * The window width.
     */
    private final int screenWidth = Math.min(Page.getCurrent().getBrowserWindowWidth(), 1000);
    /**
     * The window height.
     */
    private final int screenHeight = Math.min(Page.getCurrent().getBrowserWindowHeight(), 800);
    /**
     * Map of header title to heat/map header cell object .
     */
    private final Map<String, HeatMapHeaderCellInformationBean> fullCellInfoMap = new LinkedHashMap<>();
    List<HeatMapHeaderCellInformationBean> sortedRowHeaders = new ArrayList<>();
    List<HeatMapHeaderCellInformationBean> sortedColumnHeaders = new ArrayList<>();

    /**
     * Constructor to initialize the main attributes.
     */
    public ReorderSelectGroupsComponent() {
        //init icon
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setSource(new ThemeResource("img/sort-select.png"));
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(ReorderSelectGroupsComponent.this);
        this.setDescription("Reorder and select disease groups");

        //init window layout 
        VerticalLayout frame = new VerticalLayout();
        popupBody = new VerticalLayout();
        frame.addComponent(popupBody);
        popupBody.setWidth(100, Unit.PERCENTAGE);
        popupBody.setSpacing(true);
        HorizontalLayout btnsFrame = new HorizontalLayout();
        popupWindow = new PopupWindowFrameWithFunctionsBtns("Disease Groups", frame, btnsFrame);
        popupWindow.setFrameWidth(screenWidth);

        diseaseGroupsContaioner = new HorizontalLayout();
        popupBody.addComponent(diseaseGroupsContaioner);
        popupBody.setComponentAlignment(diseaseGroupsContaioner, Alignment.TOP_CENTER);

        diseaseGroupsContaioner.setStyleName("whitelayout");
        diseaseGroupsContaioner.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsContaioner.setSpacing(true);

        int h = 250;

        popupBody.setHeight(screenHeight - 180, Unit.PIXELS);
        groupILayout = new SortableLayoutContainer("Disease Group A", (screenHeight - h));
        groupIILayout = new SortableLayoutContainer("Disease Group B", (screenHeight - h));

        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        btnsFrame.addComponent(leftsideWrapper);
        btnsFrame.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        leftsideWrapper.setSpacing(true);

        InformationButton info = new InformationButton("The disease groups shown and the order of these groups can be controlled by dragging and dropping the groups in the table, and by selecting only the groups to display. When the wanted order is achieved click the \"Apply\" button.", true);
        leftsideWrapper.addComponent(info);

        HorizontalLayout bottomContainert = new HorizontalLayout();
        bottomContainert.setWidth(100, Unit.PERCENTAGE);
        bottomContainert.setHeight(100, Unit.PERCENTAGE);

        btnsFrame.addComponent(bottomContainert);
        btnsFrame.setComponentAlignment(bottomContainert, Alignment.BOTTOM_CENTER);

        String sortString = "Sort - drag & drop";
        String selectString = "Select to filter datasets";
        Label commentLabel = new Label(sortString);
        commentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        commentLabel.addStyleName(ValoTheme.LABEL_TINY);

//        Switch selectSortSwichBtn = new Switch();
//        selectSortSwichBtn.setDescription("Sort / Select disease groups");
//        selectSortSwichBtn.setValue(Boolean.TRUE);
//        selectSortSwichBtn.setEnabled(false);
//        selectSortSwichBtn.addValueChangeListener((ValueChangeEvent event) -> {
//            if (selectSortSwichBtn.getValue()) {
//
//                groupILayout.setLayoutMode("sort");
//                groupIILayout.setEnabled(true);
//                groupIILayout.setLayoutMode("sort");
//                commentLabel.setValue(sortString);
//
//            } else {
//                commentLabel.setValue(selectString);
//                groupILayout.setLayoutMode("select");
//                groupIILayout.setEnableSelection(false);
//                groupIILayout.setLayoutMode("select");
//                if (groupILayout.isSelectionMode()) {
//                    groupIILayout.setEnableSelection(true);
//                }
//            }
//        });
//
//        selectSortSwichBtn.setImmediate(true);
//        leftsideWrapper.addComponent(selectSortSwichBtn);
//        leftsideWrapper.setComponentAlignment(selectSortSwichBtn, Alignment.MIDDLE_LEFT);

        leftsideWrapper.addComponent(commentLabel);
        leftsideWrapper.setComponentAlignment(commentLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);

        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetFiltersBtn);

        resetFiltersBtn.setDescription("Reset all groups to default");
        resetFiltersBtn.addClickListener((Button.ClickEvent event) -> {
            groupILayout.resetToDefault();
            groupIILayout.resetToDefault();
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(cancelBtn);
        cancelBtn.addClickListener((Button.ClickEvent event) -> {
            popupWindow.view();
            groupILayout.resetToDefault();
            groupIILayout.resetToDefault();

        });

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Update the datasets");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener((Button.ClickEvent event) -> {
            if (!groupIILayout.isSelectionMode()) {
                int index = Integer.MAX_VALUE;
                for (HeatMapHeaderCellInformationBean hmheaderBean : groupILayout.getSortedSet()) {
                    index = Math.min(index, this.sortedRowHeaders.indexOf(hmheaderBean));
                    this.sortedRowHeaders.remove(hmheaderBean);
                }
                sortedRowHeaders.addAll(index, groupILayout.getSortedSet());
                index = Integer.MAX_VALUE;
                for (HeatMapHeaderCellInformationBean hmheaderBean : groupIILayout.getSortedSet()) {
                    index = Math.min(index, this.sortedColumnHeaders.indexOf(hmheaderBean));
                    this.sortedColumnHeaders.remove(hmheaderBean);
                }
                sortedColumnHeaders.addAll(index, groupILayout.getSortedSet());
            }

            ReorderSelectGroupsComponent.this.updateSystem(groupILayout.getSortedSet(), groupIILayout.getSortedSet(), groupIILayout.isSelectionMode());
            popupWindow.view();
        });

        bottomContainert.addComponent(btnLayout);
        bottomContainert.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);

        diseaseGroupsContaioner.addComponent(groupILayout);

        diseaseGroupsContaioner.addComponent(groupIILayout);

        Property.ValueChangeListener selectionChangeListenet = (Property.ValueChangeEvent event) -> {
            if (groupILayout.isSelectionMode()) {
                groupIILayout.setEnableSelection(true);
            } else {
                groupIILayout.selectAndUpdate(null, false);
                return;
            }

            Set<HeatMapHeaderCellInformationBean> updatedGroupIISet = filterPatGroup2List(groupILayout.getSelectionSet());
            groupIILayout.selectAndUpdate(updatedGroupIISet, false);
        };
        groupILayout.addSelectionValueChangeListener(selectionChangeListenet);

    }

    /**
     * Initialise the data with disease groups name and information
     *
     * @param rowHeaders Set of row header cell information objects.
     * @param colHeaders Set of column header cell information objects.
     * @param patientsGroupComparisonsSet Set of datasets sub-group comparisons.
     */
    public void initialiseData(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet) {

        this.fullDiseaseSubGroupComparisonsSet = patientsGroupComparisonsSet;
        fullCellInfoMap.clear();
        rowHeaders.stream().forEach((cell) -> {
            fullCellInfoMap.put(cell.getDiseaseGroupName() + "__" + cell.getDiseaseCategory(), cell);
            this.sortedRowHeaders.add(cell);
        });
        colHeaders.stream().forEach((cell) -> {
            fullCellInfoMap.put(cell.getDiseaseGroupName() + "__" + cell.getDiseaseCategory(), cell);
            this.sortedColumnHeaders.add(cell);
        });
//        groupILayout.updateData(sortedRowHeaders);
//        groupIILayout.updateData(sortedColumnHeaders);
//
//        popupBody.setHeight(groupILayout.getFinalHeight() + 70, Unit.PIXELS);
//        popupWindow.setFrameHeight((int) groupILayout.getFinalHeight() + 250 - 35);

    }

    /**
     * Update the data with disease groups name and information
     *
     * @param rowHeaders Set of row header cell information objects.
     * @param colHeaders Set of column header cell information objects.
     * @param patientsGroupComparisonsSet Set of datasets sub-group comparisons.
     */
    public void updateData1(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet) {

        this.fullDiseaseSubGroupComparisonsSet = patientsGroupComparisonsSet;
        fullCellInfoMap.clear();
        rowHeaders.stream().forEach((cell) -> {
            fullCellInfoMap.put(cell.getDiseaseGroupName() + "__" + cell.getDiseaseCategory(), cell);
        });
        colHeaders.stream().forEach((cell) -> {
            fullCellInfoMap.put(cell.getDiseaseGroupName() + "__" + cell.getDiseaseCategory(), cell);
        });
        groupILayout.updateData(rowHeaders);
        groupIILayout.updateData(colHeaders);

        popupBody.setHeight(groupILayout.getFinalHeight() + 70, Unit.PIXELS);
        popupWindow.setFrameHeight((int) groupILayout.getFinalHeight() + 250 - 35);

    }

    /**
     * On click on the re-order select button
     *
     * @param event reorder click button event
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.view();
    }

    /**
     * Filter the right sub-disease groups list based on selection from left
     * sub-disease groups list selection
     *
     * @param event reorder click button event
     */
    private Set<HeatMapHeaderCellInformationBean> filterPatGroup2List(Set<HeatMapHeaderCellInformationBean> cel1) {
        Set<HeatMapHeaderCellInformationBean> labels = new LinkedHashSet<>();
        datasetsIndexes = new LinkedHashSet<>();

        fullDiseaseSubGroupComparisonsSet.stream().forEach((pg) -> {
            cel1.stream().filter((label) -> (pg.checkSameComparison(label.getDiseaseGroupName() + "__" + label.getDiseaseCategory()))).map((label) -> {
                labels.add(fullCellInfoMap.get(pg.getValLabel(label.getDiseaseGroupName() + "__" + label.getDiseaseCategory())));
                return label;
            }).forEach((_item) -> {
                datasetsIndexes.add(pg.getQuantDatasetIndex());
            });
        });

        return labels;
    }

    /**
     * Update the heat-map based on user selection and actions
     *
     * @param rowHeaders Set of row header cell information objects.
     * @param colHeaders Set of column header cell information objects.
     * @param isSelectionMode The update is for Selection/Sort headers
     */
    public abstract void updateSystem(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders, boolean isSelectionMode);

    /**
     * Update the main icon button for the filters based on the container size
     *
     * @param resizeFactor Resize factor to update the button size
     */
    public void resizeFilter(double resizeFactor) {
        this.setWidth((int) (25 * resizeFactor), Unit.PIXELS);
        this.setHeight((int) (25 * resizeFactor), Unit.PIXELS);
    }

    /**
     * Update List of disease categories
     *
     * @param selectedDiseaseCategory set of disease categories
     */
    public void updateSelectedDiseaseCategory(Set<String> selectedDiseaseCategory) {
        if (fullCellInfoMap.isEmpty()) {
            return;
        }
        LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders = new LinkedHashSet<>();
        LinkedHashSet<HeatMapHeaderCellInformationBean> columnHeaders = new LinkedHashSet<>();
        for (HeatMapHeaderCellInformationBean cellBean : sortedRowHeaders) {
            if (selectedDiseaseCategory.contains(cellBean.getDiseaseCategory())) {
                rowHeaders.add(cellBean);
            }
        }
        for (HeatMapHeaderCellInformationBean cellBean : sortedColumnHeaders) {
            if (selectedDiseaseCategory.contains(cellBean.getDiseaseCategory())) {
                columnHeaders.add(cellBean);

            }
        }
        groupILayout.updateData(rowHeaders);
        groupIILayout.updateData(columnHeaders);
        popupBody.setHeight(groupILayout.getFinalHeight() + 70, Unit.PIXELS);
        popupWindow.setFrameHeight((int) groupILayout.getFinalHeight() + 250 - 35);

    }
}
