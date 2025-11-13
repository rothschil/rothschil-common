package io.github.rothschil.expression;


import io.github.rothschil.common.utils.ToolUtils;
import io.github.rothschil.poi.IntfBo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * //todo 添加类描述
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@DisplayName("SpEL变量测试")
@Slf4j
public class ExpressionTestCase {

    @DisplayName("基本使用")
    @Test
    public void baseTest() throws InterruptedException {
        ExpressionParser parser = new SpelExpressionParser();

        IntfBo intfBo = new IntfBo("Nikola Tesla", "Serbian", "Serbian");

        EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
        context.setVariable("address", "合肥"); // 设置变量
        // 获取变量newName，并将其赋值给name属性
        parser.parseExpression("address = #address").getValue(context, intfBo);
        log.info(intfBo.getAddress());  // "Mike Tesla"
    }

    @DisplayName("表达式")
    @Test
    public void baseTest2() throws InterruptedException {
        ExpressionParser parser = new SpelExpressionParser();
        IntfBo intfBo = new IntfBo("Nikola Tesla", "安徽合肥", "Serbian");

        // 解析出一个表达式
        Expression expression = parser.parseExpression("#intfBo.address");
        // 开始准备表达式运行环境
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("intfBo", intfBo);
        String value = expression.getValue(ctx, String.class);
        log.info(value);
    }

    @DisplayName("表达式")
    @Test
    public void baseTest3() throws InterruptedException {
        ExpressionParser parser = new SpelExpressionParser();
        IntfBo intfBo = new IntfBo("Nikola Tesla", "安徽合肥", "Serbian");
        // 解析出一个表达式
        Expression expression = parser.parseExpression("#intfBo.address");
        // 开始准备表达式运行环境
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("intfBo", intfBo);
        String value = expression.getValue(ctx, String.class);
        log.info(value);
    }

    @DisplayName("正则表达式")
    @Test
    public void baseTest5() throws InterruptedException {
        String pattern = "^#\\w*\\.\\w*";
        String content = "#userVo:account";
        log.info(" {}" ,ToolUtils.operation(content, pattern));
    }
}
