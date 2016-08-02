package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.Version;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.PlaySceneStrategy;
import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TouchIdentifier;
import de.mirkosertic.gameengine.type.TouchPosition;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.EventTarget;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

public class TeaVMRenderer {

    private static final Window window = Window.current();
    private static final HTMLDocument document = window.getDocument();

    public static void main(String[] args) {
        TeaVMRenderer theRenderer = new TeaVMRenderer();
        theRenderer.boot();
    }

    private GameLoopFactory gameLoopFactory;
    private PlaySceneStrategy runSceneStrategy;
    private TeaVMGameRuntimeFactory runtimeFactory;
    private TeaVMGameSceneLoader sceneLoader;
    private HTMLCanvasElement canvasElement;
    private Game game;
    private NetworkConnector networkConnector;

    private TeaVMRenderer() {
    }

    void boot() {

        TeaVMLogger.info("Starting GameEngine " + Version.VERSION);

        TeaVMLogger.info("Booting game runtime");

        canvasElement = (HTMLCanvasElement) document.getElementById("html5canvas");

        gameLoopFactory = new GameLoopFactory();
        runtimeFactory = new TeaVMGameRuntimeFactory();

        // Initialize PIXI
        final Renderer theRenderer;
        if (window.getLocation().getFullURL().contains("canvas")) {
            theRenderer = Renderer.canvasRenderer(320, 200, canvasElement);
        } else {
            theRenderer = Renderer.autodetectRenderer(320, 200, canvasElement);
        }

        switch (theRenderer.getType()) {
            case Renderer.TYPE_WEBGL:
                TeaVMLogger.info("Using: WebGL Renderer");
                break;
            case Renderer.TYPE_CANVAS:
                TeaVMLogger.info("Using: HTML5 Canvas Renderer");
                break;
            default:
                TeaVMLogger.info("Using: Unknown Renderer");
                break;
        }

        sceneLoader = new TeaVMGameSceneLoader(new TeaVMGameSceneLoader.GameSceneLoadedListener() {
            @Override
            public void onGameSceneLoaded(GameScene aScene) {
                playScene(aScene);
            }

            @Override
            public void onGameSceneLoadedError(Throwable aError) {
                TeaVMLogger.error("Failed to load scene : " + aError.getMessage());
            }
        }, runtimeFactory);

        new TeaVMGameLoader(new TeaVMGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                game = aGame;

                if (aGame.enableNetworkingProperty().get()) {
                    String theConnectionID = window.getLocation().getHash();
                    if (theConnectionID == null || theConnectionID.isEmpty()) {
                        // No connection id provided, we will start a new one
                        theConnectionID = "game" + System.currentTimeMillis();
                        window.getLocation().setHash(theConnectionID);
                    } else {
                        // Extract the hash character
                        theConnectionID = theConnectionID.substring(1);
                    }

                    boolean theTruncateDB = "?truncate".equals(window.getLocation().getSearch());

                    String theFirebaseURL = aGame.fireBaseURLProperty().get();

                    TeaVMLogger.info("Enabling Firebase Networking with URL " + theFirebaseURL+", truncate = " + theTruncateDB);
                    networkConnector = new TeaVMFirebaseNetworkConnector(theFirebaseURL, theConnectionID, theTruncateDB);
                } else {
                    networkConnector = new DefaultNetworkConnector();
                }

                runSceneStrategy = new PlaySceneStrategy(runtimeFactory, gameLoopFactory, networkConnector) {

                    private TeaVMGameView gameView;

                    @Override
                    protected void loadOtherScene(String aSceneId) {
                        sceneLoader.loadFromServer(game, aSceneId, new TeaVMGameResourceLoader(aSceneId));
                    }

                    @Override
                    protected Size getScreenSize() {
                        return new Size(window.getInnerWidth(), window.getInnerHeight());
                    }

                    @Override
                    protected GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
                        if (gameView == null) {
                            gameView = new TeaVMGameView(aGameRuntime, aCamera, aGestureDetector, theRenderer);
                        } else {
                            gameView.prepareNewScene(aGameRuntime, aCamera, aGestureDetector);
                        }
                        gameView.setSize(getScreenSize());
                        return gameView;
                    }

                    @Override
                    public void handleResize() {
                        Size theCurrentSize = getScreenSize();
                        getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theCurrentSize));
                        gameView.setSize(theCurrentSize);
                    }
                };

                String theSceneId = aGame.defaultSceneProperty().get();

                TeaVMLogger.info("Loading scene " + theSceneId);
                sceneLoader.loadFromServer(game, theSceneId, new TeaVMGameResourceLoader(theSceneId));
            }

            @Override
            public void onGameLoadedError(Throwable aError) {
                TeaVMLogger.error("Failed to load scene : " + aError);
            }
        }).loadFromServer();

        EventTarget documentEventTarget = document;
        documentEventTarget.addEventListener("keydown", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                keyPressed((TeaVMKeyEvent) aEvent);
            }
        }, false);
        documentEventTarget.addEventListener("keyup", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                keyReleased((TeaVMKeyEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("touchstart", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                touchStarted((TeaVMTouchEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("touchend", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                touchEnded((TeaVMTouchEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("touchcancel", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                touchCanceled((TeaVMTouchEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("touchmove", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                touchMoved((TeaVMTouchEvent) aEvent);
            }
        }, false);
        canvasElement.addEventListener("mousedown", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                mouseDown((TeaVMMouseEvent) aEvent);
            }
        });
        canvasElement.addEventListener("mouseup", new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                mouseUp((TeaVMMouseEvent) aEvent);
            }
        });
        window.addEventListener("resize", new EventListener() {
            @Override
            public void handleEvent(Event evt) {
                if (runSceneStrategy.hasGameLoop()) {
                    runSceneStrategy.handleResize();
                }
            }
        }, true);
    }

    private void mouseDown(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mousePressed(
                    new Position(aEvent.getClientX(), aEvent.getClientY())
            );
        }
    }

    private void mouseUp(TeaVMMouseEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mouseReleased(
                    new Position(aEvent.getClientX(), aEvent.getClientY())
            );
        }
    }

    private void keyPressed(TeaVMKeyEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            int theCode = aEvent.getKeyCode();
            GameKeyCode theKeyCode = TeaVMKeyCodeTranslator.translate(theCode);
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyPressed(theKeyCode);
            TeaVMLogger.info("KeyEvent keyPressed " + theCode);
        }
    }

    private void keyReleased(TeaVMKeyEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            int theCode = aEvent.getKeyCode();
            GameKeyCode theKeyCode = TeaVMKeyCodeTranslator.translate(theCode);
            runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyReleased(theKeyCode);
            TeaVMLogger.info("KeyEvent keyReleased " + theCode);
        }
    }

    private TouchPosition[] toArray(JSArrayReader<TeaVMTouch> aTouches) {
        TouchPosition[] thePositions = new TouchPosition[aTouches.getLength()];
        for (int i=0;i<aTouches.getLength();i++) {
            TeaVMTouch theTouch = aTouches.get(i);
            thePositions[i] = new TouchPosition(new TouchIdentifier(theTouch.getIdentifier()), theTouch.getScreenX(), theTouch.getScreenY());
        }
        return thePositions;
    }

    private void touchStarted(TeaVMTouchEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchStarted(toArray(aEvent.getTouches()));
        }
    }

    private void touchEnded(TeaVMTouchEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchEnded(toArray(aEvent.getTouches()));
        }
    }

    private void touchMoved(TeaVMTouchEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchMoved(toArray(aEvent.getTouches()));
        }
    }

    private void touchCanceled(TeaVMTouchEvent aEvent) {
        if (runSceneStrategy.hasGameLoop()) {
            GestureDetector theDetector = runSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();
            theDetector.touchCanceled(toArray(aEvent.getTouches()));
        }
    }

    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRun();
            TeaVMWindow.requestAnimationFrame(new TeaVMWindow.RenderFrameHandler() {
                @Override
                public void renderFrame(int aTimeDelta) {
                    runSingleStep(aGameLoop);
                }
            });
        }
    }

    private void playScene(GameScene aGameScene) {
        runSceneStrategy.playScene(aGameScene);
        runSingleStep(runSceneStrategy.getRunningGameLoop());
    }
}