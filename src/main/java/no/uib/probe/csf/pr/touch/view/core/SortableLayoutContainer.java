package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;

/**
 * This class represents layout container that supports Drag and Drop layout.
 *
 * @author Yehia Farag
 */
public class SortableLayoutContainer extends VerticalLayout {

    /**
     * The rows container that support drag and drop.
     */
    private final SortableLayout sortableRowsLayout;
    /**
     * The left side row indexes labels container.
     */
    private final VerticalLayout counterLayoutContainer;
    /**
     * The left side row indexes labels container wrapper.
     */
    private final VerticalLayout counterLayoutContainerWrapper;
    /**
     * The right side row check box container.
     */
    private final VerticalLayout checkboxLayout;
    /**
     * The header label text
     */
    private final String headerTitle;
    /**
     * The right side option group component.
     */
    private final OptionGroup diseaseGroupSelectOption;
    /**
     * Clear all selection.
     */
    private final Button clearBtn;
    /**
     * Map of row labels and checked value.
     */
    private final Map<HeatMapHeaderCellInformationBean, Boolean> groupSelectionMap;
    /**
     * Set of selected row labels.
     */
    private final Set<HeatMapHeaderCellInformationBean> selectionSet;
    /**
     * Automatically reset selection.
     */
    private boolean autoClear;
    /**
     * Only one row is selected.
     */
    private boolean singleSelected = false;
    /**
     * Set of all available rows(select all set).
     */
    private Set selectAllSet = new HashSet();
    /**
     * Listener for selection from right option group component.
     */
    private final ValueChangeListener selectDataListener;
    /**
     * Available height for the layout(to scroll over it).
     */
    private final int maxHeight;
    /**
     * Scrolling panel to support scroll over the maximum height.
     */
    private final Panel bodyPanel;
    /**
     * External listener to maintain interactive selection between other option
     * groups in the other layouts.
     */
    private Property.ValueChangeListener externalListener;
    /**
     * Map of row label information objects and row labels layout.
     */
    private final Map<HeatMapHeaderCellInformationBean, VerticalLayout> labelsLayoutSet = new HashMap<>();
    /**
     * Set of initial label information objects (full available labels).
     */
    private LinkedHashSet<HeatMapHeaderCellInformationBean> initalLabels;
    /**
     * Set of full label information objects (full available labels).
     */
    private final LinkedList<HeatMapHeaderCellInformationBean> groupsIds = new LinkedList<>();

    /**
     * Get the component height.
     *
     * @return component height.
     */
    public int getFinalHeight() {
        return (int) bodyPanel.getHeight() + 30;
    }

