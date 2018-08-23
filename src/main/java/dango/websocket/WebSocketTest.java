package dango.websocket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author: DANGO
 * @date 2018/8/17 10:22
 * @Description:
 */
//@ServerEndpoint("/webSocketTest")
@ServerEndpoint("/webSocketTest/{user}")
public class WebSocketTest {

    private static int onlineCount = 0;

//    private static CopyOnWriteArraySet<WebSocketTest> webSocketTests = new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<String,WebSocketTest> webSocketTests = new ConcurrentHashMap<>();

    private Session session;

    @OnOpen
    public void onOpen(@PathParam("user") String user, Session session) {
        this.session = session;
        webSocketTests.put(user,this);
        addOnlineCount();
        System.out.println("新连接接入！"+user+"  当前在线人数：" + getOnlineCount());
    }

    @OnClose
    public void onClose() {
        webSocketTests.remove(this);
        subOnlineCount();
        System.out.println("关闭连接！当前在线人数：" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("接收到的消息：" + message);
//        for (WebSocketTest webSocketTest:webSocketTests){
//            try {
//                webSocketTest.session.getBasicRemote().sendText(message);
//            }catch (IOException e){
//                e.printStackTrace();
//                continue;
//            }
//        }
        Map<String,String> map=new Gson().fromJson(message,new TypeToken<Map<String,String>>(){}.getType());
        String user=map.get("user");
        WebSocketTest webSocketTest=webSocketTests.get(user);
        try {
                if(webSocketTest!=null){
                    webSocketTest.session.getBasicRemote().sendText(map.get("message"));
                    session.getBasicRemote().sendText(map.get("message")+"  send ok!");
                }else {
                    session.getBasicRemote().sendText(map.get("user")+"未上线");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }
}
