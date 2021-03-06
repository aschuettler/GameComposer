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
package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.Position;

@InheritedClassInformation
public class MousePressed extends GameEvent {

    private static final MousePressedClassInformation CIINSTANCE = new MousePressedClassInformation();

    @ReflectiveField
    public final Position position;

    @ReflectiveField
    public final GameObjectInstance[] affectedInstances;

    MousePressed(Position aPosition, GameObjectInstance[] aAffectedInstances) {
        super("MousePressed");
        position = aPosition;
        affectedInstances = aAffectedInstances;
    }

    @Override
    public MousePressedClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
