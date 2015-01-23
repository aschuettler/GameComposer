package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.Position;

public class MouseReleased extends GameEvent {

    public final Position position;

    public final GameObjectInstance[] affectedInstances;

    MouseReleased(Position aPosition, GameObjectInstance[] aAffectedInstances) {
        super("MouseReleased");
        position = aPosition;
        affectedInstances = aAffectedInstances;
    }
}
