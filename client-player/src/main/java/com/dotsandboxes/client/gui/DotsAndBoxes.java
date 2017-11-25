/*
 * DOTS AND BOXES GAME (WRITTEN ON SOCKETS)
 *
 * Developed by Nikolay Komarov
 *
 * (c) Lobachevsky University, 2017
 */
package com.dotsandboxes.client.gui;

import com.dotsandboxes.ClientConstants;
import com.dotsandboxes.StringResourses;
import com.dotsandboxes.TCPClient;
import com.dotsandboxes.client.PresenterManager;
import com.dotsandboxes.client.gui.controller.DotsAndBoxesController;
import com.dotsandboxes.client.gui.controller.LoginController;
import com.dotsandboxes.client.threads.communication.RequestThread;
import com.dotsandboxes.client.threads.communication.ResponseThread;
import com.dotsandboxes.corbaservice.CorbaClient;
import com.dotsandboxes.shared.MessageType;
import com.dotsandboxes.shared.Request;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DotsAndBoxes extends Application {

    private TCPClient tcpClient;
    private ResponseThread responseThread;
    private RequestThread requestThread;

    private CorbaClient corbaClient;

    private Boolean isConnected = false;
    private Stage stage;

    @Override
    public void init() throws Exception {
        super.init();
        boolean isTcp = true;
        List<String> params = getParameters().getRaw();
        if (params.size() > 0) {
            isTcp = Boolean.valueOf(params.get(0));
        }

        if (isTcp) {
            tcpClient = new TCPClient();
            tcpClient.runClient();
            isConnected = tcpClient.getConnected();
            if (isConnected) {
                responseThread = new ResponseThread(tcpClient.getClientSocket());
                requestThread = new RequestThread(tcpClient.getClientSocket());
                responseThread.start();
                requestThread.start();
            }
        } else {
            corbaClient = CorbaClient.getInstance();
            isConnected = corbaClient.init("-ORBInitialPort", String.valueOf(ClientConstants.ORB_PORT));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        URL address = getClass().getResource("/fxml/LoginFormGUI.fxml");
        FXMLLoader loader = new FXMLLoader(address);
        Parent root = loader.load();
        primaryStage.setTitle(StringResourses.DOTS_AND_BOXES);
        primaryStage.setScene(new Scene(root));

        initializeLoginController(loader);

        primaryStage.show();
    }

    private void initializeLoginController(FXMLLoader loader) {
        final LoginController controller = loader.getController();
        controller.setMainApp(this);

        PresenterManager<LoginController> presenterManager = new PresenterManager<>();
        presenterManager.setController(controller);
        controller.addRequestListener(requestThread);
        controller.addOrbRequestListener(corbaClient);
        if (responseThread != null) {
            responseThread.addResponseListener(presenterManager);
        }

        controller.setStatusConnection(isConnected);
        controller.applyWaitingState();

        if (isConnected) {
            sendInitialRequest();
        }

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Request request = new Request(MessageType.ADMINISTRATIVE);
                request.setParameter(ClientConstants.CLIENT_STATE, "DISCONNECT");
                if (tcpClient != null) {
                    if (tcpClient.getClientSocket() != null && !tcpClient.getClientSocket().isClosed()) {
                        requestThread.sendRequest(request);
                    }
                } else if (corbaClient != null) {
                    corbaClient.sendRequest("rererer"); // TODO: obj
                }
                System.out.println("App is closed");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private void sendInitialRequest() {
        Request initialRequest = new Request(MessageType.TRY_CONNECT);
        if (tcpClient != null) {
            requestThread.sendRequest(initialRequest);
        } else if (corbaClient != null) {
            byte[] serializedInitialRequest = SerializationUtils.serialize(initialRequest);
            String response = corbaClient.processRequest("trt"); //TODO: obj
        }
    }

    public void openMainFrame(String userName) {
        try {
            URL address = getClass().getResource("/fxml/DotsAndBoxesGUI.fxml");
            FXMLLoader loader = new FXMLLoader(address);
            Parent root = loader.load();
            DotsAndBoxesController controller = loader.getController();
            controller.setMainApp(this);
            controller.setUserName(userName);

            loadUsersData();
            loadGameSettings();

            PresenterManager<DotsAndBoxesController> presenterManager = new PresenterManager<>();
            presenterManager.setController(controller);
            controller.addRequestListener(requestThread);
            controller.addOrbRequestLitener(corbaClient);
            if (responseThread != null) {
                responseThread.addResponseListener(presenterManager);
            }

            stage.getScene().setRoot(root);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersData() {
        Request usersRequest = new Request(MessageType.LOAD_USERS);
        if (tcpClient != null) {
            requestThread.sendRequest(usersRequest);
        } else if (corbaClient != null) {
            corbaClient.processRequest("trtr"); // TODO: obj
        }
    }

    private void loadGameSettings() {
        Request settingsRequest = new Request(MessageType.GAME_SETTINGS);
        if (tcpClient != null) {
            requestThread.sendRequest(settingsRequest);
        } else if (corbaClient != null) {
            corbaClient.processRequest("fg"); // tODO: obj
        }
    }
}
