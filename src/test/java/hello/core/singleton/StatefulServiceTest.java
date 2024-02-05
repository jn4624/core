package hello.core.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

class StatefulServiceTest {
    @Test
    @DisplayName("싱글톤 - 상태를 유지할 경우 발생하는 문제점")
    void statefulServiceSingleton_X() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = applicationContext.getBean(StatefulService.class);
        StatefulService statefulService2 = applicationContext.getBean(StatefulService.class);

        // ThreadA : A 사용자가 10000원 주문
        statefulService1.order("userA", 10000);

        // ThreadB : B 사용자가 20000원 주문
        statefulService2.order("userB", 20000);

        // ThreadA : A 사용자가 주문 금액 조회
        int price = statefulService1.getPrice();

        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }
    @Test
    @DisplayName("싱글톤 - 상태를 유지할 경우 발생하는 문제점 해결")
    void statefulServiceSingleton_O() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = applicationContext.getBean(StatefulService.class);
        StatefulService statefulService2 = applicationContext.getBean(StatefulService.class);

        // ThreadA : A 사용자가 10000원 주문
        int userAPrice = statefulService1.orderSolution("userA", 10000);

        // ThreadB : B 사용자가 20000원 주문
        int userBPrice = statefulService2.orderSolution("userB", 20000);

        System.out.println("userAPrice = " + userAPrice);
        System.out.println("userBPrice = " + userBPrice);

        assertThat(userAPrice).isEqualTo(10000);
        assertThat(userBPrice).isEqualTo(20000);
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}