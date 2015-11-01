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
package eu.maxschuster.vaadin.colorpickerfield.demo;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.shared.ui.colorpicker.Color;

/**
 * A small javascript extension that changes the background color of an
 * {@link AbstractClientConnector}
 * 
 * @author Max Schuster
 */
@JavaScript("BackgroundColorExtension.js")
public class BackgroundColorExtension extends AbstractJavaScriptExtension {

    private static final long serialVersionUID = 2L;
    
    /**
     * The current background color
     */
    private Color backgoundColor;
    
    /**
     * Extends the given {@link AbstractClientConnector}
     * @param connector Connector to extend
     */
    public BackgroundColorExtension(AbstractClientConnector connector) {
        extend(connector);
    }

    /**
     * Gets the current background color
     * @return Current background color
     */
    public Color getBackgoundColor() {
        return backgoundColor;
    }
    
    /**
     * Sets the current background color
     * @param backgroundColor New background color
     */
    public void setBackgoundColor(Color backgroundColor) {
        this.backgoundColor = backgroundColor;
        updateBackgroundColor();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        if (initial) {
            // Restore background color after reload
            updateBackgroundColor();
        }
    }
    
    /**
     * Update the background color at the client-side
     */
    protected void updateBackgroundColor() {
        // being null-save here
        String string = backgoundColor != null ? backgoundColor.getCSS() : null;
        callFunction("setBackgoundColor", string);
    }
    
}
