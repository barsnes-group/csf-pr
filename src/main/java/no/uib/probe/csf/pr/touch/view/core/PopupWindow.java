package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This class represents stander pop-up window container.
 *
 * @author Yehia Farag
 */
public class PopupWindow extends Window {

    /**
     * Slow pop-up for the window.
     */
    private boolean lazyLoading = false;
    /**
     * Main window frame.
     */
    private final VerticalLayout mainFrame;
    /**
     * Maximum window height.
     */
    private final int height = Math.max(Page.getCurrent().getBrowserWindowHeight() - 50, 1);
    /**
     * Maximum window width.
     */
    private final int width = Math.max(Page.getCurrent().getBrowserWindowWidth() - 50, 1);

    /**
     * Constructor to initialize the main attributes.
     *
     * @param layout The main layout to be placed inside the window.
     * @param title The window title.
     */
    public PopupWindow(Layout layout, String title) {

        mainFrame = new VerticalLayout();
        mainFrame.setWidth(100, Unit.PERCENTAGE);
        mainFrame.setHeight(100, Unit.PERCENTAGE);
        setContent(mainFrame);
        mainFrame.addStyleName("mainviewport");

        mainFrame.addComponent(layout);
        mainFrame.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

        setWindowMode(WindowMode.NORMAL);
        setWidth((width), Unit.PIXELS);
        PopupWindow.this.setHeight((height), Unit.PIXELS);
        PopupWindow.this.setVisible(false);
        setResizable(false);
        setModal(true);
        setDraggable(false);
        this.addStyleName("hideoverflow");
        center();
        setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;" + title + "</font>");
        UI.getCurrent().addWindow(PopupWindow.this);
        setCaptionAsHtml(true);
        setClosable(true);
    }

    /**
     * On close the window just hide it.
     */
    @Override
    public void close() {
        this.setVisible(false);

    }

    /**
     * If slow lazy loading window set lazy pop up.
     *
     * @param visible View the component
     */
    @Override
    public void setVisible(boolean visible) {

        if (visible && lazyLoading) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
        super.setVisible(visible);
    }

    /**
     * Set slow pop-up for the window.
     *
     * @param lazyLoading lazy pop up viewing.
     */
    public void setLazyLoading(boolean lazyLoading) {
        this.lazyLoading = lazyLoading;
    }

    /**
     * Set the window height to fit the screen size.
     *
     * @param height the updated height to be ignored if bigger than screen
     * height.
     * @param unit the height unit.
     */
    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(Math.min(height, this.height), unit);
        center();
        this.markAsDirty();
    }
    
     @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(Math.min(width, this.width), unit);
        center();
        this.markAsDirty();
    }

}
