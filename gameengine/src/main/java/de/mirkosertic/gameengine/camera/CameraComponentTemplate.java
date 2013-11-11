package de.mirkosertic.gameengine.camera;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

public class CameraComponentTemplate implements GameComponentTemplate<CameraComponent> {

    private Property<CameraType> type;
    private GameObject owner;

    public CameraComponentTemplate(GameObject aOwner) {
        GameEventManager theManager = aOwner.getGameScene().getRuntime().getEventManager();

        type = new Property<CameraType>(this, "type", CameraType.FOLLOWPLAYER, theManager);
        owner = aOwner;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public Property<CameraType> typeProperty() {
        return type;
    }

    public CameraComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        final CameraComponent theResult = new CameraComponent(aInstance);
        theResult.registerEvents(aGameRuntime);
        return theResult;
    }

    @Override
    public String getTypeKey() {
        return CameraComponent.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(CameraComponent.TYPE_ATTRIBUTE, CameraComponent.TYPE);
        theResult.put("cameratype", type.get().name());
        return theResult;
    }

    public static CameraComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        CameraComponentTemplate theTemplate = new CameraComponentTemplate(aOwner);
        String theCameraType = (String) aSerializedData.get("cameratype");
        if (theCameraType != null) {
            theTemplate.type.setQuietly(CameraType.valueOf(theCameraType));
        }
        return theTemplate;
    }
}