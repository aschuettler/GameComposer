package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.type.PositionAnchor;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameObjectInstanceTest {

    @Test
    public void testGetClassInformation() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertTrue(theInstance.getClassInformation() instanceof GameObjectInstanceClassInformation);
    }

    @Test
    public void testContains() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        Property theSizeProperty = mock(Property.class);
        when(theSizeProperty.get()).thenReturn(new Size(10, 20));
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);
        when(theOwner.sizeProperty()).thenReturn(theSizeProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertTrue(theInstance.contains(new Position(5, 5), new Size(100, 100)));
        assertFalse(theInstance.contains(new Position(-5, 5), new Size(100, 100)));
        assertFalse(theInstance.contains(new Position(15, 5), new Size(100, 100)));
        assertFalse(theInstance.contains(new Position(5, -5), new Size(100, 100)));
        assertFalse(theInstance.contains(new Position(5, 25), new Size(100, 100)));
    }

    @Test
    public void testUuidProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertNotNull(theInstance.uuidProperty().get());
        assertEquals("uuid", theInstance.uuidProperty().getName());
        assertEquals(String.class, theInstance.uuidProperty().getType());
        theInstance.uuidProperty().set("test");
        assertEquals("test", theInstance.uuidProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testPositionProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("position", theInstance.positionProperty().getName());
        assertEquals(Position.class, theInstance.positionProperty().getType());
        theInstance.positionProperty().set(new Position(5, 6));
        assertEquals(new Position(5, 6), theInstance.positionProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testNameProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("name", theInstance.nameProperty().getName());
        assertEquals(String.class, theInstance.nameProperty().getType());
        theInstance.nameProperty().set("test");
        assertEquals("test", theInstance.nameProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testRotationAngleProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("rotationAngle", theInstance.rotationAngleProperty().getName());
        assertEquals(Angle.class, theInstance.rotationAngleProperty().getType());
        assertEquals(new Angle(0), theInstance.rotationAngleProperty().get());
        theInstance.rotationAngleProperty().set(new Angle(17));
        assertEquals(new Angle(17), theInstance.rotationAngleProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testVisibleProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("visible", theInstance.visibleProperty().getName());
        assertEquals(Boolean.class, theInstance.visibleProperty().getType());
        assertTrue(theInstance.visibleProperty().get());
        theInstance.visibleProperty().set(false);
        assertFalse(theInstance.visibleProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testAbsolutePositionAnchorProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("positionAnchor", theInstance.positionAnchorProperty().getName());
        assertEquals(PositionAnchor.class, theInstance.positionAnchorProperty().getType());
        assertEquals(PositionAnchor.SCENE, theInstance.positionAnchorProperty().get());
        theInstance.positionAnchorProperty().set(PositionAnchor.BOTTOM_LEFT);
        assertEquals(PositionAnchor.BOTTOM_LEFT, theInstance.positionAnchorProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testGetOwnerGameObject() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertSame(theOwner, theInstance.getOwnerGameObject());
    }

    @Test
    public void testAddBehavior() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        BehaviorTemplate theTemplate = mock(BehaviorTemplate.class);
        Behavior theBehavior = mock(Behavior.class);
        when(theBehavior.getTemplate()).thenReturn(theTemplate);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertNull(theInstance.getBehavior(theBehavior.getClass()));
        theInstance.addBehavior(theBehavior);
        assertSame(theBehavior, theInstance.getBehavior(theBehavior.getClass()));
        theInstance.removeBehavior(theBehavior);
        assertNull(theInstance.getBehavior(theBehavior.getClass()));
    }

    @Test
    public void testRemoveBehaviorByTemplate() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        BehaviorTemplate theTemplate = mock(BehaviorTemplate.class);
        Behavior theBehavior = mock(Behavior.class);
        when(theBehavior.getTemplate()).thenReturn(theTemplate);

        Behavior theBehavior2 = new Behavior() {

            @Override
            public String getType() {
                return null;
            }

            @Override
            public Map<String, Object> serialize() {
                return new HashMap<>();
            }

            @Override
            public GameObjectInstance getInstance() {
                return null;
            }

            @Override
            public BehaviorTemplate getTemplate() {
                return null;
            }

            @Override
            public void markAsRemoteObject() {
            }
        };

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertNull(theInstance.getBehavior(theBehavior.getClass()));
        theInstance.addBehavior(theBehavior);
        theInstance.addBehavior(theBehavior2);
        assertSame(theBehavior, theInstance.getBehavior(theBehavior.getClass()));
        theInstance.removeBehaviorByTemplate(theTemplate);
        assertNull(theInstance.getBehavior(theBehavior.getClass()));
        assertSame(theBehavior2, theInstance.getBehavior(theBehavior2.getClass()));
    }

    @Test
    public void testSerialize() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        Property theOwnerUUID = mock(Property.class);
        when(theOwnerUUID.get()).thenReturn("OWNERID");
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.uuidProperty()).thenReturn(theOwnerUUID);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        Behavior theBehavior = new Behavior() {

            @Override
            public String getType() {
                return null;
            }

            @Override
            public Map<String, Object> serialize() {
                return new HashMap<>();
            }

            @Override
            public BehaviorTemplate getTemplate() {
                return null;
            }

            @Override
            public GameObjectInstance getInstance() {
                return null;
            }

            @Override
            public void markAsRemoteObject() {
            }
        };

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        theInstance.addBehavior(theBehavior);
        theInstance.positionProperty().set(new Position(10, 12));
        theInstance.nameProperty().set("NAME");

        Map<String, Object> theResult = theInstance.serialize();
        assertEquals(8, theResult.size());
        assertNotNull(theResult.get(GameObjectInstance.UUID_PROPERTY));
        assertEquals("OWNERID", theResult.get("gameobjectuuid"));
        assertEquals(new Position(10,12).serialize(), theResult.get(GameObjectInstance.POSITION_PROPERTY));
        assertEquals("NAME", theResult.get(GameObjectInstance.NAME_PROPERTY));
        assertEquals("true", theResult.get(GameObjectInstance.VISIBLE_PROPERTY));
        assertEquals("SCENE", theResult.get(GameObjectInstance.POSITION_ANCHOR_PROPERTY));
        assertEquals(new Angle(0).serialize(), theResult.get("rotationangle"));

        List<Map<String, Object>> theBehaviors = (List<Map<String, Object>>) theResult.get("components");
        assertEquals(1, theBehaviors.size());
        assertTrue(theBehaviors.get(0) instanceof Map);
    }

    @Test
    public void testDeserialize() throws Exception {
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameScene theScene = mock(GameScene.class);
        GameObject theOwner = mock(GameObject.class);
        when(theScene.findObjectByID(eq("OWNERID"))).thenReturn(theOwner);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        Behavior theBehavior = mock(Behavior.class);

        IORegistry theRegistry = mock(IORegistry.class);
        BehaviorUnmarshaller theUnmarshaller = mock(BehaviorUnmarshaller.class);
        when(theUnmarshaller.deserialize(any(GameRuntime.class), any(GameObjectInstance.class), anyMap())).thenReturn(theBehavior);
        when(theRegistry.getBehaviorUnmarshallerFor(eq("Behav"))).thenReturn(theUnmarshaller);

        when(theRuntime.getIORegistry()).thenReturn(theRegistry);

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObjectInstance.UUID_PROPERTY, "LALA");
        theData.put("gameobjectuuid", "OWNERID");
        theData.put(GameObjectInstance.POSITION_PROPERTY, new Position(10, 12).serialize());
        theData.put(GameObjectInstance.VISIBLE_PROPERTY, "false");
        theData.put(GameObjectInstance.NAME_PROPERTY, "name");
        theData.put("rotationangle", new Angle(17).serialize());
        theData.put("absolutePosition", "true");
        theData.put(GameObjectInstance.POSITION_ANCHOR_PROPERTY, "BOTTOM_LEFT");
        List<Map<String, Object>> theBehaviors = new ArrayList<>();
        Map<String, Object> theSingleBehavior = new HashMap<>();
        theSingleBehavior.put(Behavior.TYPE_ATTRIBUTE, "Behav");
        theBehaviors.add(theSingleBehavior);
        theData.put("components", theBehaviors);

        GameObjectInstance theInstance = GameObjectInstance.deserialize(theRuntime, theScene, theData);
        assertEquals("LALA", theInstance.uuidProperty().get());
        assertEquals("name", theInstance.nameProperty().get());
        assertEquals(theOwner, theInstance.getOwnerGameObject());
        assertEquals(new Position(10, 12), theInstance.positionProperty().get());
        assertEquals(new Angle(17), theInstance.rotationAngleProperty().get());
        assertFalse(theInstance.visibleProperty().get());
        assertEquals(PositionAnchor.BOTTOM_LEFT, theInstance.positionAnchorProperty().get());

        assertEquals(theBehavior, theInstance.getBehavior(theBehavior.getClass()));
    }

    @Test
    public void testDeserialize2() throws Exception {
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameScene theScene = mock(GameScene.class);
        GameObject theOwner = mock(GameObject.class);
        when(theScene.findObjectByID(eq("OWNERID"))).thenReturn(theOwner);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        Behavior theBehavior = mock(Behavior.class);

        IORegistry theRegistry = mock(IORegistry.class);
        BehaviorUnmarshaller theUnmarshaller = mock(BehaviorUnmarshaller.class);
        when(theUnmarshaller.deserialize(any(GameRuntime.class), any(GameObjectInstance.class), anyMap())).thenReturn(theBehavior);
        when(theRegistry.getBehaviorUnmarshallerFor(eq("Behav"))).thenReturn(theUnmarshaller);

        when(theRuntime.getIORegistry()).thenReturn(theRegistry);

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObjectInstance.UUID_PROPERTY, "LALA");
        theData.put("gameobjectuuid", "OWNERID");
        theData.put(GameObjectInstance.POSITION_PROPERTY, new Position(10, 12).serialize());
        theData.put(GameObjectInstance.VISIBLE_PROPERTY, "false");
        theData.put(GameObjectInstance.NAME_PROPERTY, "name");
        theData.put("rotationangle", new Angle(17).serialize());
        theData.put("absolutePosition", "false");
        theData.put(GameObjectInstance.POSITION_ANCHOR_PROPERTY, "BOTTOM_LEFT");
        List<Map<String, Object>> theBehaviors = new ArrayList<>();
        Map<String, Object> theSingleBehavior = new HashMap<>();
        theSingleBehavior.put(Behavior.TYPE_ATTRIBUTE, "Behav");
        theBehaviors.add(theSingleBehavior);
        theData.put("components", theBehaviors);

        GameObjectInstance theInstance = GameObjectInstance.deserialize(theRuntime, theScene, theData);
        assertEquals("LALA", theInstance.uuidProperty().get());
        assertEquals("name", theInstance.nameProperty().get());
        assertEquals(theOwner, theInstance.getOwnerGameObject());
        assertEquals(new Position(10, 12), theInstance.positionProperty().get());
        assertEquals(new Angle(17), theInstance.rotationAngleProperty().get());
        assertFalse(theInstance.visibleProperty().get());
        assertEquals(PositionAnchor.SCENE, theInstance.positionAnchorProperty().get());

        assertEquals(theBehavior, theInstance.getBehavior(theBehavior.getClass()));
    }
}