package no.uib.probe.csf.pr.touch.view.core;

/**
 * This interface for Controlling View components to allow the view manager to
 * control different layout across the system.
 *
 * @author Yehia Farag
 */
public interface ControllingView {

    /**
     * Get the view unique id.
     *
     * @return the view id.
     */
    public String getViewId();

    /**
     * View the layout.
     */
    public void view();

    /**
     * Hide the layout.
     */
    public void hide();
}
