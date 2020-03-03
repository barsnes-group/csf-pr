package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.server.ThemeResource;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;

/**
 * This class is a column filter component that is responsible for filtering
 * protein table.
 *
 * @author Yehia Farag
 *
 *
 */
public abstract class FilterColumnButton extends ImageContainerBtn {

    /**
     * Resource for sort icon.
     */
    private final ThemeResource sortFilterRes;
    /**
     * Resource for filter icon.
     */
    private final ThemeResource filterSortRes;
    /**
     * The component in filtering mode.
     */
    private boolean activeFilter = false;

    /**
     * Constructor to initialize the main attributes.
     */
    public FilterColumnButton() {
        this.setHeight(40, Unit.PIXELS);
        this.setWidth(40, Unit.PIXELS);
        this.sortFilterRes = new ThemeResource("img/filter_sort.png");
        this.filterSortRes = new ThemeResource("img/sort_filter.png");
        this.updateIcon(sortFilterRes);
        this.setEnabled(true);
        this.setReadOnly(false);
        this.setDescription("Sort or filter comparisons");

    }

    /**
     * On click update the component icon.
     */
    @Override
    public void onClick() {
        if (activeFilter) {
            activeFilter = false;
            this.updateIcon(sortFilterRes);

        } else {
            activeFilter = true;
            this.updateIcon(filterSortRes);
        }
        onClickFilter(activeFilter);

    }

    /**
     * Reset to default mode (sorting mode).
     */
    @Override
    public void reset() {
        activeFilter = false;
        this.updateIcon(sortFilterRes);
    }

    /**
     * Perform action (sort table or open filter pop up layout).
     *
     * @param isFilter the component in filtering mode.
     */
    public abstract void onClickFilter(boolean isFilter);

}
