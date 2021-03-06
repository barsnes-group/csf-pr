package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents stander pop-up window container with frame and no
 * functions button layout
 *
 * @author Yehia Farag
 */
public class PopupWindowFrame {

    /**
     * Stander pop-up window container.
     */
    private final PopupWindow popupWindow;
    /**
     * Main window content.
     */
    private final AbstractOrderedLayout popupBody;

    /**
     * Constructor to initialize the main attributes.
     *
     * @param title The window title.
     * @param popupBody The main window body.
     *
     */
    public PopupWindowFrame(String title, AbstractOrderedLayout popupBody) {
        popupBody.setMargin(false);
        popupBody.setSpacing(true);
        popupBody.addStyleName("roundedborder");
        popupBody.addStyleName("whitelayout");
        popupBody.addStyleName("padding20");
        popupBody.addStyleName("scrollable");
        popupBody.addStyleName("margin");
        popupBody.addStyleName("minh450");

        this.popupBody = popupBody;

        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(100, Unit.PERCENTAGE);
        frame.setHeight(100, Unit.PERCENTAGE);
        frame.setSpacing(true);
        frame.addComponent(popupBody);
        popupWindow = new PopupWindow(frame, title) {

            @Override
            public void close() {
                popupWindow.setVisible(false);

            }

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

        };

        popupBody.setWidth(popupWindow.getWidth() - 24, Unit.PIXELS);

    }

    /**
     * Set the window and sub content height.
     *
     * @param height the height of the window.
     */
    public void setFrameHeight(int height) {
        popupWindow.setHeight(Math.max(height, 61), Unit.PIXELS);
        popupBody.setHeight(popupWindow.getHeight() - 60, Unit.PIXELS);
    }

    /**
     * Show the pop-up window.
     */
    public void view() {
        popupWindow.setVisible(true);
    }

    /**
     * Get the pop-up window width.
     *
     * @return the window width.
     */
    public int getFrameWidth() {
        return (int) popupWindow.getWidth();
    }

    /**
     * Set the window and sub content width.
     *
     * @param width the with of the window.
     */
    public void setFrameWidth(int width) {
        popupWindow.setWidth(width - 20, Unit.PIXELS);
        popupBody.setWidth(popupWindow.getWidth() - 24, Unit.PIXELS);
    }

}
