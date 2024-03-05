package com.chensoul.audit.support;

import com.chensoul.audit.AuditActionContext;
import com.chensoul.audit.AuditManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;

/**
 * Abstract asynchronous audit manager
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractAsyncAuditManager implements AuditManager, DisposableBean {
    protected boolean asynchronous;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void record(final AuditActionContext audit) {
        if (this.asynchronous) {
            executorService.execute(() -> saveAuditRecord(audit));
        } else {
            saveAuditRecord(audit);
        }
    }

    @Override
    public void destroy() {
        executorService.shutdown();
    }

    protected abstract void saveAuditRecord(AuditActionContext audit);
}
