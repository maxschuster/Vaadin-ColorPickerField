package eu.maxschuster.vaadin.colorpickerfield.demo;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.UI;
import eu.maxschuster.vaadin.colorpickerfield.converter.AbstractStringToColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.HexToColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.RgbToColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.RgbaToColorConverter;

/**
 * 
 * @author Max Schuster
 */
@Theme("demo")
@Title("ColorPickerField Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "eu.maxschuster.vaadin.colorpickerfield.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    private final DemoUILayout l = new DemoUILayout();
    private final IndexedContainer converters = new IndexedContainer();
    private final AbstractStringToColorConverter[] converterTypes
            = new AbstractStringToColorConverter[]{
                new HexToColorConverter(),
                new RgbToColorConverter(),
                new RgbaToColorConverter()
            };
    {
        // Fill converters container
        converters.addContainerProperty("name", String.class, null);
        converters.addContainerProperty("instance", AbstractStringToColorConverter.class, null);
        for (AbstractStringToColorConverter converter : converterTypes) {
            Object itemId = converters.addItem();
            Item item = converters.getItem(itemId);

            @SuppressWarnings("unchecked")
            Property<String> nameProperty = item.getItemProperty("name");
            nameProperty.setValue(converter.getClass().getSimpleName());

            @SuppressWarnings("unchecked")
            Property<AbstractStringToColorConverter> instanceProperty
                    = item.getItemProperty("instance");
            instanceProperty.setValue(converter);
        }
    }
    
    private final ObjectProperty<Color> colorProperty =
            new ObjectProperty<Color>(null, Color.class);

    @Override
    protected void init(VaadinRequest request) {

        setContent(l);
        
        final BackgroundColorExtension backgroundColorExtension =
                new BackgroundColorExtension(this);
        
        colorProperty.setValue(new Color(0, 180, 240));

        l.converterComboBox.setContainerDataSource(converters);
        l.converterComboBox.setNullSelectionAllowed(false);
        l.converterComboBox.setItemCaptionPropertyId("name");
        l.converterComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object itemId = event.getProperty().getValue();
                Item item = converters.getItem(itemId);
                AbstractStringToColorConverter converter = (AbstractStringToColorConverter) item.getItemProperty("instance").getValue();
                l.colorTextField.setConverter(converter);
            }
        });
        l.converterComboBox.setValue(converters.firstItemId());
        
        l.colorPickerField.setPropertyDataSource(colorProperty);
        l.colorPickerAreaField.setPropertyDataSource(colorProperty);
        l.colorTextField.setPropertyDataSource(colorProperty);
        colorProperty.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                backgroundColorExtension.setBackgoundColor(colorProperty.getValue());
            }
        });
        
        backgroundColorExtension.setBackgoundColor(colorProperty.getValue());
    }

}
