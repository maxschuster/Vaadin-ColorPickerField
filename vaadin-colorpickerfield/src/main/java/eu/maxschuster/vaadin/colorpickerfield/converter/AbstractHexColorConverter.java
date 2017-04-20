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

import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.shared.ui.colorpicker.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for {@link Converter}s that convert between CSS hex color and {@link Color}
 * 
 * @author Max Schuster
 * @param <PRESENTATION> The presentation type. Must be compatible with what
 * {@link #getPresentationType()} returns.
 * @param <MODEL> The model type. Must be compatible with what
 * {@link #getModelType()} returns.
 */
public abstract class AbstractHexColorConverter<PRESENTATION, MODEL> extends AbstractColorConverter<PRESENTATION, MODEL> {
    
    private static final long serialVersionUID = 1L;

    public AbstractHexColorConverter(Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
        super(presentationType, modelType);
    }
    
    private static final Pattern HEX_PATTERN = Pattern.compile(
            "^(#)?([0-9a-f]{3}|[0-9a-f]{6})$", Pattern.CASE_INSENSITIVE);

    @Override
    protected String serializeColor(Color color) throws ConversionException {
        return color.getCSS();
    }

    @Override
    protected Color unserializeColor(String string) throws ConversionException {
        Matcher m = HEX_PATTERN.matcher(string);
        if (!m.matches()) {
            throw new ConversionException("Could not convert '" + string
                    + "' to a css hex color");
        }
        String hex = m.group(2);
        if (hex.length() == 3) {
            // Maybe a mathematically whay to do this?
            StringBuilder sb = new StringBuilder(6);
            for (char character : hex.toCharArray()) {
                sb.append(character).append(character);
            }
            hex = sb.toString();
        }
        return new Color(Integer.parseInt(hex, 16));
    }
    
}
