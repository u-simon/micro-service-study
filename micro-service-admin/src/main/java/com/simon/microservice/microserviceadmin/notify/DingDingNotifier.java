package com.simon.microservice.microserviceadmin.notify;

import com.simon.microservice.microserviceadmin.utils.DingDingMessageUtil;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import reactor.core.publisher.Mono;


/**
 * @author Simon
 * @Date 2019-07-15 11:27
 */
public class DingDingNotifier extends AbstractStatusChangeNotifier {

    private Expression text;

    private final SpelExpressionParser parser = new SpelExpressionParser();

    public DingDingNotifier(InstanceRepository repository) {
        super(repository);
        this.text = this.parser.parseExpression("#{application.name}(#{application.id}) \n status changed from #{from.status} to #{to.status} \n\n" +
                "#{application.healthUrl}", ParserContext.TEMPLATE_EXPRESSION);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        EvaluationContext context = new StandardEvaluationContext(event);
        String value = this.text.getValue(context, String.class);
        DingDingMessageUtil.sendTextMessage(value);
        return null;
    }

    public Expression getText() {
        return text;
    }

    public void setText(Expression text) {
        this.text = text;
    }
}
