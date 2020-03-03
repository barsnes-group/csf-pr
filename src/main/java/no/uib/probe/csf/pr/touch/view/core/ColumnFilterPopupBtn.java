package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashSet;
import java.util.Set;
import org.vaadin.teemu.VaadinIcons;

/**
 * This class allow users to filter the columns of the protein table or drop one
 * comparison.
 *
 * @author Yehia Farag
 */
public abstract class ColumnFilterPopupBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main pop-up window.
     */
    private final PopupView filterPopupLayout;
    /**
     * Different options available to filter or drop the dataset.
     */
    private final OptionGroup tableHeaderFilterOptions;
    /**
     * Main layout click listener.
     */
    private final Property.ValueChangeListener listener;
    private final RangeComponent studiesRange;
    private final RangeComponent studiesNumber;
    private final TabSheet tabsheetContainer;

    /**
     * Constructor to initialize the main attributes.
     *
     * @param quantCustomizedComparison The comparison is user customized
     * comparison.
     */
    public ColumnFilterPopupBtn(boolean quantCustomizedComparison) {
        ColumnFilterPopupBtn.this.addStyleName("unselectedfilter");

        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(300, Unit.PIXELS);
        popupbodyLayout.setMargin(new MarginInfo(false, false, false, true));
        popupbodyLayout.addStyleName("border");

        CloseButton closePopup = new CloseButton();
        closePopup.setWidth(10, Unit.PIXELS);
        closePopup.setHeight(10, Unit.PIXELS);
        popupbodyLayout.addComponent(closePopup);
        popupbodyLayout.setComponentAlignment(closePopup, Alignment.TOP_RIGHT);
        closePopup.addStyleName("translateleft10");

        tabsheetContainer = new TabSheet();
        tabsheetContainer.setSizeFull();
        tabsheetContainer.setStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
        popupbodyLayout.addComponent(tabsheetContainer);
        tableHeaderFilterOptions = new OptionGroup();
        tabsheetContainer.addTab(tableHeaderFilterOptions, "Category");
//        popupbodyLayout.addComponent(tableHeaderFilterOptions);
        tableHeaderFilterOptions.setWidth(100, Unit.PERCENTAGE);
        tableHeaderFilterOptions.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
        tableHeaderFilterOptions.setDescription("Select to filter the table");
        tableHeaderFilterOptions.addItem(4);
        tableHeaderFilterOptions.setItemCaption(4, "Increased 100%");
        tableHeaderFilterOptions.addItem(3);
        tableHeaderFilterOptions.setItemCaption(3, "Increased < 100%");
        tableHeaderFilterOptions.addItem(2);
        tableHeaderFilterOptions.setItemCaption(2, "Equal");
        tableHeaderFilterOptions.addItem(1);
        tableHeaderFilterOptions.setItemCaption(1, "Decreased < 100%");
        tableHeaderFilterOptions.addItem(0);
        tableHeaderFilterOptions.setItemCaption(0, "Decreased 100%");
        tableHeaderFilterOptions.addItem(5);
        tableHeaderFilterOptions.setItemCaption(5, "No Quant Information");
        tableHeaderFilterOptions.setNullSelectionAllowed(true);
        tableHeaderFilterOptions.setImmediate(true);
        tableHeaderFilterOptions.setNewItemsAllowed(false);
        tableHeaderFilterOptions.setMultiSelect(true);
        tableHeaderFilterOptions.setItemEnabled(3, !quantCustomizedComparison);
        tableHeaderFilterOptions.setItemEnabled(1, !quantCustomizedComparison);
        tableHeaderFilterOptions.setItemEnabled(5, !quantCustomizedComparison);

        //add range filter
        String[] colors = new String[]{"green", "red"};

        studiesRange = new RangeComponent("Decreased<br/>%", -100.0, "Increased<br/>%", 100.0, colors, 0.0, true) {
            @Override
            public void selectedRange(double min, double max, boolean filterApplied) {
                filterTable(min, max, !filterApplied);;
                if (filterApplied) {
                    ColumnFilterPopupBtn.this.addStyleName("selectedfilter");
                    ColumnFilterPopupBtn.this.removeStyleName("unselectedfilter");

                } else {
                    ColumnFilterPopupBtn.this.addStyleName("unselectedfilter");
                    ColumnFilterPopupBtn.this.removeStyleName("selectedfilter");

                }
            }

            @Override
            public void selectedRange(double min, double max, double secMin, double secMax, boolean filterApplied) {
                filterTable((int) min, (int) max, (int) secMin, (int) secMax, !filterApplied);
                if (filterApplied) {
                    ColumnFilterPopupBtn.this.addStyleName("selectedfilter");
                    ColumnFilterPopupBtn.this.removeStyleName("unselectedfilter");

                } else {
                    ColumnFilterPopupBtn.this.addStyleName("unselectedfilter");
                    ColumnFilterPopupBtn.this.removeStyleName("selectedfilter");

                }
            }

        };
        tabsheetContainer.addTab(studiesRange, "Trend (%)");

        //add study number range
        studiesNumber = new RangeComponent("Min<br/>Number", 0.0, "Max<br/>Number<br/>", 100.0, colors, null, false) {
            @Override
            public void selectedRange(double min, double max, boolean filterApplied) {
                filterTable((int) min, (int) max, !filterApplied);
                if (filterApplied) {
                    ColumnFilterPopupBtn.this.addStyleName("selectedfilter");
                    ColumnFilterPopupBtn.this.removeStyleName("unselectedfilter");

                } else {
                    ColumnFilterPopupBtn.this.addStyleName("unselectedfilter");
                    ColumnFilterPopupBtn.this.removeStyleName("selectedfilter");

                }
            }

            @Override
            public void selectedRange(double min, double max, double secMin, double secMax, boolean filterApplied) {

            }

        };
        tabsheetContainer.addTab(studiesNumber, "#Study");

        //-----------------------end filters ---------------------
        filterPopupLayout = new PopupView(null, popupbodyLayout) {
            @Override
            public void setPopupVisible(boolean visible) {
                this.setVisible(visible);
                super.setPopupVisible(visible);
                tabsheetContainer.setSelectedTab(0);
            }
        };
        HorizontalLayout labelContainer = new HorizontalLayout();
        if (!quantCustomizedComparison) {

            labelContainer.setSpacing(true);
            Label icon = new Label();
            icon.setIcon(VaadinIcons.CLOSE_CIRCLE);

            labelContainer.addComponent(icon);
            Label headerLabel = new Label("Drop comparison");
            headerLabel.addStyleName("marginleft");
            headerLabel.setStyleName(ValoTheme.LABEL_BOLD);
            headerLabel.addStyleName(ValoTheme.LABEL_SMALL);
            labelContainer.addStyleName("pointer");
            labelContainer.addComponent(headerLabel);
            popupbodyLayout.addComponent(labelContainer);
            labelContainer.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                filterPopupLayout.setPopupVisible(false);
                ColumnFilterPopupBtn.this.dropComparison();
            });
        }

        closePopup.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            filterPopupLayout.setPopupVisible(false);
        });
        filterPopupLayout.setVisible(false);
        filterPopupLayout.setCaptionAsHtml(true);
        filterPopupLayout.setHideOnMouseOut(false);
        filterPopupLayout.addStyleName("margintop");
        ColumnFilterPopupBtn.this.addComponent(filterPopupLayout);
        listener = (Property.ValueChangeEvent event) -> {
            Set<Object> headersSet = new HashSet<>((Set<Object>) event.getProperty().getValue());

            if (headersSet.isEmpty()) {
                this.addStyleName("unselectedfilter");
                this.removeStyleName("selectedfilter");

            } else {
                this.addStyleName("selectedfilter");
                this.removeStyleName("unselectedfilter");
            }
            ColumnFilterPopupBtn.this.filterTable(headersSet, headersSet.isEmpty());

        };
        tableHeaderFilterOptions.addValueChangeListener(listener);
        ColumnFilterPopupBtn.this.addLayoutClickListener(ColumnFilterPopupBtn.this);
        tabsheetContainer.addSelectedTabChangeListener((TabSheet.SelectedTabChangeEvent event) -> {
            labelContainer.setVisible(tabsheetContainer.getTab(event.getTabSheet().getSelectedTab()).getCaption().equalsIgnoreCase("Category"));
        });
    }

    /**
     *Update chart data
     * @param studiesRangeData dataset range data
     * @param studiesNumberData dataset number data
     */
    public void updateChartsData(double[][] studiesRangeData, double[][] studiesNumberData) {
        if (studiesRangeData != null) {
            studiesRange.updateData(studiesRangeData);
        }
        if (studiesNumberData != null) {
            studiesNumber.updateData(studiesNumberData);
        }

    }

    /**
     * Layout click to show the filter pop-up layout.
     *
     * @param event show filter layout.
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        applyChartChanges(true, true);
        filterPopupLayout.setPopupVisible(true);
    }

    /**
     * Apply changes to chart
     *
     * @param studyRange dataset range selected
     * @param studyNumber dataset number selected
     */
    public void applyChartChanges(boolean studyRange, boolean studyNumber) {
        if (studyRange) {
            studiesRange.updateFilter();
        }
        if (studyNumber) {
            studiesNumber.updateFilter();
        }

    }

    /**
     * Reset filter and remove all applied filters.
     */
    public void reset() {
        tableHeaderFilterOptions.removeValueChangeListener(listener);
        tableHeaderFilterOptions.setValue(null);
        tableHeaderFilterOptions.addValueChangeListener(listener);
        studiesRange.resetRange();
        studiesNumber.resetRange();
    }

    /**
     * Drop comparison upon user drop comparison selection.
     */
    public abstract void dropComparison();

    /**
     * Filter table based on selected filters.
     *
     * @param selectedFiltersSet Set of user selected filters.
     * @param appliedFilter the filter applied
     */
    public abstract void filterTable(Set<Object> selectedFiltersSet, boolean appliedFilter);

    /**
     * Filter table based on selected range.
     *
     * @param min minimum range
     * @param max maximum range
     * @param appliedFilter the filter applied
     */
    public abstract void filterTable(double min, double max, boolean appliedFilter);

    /**
     * Filter table based on selected range.
     *
     * @param min first minimum range
     * @param max first maximum range
     * @param secMin second minimum range
     * @param secMax second maximum range
     * @param appliedFilter the filter applied
     *
     */
    public abstract void filterTable(double min, double max, double secMin, double secMax, boolean appliedFilter);

    /**
     * Filter table based on selected range.
     *
     * @param minStudiesNumber minimum number of datasets
     * @param maxStudiesNumber maximum number of datasets
     * @param appliedFilter the filter applied
     */
    public abstract void filterTable(long minStudiesNumber, long maxStudiesNumber, boolean appliedFilter);

}