    /**
     * Constructor to initialize the main attributes.
     *
     * @param headerTitle The main header title text
     * @param height the available height for the component.
     */
    public SortableLayoutContainer(final String headerTitle, int height) {

        SortableLayoutContainer.this.setStyleName("whitelayout");
        SortableLayoutContainer.this.setSpacing(true);
        SortableLayoutContainer.this.setMargin(new MarginInfo(false, true, false, false));
        SortableLayoutContainer.this.setWidth(100, Unit.PERCENTAGE);
        SortableLayoutContainer.this.setHeightUndefined();

        this.headerTitle = headerTitle;
        this.groupSelectionMap = new HashMap<>();
        this.selectionSet = new LinkedHashSet<>();

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth(100, Unit.PERCENTAGE);
        headerLayout.setHeight(30, Unit.PIXELS);
        headerLayout.setStyleName("bottomborder");
        headerLayout.addStyleName("margin");
        headerLayout.addStyleName("paddingleft20");
        headerLayout.setMargin(new MarginInfo(false, true, false, false));

        SortableLayoutContainer.this.addComponent(headerLayout);

        Label titileI = new Label(headerTitle);
        titileI.setStyleName(ValoTheme.LABEL_SMALL);
        titileI.addStyleName(ValoTheme.LABEL_BOLD);
        headerLayout.addComponent(titileI);
        titileI.setWidth(100, Unit.PERCENTAGE);

        diseaseGroupSelectOption = new OptionGroup();
        diseaseGroupSelectOption.setHeight(100, Unit.PERCENTAGE);
        diseaseGroupSelectOption.setNullSelectionAllowed(true); // user can not 'unselect'
        diseaseGroupSelectOption.setMultiSelect(true);
        diseaseGroupSelectOption.addStyleName("sortablelayoutselect");

        clearBtn = new Button("Clear");
        clearBtn.setHeight(100, Unit.PERCENTAGE);
        clearBtn.setStyleName(ValoTheme.BUTTON_LINK);
        clearBtn.addStyleName("link");
        clearBtn.addStyleName("marginleft28");
        clearBtn.setEnabled(false);
        headerLayout.addComponent(clearBtn);
        headerLayout.setComponentAlignment(clearBtn, Alignment.TOP_RIGHT);
        clearBtn.addClickListener((Button.ClickEvent event) -> {
            singleSelected = false;
            diseaseGroupSelectOption.setValue(null);
        });

        bodyPanel = new Panel();
        maxHeight = height - 50;
        bodyPanel.setWidth(100, Unit.PERCENTAGE);
        bodyPanel.setStyleName(ValoTheme.PANEL_BORDERLESS);
        bodyPanel.addStyleName("margin");

        HorizontalLayout bodyLayout = new HorizontalLayout();
        bodyLayout.setMargin(false);
        bodyLayout.setStyleName("paddingleft20");

        bodyLayout.setWidth(100, Unit.PERCENTAGE);

        bodyPanel.setContent(bodyLayout);
        SortableLayoutContainer.this.addComponent(bodyPanel);
        SortableLayoutContainer.this.setComponentAlignment(bodyPanel, Alignment.BOTTOM_CENTER);

        counterLayoutContainerWrapper = new VerticalLayout();
        bodyLayout.addComponent(counterLayoutContainerWrapper);

        counterLayoutContainerWrapper.setWidth(100, Unit.PERCENTAGE);
        bodyLayout.setExpandRatio(counterLayoutContainerWrapper, 10);
        counterLayoutContainer = new VerticalLayout();
        counterLayoutContainerWrapper.addComponent(counterLayoutContainer);
        counterLayoutContainerWrapper.setComponentAlignment(counterLayoutContainer, Alignment.TOP_RIGHT);
        counterLayoutContainer.setWidth(100, Unit.PERCENTAGE);
        counterLayoutContainer.setSpacing(false);

        sortableRowsLayout = new SortableLayout();
        bodyLayout.addComponent(sortableRowsLayout);
        bodyLayout.setExpandRatio(sortableRowsLayout, 80);
        sortableRowsLayout.setWidth(100, Unit.PERCENTAGE);

        sortableRowsLayout.setData(headerTitle);
        sortableRowsLayout.addStyleName("no-horizontal-drag-hints");

        checkboxLayout = new VerticalLayout();
        bodyLayout.addComponent(checkboxLayout);
        bodyLayout.setExpandRatio(checkboxLayout, 10);

        checkboxLayout.setSpacing(false);
        checkboxLayout.setEnabled(false);
        checkboxLayout.setMargin(false);

        checkboxLayout.addComponent(diseaseGroupSelectOption);
        checkboxLayout.setComponentAlignment(diseaseGroupSelectOption, Alignment.TOP_LEFT);
        selectDataListener = (Property.ValueChangeEvent event) -> {
            if (autoClear) {
                autoClear = false;
                return;
            }
            groupSelectionMap.keySet().stream().forEach((key) -> {
                groupSelectionMap.put(key, Boolean.FALSE);
            });
            getSelectionSet().clear();
            singleSelected = false;
            int counter = 0;
            for (Object key : ((Set) diseaseGroupSelectOption.getValue())) {
                if (((Integer) key) >= groupsIds.size()) {
                    continue;
                }
                groupSelectionMap.put(groupsIds.get((Integer) key), Boolean.TRUE);
                singleSelected = true;
                counter++;
                getSelectionSet().add(groupsIds.get((Integer) key));
            }
            if (counter == groupsIds.size()) {
                singleSelected = false;
            }
        };
        diseaseGroupSelectOption.addValueChangeListener(selectDataListener);
        checkboxLayout.setVisible(false);
        clearBtn.setVisible(false);
    }

