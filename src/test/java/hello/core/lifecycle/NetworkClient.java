package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class NetworkClient {
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 서비스 시작시 호출
     */
    public void connect() {
        System.out.println("connect = " + url);
    }

    public void call(String message) {
        System.out.println("close = " + url + " message = " + message);
    }

    /**
     * 서비스 종료시 호출
     */
    public void disconnect() {
        System.out.println("close = " + url);
    }

    /**
     * 의존 관계 주입이 끝난 후 호출
     */
    @PostConstruct
    public void init() throws Exception {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() throws Exception {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
