package com.chensoul.spring.boot.audit.support;

import com.chensoul.spring.boot.audit.AuditActionContext;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;
import java.io.File;
import java.util.HashMap;
import lombok.val;
import org.springframework.context.ApplicationContext;

/**
 * GroovyAuditManager is responsible for recording AuditActionContext instances to a groovy template.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class GroovyAuditManager extends Slf4jAuditManager {
    private final Template groovyTemplate;
    private final ApplicationContext applicationContext;

    public GroovyAuditManager(final File groovyTemplate, final ApplicationContext applicationContext) throws Exception {
        this.groovyTemplate = new GStringTemplateEngine().createTemplate(groovyTemplate);
        this.applicationContext = applicationContext;
    }

    @Override
    protected String toString(final AuditActionContext auditActionContext) {
        val map = new HashMap<String, Object>(auditActionContext.getClientInfo().getHeaders());
        map.putAll(auditActionContext.getClientInfo().getExtraInfo());
        map.putAll(getMappedAuditActionContext(auditActionContext));
        map.put("applicationContext", applicationContext);
        map.put("logger", log);
        return groovyTemplate.make(map).toString();
    }
}
