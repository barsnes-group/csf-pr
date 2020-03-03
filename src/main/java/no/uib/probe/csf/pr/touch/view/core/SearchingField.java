/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This class represents searching field layout
 *
 * @author Yehia Farag
 */
public abstract class SearchingField extends HorizontalLayout {

    /**
     * Comment label to show number of found data.
     */
    private final Label searchingCommentLabel;

    /**
     * Default constructor to initialize the main attributes.
     */
    public SearchingField() {
        this.setSpacing(true);
        this.setHeightUndefined();

        HorizontalLayout searchFieldContainerLayout = new HorizontalLayout();
        searchFieldContainerLayout.setWidthUndefined();
        searchFieldContainerLayout.setHeight(100, Unit.PERCENTAGE);
        searchFieldContainerLayout.setSpacing(true);
        TextField searchField = new TextField();
        searchField.setDescription("Search proteins by name or accession");
        searchField.setImmediate(true);
        searchField.setWidth(100, Unit.PIXELS);
        searchField.setHeight(18, Unit.PIXELS);
        searchField.setInputPrompt("Search...");
        searchFieldContainerLayout.addComponent(searchField);
        searchField.setTextChangeTimeout(1500);
        searchField.setStyleName(ValoTheme.TEXTFIELD_SMALL);
        searchField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        final Button b = new Button();
        searchField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);

        searchField.addShortcutListener(new Button.ClickShortcut(b, ShortcutListener.KeyCode.ENTER));

        VerticalLayout searchingBtn = new VerticalLayout();
        searchingBtn.setWidth(22, Unit.PIXELS);
        searchingBtn.setHeight(18, Unit.PIXELS);
        searchingBtn.setStyleName("tablesearchingbtn");
        searchFieldContainerLayout.addComponent(searchingBtn);
        searchFieldContainerLayout.setComponentAlignment(searchingBtn, Alignment.TOP_CENTER);
        this.addComponent(searchFieldContainerLayout);
        this.setComponentAlignment(searchFieldContainerLayout, Alignment.TOP_CENTER);
        searchingCommentLabel = new Label();
        searchingCommentLabel.setWidth(100, Unit.PERCENTAGE);
        searchingCommentLabel.setHeight(23, Unit.PIXELS);
        searchingCommentLabel.addStyleName(ValoTheme.LABEL_BOLD);
        searchingCommentLabel.addStyleName(ValoTheme.LABEL_SMALL);
        searchingCommentLabel.addStyleName(ValoTheme.LABEL_TINY);
        this.addComponent(searchingCommentLabel);
        this.setComponentAlignment(searchingCommentLabel, Alignment.TOP_CENTER);

        searchingBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            b.click();
        });
        searchField.addTextChangeListener((FieldEvents.TextChangeEvent event) -> {
            if (searchField.getValue().trim().equalsIgnoreCase("")) {
                return;
            }
            SearchingField.this.textChanged(event.getText());
        });
        b.addClickListener((Button.ClickEvent event) -> {
            SearchingField.this.textChanged(searchField.getValue());
        });

    }

    /**
     * Action on text changing (user input data) in the searching field
     *
     * @param text User input data
     */
    public abstract void textChanged(String text);

    /**
     * Update comment label
     *
     * @param text searching filed comment data
     */
    public void updateLabel(String text) {
        this.searchingCommentLabel.setValue(text);
    }

}
