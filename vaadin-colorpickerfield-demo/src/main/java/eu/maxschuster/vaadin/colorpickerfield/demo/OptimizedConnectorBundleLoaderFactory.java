/*
 * Copyright 2015 Max.
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

import java.util.HashSet;
import java.util.Set;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect.LoadStyle;

public class OptimizedConnectorBundleLoaderFactory extends
        ConnectorBundleLoaderFactory {

    private final Set<String> eagerConnectors = new HashSet<String>();

    {
        eagerConnectors.add(com.vaadin.client.JavaScriptExtension.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.formlayout.FormLayoutConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.button.ButtonConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.combobox.ComboBoxConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.checkbox.CheckBoxConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.csslayout.CssLayoutConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.ui.UIConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.panel.PanelConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.customcomponent.CustomComponentConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.colorpicker.ColorPickerGridConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.tabsheet.TabsheetConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.slider.SliderConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.colorpicker.ColorPickerConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.window.WindowConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.colorpicker.ColorPickerGradientConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.textfield.TextFieldConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.colorpicker.ColorPickerAreaConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.customfield.CustomFieldConnector.class.getName());
    }

    @Override
    protected LoadStyle getLoadStyle(JClassType connectorType) {
        if (eagerConnectors.contains(connectorType.getQualifiedBinaryName())) {
            return LoadStyle.EAGER;
        } else {
            // Loads all other connectors immediately after the initial view has
            // been rendered
            return LoadStyle.DEFERRED;
        }
    }
}
