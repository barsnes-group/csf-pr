package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents View Panel with dynamic left scrolling and folding
 * support ( touch friendly slide panel).
 *
 * @author Yehia Farag
 */
public class SlidePanel extends HorizontalLayout implements ControllingView {

    /**
     * The expanding layout container.
     */
    private final Layout mainLayout;
    /**
     * The minimized layout container.
     */
    private final Layout miniLayout;
    /**
     * The component unique id.
     */
    private final String viewId;

    @Override
    public String getViewId() {
        return viewId;
    }

    @Override
    public void view() {
        setShowPanel(true);
    }

    @Override
    public void hide() {
        setVisible(false);
    }

    /**
     * Constructor to initialize the Slide touch friendly panel
     *
     * @param mainLayout main panel components
     * @param miniLayout mini layout on close panel
     * @param orientation the slide direction 0 slide to left any other value
     * slide to right
     * @param viewId the view id is a unique id used for view manager
     */
    public SlidePanel(Layout mainLayout, Layout miniLayout, int orientation, String viewId) {
        this.mainLayout = mainLayout;
        this.viewId = viewId;
        if (miniLayout == null) {
            miniLayout = new VerticalLayout();
        }
        this.miniLayout = miniLayout;
        miniLayout.addStyleName("slowslide");
        miniLayout.addStyleName("hideslidelayout");
        mainLayout.addStyleName("slowslide");
        if (orientation == 0) {
            this.addComponent(miniLayout);
            this.addComponent(mainLayout);

        } else {
            this.addComponent(mainLayout);
            this.addComponent(miniLayout);
        }
        this.setHeight(100, Unit.PERCENTAGE);
        this.setStyleName("slidepanel");
        this.setWidthUndefined();

    }

    /**
     * Set showPanel (expand) full panel (Main layout)
     *
     * @param showPanel expand the view panel.
     */
    public final void setShowPanel(boolean showPanel) {
        if (showPanel) {
            this.setVisible(true);
            this.mainLayout.removeStyleName("hideslidelayout");
            this.miniLayout.addStyleName("hideslidelayout");
        } else {
            this.mainLayout.addStyleName("hideslidelayout");
            this.miniLayout.removeStyleName("hideslidelayout");
        }

    }

}
