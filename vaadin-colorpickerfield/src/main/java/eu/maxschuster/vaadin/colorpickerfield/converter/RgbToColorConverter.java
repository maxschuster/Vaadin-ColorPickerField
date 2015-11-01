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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link Converter} that can convert a {@link Color} to CSS rgb() color
 * notation and back
 *
 * @author Max Schuster
 */
public class RgbToColorConverter extends AbstractStringToColorConverter {

    private static final long serialVersionUID = 2L;

    private static final Pattern RGB_PATTERN = Pattern.compile(
            "^rgb\\(\\s*(\\d{0,3})\\s*,\\s*(\\d{0,3})\\s*,\\s*(\\d{0,3})\\s*\\)$",
            Pattern.CASE_INSENSITIVE);

    public RgbToColorConverter() {
        super();
    }

    @Override
    protected String serialize(Color color) throws ConversionException {
        return String.format("rgb(%d,%d,%d)",
                color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    protected Color unserialize(String string) throws ConversionException {
        Matcher m = RGB_PATTERN.matcher(string);
        if (!m.matches()) {
            throw new ConversionException("Could not convert '" + string
                    + "' to a css rgb color");
        }
        int red = parseColor(m.group(1));
        int greed = parseColor(m.group(2));
        int blue = parseColor(m.group(3));
        return new Color(red, greed, blue);
    }

    protected int parseColor(String colorString) throws ConversionException {
        if (colorString == null) {
            throw new ConversionException("Color string mustn't be null");
        }
        int color;
        try {
            color = Integer.valueOf(colorString);
        } catch (NumberFormatException e) {
            throw new ConversionException("Can't convert color string '"
                    + colorString + "' to integer", e);
        }
        if (color < 0 || color > 255) {
            throw new ConversionException("Illegal value of color '" + color + "'");
        }
        return color;
    }

}
