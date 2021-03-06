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
package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.ArrayUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventManager implements GameEventListener {

    private final Map<Class<GameEvent>, GameEventListener[]> registeredListeners;

    private GameEventListener catchAllEventListener[];

    public GameEventManager() {
        registeredListeners = new HashMap<>();
        catchAllEventListener = new GameEventListener[0];
    }

    public void register(Object aOwningInstance, Class aEvent, GameEventListener aEventListener) {
        GameEventListener[] theListener = registeredListeners.get(aEvent);
        if (theListener == null) {
            theListener = new GameEventListener[] { aEventListener };
            registeredListeners.put(aEvent, theListener);

            if (aEvent == GameEvent.class) {
                catchAllEventListener = new GameEventListener[] { aEventListener };
            }

            return;
        }
        List<GameEventListener> theListenerList = ArrayUtils.asList(theListener);
        theListenerList.add(aEventListener);

        GameEventListener[] theEventListener = theListenerList.toArray(new GameEventListener[theListenerList.size()]);
        registeredListeners.put(aEvent, theEventListener);

        if (aEvent == GameEvent.class) {
            catchAllEventListener = theEventListener;
        }
    }

    public void fire(GameEvent aEvent) {
        try {
            for (GameEventListener theListener : catchAllEventListener) {
                theListener.handleGameEvent(aEvent);
            }

            GameEventListener[] theRegisteredListener = registeredListeners.get(aEvent.getClass());
            if (theRegisteredListener != null) {
                for (GameEventListener theListener : theRegisteredListener) {
                    theListener.handleGameEvent(aEvent);
                }
            }
        } catch (Exception e) {
            if (aEvent instanceof SystemException) {
                throw new RuntimeException("Error dispatching system exception", e);
            } else {
                fire(new SystemException(e));
            }
        }
    }

    @Override
    public void handleGameEvent(GameEvent aEvent) {
        fire(aEvent);
    }

    public void clearListener() {
        registeredListeners.clear();
        catchAllEventListener = new GameEventListener[0];
    }
}
