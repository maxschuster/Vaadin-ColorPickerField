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

import com.vaadin.data.Property;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.AbstractColorPicker;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
import com.vaadin.ui.declarative.DesignAttributeHandler;
import com.vaadin.ui.declarative.DesignContext;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

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
     * @param popupCaption the caption of the popup window
     */
    public AbstractColorPickerField(Class<COLOR_PICKER> pickerType, String popupCaption) {
        this(pickerType, popupCaption, DEFAULT_INITIAL_COLOR);
    }

    /**
     * Instantiates a new color picker.
     *
     * @param pickerType Type of the wrapped color picker
     * @param popupCaption the caption of the popup window
     * @param initialColor the initial color
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
            colorPicker = contructor.newInstance(getPopupCaption());
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

    public boolean isContentInitialized() {
        return contentInitialized;
    }

    protected Collection<String> getColorPickerAttributes() {
        ArrayList<String> c = new ArrayList<String>(0);

        // custom attributes
        c.add("color");
        c.add("position");
        c.add("popup-style");

        return c;
    }

    @Override
    protected Collection<String> getCustomAttributes() {
        Collection<String> c = super.getCustomAttributes();
        c.addAll(getColorPickerAttributes());
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
        Attributes attributes = design.attributes();

        DesignAttributeHandler.writeAttribute("color", attributes,
                getClientColor(getValue()).getCSS(), Color.WHITE.getCSS(), String.class);
        DesignAttributeHandler.writeAttribute("popup-style", attributes,
                (getPopupStyle() == AbstractColorPicker.PopupStyle.POPUP_NORMAL ? "normal" : "simple"),
                "normal", String.class);
        DesignAttributeHandler.writeAttribute("position", attributes, positionX
                + "," + positionY, "0,0", String.class);
    }

    protected Color getClientColor(Color color) {
        if (color != null) {
            return color;
        } else if (nullRepresentation != null) {
            return nullRepresentation;
        } else {
            return defaultColor;
        }
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        if (defaultColor == null) {
            throw new NullPointerException("The default color mustn't be null!");
        }
        this.defaultColor = defaultColor;
    }

    public void setNullRepresentation(Color nullRepresentation) {
        this.nullRepresentation = nullRepresentation;
    }

    public Color getNullRepresentation() {
        return nullRepresentation;
    }

    public void setDefaultCaptionEnabled(boolean enabled) {
        defaultCaptionEnabled = enabled;
        if (isContentInitialized()) {
            getContent().setDefaultCaptionEnabled(enabled);
        }
    }

    public boolean isDefaultCaptionEnabled() {
        return defaultCaptionEnabled;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
        if (isContentInitialized()) {
            getContent().setPosition(positionX, positionY);
        }
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
        if (isContentInitialized()) {
            getContent().setPosition(positionX, positionY);
        }
    }

    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        if (isContentInitialized()) {
            getContent().setPosition(x, y);
        }
    }

    public void setPopupStyle(AbstractColorPicker.PopupStyle style) {
        popupStyle = style;
        if (isContentInitialized()) {
            getContent().setPopupStyle(style);
        }
    }

    public AbstractColorPicker.PopupStyle getPopupStyle() {
        return popupStyle;
    }

    public void setRGBVisibility(boolean visible) {
        rgbVisibility = visible;
        if (isContentInitialized()) {
            getContent().setRGBVisibility(visible);
        }
    }

    public boolean getRGBVisibility() {
        return rgbVisibility;
    }

    public void setHSVVisibility(boolean visible) {
        hsvVisibility = visible;
        if (isContentInitialized()) {
            getContent().setHSVVisibility(visible);
        }
    }

    public boolean getHSVVisibility() {
        return hsvVisibility;
    }

    public void setSwatchesVisibility(boolean visible) {
        swatchesVisibility = visible;
        if (isContentInitialized()) {
            getContent().setSwatchesVisibility(visible);
        }
    }

    public boolean getSwatchesVisibility() {
        return swatchesVisibility;
    }

    public void setHistoryVisibility(boolean visible) {
        historyVisibility = visible;
        if (isContentInitialized()) {
            getContent().setHistoryVisibility(visible);
        }
    }

    public boolean getHistoryVisibility() {
        return historyVisibility;
    }

    public void setTextfieldVisibility(boolean visible) {
        textfieldVisibility = visible;
        if (isContentInitialized()) {
            getContent().setTextfieldVisibility(visible);
        }
    }

    public boolean getTextfieldVisibility() {
        return textfieldVisibility;
    }

    public void showPopup() {
        if (isContentInitialized()) {
            getContent().showPopup();
        }
    }

    public void hidePopup() {
        if (isContentInitialized()) {
            getContent().hidePopup();
        }
    }

    public String getPopupCaption() {
        return popupCaption;
    }

    public void setPopupCaption(String popupCaption) {
        this.popupCaption = popupCaption;
        if (isContentInitialized()) {
            Logger.getLogger(AbstractColorPickerField.class.getName()).warning(
                    "This change of the popupCaption maybe has no effect. "
                    + "The content has already been initialized.");
        }
    }

}
