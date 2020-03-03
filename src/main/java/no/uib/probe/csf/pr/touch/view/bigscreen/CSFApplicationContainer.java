package no.uib.probe.csf.pr.touch.view.bigscreen;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer.QuantDataLayoutContainer;
import no.uib.probe.csf.pr.touch.view.core.ScrollPanel;
import no.uib.probe.csf.pr.touch.view.bigscreen.welcomepagecontainer.WelcomeLayoutComponents;
import no.uib.probe.csf.pr.touch.view.core.BusyTaskProgressBar;
import no.uib.probe.csf.pr.touch.view.core.SlidePanel;

/**
 * This class represents the welcome page HTML template the class includes
 * buttons for starting the main functions
 *
 * @author Yehia Farag
 *
 */
public class CSFApplicationContainer extends VerticalLayout {

    /**
     * The welcome page container panel.
     */
    private ScrollPanel welcomeLayoutPanel;

    /**
     * The quantitative overview layout container panel.
     */
    private SlidePanel quantLayoutPanel;

    /**
     * The layout manager to control the different visualizations.
     */
    private final LayoutViewManager View_Manager;

    /**
     * The quantitative data handler to work as controller layer to interact
     * between visualizations and logic layer.
     */
    private final Data_Handler Data_handler;

    /**
     * Constructor the initialize the main attributes (Database connection data
     * and layout width and heights)
     *
     * @param width the current available width
     * @param height the current available height
     * @param url database URL
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param password database password
     */
    public CSFApplicationContainer(int width, int height, String url, String dbName, String driver, String userName, String password) {
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.setStyleName("whitelayout");
        this.setSpacing(false);

        BusyTaskProgressBar busyTask = new BusyTaskProgressBar();
        this.View_Manager = new LayoutViewManager(busyTask);
        this.Data_handler = new Data_Handler(url, dbName, driver, userName, password);
        CSFPR_Central_Manager CSFPR_Central_Manager = new CSFPR_Central_Manager(busyTask);

        VerticalLayout bodyWrapper = new VerticalLayout();
        bodyWrapper.setHeightUndefined();
        bodyWrapper.setWidth(100, Unit.PERCENTAGE);
        this.addComponent(bodyWrapper);

        if (Data_handler.getResourceOverviewInformation() == null) {
            Notification.show("No Database available at the moment please try again later", Notification.Type.ERROR_MESSAGE);
            return;
        }
        WelcomeLayoutComponents welcomeContent = new WelcomeLayoutComponents(Data_handler, CSFPR_Central_Manager, View_Manager, width, height, Data_handler.getResourceOverviewInformation(), Data_handler.getPublicationList(), Data_handler.getQuantDatasetList());

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight(height, Unit.PIXELS);
        mainLayout.setWidth(width, Unit.PIXELS);
        mainLayout.setStyleName("whitelayout");
        mainLayout.addComponent(welcomeContent);
        mainLayout.setComponentAlignment(welcomeContent, Alignment.TOP_CENTER);

        this.welcomeLayoutPanel = new ScrollPanel(mainLayout, welcomeContent.getTopRightThumbBtnsLayoutContainer(), 0, "welcomeview");
        View_Manager.registerComponent(welcomeLayoutPanel);
        bodyWrapper.addComponent(welcomeLayoutPanel);
        bodyWrapper.setComponentAlignment(welcomeLayoutPanel, Alignment.TOP_RIGHT);

        QuantDataLayoutContainer quantLayout = new QuantDataLayoutContainer(Data_handler, CSFPR_Central_Manager, width, height);

        quantLayoutPanel = new SlidePanel(quantLayout, null, 1, "quantview") {

            @Override
            public void setVisible(boolean visible) {
                super.setVisible(visible);
                if (this.getParent() != null) {
                    this.getParent().setVisible(visible);
                }
            }

        };
        quantLayoutPanel.setHeight(height, Unit.PIXELS);
        View_Manager.registerComponent(quantLayoutPanel);

        Panel quantPanelContaner = new Panel(quantLayoutPanel);// 
        quantLayoutPanel.setVisible(false);
        quantPanelContaner.setWidth(width, Unit.PIXELS);
        quantPanelContaner.setHeight(height, Unit.PIXELS);
        quantPanelContaner.setStyleName(ValoTheme.PANEL_BORDERLESS);

        bodyWrapper.addComponent(quantPanelContaner);
        bodyWrapper.setComponentAlignment(quantPanelContaner, Alignment.TOP_LEFT);

        View_Manager.viewLayout("welcomeview");

    }

}
