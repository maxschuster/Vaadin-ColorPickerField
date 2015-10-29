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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 * @author Max Schuster
 */
public abstract class AbstractStringToColorConverterTest<CONVERTER extends AbstractStringToColorConverter> {
    
    private final CONVERTER converter;
    
    private final List<Color> models;
    
    private final List<String> presentations;

    public AbstractStringToColorConverterTest(CONVERTER converter, 
            List<Color> models, List<String> presentations) {
        this.converter = converter;
        this.models = Collections.unmodifiableList(models);
        this.presentations = Collections.unmodifiableList(presentations);
    }

    public CONVERTER getConverter() {
        return converter;
    }

    public List<Color> getModels() {
        return models;
    }

    public List<String> getPresentations() {
        return presentations;
    }
    
    @Test
    public void convertModelToPresentation() {
        assertEquals(models.size(), presentations.size());
        CONVERTER conv = getConverter();
        int size = presentations.size();
        for (int i = 0; i < size; i++) {
            String presentation = presentations.get(i);
            Color model = models.get(i);
            String convertedPresentation =
                    conv.convertToPresentation(model, String.class, Locale.US);
            assertEquals("Error converting model '" + model.getCSS() + 
                    "' to presentation '" + presentation + "'", 
                    convertedPresentation.toLowerCase(),
                    presentation.toLowerCase());
        }
    }
    
    @Test
    public void convertPresentationToModel() {
        assertEquals(models.size(), presentations.size());
        CONVERTER conv = getConverter();
        int size = models.size();
        for (int i = 0; i < size; i++) {
            String presentation = presentations.get(i);
            Color model = models.get(i);
            Color convertedModel =
                    conv.convertToModel(presentation, Color.class, Locale.US);
            assertEquals("Error converting presentation '" + presentation + 
                    "' to model '" + model.getCSS() + "'", convertedModel, model);
        }
    }
    
    @Test
    public void convertNullModelToPresentationShouldReturnNull() {
        CONVERTER conv = getConverter();
        assertNull(conv.convertToPresentation(null, String.class, Locale.US));
    }
    
    @Test
    public void convertNullPresentationToModelShouldReturnNull() {
        CONVERTER conv = getConverter();
        assertNull(conv.convertToModel(null, Color.class, Locale.US));
    }
    
}
