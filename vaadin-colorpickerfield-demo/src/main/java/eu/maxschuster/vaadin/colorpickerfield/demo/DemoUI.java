package eu.maxschuster.vaadin.colorpickerfield.demo;

import com.vaadin.annotations.PreserveOnRefresh;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import eu.maxschuster.vaadin.colorpickerfield.converter.AbstractStringToColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.HexToColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.RgbToColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.RgbaToColorConverter;

/**
 * Demo UI of the Vaadin-ColorPickerField add-on
 * 
 * @author Max Schuster
 */
@Theme("valo")
@Title("ColorPickerField Add-on Demo")
@PreserveOnRefresh
public class DemoUI extends UI {

    private static final long serialVersionUID = 1L;

    /**
     * The demo servlet
     */
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(
            productionMode = false,
            ui = DemoUI.class,
            widgetset = "eu.maxschuster.vaadin.colorpickerfield.demo.DemoWidgetSet"
    )
    public static class Servlet extends VaadinServlet {
        private static final long serialVersionUID = 1L;
    }

    /**
     * The main demo layout
     */
    private final DemoUILayout l = new DemoUILayout();
    
    /**
     * A Container to collect the available converters
     */
    private final IndexedContainer converters = new IndexedContainer(); {
        
        // define the container properties
        converters.addContainerProperty("name", String.class, null);
        converters.addContainerProperty("instance",
                AbstractStringToColorConverter.class, null);
        
        // An array of our available converters
        AbstractStringToColorConverter[] converterTypes = 
            new AbstractStringToColorConverter[]{
                new HexToColorConverter(),
                new RgbToColorConverter(),
                new RgbaToColorConverter()
            };
        
        // Fill converters container with our available converters
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
    
    /**
     * A property we use for testing binding 
     */
    private final ObjectProperty<Color> colorProperty =
            new ObjectProperty<Color>(null, Color.class);

    @Override
    protected void init(VaadinRequest request) {

        // Set the demo layout as content
        setContent(l);
        
        // A small javascript extension that changes the background color
        final BackgroundColorExtension backgroundColorExtension =
                new BackgroundColorExtension(this);
        
        // Set the initial color value
        colorProperty.setValue(new Color(0, 180, 240)); // Vaadin blue ;-)
        colorProperty.addReadOnlyStatusChangeListener(
                new Property.ReadOnlyStatusChangeListener() {

            @Override
            public void readOnlyStatusChange(Property.ReadOnlyStatusChangeEvent event) {
                boolean readOnly = event.getProperty().isReadOnly();
                l.clearButton.setEnabled(!readOnly);
            }
        });

        l.converterComboBox.setContainerDataSource(converters);
        l.converterComboBox.setNullSelectionAllowed(false);
        l.converterComboBox.setItemCaptionPropertyId("name");
        l.converterComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object itemId = event.getProperty().getValue();
                Item item = converters.getItem(itemId);
                AbstractStringToColorConverter converter = (AbstractStringToColorConverter) 
                        item.getItemProperty("instance").getValue();
                l.colorTextField.setConverter(converter);
            }
        });
        l.converterComboBox.setValue(converters.firstItemId());
        
        l.colorPickerField.setPropertyDataSource(colorProperty);
        l.colorPickerAreaField.setPropertyDataSource(colorProperty);
        l.colorTextField.setPropertyDataSource(colorProperty);
        l.readOnlyCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Boolean readOnly = (Boolean)event.getProperty().getValue();
                colorProperty.setReadOnly(readOnly);
                Notification.show("ReadOnly changed to '" + readOnly + "'",
                        Notification.Type.TRAY_NOTIFICATION);
            }
        });
        
        l.clearButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                colorProperty.setValue(null);
            }
        });
        
        colorProperty.addValueChangeListener(new Property.ValueChangeListener() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Color color = colorProperty.getValue();
                String string = color == null ? null : color.getCSS();
                backgroundColorExtension.setBackgoundColor(color);
                Notification.show("Color changed to '" + string + "'",
                        Notification.Type.TRAY_NOTIFICATION);
            }
        });
        
        
        backgroundColorExtension.setBackgoundColor(colorProperty.getValue());
    }

}
