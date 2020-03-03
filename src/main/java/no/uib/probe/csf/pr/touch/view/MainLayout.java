package no.uib.probe.csf.pr.touch.view;

import no.uib.probe.csf.pr.touch.view.bigscreen.CSFApplicationContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents the main HTML template for CSF-PR 2.0 touch (the main
 * layout container)
 *
 * @author Yehia Farag
 */
public class MainLayout extends VerticalLayout {

    /**
     * Constructor to initialize the main attributes (Database connection data)
     *
     * @param url database URL
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param password database password
     * @param windowWidth Available width for the layout
     * @param windowHeight Available height for the layout
     */
    public MainLayout(String url, String dbName, String driver, String userName, String password, int windowWidth, int windowHeight) {

        this.setWidth(windowWidth, Unit.PIXELS);
        this.setHeight(windowHeight, Unit.PIXELS);

        this.setSpacing(true);
        this.setStyleName("whitelayout");
        windowHeight = windowHeight - 10;
        windowWidth = windowWidth - 10;

        CSFApplicationContainer welcomePageContainerLayout = new CSFApplicationContainer(windowWidth, windowHeight, url, dbName, driver, userName, password);
        this.addComponent(welcomePageContainerLayout);
        this.setComponentAlignment(welcomePageContainerLayout, Alignment.TOP_CENTER);

    }

}
