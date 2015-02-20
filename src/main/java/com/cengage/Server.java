package com.cengage;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.core.streams.Pump;
import org.vertx.java.platform.Verticle;

public class Server extends Verticle {
    public void start() {
        int port = 8080;
        HttpServer httpServer = vertx.createHttpServer();
        JsonObject config = new JsonObject().putString("prefix", "/eventbus");

        final EventBus eb = vertx.eventBus();
        final Random random = new Random();

        final JsonObject activity = new JsonObject();
        activity.putNumber("id", 1279983);

        JsonArray noPermitted = new JsonArray();
        noPermitted.add(new JsonObject());

        vertx.createSockJSServer(httpServer).bridge(config, noPermitted, noPermitted);

        Timer t = new Timer();
        int delay = 0;
        int period = 500;
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int userScore = random.nextInt(100);
                activity.putString("userScore", userScore + "%");
                eb.send("activity", activity);
            }
        }, delay, period);

        httpServer.listen(port);
        System.out.println("Listening on " + port);
    }
}