    /**
     * Update rows initial labels information objects (full available labels)
     *
     * @param labelInformationObjectSet Set of initial label information objects
     * (full available labels)
     */
    public void updateData(LinkedHashSet<HeatMapHeaderCellInformationBean> labelInformationObjectSet) {
        if (labelInformationObjectSet.isEmpty()) {
            return;
        }
        this.initalLabels = labelInformationObjectSet;
        diseaseGroupSelectOption.removeValueChangeListener(selectDataListener);
        if (externalListener != null) {
            diseaseGroupSelectOption.removeValueChangeListener(externalListener);
        }
        diseaseGroupSelectOption.removeAllItems();
        sortableRowsLayout.removeAllComponents();
        counterLayoutContainer.removeAllComponents();
        selectAllSet.clear();
        int counter = 0;
        labelsLayoutSet.clear();
        for (final VerticalLayout component : createComponents(labelInformationObjectSet)) {
            VerticalLayout container = new VerticalLayout();
            container.setWidth(100, Unit.PERCENTAGE);
            container.setStyleName("countItem");
            Label label = new Label(counter + 1 + "");
            container.addComponent(label);
            this.counterLayoutContainer.addComponent(container);
            sortableRowsLayout.addComponent(component, headerTitle, component.getData().toString());
            diseaseGroupSelectOption.addItem(counter);
            diseaseGroupSelectOption.setItemCaption(counter, (counter + 1) + "");
            autoClear = true;
            diseaseGroupSelectOption.select(counter);
            selectAllSet.add(counter);
            counter++;
        }
        counterLayoutContainer.setHeight((24 * counterLayoutContainer.getComponentCount()), Unit.PIXELS);
        bodyPanel.setHeight(Math.min(maxHeight, 25 * (counter)), Unit.PIXELS);
        diseaseGroupSelectOption.setHeight(25 * (counter - 1), Unit.PIXELS);
        autoClear = false;
        diseaseGroupSelectOption.addValueChangeListener(selectDataListener);
        if (externalListener != null) {
            diseaseGroupSelectOption.addValueChangeListener(externalListener);
        }

    }

    /**
     * Check if only one row is selected.
     *
     * @return only one row selected.
     */
    public boolean isSelectionMode() {
        return singleSelected;
    }

    /**
     * Add external listener to maintain interactive selection between other
     * option groups in the other layouts.
     *
     * @param listener external value change listener for disease group
     * selection option group component.
     */
    public void addSelectionValueChangeListener(Property.ValueChangeListener listener) {
        this.externalListener = listener;
        diseaseGroupSelectOption.addValueChangeListener(listener);

    }

    /**
     * Reset row layout to initial state.
     */
    public void resetToDefault() {
        updateData(initalLabels);
    }

    /**
     * Update selectedLabelsSet (selected) and hide unselected.
     *
     * @param selectedLabelsSet Set of label information objects to update the
     * layout.
     * @param selectOnly Show selected labels only.
     */
    public void selectAndUpdate(Set<HeatMapHeaderCellInformationBean> selectedLabelsSet, boolean selectOnly) {
        autoClear = true;
        diseaseGroupSelectOption.setValue(null);

        Set updatedSelectionSet = new HashSet();
        if (selectedLabelsSet == null || selectedLabelsSet.isEmpty() || (selectedLabelsSet.size() == groupsIds.size())) {
            updatedSelectionSet.addAll(selectAllSet);
            for (int i = 0; i < groupsIds.size(); i++) {
                labelsLayoutSet.get(groupsIds.get(i)).removeStyleName("disableLayout");
                diseaseGroupSelectOption.setItemEnabled(i, true);
            }

        } else {
            if (selectOnly) {
                for (int i = 0; i < groupsIds.size(); i++) {
                    if (selectedLabelsSet.contains(groupsIds.get(i))) {
                        updatedSelectionSet.add(i);
                    }
                }

            } else {
                for (int i = 0; i < groupsIds.size(); i++) {
                    if (selectedLabelsSet.contains(groupsIds.get(i))) {
                        updatedSelectionSet.add(i);
                        labelsLayoutSet.get(groupsIds.get(i)).removeStyleName("disableLayout");
                        diseaseGroupSelectOption.setItemEnabled(i, true);

                    } else {
                        labelsLayoutSet.get(groupsIds.get(i)).addStyleName("disableLayout");
                        diseaseGroupSelectOption.setItemEnabled(i, false);
                    }
                }
            }
        }
        diseaseGroupSelectOption.setValue(updatedSelectionSet);
    }

