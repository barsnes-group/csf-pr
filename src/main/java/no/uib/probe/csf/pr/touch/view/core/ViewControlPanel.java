package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import java.util.TreeMap;

/**
 * This class represents the main view panel with left side control buttons and
 * right side tools button, the class is the main view structure for the quant
 * data layout. The panel support one view in time.
 *
 * @author Yehia Farag
 *
 */
public class ViewControlPanel extends HorizontalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main container contain the left, middle and right side containers.
     */
    private final HorizontalLayout mainLayoutWrapper;
    /**
     * The left side container contain the left buttons container.
     */
    private final VerticalLayout leftSideContainer;
    /**
     * The middle container contain the main view container.
     */
    private final HorizontalLayout mainViewContainer;
    /**
     * The right side container contain the right tools buttons container.
     */
    private final VerticalLayout rightSideToolBtnContainer;
    /**
     * The map of each middle view and its index.
     */
    private final TreeMap<Integer, AbstractOrderedLayout> layoutControlMap;
    /**
     * Layout click listener for the left side buttons container to maintain
     * view change between different visualizations layouts.
     */
    private LayoutEvents.LayoutClickListener listener;
    /**
     * Marker for current view.
     */
    private AbstractOrderedLayout currentView;
    /**
     * Marker for current used button.
     */
    private Component currentBtn;
    /**
     * Default layout button marker.
     */
    private Component defaultBtn;
    /**
     * Default visualizations layout marker.
     */
    private AbstractOrderedLayout defaultView;

    /**
     * Constructor to initialize the main attributes and layout.
     *
     * @param bodyWidth the frame width
     * @param bodyHeight the frame height
     */
    public ViewControlPanel(int bodyWidth, int bodyHeight) {

        this.setWidth(bodyWidth, Unit.PIXELS);
        this.setHeight(bodyHeight, Unit.PIXELS);
        this.addStyleName("slowslide");
        this.addStyleName("whitelayout");
        this.layoutControlMap = new TreeMap<>();

        this.mainLayoutWrapper = new HorizontalLayout();
        mainLayoutWrapper.setHeight(100, Unit.PERCENTAGE);
        mainLayoutWrapper.setWidthUndefined();
        this.mainLayoutWrapper.addStyleName("slowslide");
        this.addComponent(mainLayoutWrapper);

        VerticalLayout leftSideContainerWrapper = new VerticalLayout();
        leftSideContainerWrapper.setHeight(100, Unit.PERCENTAGE);

        leftSideContainerWrapper.setSpacing(true);
        mainLayoutWrapper.addComponent(leftSideContainerWrapper);

        leftSideContainer = new VerticalLayout();

        leftSideContainer.setHeightUndefined();
        leftSideContainer.setSpacing(true);
        leftSideContainerWrapper.addComponent(leftSideContainer);
        leftSideContainerWrapper.setComponentAlignment(leftSideContainer, Alignment.TOP_CENTER);

        HorizontalLayout mainViewContainerFrame = new HorizontalLayout();
        mainLayoutWrapper.addComponent(mainViewContainerFrame);
        mainViewContainerFrame.setHeight(100, Unit.PERCENTAGE);

        leftSideContainerWrapper.setWidth(120, Unit.PIXELS);
        leftSideContainer.setWidth(120, Unit.PIXELS);
        mainViewContainerFrame.setWidth(bodyWidth - 120 - 55, Unit.PIXELS);

        mainViewContainerFrame.addStyleName("mainviewport");
        mainViewContainer = new HorizontalLayout();
        mainViewContainerFrame.addComponent(mainViewContainer);
        mainViewContainer.setWidthUndefined();
        mainViewContainer.setHeightUndefined();
        rightSideToolBtnContainer = new VerticalLayout();
        rightSideToolBtnContainer.setStyleName("sidebtnsmenue");
        rightSideToolBtnContainer.setHeightUndefined();
        rightSideToolBtnContainer.setWidthUndefined();
        mainLayoutWrapper.addComponent(rightSideToolBtnContainer);
        mainLayoutWrapper.setComponentAlignment(rightSideToolBtnContainer, Alignment.BOTTOM_RIGHT);

    }

    /**
     * Add visualization layout include side button, main layout, control
     * buttons, and if the layout is default or not
     *
     * @param Btn left side control button.
     * @param mainViewLayout main visualization layout (middle panel content).
     * @param toolBtnsLayout right side tool buttons layout.
     * @param isDefault The view will be used as default visualization.
     */
    public void addVisualization(Layout Btn, AbstractOrderedLayout mainViewLayout, AbstractOrderedLayout toolBtnsLayout, boolean isDefault) {
        VerticalLayout btnWrapper = new VerticalLayout(Btn);
        btnWrapper.addStyleName("sidebtnwrapper");
        btnWrapper.addStyleName("unselectedbtn");
        btnWrapper.setWidth(100, Unit.PERCENTAGE);
        leftSideContainer.addComponent(btnWrapper);

        btnWrapper.addLayoutClickListener(this);
        Btn.setEnabled(false);
        Btn.setReadOnly(false);

        layoutControlMap.put(leftSideContainer.getComponentIndex(btnWrapper), mainViewLayout);
        mainViewContainer.addComponent(mainViewLayout);
        mainViewLayout.addStyleName("slowslide");
        mainViewLayout.addStyleName("hideslidelayout");

        if (toolBtnsLayout != null) {
            this.rightSideToolBtnContainer.addComponent(toolBtnsLayout);
            this.rightSideToolBtnContainer.setComponentAlignment(toolBtnsLayout, Alignment.MIDDLE_RIGHT);
            mainViewLayout.setData(toolBtnsLayout);
            toolBtnsLayout.setVisible(false);
        } else {

            mainViewLayout.setData(null);
        }
        if (isDefault) {
            defaultView = mainViewLayout;
            defaultBtn = btnWrapper;
            mainViewLayout.removeStyleName("hideslidelayout");
            btnWrapper.removeStyleName("unselectedbtn");
            if ((AbstractOrderedLayout) mainViewLayout.getData() != null) {
                ((AbstractOrderedLayout) mainViewLayout.getData()).setVisible(true);
                rightSideToolBtnContainer.setStyleName("sidebtnsmenue");
            } else {
                rightSideToolBtnContainer.removeStyleName("sidebtnsmenue");
            }
            currentView = mainViewLayout;
            currentBtn = btnWrapper;
        }
    }

    /**
     * Set the current visualization to the selected view.
     *
     * @param viewName selected visualization name.
     */
    public void setCurrentLayout(String viewName) {
        if (viewName.equalsIgnoreCase("heatmap")) {
            updateVew(leftSideContainer.getComponent(1));
        } else if (viewName.equalsIgnoreCase("proteintable")) {
            updateVew(leftSideContainer.getComponent(3));
        } else if (viewName.equalsIgnoreCase("initiallayout")) {
            updateVew(leftSideContainer.getComponent(0));
        }

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        updateVew(event.getComponent());
    }

    /**
     * Update the current visualization to the selected view component.
     *
     * @param comp selected visualization component.
     */
    private void updateVew(Component comp) {
        AbstractOrderedLayout tempMainViewLayout = layoutControlMap.get(leftSideContainer.getComponentIndex(comp));
        if (currentView == null) {
            currentView = tempMainViewLayout;
            comp.removeStyleName("unselectedbtn");
            currentBtn = comp;
            if ((AbstractOrderedLayout) tempMainViewLayout.getData() != null) {
                ((AbstractOrderedLayout) tempMainViewLayout.getData()).setVisible(true);
                rightSideToolBtnContainer.setStyleName("sidebtnsmenue");
            } else {
                rightSideToolBtnContainer.removeStyleName("sidebtnsmenue");
            }
        } else if (tempMainViewLayout != currentView) {
            currentView.addStyleName("hideslidelayout");
            if ((AbstractOrderedLayout) currentView.getData() != null) {
                ((AbstractOrderedLayout) currentView.getData()).setVisible(false);
            }

            currentBtn.addStyleName("unselectedbtn");

            currentView = tempMainViewLayout;
            currentBtn = comp;
            if ((AbstractOrderedLayout) tempMainViewLayout.getData() != null) {
                ((AbstractOrderedLayout) tempMainViewLayout.getData()).setVisible(true);
                rightSideToolBtnContainer.setStyleName("sidebtnsmenue");
            } else {
                rightSideToolBtnContainer.removeStyleName("sidebtnsmenue");
            }
        }
        tempMainViewLayout.removeStyleName("hideslidelayout");
        comp.removeStyleName("unselectedbtn");
        if ((AbstractOrderedLayout) tempMainViewLayout.getData() != null) {
            ((AbstractOrderedLayout) tempMainViewLayout.getData()).setVisible(true);
            rightSideToolBtnContainer.setStyleName("sidebtnsmenue");
        } else {
            rightSideToolBtnContainer.removeStyleName("sidebtnsmenue");
        }

    }

    /**
     * Set the current visualization to the default layout visualization view.
     */
    public void defaultView() {
        if (defaultView == null) {
            return;
        }
        if (currentView == null) {
            currentView = defaultView;
            currentBtn = defaultBtn;
        } else if (defaultView != currentView) {
            currentView.addStyleName("hideslidelayout");
            currentBtn.addStyleName("unselectedbtn");
            ((AbstractOrderedLayout) currentView.getData()).setVisible(false);
            currentView = defaultView;
            currentBtn = defaultBtn;
        }
        defaultView.removeStyleName("hideslidelayout");
        defaultBtn.removeStyleName("unselectedbtn");
        if ((AbstractOrderedLayout) defaultView.getData() != null) {
            ((AbstractOrderedLayout) defaultView.getData()).setVisible(true);
            rightSideToolBtnContainer.setStyleName("sidebtnsmenue");
        } else {
            rightSideToolBtnContainer.removeStyleName("sidebtnsmenue");
        }

    }

}
