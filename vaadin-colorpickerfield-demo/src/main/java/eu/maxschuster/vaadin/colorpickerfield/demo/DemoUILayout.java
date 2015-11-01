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
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;
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
        Design.read(this);
    }
    
}