    /**
     * Get the sorted row label information objects set.
     *
     * @return sortedSet User sorted row label information objects set.
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getSortedSet() {
        LinkedHashSet sortedSet;
        if (isSelectionMode()) {
            sortedSet = new LinkedHashSet(selectionSet);

        } else {
            sortedSet = new LinkedHashSet();
            groupsIds.stream().filter((groupsId) -> (!labelsLayoutSet.get(groupsId).getStyleName().trim().contains("disableLayout"))).forEach((groupsId) -> {
                sortedSet.add(groupsId);
            });
        }
        return sortedSet;
    }

    /**
     * Set the layout function mode (Select/Drag and drop)
     *
     * @param mode Select/Drag and drop
     */
    public void setLayoutMode(String mode) {
        if (mode.equalsIgnoreCase("select")) {
            checkboxLayout.setEnabled(true);
            clearBtn.setEnabled(true);
            sortableRowsLayout.setEnabled(false);
        } else {
            checkboxLayout.setEnabled(false);
            clearBtn.setEnabled(false);
            sortableRowsLayout.setEnabled(true);
        }

    }

    /**
     * Set enable option group selection and clear selection button (selection
     * mode)
     *
     * @param enable enable/disable selection layouts.
     */
    public void setEnableSelection(boolean enable) {
        checkboxLayout.setEnabled(enable);
        clearBtn.setEnabled(enable);
    }

    /**
     * Create sorting label components.
     *
     * @param datasource Set of label information objects.
     */
    private List<VerticalLayout> createComponents(Set<HeatMapHeaderCellInformationBean> datasource) {
        final List<VerticalLayout> componentsList = new ArrayList<>();
        groupsIds.clear();
        groupSelectionMap.clear();
        datasource.forEach((strLabel) -> {
            DiseaseGroupLabel container = new DiseaseGroupLabel(strLabel.getDiseaseGroupName(), strLabel.getDiseaseStyleName());
            container.addStyleName("border");
            container.addStyleName("grab");
            container.setDescription(strLabel.getDiseaseGroupFullName());
            componentsList.add(container);
            container.setData(strLabel.getDiseaseCategory());
            groupSelectionMap.put(strLabel, Boolean.TRUE);
            groupsIds.add(strLabel);
            labelsLayoutSet.put(strLabel, container);
        });
        return componentsList;
    }

    /**
     * Get current selected label information objects set
     *
     * @return Set of selected row labels
     */
    public final Set<HeatMapHeaderCellInformationBean> getSelectionSet() {
        return selectionSet;
    }

    /**
     * This class create custom component that is used as container for
     * Drag-Drop layout.
     *
     * @author Yehia Farag
     */
    class SortableLayout extends CustomComponent {

        /**
         * The main layout.
         */
        private final AbstractOrderedLayout layout;
        /**
         * The layout drop handler.
         */
        private final DropHandler dropHandler;

        /**
         * Constructor to initialize the main attributes.
         */
        public SortableLayout() {
            layout = new VerticalLayout();
            layout.setWidth(100, Unit.PERCENTAGE);
            layout.setHeight(100, Unit.PERCENTAGE);
            dropHandler = new ReorderLayoutDropHandler(layout);
            SortableLayoutContainer.this.setEnabled(true);
            final DragAndDropWrapper pane = new DragAndDropWrapper(layout);
            super.setCompositionRoot(pane);

        }

        /**
         * Add component to the drag-drop layout.
         *
         * @param component the added component.
         * @param labelId The label id
         */
        public void addComponent(final Component component, String labelId, String category) {
            final WrappedComponent wrapper = new WrappedComponent(component,
                    dropHandler);
            wrapper.setHeight(100, Unit.PERCENTAGE);
            wrapper.setWidth(100, Unit.PERCENTAGE);
            wrapper.setData(labelId + "__" + category);
            wrapper.setDescription(component.getDescription());
            layout.addComponent(wrapper);

        }

