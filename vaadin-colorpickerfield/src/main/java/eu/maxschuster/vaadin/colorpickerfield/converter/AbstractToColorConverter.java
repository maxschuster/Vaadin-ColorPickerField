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

/**
 *
 * @author Max Schuster
 */
public abstract class AbstractToColorConverter<PRESENTATION> implements Converter<PRESENTATION, Color> {

    private static final long serialVersionUID = 1L;
    
    private final Class<PRESENTATION> presentationType;
    
    private final Class<Color> modelType = Color.class;

    public AbstractToColorConverter(Class<PRESENTATION> presentationType) {
        this.presentationType = presentationType;
    }

    @Override
    public Class<Color> getModelType() {
        return this.modelType;
    }

    @Override
    public Class<PRESENTATION> getPresentationType() {
        return this.presentationType;
    }

}
