package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * This class contains 2 filters CSF filter allows user to view CSF datasets
 * Serum filter allows user to view Serum datasets users need to select at least
 * on of the filters
 *
 * @author Yehia Farag
 */
public abstract class SerumCsfFilter extends HorizontalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * No CSF datasets button.
     */
    private final VerticalLayout noCSFOptionBtn;
    /**
     * No SERUM datasets button.
     */
    private final VerticalLayout noSerumOptionBtn;
    /**
     * Is SERUM datasets included (No by default).
     */
    private boolean serumApplied = false;
    /**
     * Is CSF datasets included (yes by default).
     */
    private boolean csfApplied = true;

    /**
     * Constructor to initialize the main attributes.
     */
    public SerumCsfFilter() {
        this.setWidthUndefined();
        this.setHeightUndefined();
        this.setSpacing(true);

        noCSFOptionBtn = new VerticalLayout();
        noCSFOptionBtn.setDescription("Include CSF datasets");
        this.addComponent(noCSFOptionBtn);
        this.setComponentAlignment(noCSFOptionBtn, Alignment.BOTTOM_LEFT);

        noCSFOptionBtn.setStyleName("filterbtn");
        Image noCSFIcon = new Image();
        noCSFIcon.setSource(new ThemeResource("img/bluedrop.png"));
        noCSFOptionBtn.addComponent(noCSFIcon);
        noCSFOptionBtn.setComponentAlignment(noCSFIcon, Alignment.TOP_CENTER);
        noCSFIcon.setHeight(100, Unit.PERCENTAGE);
        noCSFOptionBtn.addStyleName("applied");
        noCSFOptionBtn.addLayoutClickListener(SerumCsfFilter.this);

        noCSFOptionBtn.setData("csfBtn");

        noSerumOptionBtn = new VerticalLayout();
        noSerumOptionBtn.setDescription("Include serum datasets");
        this.addComponent(noSerumOptionBtn);
        this.setComponentAlignment(noSerumOptionBtn, Alignment.BOTTOM_RIGHT);

        noSerumOptionBtn.setStyleName("filterbtn");
        Image noSerumIcon = new Image();
        noSerumIcon.setSource(new ThemeResource("img/reddrop.png"));
        noSerumOptionBtn.addComponent(noSerumIcon);
        noSerumOptionBtn.setComponentAlignment(noSerumIcon, Alignment.TOP_CENTER);
        noSerumIcon.setHeight(100, Unit.PERCENTAGE);
        noSerumOptionBtn.addStyleName("unapplied");
        noSerumOptionBtn.addLayoutClickListener(SerumCsfFilter.this);
        noSerumOptionBtn.setData("serumBtn");

    }

    /**
     * On click method used to check the selected button and update the system.
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {

        VerticalLayout clickedBtn = (VerticalLayout) event.getComponent();
        if (clickedBtn.getData().toString().equalsIgnoreCase("csfBtn")) {
            if (csfApplied && !serumApplied) {
                Notification.show("You can not hide both CSF and serum datasets", Notification.Type.WARNING_MESSAGE);
            } else if (csfApplied && serumApplied) {
                csfApplied = false;
                clickedBtn.removeStyleName("applied");
                clickedBtn.addStyleName("unapplied");
                //update system
                updateSystem(serumApplied, csfApplied);

            } else if (!csfApplied) {
                csfApplied = true;
                clickedBtn.removeStyleName("unapplied");
                clickedBtn.addStyleName("applied");
                //update system

                updateSystem(serumApplied, csfApplied);
            }

        } else {
            if (!csfApplied && serumApplied) {
                Notification.show("You can not hide both CSF and serum datasets", Notification.Type.WARNING_MESSAGE);
            } else if (csfApplied && serumApplied) {
                serumApplied = false;
                clickedBtn.removeStyleName("applied");
                clickedBtn.addStyleName("unapplied");
                //update system

                updateSystem(serumApplied, csfApplied);

            } else if (!serumApplied) {
                serumApplied = true;
                clickedBtn.removeStyleName("unapplied");
                clickedBtn.addStyleName("applied");
                //update system

                updateSystem(serumApplied, csfApplied);
            }

        }

    }

    /**
     * Get No SERUM datasets button layout
     *
     * @return noSerumOptionBtn Serum button layout.
     */
    public VerticalLayout getNoSerumOptionBtn() {
        return noSerumOptionBtn;
    }

    /**
     * Update the Data Handler to include the selected datasets type
     *
     * @param serumApplied include SERUM datasets
     * @param csfApplied include CSF datasets
     */
    public abstract void updateSystem(boolean serumApplied, boolean csfApplied);

    /**
     * Reset the both buttons to initial state (CSF datasets included and SERUM
     * datasets excluded).
     */
    public void resetFilter() {
        serumApplied = false;
        csfApplied = true;
        noCSFOptionBtn.removeStyleName("unapplied");
        noCSFOptionBtn.addStyleName("applied");

        noSerumOptionBtn.removeStyleName("applied");
        noSerumOptionBtn.addStyleName("unapplied");

        updateSystem(serumApplied, csfApplied);
    }

    /**
     * Update the main buttons size for the filters based on the container size
     *
     * @param resizeFactor Resize factor to update the button size
     */
    public void resizeFilter(double resizeFactor) {
        this.setWidth((int) (53 * resizeFactor), Unit.PIXELS);
        noCSFOptionBtn.setWidth((int) (25 * resizeFactor), Unit.PIXELS);
        noCSFOptionBtn.setHeight((int) (25 * resizeFactor), Unit.PIXELS);
        noSerumOptionBtn.setWidth((int) (25 * resizeFactor), Unit.PIXELS);
        noSerumOptionBtn.setHeight((int) (25 * resizeFactor), Unit.PIXELS);
    }

    /**
     * Check if serum datasets are selected
     *
     * @return if serum dataset option selected
     */
    public boolean isSerumApplied() {
        return serumApplied;
    }

    /**
     * Check if CSF datasets are selected
     *
     * @return if CSF dataset option selected
     */
    public boolean isCsfApplied() {
        return csfApplied;
    }

}
