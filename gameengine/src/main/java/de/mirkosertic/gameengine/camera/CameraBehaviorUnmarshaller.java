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
package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class CameraBehaviorUnmarshaller implements BehaviorUnmarshaller<CameraBehavior> {

    @Override
    public String getTypeKey() {
        return CameraBehavior.TYPE;
    }

    @Override
    public CameraBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        CameraBehavior theResult = CameraBehavior.deserialize(aObjectInstance);
        theResult.registerEvents(aRuntime);
        return theResult;
    }
}
