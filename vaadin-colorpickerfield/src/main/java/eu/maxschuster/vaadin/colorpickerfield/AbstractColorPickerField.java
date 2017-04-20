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

import com.vaadin.ui.declarative.DesignAttributeHandler;
import com.vaadin.ui.declarative.DesignContext;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.shared.ui.colorpicker.Color;
import com.vaadin.v7.ui.AbstractColorPicker;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.v7.ui.components.colorpicker.ColorChangeListener;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * A wrapper for {@link AbstractColorPicker}s that implements the {@link Field}.
 *
 * @author Max Schuster
 * @param <COLOR_PICKER> Color picker type to wrap
 */
public abstract class AbstractColorPickerField<COLOR_PICKER extends AbstractColorPicker>
        extends CustomField<Color> implements ColorChangeListener {

    private static final long serialVersionUID = 1L;

    /**
     * Default popup caption
     */
    protected static final String DEFAULT_POPUP_CAPTION = "Colors";

    /**
     * Default initial color
     */
    protected static final Color DEFAULT_INITIAL_COLOR = Color.WHITE;

    /**
     * The type of the wrapped color picker
     */
    private final Class<COLOR_PICKER> colorPickerType;

    /**
     * Default color for the client-side when value is {@code null}
     */
    private Color defaultColor = Color.WHITE;

    /**
     * Color used as {@code null} equivalent
     */
    private Color nullRepresentation;

    /**
     * Show popup when content gets initialized
     */
    private boolean showPopup = false;

    /*
     * Properties passed to the color picker:
     */
    private String caption;
    private boolean captionAsHtml;
    private boolean contentInitialized = false;
    private String popupCaption;
    private int positionX = 0;
    private int positionY = 0;
    private boolean defaultCaptionEnabled = false;
    private AbstractColorPicker.PopupStyle popupStyle
            = AbstractColorPicker.PopupStyle.POPUP_NORMAL;
    private boolean rgbVisibility = true;
    private boolean hsvVisibility = true;
    private boolean swatchesVisibility = true;
    private boolean historyVisibility = true;
    private boolean textfieldVisibility = true;

    /**
     * Instantiates a new color picker field.
     *
     * @param pickerType Type of the wrapped color picker
     */
    public AbstractColorPickerField(Class<COLOR_PICKER> pickerType) {
        this(pickerType, DEFAULT_POPUP_CAPTION, DEFAULT_INITIAL_COLOR);
    }

    /**
     * Instantiates a new color picker field.
     *
     * @param pickerType Type of the wrapped color picker
     * @param popupCaption The caption of the popup window
     */
    public AbstractColorPickerField(Class<COLOR_PICKER> pickerType, String popupCaption) {
        this(pickerType, popupCaption, DEFAULT_INITIAL_COLOR);
    }

    /**
     * Instantiates a new color picker.
     *
     * @param pickerType Type of the wrapped color picker
     * @param popupCaption The caption of the popup window
     * @param initialColor The initial color
     */
    public AbstractColorPickerField(Class<COLOR_PICKER> pickerType, String popupCaption, Color initialColor) {
        if (pickerType == null) {
            throw new NullPointerException("The picker type mustn't be null!");
        }
        this.colorPickerType = pickerType;
        this.popupCaption = popupCaption;
        setValue(initialColor);
    }

    @Override
    protected COLOR_PICKER initContent() {
        COLOR_PICKER colorPicker;
        try {
            Constructor<COLOR_PICKER> contructor
                    = colorPickerType.getDeclaredConstructor(String.class);
            colorPicker = contructor.newInstance(popupCaption);
        } catch (ReflectiveOperationException ex) {
            // passthru all reflection exceptions because they should never happen
            throw new RuntimeException(ex);
        }
        colorPicker.setSizeFull();
        colorPicker.setReadOnly(isReadOnly());
        colorPicker.setImmediate(isImmediate());

        colorPicker.setPosition(positionX, positionY);
        colorPicker.setCaption(caption);
        colorPicker.setCaptionAsHtml(captionAsHtml);
        colorPicker.setDefaultCaptionEnabled(defaultCaptionEnabled);
        colorPicker.setPopupStyle(popupStyle);
        colorPicker.setRGBVisibility(rgbVisibility);
        colorPicker.setHSVVisibility(hsvVisibility);
        colorPicker.setSwatchesVisibility(swatchesVisibility);
        colorPicker.setHistoryVisibility(historyVisibility);
        colorPicker.setTextfieldVisibility(textfieldVisibility);

        colorPicker.setColor(getClientColor(getInternalValue()));
        colorPicker.addColorChangeListener(this);

        if (showPopup) {
            colorPicker.showPopup();
        }

        return colorPicker;
    }

    @Override
    public void attach() {
        super.attach();
        // The content should be ready at this time
        contentInitialized = true;
    }

    @Override
    protected COLOR_PICKER getContent() {
        return colorPickerType.cast(super.getContent());
    }

    @Override
    public Class<? extends Color> getType() {
        return Color.class;
    }

    @Override
    public void colorChanged(ColorChangeEvent event) {
        if (event.getComponent() != getContent()) {
            throw new UnsupportedOperationException("This field should not be "
                    + "used as color change listener");
        }
        final Color newValue = event.getColor();
        Color value = null;
        if (newValue != null && (nullRepresentation == null
                || !nullRepresentation.equals(newValue))) {
            value = newValue;
        }
        setValue(value);
    }

    @Override
    public void readOnlyStatusChange(Property.ReadOnlyStatusChangeEvent event) {
        super.readOnlyStatusChange(event);
        if (isContentInitialized()) {
            COLOR_PICKER colorPicker = getContent();
            boolean readOnly = event.getProperty().isReadOnly();
            colorPicker.setReadOnly(readOnly);
            colorPicker.hidePopup();
        }
    }

    @Override
    protected void setInternalValue(Color newValue) {
        super.setInternalValue(newValue);
        if (isContentInitialized()) {
            getContent().setColor(getClientColor(newValue));
        }
    }

    @Override
    public void setImmediate(boolean immediate) {
        super.setImmediate(immediate);
        if (isContentInitialized()) {
            getContent().setImmediate(immediate);
        }
    }

    @Override
    public void setCaptionAsHtml(boolean captionAsHtml) {
        this.captionAsHtml = captionAsHtml;
        if (isContentInitialized()) {
            getContent().setCaptionAsHtml(captionAsHtml);
        }
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        if (isContentInitialized()) {
            getContent().setCaption(caption);
        }
    }

    @Override
    public String getCaption() {
        return caption;
    }

    /**
     * @return True if content has been initialized
     */
    public boolean isContentInitialized() {
        return contentInitialized;
    }

    @Override
    protected Collection<String> getCustomAttributes() {
        Collection<String> c = super.getCustomAttributes();

        c.add("color");
        c.add("position");
        c.add("popup-style");

        return c;
    }

    @Override
    public void readDesign(Element design, DesignContext designContext) {
        super.readDesign(design, designContext);
        Attributes attributes = design.attributes();

        // see com.vaadin.ui.AbstractColorPicker#readDesign()
        if (attributes.hasKey("color")) {
            // Ignore the # character
            String hexColor = DesignAttributeHandler.readAttribute(
                    "color", attributes, String.class).substring(1);
            setValue(new Color(Integer.parseInt(hexColor, 16)));
        }

        if (attributes.hasKey("position")) {
            String[] position = attributes.get("position").split(",");
            setPosition(Integer.parseInt(position[0]),
                    Integer.parseInt(position[1]));
        }

        if (attributes.hasKey("popup-style")) {
            setPopupStyle(AbstractColorPicker.PopupStyle.valueOf("POPUP_"
                    + attributes.get("popup-style").toUpperCase()));
        }

    }

    @Override
    public void writeDesign(Element design, DesignContext designContext) {
        super.writeDesign(design, designContext);
        // FIXME: 2017. 04. 20. have to be refactored to v7-compatibility
//        Attributes attributes = design.attributes();
//
//        DesignAttributeHandler.writeAttribute("color", attributes,
//                getClientColor(getValue()).getCSS(), Color.WHITE.getCSS(), String.class);
//        DesignAttributeHandler.writeAttribute("popup-style", attributes,
//                (popupStyle == AbstractColorPicker.PopupStyle.POPUP_NORMAL ? "normal" : "simple"),
//                "normal", String.class);
//        DesignAttributeHandler.writeAttribute("position", attributes, positionX
//                + "," + positionY, "0,0", String.class);
    }

    /**
     * Gets the {@link Color} that should be used for the
     * {@link AbstractColorPicker}. Returns the given {@link Color} or a default
     * value if the given {@link Color} is null {@code null}. The default value
     * is eighter {@link #nullRepresentation} or {@link #defaultColor}
     *
     * @param color {@link Color} value
     * @return A {@link Color} that is never null
     */
    protected Color getClientColor(Color color) {
        if (color != null) {
            return color;
        } else if (nullRepresentation != null) {
            return nullRepresentation;
        } else {
            return defaultColor;
        }
    }

    /**
     * Gets the default {@link Color}
     *
     * @return The default {@link Color}
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * Sets the default {@link Color}
     *
     * @param defaultColor The default {@link Color}
     */
    public void setDefaultColor(Color defaultColor) {
        if (defaultColor == null) {
            throw new NullPointerException("The default color mustn't be null!");
        }
        this.defaultColor = defaultColor;
    }

    /**
     * Sets the {@link Color} that represents a {@code null} value.
     *
     * @param nullRepresentation {@link Color} that represents a {@code null}
     * value.
     */
    public void setNullRepresentation(Color nullRepresentation) {
        this.nullRepresentation = nullRepresentation;
    }

    /**
     * Gets the {@link Color} that represents a {@code null} value.
     *
     * @return {@link Color} that represents a {@code null} value.
     */
    public Color getNullRepresentation() {
        return nullRepresentation;
    }

    /**
     * Set true if the component should show a default caption (css-code for the
     * currently selected color, e.g. #ffffff) when no other caption is
     * available.
     *
     * @param enabled Default caption enabled
     */
    public void setDefaultCaptionEnabled(boolean enabled) {
        defaultCaptionEnabled = enabled;
        if (isContentInitialized()) {
            getContent().setDefaultCaptionEnabled(enabled);
        }
    }

    /**
     * Sets the position of the popup window
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        if (isContentInitialized()) {
            getContent().setPosition(x, y);
        }
    }

    /**
     * The style for the popup window
     *
     * @param style The style
     */
    public void setPopupStyle(AbstractColorPicker.PopupStyle style) {
        popupStyle = style;
        if (isContentInitialized()) {
            getContent().setPopupStyle(style);
        }
    }

    /**
     * Set the visibility of the RGB Tab
     *
     * @param visible The visibility
     */
    public void setRGBVisibility(boolean visible) {
        rgbVisibility = visible;
        if (isContentInitialized()) {
            getContent().setRGBVisibility(visible);
        }
    }

    /**
     * Set the visibility of the HSV Tab
     *
     * @param visible The visibility
     */
    public void setHSVVisibility(boolean visible) {
        hsvVisibility = visible;
        if (isContentInitialized()) {
            getContent().setHSVVisibility(visible);
        }
    }

    /**
     * Set the visibility of the Swatches Tab
     *
     * @param visible The visibility
     */
    public void setSwatchesVisibility(boolean visible) {
        swatchesVisibility = visible;
        if (isContentInitialized()) {
            getContent().setSwatchesVisibility(visible);
        }
    }

    /**
     * Sets the visibility of the Color History
     *
     * @param visible The visibility
     */
    public void setHistoryVisibility(boolean visible) {
        historyVisibility = visible;
        if (isContentInitialized()) {
            getContent().setHistoryVisibility(visible);
        }
    }

    /**
     * Sets the visibility of the CSS color code text field
     *
     * @param visible The visibility
     */
    public void setTextfieldVisibility(boolean visible) {
        textfieldVisibility = visible;
        if (isContentInitialized()) {
            getContent().setTextfieldVisibility(visible);
        }
    }

    /**
     * Shows a popup-window for color selection.
     */
    public void showPopup() {
        showPopup = true;
        if (isContentInitialized()) {
            getContent().showPopup();
        }
    }

    /**
     * Hides a popup-window for color selection.
     */
    public void hidePopup() {
        showPopup = false;
        if (isContentInitialized()) {
            getContent().hidePopup();
        }
    }

    /**
     * Sets the caption of the popup window
     *
     * @param popupCaption The caption of the popup window
     */
    protected void setPopupCaption(String popupCaption) {
        this.popupCaption = popupCaption;
        if (isContentInitialized()) {
            Logger.getLogger(AbstractColorPickerField.class.getName()).warning(
                    "This change of the popupCaption maybe has no effect. "
                    + "The content has already been initialized.");
        }
    }

}