        /**
         * Remove all components.
         */
        public void removeAllComponents() {
            layout.removeAllComponents();
        }

        /**
         * Get drag-drop layout.
         *
         * @return layout Drag-drop layout
         */
        public AbstractOrderedLayout get_Drag_Drop_Layout() {
            return layout;
        }
    }

    /**
     * This class is a wrapper for the dropped component that is used in the
     * Drag-Drop layout.
     *
     * @author Yehia Farag
     */
    class WrappedComponent extends DragAndDropWrapper {

        /**
         * The layout drop handler.
         */
        private final DropHandler dropHandler;

        /**
         * Constructor to initialize the main attributes.
         *
         * @param content the dropped component (the label layout)
         * @param dropHandler The layout drop handler.
         */
        public WrappedComponent(final Component content, final DropHandler dropHandler) {
            super(content);
            this.dropHandler = dropHandler;
            super.setDragStartMode(DragAndDropWrapper.DragStartMode.WRAPPER);
        }

        @Override
        public DropHandler getDropHandler() {
            return dropHandler;
        }

    }

    /**
     * This class is an implementation for the the layout drop handler.
     *
     * @author Yehia Farag
     */
    class ReorderLayoutDropHandler implements DropHandler {

        /**
         * The layout drop handler.
         */
        private final AbstractOrderedLayout layout;

        /**
         * Get drop handler layout.
         *
         * @return layout Drop handler layout.
         */
        public AbstractOrderedLayout getLayout() {
            return layout;
        }

        /**
         * Constructor to initialize the main attributes.
         *
         * @param layout the Drag-Drop container layout.
         */
        public ReorderLayoutDropHandler(final AbstractOrderedLayout layout) {
            this.layout = layout;
        }

        @Override
        public AcceptCriterion getAcceptCriterion() {
            return new Not(SourceIsTarget.get());
        }

        @Override
        public void drop(final DragAndDropEvent dropEvent) {
            final Transferable transferable = dropEvent.getTransferable();
            final Component sourceComponent = transferable.getSourceComponent();
            if (sourceComponent instanceof WrappedComponent) {
                final TargetDetails dropTargetData = dropEvent
                        .getTargetDetails();
                final DropTarget target = dropTargetData.getTarget();
                if (!((WrappedComponent) sourceComponent).getData().toString().equalsIgnoreCase(((WrappedComponent) target).getData().toString())) {
                    return;
                }
                // find the location where to move the dragged component
                boolean sourceWasAfterTarget = true;
                int index = 0;
                final Iterator<Component> componentIterator = layout.iterator();
                Component next = null;
                while (next != target && componentIterator.hasNext()) {
                    next = componentIterator.next();
                    if (next != sourceComponent) {
                        index++;
                    } else {
                        sourceWasAfterTarget = false;
                    }
                }
                if (next == null || next != target) {
                    return;
                }

                // drop on top of target?
                if (dropTargetData.getData("verticalLocation").equals(
                        VerticalDropLocation.MIDDLE.toString())) {
                    if (sourceWasAfterTarget) {
                        index--;
                    }
                } // drop before the target?
                else if (dropTargetData.getData("verticalLocation").equals(
                        VerticalDropLocation.TOP.toString())) {
                    index--;
                    if (index < 0) {
                        index = 0;
                    }
                }
                //update the selection (unselect current selected)
                int sourceIndex = layout.getComponentIndex(sourceComponent);

                HeatMapHeaderCellInformationBean srcLabel = groupsIds.get(sourceIndex);
                groupsIds.remove(srcLabel);
                groupsIds.add(index, srcLabel);
                autoClear = true;
                diseaseGroupSelectOption.setValue(null);
                Set selectionSet = new HashSet();
                for (int i = 0; i < groupsIds.size(); i++) {
                    if (groupSelectionMap.get(groupsIds.get(i))) {
                        selectionSet.add(i);
                        diseaseGroupSelectOption.setItemEnabled(i, true);
                    } else {
                        diseaseGroupSelectOption.setItemEnabled(i, false);
                    }

                }
                diseaseGroupSelectOption.setValue(selectionSet);
                // move component within the layout
                layout.removeComponent(sourceComponent);
                layout.addComponent(sourceComponent, index);

            }
        }

    }

}
