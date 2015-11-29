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

import com.vaadin.shared.ui.colorpicker.Color;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Max Schuster
 * @param <STC> The {@link String} to {@link Color} converter
 * @param <CTS> The {@link Color} to {@link String} converter
 */
public abstract class AbstractStringToColorConverterTest<STC extends AbstractColorConverter<String, Color>, CTS extends AbstractColorConverter<Color, String>> {

    private final STC stringToColorConverter;
    
    private final CTS colorToStringConverter;

    private final List<Color> colors;

    private final List<String> strings;

    public AbstractStringToColorConverterTest(STC stringToColorConverter,
            CTS colorToStringConverter, List<Color> colors, 
            List<String> string) {
        this.stringToColorConverter = stringToColorConverter;
        this.colorToStringConverter = colorToStringConverter;
        this.colors = Collections.unmodifiableList(colors);
        this.strings = Collections.unmodifiableList(string);
    }

    public STC getStringToColorConverter() {
        return stringToColorConverter;
    }
    
    public CTS getColorToStringConverter() {
        return colorToStringConverter;
    }

    public List<Color> getColors() {
        return colors;
    }

    public List<String> getString() {
        return strings;
    }

    @Test
    public void convertModelToPresentation() {
        Assert.assertEquals(colors.size(), strings.size());
        
        int size = strings.size();
        
        STC stc = getStringToColorConverter();
        CTS cts = getColorToStringConverter();
        
        for (int i = 0; i < size; i++) {
            String string = this.strings.get(i);
            Color color = colors.get(i);
            
            String convertedString =
                    stc.convertToPresentation(color, String.class, Locale.GERMANY);
            Assert.assertEquals("Error converting model '" + color.getCSS()
                    + "' to presentation '" + string + "'",
                    convertedString.toLowerCase(Locale.GERMANY),
                    string.toLowerCase(Locale.GERMANY));
            
            Color convertedColor = 
                    cts.convertToPresentation(string, Color.class, Locale.GERMANY);
            Assert.assertEquals("Error converting model '" + string
                    + "' to presentation '" + color.getCSS() + "'",
                    convertedColor, color);
        }
    }

    @Test
    public void convertPresentationToModel() {
        Assert.assertEquals(colors.size(), strings.size());
        
        int size = colors.size();
        
        STC stc = getStringToColorConverter();
        CTS cts = getColorToStringConverter();
        
        for (int i = 0; i < size; i++) {
            String string = this.strings.get(i);
            Color color = colors.get(i);
            
            Color convertedColor =
                    stc.convertToModel(string, Color.class, Locale.GERMANY);
            Assert.assertEquals("Error converting presentation '" + string
                    + "' to model '" + color.getCSS() + "'", convertedColor, color);
            
            String convertedString =
                    cts.convertToModel(color, String.class, Locale.GERMANY);
            Assert.assertEquals("Error converting presentation '" + color.getCSS()
                    + "' to model '" + string + "'",
                    convertedString.toLowerCase(Locale.GERMANY),
                    string.toLowerCase(Locale.GERMANY));
        }
        
    }

    @Test
    public void convertNullModelToPresentationShouldReturnNull() {
        STC stc = getStringToColorConverter();
        Assert.assertNull(stc.convertToPresentation(null, String.class, Locale.GERMANY));
        
        CTS cts = getColorToStringConverter();
        Assert.assertNull(cts.convertToPresentation(null, Color.class, Locale.GERMANY));
    }

    @Test
    public void convertNullPresentationToModelShouldReturnNull() {
        STC stc = getStringToColorConverter();
        Assert.assertNull(stc.convertToModel(null, Color.class, Locale.GERMANY));
        
        CTS cts = getColorToStringConverter();
        Assert.assertNull(cts.convertToModel(null, String.class, Locale.GERMANY));
    }

}
