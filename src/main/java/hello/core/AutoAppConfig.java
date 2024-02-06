package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @ComponentScan은 @Component가 붙은 모든 클래스를 스프링 빈으로 등록
 */
@ComponentScan(
        /**
         * @Configuration 설정 정보는 컴포넌트 스캔 대상에서 제외 처리
         * 보통 컴포넌트 스캔 대상에서 제외하지 않지만, 기존 예제 코드를 유지하기 위함.
        */
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
@Configuration
public class AutoAppConfig {
}
