package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This class represents text area component that has title, text area and clear
 * text button.
 *
 * @author Yehia Farag
 */
public class TextAreaComponent extends VerticalLayout {

    /**
     * Main text area.
     */
    private final TextArea textBox;

    /**
     * Constructor to initialize the main attributes and layout components.
     *
     * @param labelValue The header title.
     * @param height the height of the component.
     */
    public TextAreaComponent(String labelValue, int height) {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setSpacing(true);
        this.addStyleName("margintop");
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(topLayout);

        Label label = new Label(labelValue);
        label.setContentMode(ContentMode.HTML);
        label.setStyleName(ValoTheme.LABEL_TINY);
        label.addStyleName(ValoTheme.LABEL_SMALL);
        topLayout.addComponent(label);

        VerticalLayout clearBtn = new VerticalLayout();
        topLayout.addComponent(clearBtn);
        clearBtn.addStyleName("pointer");
        Image icon = new Image();
        icon.addStyleName("pointer");
        icon.setSource(new ThemeResource("img/eraser.png"));
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        clearBtn.addComponent(icon);

        topLayout.setComponentAlignment(clearBtn, Alignment.TOP_RIGHT);
        clearBtn.setDescription("Clear field");
        clearBtn.setStyleName("clearfieldbtn");
        clearBtn.setWidth(20, Unit.PIXELS);
        clearBtn.setHeight(20, Unit.PIXELS);

        textBox = new TextArea();
        textBox.setWidth(100, Unit.PERCENTAGE);
        textBox.setHeight(height, Unit.PIXELS);
        this.addComponent(textBox);

        clearBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            textBox.clear();
        });

    }

    /**
     * Set input text.
     *
     * @param text to fill the text area.
     */
    public void setText(String text) {
        textBox.setValue(text);
    }

    /**
     * Reset the text area (clear all data).
     */
    public void reset() {
        textBox.clear();
    }

    /**
     * Get input text value.
     *
     * @return value of the text area.
     */
    public String getText() {
        return textBox.getValue();
    }
}
