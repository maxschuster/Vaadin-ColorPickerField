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
 * Base class for {@link Converter}s that convert to or from {@link Color}
 * 
 * @author Max Schuster
 * @param <PRESENTATION> The presentation type. Must be compatible with what
 * {@link #getPresentationType()} returns.
 * @param <MODEL> The model type. Must be compatible with what
 * {@link #getModelType()} returns.
 */
public abstract class AbstractColorConverter<PRESENTATION, MODEL> implements Converter<PRESENTATION, MODEL> {
    
    private static final long serialVersionUID = 1L;

    /**
     * The presentation type
     */
    private final Class<PRESENTATION> presentationType;

    /**
     * The model type
     */
    private final Class<MODEL> modelType;

    public AbstractColorConverter(Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
        this.presentationType = presentationType;
        this.modelType = modelType;
    }
    
    @Override
    public Class<MODEL> getModelType() {
        return this.modelType;
    }

    @Override
    public Class<PRESENTATION> getPresentationType() {
        return this.presentationType;
    }

    /**
     * Serializes the given {@link Color} as a {@link String}. The String must
     * be unserialize by {@link #unserializeColor(java.lang.String)}
     *
     * @param color The {@link Color} to serialize. Never {@code null}.
     * @return {@link Color} serialized as {@link String}
     * @throws ConversionException If the {@link Color} can't be serialized
     */
    protected abstract String serializeColor(Color color) throws ConversionException;

    /**
     * Unserializes the given {@link String} as a {@link Color}.
     *
     * @param string The {@link String} to unserialize. Never {@code null}.
     * @return {@link String} unserialized as {@link Color}
     * @throws ConversionException If the {@link String} can't be unserialized
     */
    protected abstract Color unserializeColor(String string) throws ConversionException;
    
}
