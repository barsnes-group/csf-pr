package no.uib.probe.csf.pr.touch;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.SizeReporter;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import javax.servlet.ServletContext;
import no.uib.probe.csf.pr.touch.view.MainLayout;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("no.uib.probe.csf.pr.touch.CSF_PR_Widgetset")
public class CSF_PR_UI extends UI {

    /**
     * Database URL.
     */
    private String dbURL;
    /**
     * Database name.
     */
    private String dbName;
    /**
     * Database driver.
     */
    private String dbDriver;
    /**
     * Database username.
     */
    private String dbUserName;
    /**
     * Database password.
     */
    private String dbPassword;
    /**
     * This is main application window width and height.
     */
    private int windowHeight, windowWidth;
    /**
     * This is main application layout container.
     */
    private MainLayout layout;

    /**
     * This is notification window to notify users when they use small screen.
     */
    private final Window notificationWindow = new Window();
    /**
     * Google Analytics Trackerto track web application usage.
     */
    private final GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker("UA-88489710-1",
            "http://129.177.231.63/csf-pr/");

    /**
     * Reload the page on changing width and height (turn touch devices to
     * landscape orientation).
     */
    private boolean toReload = false;

    /**
     * Entry point - This is the initial Vaadin request method initialize
     * components and non-component functionality.
     *
     * @param vaadinRequest Main request object.
     */
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        notificationWindow.setWidth(60, Unit.PERCENTAGE);
        notificationWindow.setHeight(20, Unit.PERCENTAGE);
        notificationWindow.setClosable(false);
        notificationWindow.setModal(true);
        notificationWindow.setDraggable(false);
        notificationWindow.setResizable(false);
        notificationWindow.center();
        notificationWindow.setVisible(false);
        UI.getCurrent().addWindow(notificationWindow);
        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();
        notificationWindow.addStyleName("notification");
        notificationWindow.setWindowMode(WindowMode.NORMAL);

        Label message = new Label("Use landscape screen orientation (a bigger screen is recommended)");
        message.addStyleName(ValoTheme.LABEL_H2);
        message.setWidth(70, Unit.PERCENTAGE);
        container.addComponent(message);
        container.setComponentAlignment(message, Alignment.MIDDLE_CENTER);
        notificationWindow.setContent(container);

        ServletContext scx = VaadinServlet.getCurrent().getServletContext();
        dbURL = (scx.getInitParameter("url"));
        dbName = (scx.getInitParameter("dbName"));
        dbDriver = (scx.getInitParameter("driver"));
        dbUserName = (scx.getInitParameter("userName"));
        dbPassword = (scx.getInitParameter("password"));
        String csf_pr_Url = scx.getInitParameter("csf-pr-id");
        VaadinSession.getCurrent().setAttribute("csf_pr_Url", csf_pr_Url);

        this.getPage().setTitle("CSF Proteome Resource (CSF-PR) v2.0");

        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);

        final SizeReporter sizeReporter = new SizeReporter(this);
        sizeReporter.addResizeListener((ComponentResizeEvent event) -> {
            resizeScreen();
        });
        resizeScreen();
        if (toReload) {
            return;
        }

        VerticalLayout appWrapper = new VerticalLayout();
        appWrapper.setWidth(100, Unit.PERCENTAGE);
        appWrapper.setHeight(100, Unit.PERCENTAGE);
        appWrapper.setStyleName("whitelayout");
        appWrapper.addStyleName("border");
        appWrapper.addStyleName("scrollable");

        setContent(appWrapper);

        layout = new MainLayout(dbURL, dbName, dbDriver, dbUserName, dbPassword, windowWidth, windowHeight);
        appWrapper.addComponent(layout);
        appWrapper.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
//        tracker.trackPageview("http://localhost:8084/CSF-PR-2.0-Touch/");

     

    }

    /**
     * Resize the layout on changing window size.
     */
    private void resizeScreen() {
        windowHeight = Math.max(615, Page.getCurrent().getBrowserWindowHeight() - 20);
        windowWidth = Math.max(1004, Page.getCurrent().getBrowserWindowWidth() - 20);
        if (Page.getCurrent().getWebBrowser().isTouchDevice() && Page.getCurrent().getBrowserWindowHeight() > Page.getCurrent().getBrowserWindowWidth()) {
            notificationWindow.setVisible(true);
            toReload = true;
        } else {
            notificationWindow.setVisible(false);
            if (layout == null && toReload) {
                Page.getCurrent().reload();
            }
            toReload = false;
            getWindows().forEach((w) -> {
                w.center();
            });
        }

    }

}
