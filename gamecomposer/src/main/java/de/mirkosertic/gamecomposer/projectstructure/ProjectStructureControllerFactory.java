/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.ProjectStructure;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class ProjectStructureControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Produces
    @ProjectStructure
    public ProjectStructureController create() throws IOException {
        try (InputStream fxml = ProjectStructureController.class.getResourceAsStream("ProjectStructure.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.projectstructure.ProjectStructure");
            theLoader.setResources(theBundle);
            Parent root = theLoader.load(fxml);
            return ((ProjectStructureController)theLoader.getController()).initialize(root);
        }
    }

}
