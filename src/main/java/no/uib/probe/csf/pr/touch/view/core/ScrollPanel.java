package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents View Panel with dynamic top scrolling and folding
 * support ( touch friendly slide panel).
 *
 * @author Yehia Farag
 */
public class ScrollPanel extends VerticalLayout implements ControllingView {

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
        setShowPanel(false);
    }

    /**
     * Constructor to initialize the Slide touch friendly panel
     *
     * @param mainLayout main panel components
     * @param miniLayout mini layout on close panel
     * @param orientation the scroll direction 0 scroll to up any other scroll
     * bottom slide to right
     * @param viewId the view id is a unique id used for view manager
     */
    public ScrollPanel(Layout mainLayout, Layout miniLayout, int orientation, String viewId) {
        this.viewId = viewId;
        this.mainLayout = mainLayout;

        if (miniLayout == null) {
            miniLayout = new VerticalLayout();
        }
        this.miniLayout = miniLayout;
        miniLayout.addStyleName("slowscroll");
        miniLayout.addStyleName("hidescrolllayout");
        mainLayout.addStyleName("slowscroll");
        miniLayout.addStyleName("topbtns");
        miniLayout.addStyleName("rightbtns");
        if (orientation == 0) {

            this.addComponent(mainLayout);
            this.addComponent(miniLayout);

        } else {
            this.addComponent(miniLayout);
            this.addComponent(mainLayout);

        }
        this.setWidthUndefined();
        this.setHeightUndefined();
        this.setStyleName("scrollpanel");
        this.addStyleName("slowslide");
        this.setWidthUndefined();

    }

    /**
     * Set showPanel (expand) full panel (Main layout)
     *
     * @param showPanel expand the view panel.
     */
    public final void setShowPanel(boolean showPanel) {
        if (showPanel) {
            this.mainLayout.removeStyleName("hidescrolllayout");
            this.miniLayout.addStyleName("hidescrolllayout");
            this.removeStyleName("hidescrollpanel");
            this.setWidthUndefined();

        } else {
            this.mainLayout.addStyleName("hidescrolllayout");
            this.miniLayout.removeStyleName("hidescrolllayout");
            this.addStyleName("hidescrollpanel");

        }

    }

}
