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
 * A {@link Converter} that can convert a {@link Color} to a {@link String}
 * presentation
 *
 * @author Max Schuster
 */
public abstract class AbstractStringToColorConverter extends AbstractToColorConverter<String> {

    private static final long serialVersionUID = 2L;

    /**
     * Constructs a new {@link AbstractStringToColorConverter}
     */
    public AbstractStringToColorConverter() {
        super(String.class);
    }

    @Override
    public Color convertToModel(String value, Class<? extends Color> targetType,
            Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }
        return unserialize(value);
    }

    @Override
    public String convertToPresentation(Color value, Class<? extends String> targetType,
            Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }
        return serialize(value);
    }

    /**
     * Serializes the given {@link Color} as a {@link String}. The String must
     * be unserialize by {@link #unserialize(java.lang.String)}
     *
     * @param color The {@link Color} to serialize. Never {@code null}.
     * @return {@link Color} serialized as {@link String}
     * @throws ConversionException If the {@link Color} can't be serialized
     */
    protected abstract String serialize(Color color) throws ConversionException;

    /**
     * Unserializes the given {@link String} as a {@link Color}.
     *
     * @param string The {@link String} to unserialize. Never {@code null}.
     * @return {@link String} unserialized as {@link Color}
     * @throws ConversionException If the {@link String} can't be unserialized
     */
    protected abstract Color unserialize(String string) throws ConversionException;

}
