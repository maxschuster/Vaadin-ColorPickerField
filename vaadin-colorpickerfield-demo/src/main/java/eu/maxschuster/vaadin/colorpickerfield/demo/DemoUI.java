package eu.maxschuster.vaadin.colorpickerfield.demo;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.ObjectProperty;
import com.vaadin.v7.shared.ui.colorpicker.Color;
import eu.maxschuster.vaadin.colorpickerfield.converter.AbstractColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.HexToColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.RgbToColorConverter;
import eu.maxschuster.vaadin.colorpickerfield.converter.RgbaToColorConverter;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Demo UI of the Vaadin-ColorPickerField add-on
 * 
 * @author Max Schuster
 */
@Theme("valo")
@Title("ColorPickerField Add-on Demo")
@PreserveOnRefresh
@SuppressWarnings({"unchecked", "rawtypes"})
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
                AbstractColorConverter.class, null);
        
        // An array of our available converters
        AbstractColorConverter[] converterTypes = 
            new AbstractColorConverter[]{
                new HexToColorConverter(),
                new RgbToColorConverter(),
                new RgbaToColorConverter()
            };
        
        // Fill converters container with our available converters
        for (AbstractColorConverter converter : converterTypes) {
            Object itemId = converters.addItem();
            Item item = converters.getItem(itemId);

            Property<String> nameProperty = item.getItemProperty("name");
            nameProperty.setValue(converter.getClass().getSimpleName());

            Property<AbstractColorConverter> instanceProperty
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
                    
            private static final long serialVersionUID = 1L;

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
                AbstractColorConverter converter = (AbstractColorConverter) 
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
            
            private static final long serialVersionUID = 1L;

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
    
    public static String getVersion() {
        String version = "UNKNOWN";
        
        try {
            InputStream is = DemoUI.class.getResourceAsStream("version.properties");
            Properties properties = new Properties();
            properties.load(is);
            version = properties.getProperty("version");
        } catch (IOException e) {
            Logger.getLogger(DemoUI.class.getName()).severe("Error loading");
        }
        
        return version;
    }

}
