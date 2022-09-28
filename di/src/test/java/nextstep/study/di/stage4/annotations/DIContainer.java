package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.study.ConsumerWrapper;
import nextstep.study.FunctionWrapper;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return ClassPathScanner.getAllClassesInPackage(rootPackageName)
                .stream()
                .filter(DIContainer::filterServiceAndRepositoryAnnotation)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), DIContainer::new));
    }

    private static boolean filterServiceAndRepositoryAnnotation(final Class<?> aClass) {
        return aClass.isAnnotationPresent(Service.class) || aClass.isAnnotationPresent(Repository.class);
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(it -> it.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toSet());
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    private void setFields(final Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Inject.class))
                .forEach(it -> setField(bean, it));
    }

    // 각 필드에 빈을 대입(assign)한다.
    private void setField(final Object bean, final Field field) {
        field.setAccessible(true);
        final Class<?> fieldType = field.getType();

        beans.stream()
                .filter(fieldType::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("fail to find bean"));
    }
}
