package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.TouchIdentifier;
import de.mirkosertic.gameengine.type.TouchPosition;

import java.util.HashMap;
import java.util.Map;

public class DefaultGestureDetector implements GestureDetector {

    private static final int THRESHOLD = 40;

    private final GameEventManager eventManager;
    private final CameraBehavior cameraBehavior;
    private final Map<TouchIdentifier, TouchPosition> currentTouchPositions;

    private boolean rightKeyDown;
    private boolean leftKeyDown;
    private boolean upKeyDown;

    public DefaultGestureDetector(GameEventManager aEventManager, CameraBehavior aCameraBehavior) {
        eventManager = aEventManager;
        currentTouchPositions = new HashMap<>();
        cameraBehavior = aCameraBehavior;
    }

    @Override
    public void keyPressed(GameKeyCode aKeyCode) {
        eventManager.fire(new KeyPressed(aKeyCode));
    }

    @Override
    public void keyReleased(GameKeyCode aKeyCode) {
        eventManager.fire(new KeyReleased(aKeyCode));
    }

    @Override
    public void touchStarted(TouchPosition[] aTouchPositions) {

        currentTouchPositions.clear();
        clearLeftKeyDown();
        clearRightKeyDown();
        clearUpKeyDown();
        for (TouchPosition thePosition : aTouchPositions) {
            currentTouchPositions.put(thePosition.identifier, thePosition);
        }
    }

    @Override
    public void touchEnded(TouchPosition[] aTouchPositions) {

        for (TouchPosition thePosition : aTouchPositions) {
            currentTouchPositions.remove(thePosition.identifier);
        }

        clearUpKeyDown();
        clearLeftKeyDown();
        clearRightKeyDown();
    }

    @Override
    public void touchMoved(TouchPosition[] aTouchPositions) {

        if (aTouchPositions.length == 1) {
            TouchPosition theFirstPosition = aTouchPositions[0];
            TouchPosition theOriginTouchPosition = currentTouchPositions.get(theFirstPosition.identifier);

            if (theFirstPosition == null || theOriginTouchPosition == null) {
                return;
            }

            int theDX = theFirstPosition.x - theOriginTouchPosition.x;
            int theDY = theFirstPosition.y - theOriginTouchPosition.y;

            if (theDY < -THRESHOLD) {
                if (!upKeyDown) {
                    eventManager.fire(new KeyPressed(GameKeyCode.UP));
                    upKeyDown = true;
                }
            }

            if (theDX > THRESHOLD) {
                if (!rightKeyDown) {
                    clearLeftKeyDown();
                    eventManager.fire(new KeyPressed(GameKeyCode.RIGHT));
                    rightKeyDown = true;
                }
                return;
            }
            if (theDX > 0) {
                clearRightKeyDown();
                return;
            }
            if (theDX < -THRESHOLD) {
                if (!leftKeyDown) {
                    clearRightKeyDown();
                    eventManager.fire(new KeyPressed(GameKeyCode.LEFT));
                    leftKeyDown = true;
                }
                return;
            }
            if (theDX < 0) {
                clearLeftKeyDown();
                return;
            }
        }
    }

    private void clearLeftKeyDown() {
        if (leftKeyDown) {
            leftKeyDown = false;
            eventManager.fire(new KeyReleased(GameKeyCode.LEFT));
        }
    }

    private void clearRightKeyDown() {
        if (rightKeyDown) {
            rightKeyDown = false;
            eventManager.fire(new KeyReleased(GameKeyCode.RIGHT));
        }
    }

    private void clearUpKeyDown() {
        if (upKeyDown) {
            upKeyDown = false;
            eventManager.fire(new KeyReleased(GameKeyCode.UP));
        }
    }


    @Override
    public void touchCanceled(TouchPosition[] aTouchPositions) {
        clearLeftKeyDown();
        clearRightKeyDown();
        clearUpKeyDown();
    }

    @Override
    public void mousePressed(Position aMousePosition) {
        eventManager.fire(new MousePressed(cameraBehavior.transformFromScreen(aMousePosition), cameraBehavior.findInstancesAt(aMousePosition, true)));
    }

    @Override
    public void mouseReleased(Position aMousePosition) {
        eventManager.fire(new MouseReleased(cameraBehavior.transformFromScreen(aMousePosition), cameraBehavior.findInstancesAt(aMousePosition, true)));
    }
}