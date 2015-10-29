package eu.maxschuster.vaadin.colorpickerfield;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.AbstractColorPicker;
import com.vaadin.ui.CustomField;
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

public abstract class AbstractColorPickerField<COLOR_PICKER extends AbstractColorPicker>
        extends CustomField<Color> implements ColorChangeListener {

    private static final long serialVersionUID = 1L;
    
    protected static final String DEFAULT_POPUP_CAPTION = "Colors";
    protected static final Color DEFAULT_INITIAL_COLOR = Color.WHITE;

    private final Class<COLOR_PICKER> colorPickerType;
    private boolean contentInitialized = false;
    private Color defaultColor = Color.WHITE;
    private Color nullRepresentation;
    private String popupCaption;
    private int positionX = 0;
    private int positionY = 0;
    private boolean defaultCaptionEnabled = false;
    private AbstractColorPicker.PopupStyle popupStyle =
            AbstractColorPicker.PopupStyle.POPUP_NORMAL;
    private boolean rgbVisibility = true;
    private boolean hsvVisibility = true;
    private boolean swatchesVisibility = true;
    private boolean historyVisibility = true;
    private boolean textfieldVisibility = true;
    
    public AbstractColorPickerField(Class<COLOR_PICKER> pickerType) {
        this(pickerType, DEFAULT_POPUP_CAPTION, DEFAULT_INITIAL_COLOR);
    }
    
    public AbstractColorPickerField(Class<COLOR_PICKER> pickerType, String popupCaption) {
        this(pickerType, popupCaption, DEFAULT_INITIAL_COLOR);
    }
    
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
            Constructor<COLOR_PICKER> contructor =
                    colorPickerType.getDeclaredConstructor(String.class);
            colorPicker = contructor.newInstance(getPopupCaption());
        } catch (ReflectiveOperationException ex) {
            // passthru all reflection exceptions because they should never happen
            throw new RuntimeException(ex);
        }
        colorPicker.setSizeFull();
        colorPicker.setReadOnly(isReadOnly());
        colorPicker.setImmediate(isImmediate());
        
        colorPicker.setPosition(positionX, positionY);
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
        if (newValue != null && (nullRepresentation == null || 
                !nullRepresentation.equals(newValue))) {
            value = newValue;
        }
        setValue(value);
    }

    @Override
    protected void setInternalValue(Color newValue) {
        super.setInternalValue(newValue);
        if (isContentInitialized()) {
            getContent().setColor(getClientColor(newValue));
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if (isContentInitialized()) {
            getContent().setReadOnly(readOnly);
        }
    }

    @Override
    public void setImmediate(boolean immediate) {
        super.setImmediate(immediate);
        if (isContentInitialized()) {
            getContent().setImmediate(immediate);
        }
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
        getContent().showPopup();
    }

    public void hidePopup() {
        getContent().hidePopup();
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
