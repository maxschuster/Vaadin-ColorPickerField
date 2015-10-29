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
package eu.maxschuster.vaadin.colorpickerfield;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.ColorPickerArea;
import com.vaadin.ui.Component;

/**
 *
 * @author Max Schuster
 */
public class ColorPickerAreaFieldFix extends ColorPickerAreaField {

    private static final long serialVersionUID = 1L;
    
    public ColorPickerAreaFieldFix() {
        super();
    }

    public ColorPickerAreaFieldFix(String popupCaption) {
        super(popupCaption);
    }

    public ColorPickerAreaFieldFix(String popupCaption, Color initialColor) {
        super(popupCaption, initialColor);
    }
    
    protected void setChildWidth(Component child) {
        float width = getWidth();
        Unit unit = getWidthUnits();
        if (Unit.PERCENTAGE == unit) {
            child.setWidth(100, Unit.PERCENTAGE);
        } else {
            child.setWidth(width, unit);
        }
    }
    
    protected void setChildHeight(Component child) {
        float width = getHeight();
        Unit unit = getHeightUnits();
        if (Unit.PERCENTAGE == unit) {
            child.setHeight(100, Unit.PERCENTAGE);
        } else {
            child.setHeight(width, unit);
        }
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);
        if (isContentInitialized()) {
            setChildWidth(getContent());
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);
        if (isContentInitialized()) {
            setChildHeight(getContent());
        }
    }

    @Override
    protected ColorPickerArea initContent() {
        ColorPickerArea colorPicker = super.initContent();
        setChildWidth(colorPicker);
        setChildHeight(colorPicker);
        return colorPicker;
    }
    
}
