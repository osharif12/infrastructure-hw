package com.sharif;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/server-events")
public class ViewBroadcastsController {
    private final ExecutorService cachedThreadPool;

    ViewBroadcastsController(){
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping(path = "/view-broadcasts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter viewBroadcasts() throws Exception{
        long maxAsyncTimeout = Long.MAX_VALUE;
        SseEmitter emitter = new SseEmitter(maxAsyncTimeout);

        this.cachedThreadPool.execute(() -> {
            new ClientServer(50, emitter).run();
        });

        return emitter;
    }

    private class ClientServer implements Runnable {
        private int count;
        private SseEmitter emitter;

        public ClientServer(int count, SseEmitter emitter){
            this.count = count;
            this.emitter = emitter;
        }

        @Override
        public void run(){
            receiveHelloWorldMessages(this.count, this.emitter);
        }

        public void receiveHelloWorldMessages(int count, SseEmitter emitter){
            try {
                while(count-- > 0){
                    // connects to the broadcaster server, then gets the buffered message
                    Socket socket = new Socket("broadcaster", 8081);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String msg = bufferedReader.readLine();
                    System.out.println("Broadcast message received: " + msg);
                    emitter.send(msg);

                    socket.close();
                }

                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }
    }
}
