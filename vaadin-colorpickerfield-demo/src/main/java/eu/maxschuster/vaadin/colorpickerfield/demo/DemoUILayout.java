/*
 * Copyright 2015 Max Schuster.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.maxschuster.vaadin.colorpickerfield.demo;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import eu.maxschuster.vaadin.colorpickerfield.ColorPickerAreaField;
import eu.maxschuster.vaadin.colorpickerfield.ColorPickerField;

/**
 * Demo layout fields
 *
 * @author Max Schuster
 */
@DesignRoot
public class DemoUILayout extends VerticalLayout {

    private static final long serialVersionUID = 2L;

    public ComboBox converterComboBox;
    public ColorPickerField colorPickerField;
    public ColorPickerAreaField colorPickerAreaField;
    public TextField colorTextField;
    public CheckBox readOnlyCheckBox;
    public Button clearButton;

    public DemoUILayout() {
        VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Panel panel = new Panel("Vaadin-ColorPickerField");
        panel.setSizeFull();
        HorizontalLayout hl = new HorizontalLayout();
//        hl.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        FormLayout form = new FormLayout();
        form.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        form.setSpacing(true);
        form.setMargin(true);
        hl.addComponent(form);

        colorPickerField = new ColorPickerField();
        colorPickerField.setCaption("ColorPicker(Field)");
        colorPickerField.setSizeFull();
        colorPickerField.setId("colorPickerField");
        form.addComponent(colorPickerField);

        colorPickerAreaField = new ColorPickerAreaField();
        colorPickerAreaField.setCaption("ColorPickerArea(Field)");
        colorPickerAreaField.setSizeFull();
        colorPickerAreaField.setId("colorPickerAreaField");
        form.addComponent(colorPickerAreaField);

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setMargin(true);
        bottomLayout.setSpacing(true);
        bottomLayout.setWidth("300px");
        bottomLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        converterComboBox = new ComboBox("Converter:");
        converterComboBox.setWidth("100%");
        converterComboBox.setId("converterComboBox");
        bottomLayout.addComponent(converterComboBox);

        colorTextField = new TextField("Value:");
        colorTextField.setId("colorTextField");
        colorTextField.setImmediate(true);
        colorTextField.setWidth("100%");
        bottomLayout.addComponent(colorTextField);

        readOnlyCheckBox = new CheckBox("ReadOnly");
        readOnlyCheckBox.setId("readOnlyCheckBox");
        bottomLayout.addComponent(readOnlyCheckBox);

        clearButton = new Button("Clear");
        clearButton.setId("clearButton");
        bottomLayout.addComponent(clearButton);

        hl.addComponent(bottomLayout);
        panel.setContent(hl);
        addComponent(root);
    }
}
