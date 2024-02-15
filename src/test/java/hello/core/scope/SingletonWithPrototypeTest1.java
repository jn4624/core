package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {
    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = applicationContext.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = applicationContext.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = applicationContext.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = applicationContext.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }

    @Test
    void singletonClientUseObjectProviderPrototype() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientBeanUseObjectProvider.class, PrototypeBean.class);

        ClientBeanUseObjectProvider clientBeanUseObjectProvider1 = applicationContext.getBean(ClientBeanUseObjectProvider.class);
        int count1 = clientBeanUseObjectProvider1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBeanUseObjectProvider clientBeanUseObjectProvider2 = applicationContext.getBean(ClientBeanUseObjectProvider.class);
        int count2 = clientBeanUseObjectProvider2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Test
    void singletonClientUseObjectFactoryPrototype() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientBeanUseObjectFactory.class, PrototypeBean.class);

        ClientBeanUseObjectFactory clientBeanUseObjectFactory1 = applicationContext.getBean(ClientBeanUseObjectFactory.class);
        int count1 = clientBeanUseObjectFactory1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBeanUseObjectFactory clientBeanUseObjectFactory2 = applicationContext.getBean(ClientBeanUseObjectFactory.class);
        int count2 = clientBeanUseObjectFactory2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Test
    void singletonClientUseProviderPrototype() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientBeanUseProvider.class, PrototypeBean.class);

        ClientBeanUseProvider clientBeanUseProvider1 = applicationContext.getBean(ClientBeanUseProvider.class);
        int count1 = clientBeanUseProvider1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBeanUseProvider clientBeanUseProvider2 = applicationContext.getBean(ClientBeanUseProvider.class);
        int count2 = clientBeanUseProvider2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    static class ClientBeanUseProvider {
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("singleton")
    static class ClientBeanUseObjectFactory {
        @Autowired
        private ObjectFactory<PrototypeBean> prototypeBeanObjectFactory;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanObjectFactory.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("singleton")
    static class ClientBeanUseObjectProvider {
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanObjectProvider.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("singleton")
    static class ClientBean {
        /**
         * ClientBean 생성 시점에 주입되어
         * 한개의 인스턴스만 생성되고 더 이상 생성되지 않는다.
         */
        private final PrototypeBean prototypeBean;

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init = " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
