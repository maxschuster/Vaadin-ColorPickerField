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

import com.vaadin.shared.ui.colorpicker.Color;
import java.util.Arrays;

/**
 *
 * @author Max Schuster
 */
public class HexToColorConverterTest extends
        AbstractStringToColorConverterTest<HexToColorConverter> {

    public HexToColorConverterTest() {
        super(new HexToColorConverter(),
                Arrays.asList(new Color[]{
                    new Color(255, 255, 255),
                    new Color(15, 224, 0),
                    new Color(255, 255, 255),
                    new Color(0, 0, 0)
                }),
                Arrays.asList(new String[]{
                    "#ffFFff",
                    "#0FE000",
                    "#ffffff",
                    "#000000"
                }));
    }
    
    
    
}
