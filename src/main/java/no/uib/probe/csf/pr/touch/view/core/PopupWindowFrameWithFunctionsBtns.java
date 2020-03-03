package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents stander pop-up window container with frame and
 * functions button layout.
 *
 * @author Yehia Farag
 */
public class PopupWindowFrameWithFunctionsBtns {

    /**
     * Stander pop-up window container.
     */
    private final PopupWindow popupWindow;
    /**
     * Main body content.
     */
    private final AbstractOrderedLayout popupBody;
    /**
     * Main buttons container content.
     */
    private final AbstractOrderedLayout btnsLayout;
    /**
     * Main contents frame.
     */
    private final VerticalLayout frame;

    /**
     * Constructor to initialize the main attributes.
     *
     * @param title The window title.
     * @param popupBody The main window body.
     * @param btnsLayout The main window buttons layout.
     *
     */
    public PopupWindowFrameWithFunctionsBtns(String title, AbstractOrderedLayout popupBody, AbstractOrderedLayout btnsLayout) {
        popupBody.setMargin(false);
        popupBody.setSpacing(true);
        popupBody.addStyleName("roundedborder");
        popupBody.addStyleName("whitelayout");
        popupBody.addStyleName("padding20");
        popupBody.addStyleName("scrollable");
        popupBody.addStyleName("margin");

        this.popupBody = popupBody;

        frame = new VerticalLayout();
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
                super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }

        };

        popupBody.setWidth(popupWindow.getWidth() - 24, Unit.PIXELS);
        this.btnsLayout = btnsLayout;
        frame.addComponent(this.btnsLayout);
        btnsLayout.addStyleName("roundedborder");
        btnsLayout.addStyleName("whitelayout");
        btnsLayout.addStyleName("padding10");
        btnsLayout.addStyleName("marginleft");
        btnsLayout.addStyleName("marginbottom");
        btnsLayout.setHeight(50, Unit.PIXELS);
        btnsLayout.setWidth(popupWindow.getWidth() - 24, Unit.PIXELS);
        frame.setExpandRatio(popupBody, popupWindow.getHeight() - 110);
        frame.setExpandRatio(this.btnsLayout, 50);

    }

    /**
     * Set the window and sub content height.
     *
     * @param height the height of the window.
     */
    public void setFrameHeight(int height) {

        popupWindow.setHeight(Math.max(height, 111), Unit.PIXELS);
        popupBody.setHeight(popupWindow.getHeight() - 120, Unit.PIXELS);
        frame.setExpandRatio(popupBody, popupWindow.getHeight() - 120);
        frame.setExpandRatio(this.btnsLayout, 60);

    }

    /**
     * Show the pop-up window.
     */
    public void view() {
        popupWindow.setVisible(!popupWindow.isVisible());
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
     * Get the pop-up window height.
     *
     * @return the window height.
     */
    public int getFrameHeight() {
        return (int) popupWindow.getHeight();
    }

    /**
     * Set the window and sub content width.
     *
     * @param width the with of the window.
     */
    public void setFrameWidth(int width) {
        popupWindow.setWidth(width - 20, Unit.PIXELS);
        popupBody.setWidth(popupWindow.getWidth() - 24, Unit.PIXELS);
        btnsLayout.setWidth(popupWindow.getWidth() - 24, Unit.PIXELS);
    }

}
