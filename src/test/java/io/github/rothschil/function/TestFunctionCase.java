package io.github.rothschil.function;

import cn.hutool.core.util.StrUtil;
import io.github.rothschil.function.pkg1.MySecondFunctionalInterface;
import io.github.rothschil.function.pkg2.IdCard;
import io.github.rothschil.function.pkg2.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.Collectors;


@DisplayName("Function方法测试")
public class TestFunctionCase {


    @DisplayName("01-基本使用方式")
    @Test
    public void testSecondFunction() {
        MySecondFunctionalInterface<String, Integer> inputLength = String::length;
        MySecondFunctionalInterface<String, String> addGreeting = s -> "welcome " + s;
        MySecondFunctionalInterface<String, String> revertInput = StrUtil::reverse;

        System.out.println(inputLength.doSomething("hello world"));
        System.out.println(addGreeting.doSomething("sea"));
        System.out.println(revertInput.doSomething("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }

    @DisplayName("02-结合Optional")
    @Test
    public void testOptional() {
        Function<User, Optional<Integer>> getAge = user -> Optional.ofNullable(user).map(u -> u.getAge());

        // 创建一个 User 对象
        User user= new User("Alice", 30);

        // 使用 Optional 来安全地获取年龄
        Optional<Integer> age = getAge.apply(user);
        age.ifPresent(a -> System.out.println("User's age: " + a));  // 输出 User's age: 30

        // 使用 null 时，返回空 Optional
        age = getAge.apply(null);
        age.ifPresent(a -> System.out.println("User's age: " + a));  // 不输出任何内容
    }


    /**
     *  在处理数据集合时，我们经常需要对数据进行过滤并进行转换，
     * Function 和 Predicate 联合能够简洁地实现这一功能。例如，可以先使用 Predicate 筛选符合条件的数据，
     * 再使用 Function 转换数据的形式，避免了显式的 for 循环，提高了代码的可读性和可维护性
     **/
    @DisplayName("03-结合Predicate")
    @Test
    public void testPredicate() {

        // 创建一组 User 数据
        List<User> users = Arrays.asList(
                new User("Alice", 25),
                new User("Bob", 35),
                new User("Charlie", 28),
                new User("David", 40)
        );

        // 定义一个 Predicate，筛选年龄大于 30 的用户
        Predicate<User> isOlderThan30 = user -> user.getAge() > 30;

        // 定义一个 Function，提取用户的姓名
        Function<User, String> getName = User::getName;

        // 筛选年龄大于 30 的用户并提取姓名
        List<String> names = users.stream()
                .filter(isOlderThan30)  // 使用 Predicate 进行筛选
                .map(getName)           // 使用 Function 转换数据
                .collect(Collectors.toList());

        System.out.println(names);  // 输出 [Bob, David]

    }

    /**
     *  异步编程是现代应用中常见的模式，尤其是在处理 I/O 密集型任务时。
     *  Function 和 Consumer 可以联合使用，用于处理异步回调中的数据流.
     *  将数据的获取与后续处理分离，使得异步编程变得更加简洁和易于理解。
     **/
    @DisplayName("04-结合Consumer")
    @Test
    public void testConsumer() {
        // 假设我们有一个异步任务：获取用户信息
        Function<Integer, CompletableFuture<String>> funcCom = userId ->CompletableFuture.supplyAsync(() -> "User 通知各系统 " + userId);
        // 定义一个 Consumer 用于处理异步结果
        Consumer<String> printUserInfo = userInfo -> System.out.println("Fetched user info: " + userInfo);
        // 异步获取用户信息并处理
        funcCom.apply(123).thenAccept(printUserInfo);  // 使用 Consumer 处理结果

    }


    /**
     **/
    @DisplayName("05-apply:")
    @Test
    public void testApply() {
        User user = new User("Alice", 25);
        Function<User, Integer> funcApply = User::getAge;
        System.out.println(funcApply.apply(user));
    }

    /**
     **/
    @DisplayName("06-Supplier:")
    @Test
    public void testSupplier() {
        User user = new User("Alice", 25);
        Supplier<Integer> supplier = user::getAge;
        System.out.println(supplier.get());
    }

    @DisplayName("07-结合Predicate")
    @Test
    public void testPredicate2() {
        List<User> users = Arrays.asList(
                new User("张三", 36),
                new User("李四", 17),
                new User("王五", 29),
                new User("周六", 69)
        );
        Predicate<User> isOlderThan30 = user -> user.getAge() > 30;
        Function<User, String> getName = User::getName;

        List<String> names = users.stream()
                .filter(isOlderThan30)
                .map(getName)
                .collect(Collectors.toList());
        System.out.println(names);
    }

    @DisplayName("08-BiFunction")
    @Test
    public void testBiFunction() {
        User user = new User("Alice", 25);
        IdCard idCard = new IdCard("身份证","123");
        BiFunction<User,IdCard,String> biFunction =(User,IdCard) -> user.getName()+"的证件为 "+idCard.getCardNum();
        System.out.println(biFunction.apply(user,idCard));
    }


    @DisplayName("09-BinaryOperator")
    @Test
    public void testBinaryOperator() {
        User user1 = new User("王五", 12);
        User user2 = new User("张三", 25);
        Comparator<User> ageComparator = Comparator.comparing(User::getAge);
        BinaryOperator<User> binaryOperator =BinaryOperator.maxBy(ageComparator);
        System.out.println(binaryOperator.apply(user1,user2));
    }

    @DisplayName("10-UnaryOperator")
    @Test
    public void testUnaryOperator() {
        UnaryOperator<Integer> unaryOperator =age->age-1;
        System.out.println(unaryOperator.apply(3));

        UnaryOperator<Integer> incrementAndSquare = age -> age + 1;
        Function<Integer, Integer> fun = incrementAndSquare.andThen(unaryOperator);
        int result = fun.apply(5);
        System.out.println(result);
    }

}

