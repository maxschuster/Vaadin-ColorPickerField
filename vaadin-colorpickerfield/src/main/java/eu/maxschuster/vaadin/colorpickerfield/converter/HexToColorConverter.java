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
package eu.maxschuster.vaadin.colorpickerfield.converter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.colorpicker.Color;
import java.util.Locale;

/**
 * A {@link Converter} that can convert a {@link Color} to CSS hex color
 * notation and back
 * 
 * @author Max Schuster
 */
public class HexToColorConverter extends AbstractHexColorConverter<String, Color> {

    private static final long serialVersionUID = 3L;

    public HexToColorConverter() {
        super(String.class, Color.class);
    }

    @Override
    public Color convertToModel(String value, Class<? extends Color> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }
        return unserializeColor(value);
    }

    @Override
    public String convertToPresentation(Color value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }
        return serializeColor(value);
    }
    
}
