package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * This class allow the progress bar to be visible if the system is doing long
 * processing task.
 *
 * @author Yehia Farag
 */
public class BusyTaskProgressBar extends Window {

    /**
     * Constructor to initialize the main attributes.
     */
    public BusyTaskProgressBar() {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);
        this.setModal(true);
        this.setClosable(false);
        this.setDraggable(false);
        BusyTaskProgressBar.this.setVisible(false);
        this.setResizable(false);
        this.addStyleName("busytaskwindow");
        this.setWindowMode(WindowMode.NORMAL);
        UI.getCurrent().addWindow(BusyTaskProgressBar.this);

    }

    /**
     * Invoke the progress bar.
     *
     * @param visible Visible progress bar
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        super.setVisible(visible);
    }

}
