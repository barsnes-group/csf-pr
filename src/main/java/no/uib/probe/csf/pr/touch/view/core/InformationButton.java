package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This class represents Information (!) button that provide users with the
 * required information.
 *
 * @author Yehia Farag
 *
 *
 */
public class InformationButton extends ImageContainerBtn {

    /**
     * Main information component pop-up window.
     */
    private final PopupView popupLayout;
    /**
     * Main information component pop-up body.
     */
    private final VerticalLayout popupBodyLayout;

    /**
     * Constructor to initialize the main attributes.
     *
     * @param infoText Main information text.
     * @param smallBtn Small information button.
     */
    public InformationButton(String infoText,boolean smallBtn) {
        updateIcon(new ThemeResource("img/help.png"));
        VerticalLayout mainBody = new VerticalLayout();
        mainBody.setHeightUndefined();

        mainBody.setStyleName("border");
        mainBody.addStyleName("infoLayout");
        mainBody.addStyleName("maxwidth500");
        mainBody.addStyleName("maxhight500");
        this.addStyleName("pointer");

        this.setDescription("Click to view information");
        this.setEnabled(true);
        this.setReadOnly(false);

        popupBodyLayout = new VerticalLayout();

        HorizontalLayout containerLayout = new HorizontalLayout();
        containerLayout.setWidth(100, Unit.PERCENTAGE);
        mainBody.addComponent(containerLayout);

        Label infoHeaderLabel = new Label("Information");
        infoHeaderLabel.setStyleName(ValoTheme.LABEL_H3);
        Label infoLable = new Label(infoText);
        infoLable.setContentMode(ContentMode.HTML);
        infoLable.setWidth(100, Unit.PERCENTAGE);
        infoLable.addStyleName("infolabel");
        containerLayout.addComponent(infoLable);
        containerLayout.setExpandRatio(infoLable, 460);
        popupBodyLayout.addComponent(mainBody);

        CloseButton closeBtn = new CloseButton();
        containerLayout.addComponent(closeBtn);
        containerLayout.setExpandRatio(closeBtn, 40);
        containerLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);
        popupLayout = new PopupView(null, popupBodyLayout) {

            @Override
            public void setPopupVisible(boolean visible) {
                super.setPopupVisible(visible);
                this.setVisible(visible);
            }

        };
        popupLayout.setVisible(false);
        this.addComponent(popupLayout);
        this.popupLayout.setHideOnMouseOut(false);

        closeBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            popupLayout.setPopupVisible(false);
        });
        if (smallBtn) {
            this.setHeight(30, Unit.PIXELS);
            this.setWidth(30, Unit.PIXELS);
            this.addStyleName("midimg");
            popupLayout.addStyleName("infoLayout");

        } else {
            this.setHeight(40, Unit.PIXELS);
            this.setWidth(40, Unit.PIXELS);
            this.addStyleName("smallimg");
            popupLayout.addStyleName("biginfo");
        }

    }

    /**
     * On click view pop-up information panel.
     */
    @Override
    public void onClick() {
        popupLayout.setPopupVisible(true);
    }

}
