package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

/**
 * This class contains pool sampling filter which allows user to show/hide pool
 * samples datasets
 *
 * @author Yehia Farag
 */
public abstract class PooledSamplesFilter extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * Constructor to initialize the layout.
     */
    public PooledSamplesFilter() {
        this.setDescription("Include pooled samples datasets");
        this.setStyleName("filterbtn");
        this.addStyleName("smallpaading");
        this.addStyleName("applied");
        Image icon = new Image();
        icon.setSource(new ThemeResource("img/group.png"));
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(PooledSamplesFilter.this);
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (this.getStyleName().contains("unapplied")) {
            this.removeStyleName("unapplied");
            this.addStyleName("applied");
        } else {
            this.removeStyleName("applied");
            this.addStyleName("unapplied");
        }
        updateSystem(isHidePoolSamplesDatasets());
    }

    /**
     * Check if hide pool sample datasets is applied
     *
     * @return hidePoolSamplesDatasets hide pool samples datasets
     */
    public boolean isHidePoolSamplesDatasets() {
        return this.getStyleName().contains("unapplied");
    }

    /**
     * Reset the both buttons to initial state (CSF datasets included and SERUM
     * datasets excluded).
     */
    public void resetFilter() {
        this.addStyleName("applied");
        this.removeStyleName("unapplied");
        updateSystem(false);
    }

    /**
     * Update the system (hide/show pool samples datasets) based on user
     * selection
     *
     * @param hidePoolSamplesDatasets hide pool samples datasets
     */
    public abstract void updateSystem(boolean hidePoolSamplesDatasets);

}
