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

import com.vaadin.annotations.StyleSheet;
import com.vaadin.v7.shared.ui.colorpicker.Color;
import com.vaadin.v7.ui.ColorPicker;
import com.vaadin.v7.ui.ColorPickerArea;
import com.vaadin.v7.ui.Field;

/**
 * A class that defines area-like implementation for a color picker {@link Field}.
 * Wraps a {@link ColorPickerArea}.
 * 
 * @author Max Schuster
 * @see ColorPicker
 */
@StyleSheet("ColorPickerAreaField.css")
public class ColorPickerAreaField extends AbstractColorPickerField<ColorPickerArea> {

    private static final long serialVersionUID = 1L;

    private static final String STYLE_NAME = "color-picker-area-field";

    /**
     * Instantiates a new color picker area field.
     */
    public ColorPickerAreaField() {
        this(DEFAULT_POPUP_CAPTION, DEFAULT_INITIAL_COLOR);
    }

    /**
     * Instantiates a new color picker area field.
     * 
     * @param popupCaption Caption of the popup window
     */
    public ColorPickerAreaField(String popupCaption) {
        this(popupCaption, DEFAULT_INITIAL_COLOR);
    }

    /**
     * Instantiates a new color picker area field.
     * 
     * @param popupCaption Caption of the popup window
     * @param initialColor Initial {@link Color}
     */
    public ColorPickerAreaField(String popupCaption, Color initialColor) {
        super(ColorPickerArea.class, popupCaption, initialColor);
        setPrimaryStyleName(STYLE_NAME);
    }

}
