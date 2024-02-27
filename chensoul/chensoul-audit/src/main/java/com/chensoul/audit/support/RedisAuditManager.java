package com.chensoul.audit.support;

import com.chensoul.audit.AuditActionContext;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

/**
 * RedisAuditManager is responsible for recording AuditActionContext instances to Redis.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Setter
@RequiredArgsConstructor
public class RedisAuditManager extends AbstractAsyncAuditManager {
    public static final String CAS_AUDIT_CONTEXT_PREFIX = AuditActionContext.class.getSimpleName() + ':';

    private final RedisTemplate redisTemplate;

    private final Long scanCount;

    public RedisAuditManager(final RedisTemplate redisTemplate,
                             final boolean asynchronous,
                             final Long scanCount) {
        super(asynchronous);
        this.redisTemplate = Objects.requireNonNull(redisTemplate);
        this.scanCount = scanCount;
    }

    private static String getPatternAuditRedisKey(final String time, final String principal) {
        return CAS_AUDIT_CONTEXT_PREFIX + time + ':' + principal;
    }

    private static String getPatternAuditRedisKey() {
        return CAS_AUDIT_CONTEXT_PREFIX + '*';
    }

    @Override
    public Set<? extends AuditActionContext> getAuditRecords(final Map<WhereClauseFields, Object> whereClause) {
        val localDate = (LocalDate) whereClause.get(WhereClauseFields.DATE);
        log.debug("Retrieving audit records since [{}]", localDate);

        try (val keys = whereClause.containsKey(WhereClauseFields.USERNAME)
            ? getAuditRedisKeys(whereClause.get(WhereClauseFields.USERNAME).toString())
            : getAuditRedisKeys()) {
            return keys
                .map(redisKey -> redisTemplate.boundValueOps(redisKey).get())
                .filter(Objects::nonNull)
                .map(AuditActionContext.class::cast)
                .filter(audit -> audit.getOperateTime().isAfter(localDate.atStartOfDay()))
                .collect(Collectors.toSet());
        }
    }

    @Override
    public void removeAll() {
        try (val keys = getAuditRedisKeys()) {
            keys.forEach(redisTemplate::delete);
        }
    }

    @Override
    protected void saveAuditRecord(final AuditActionContext audit) {
        val redisKey = getPatternAuditRedisKey(String.valueOf(audit.getOperateTime().toEpochSecond(ZoneOffset.UTC)), audit.getUsername());
        this.redisTemplate.boundValueOps(redisKey).set(audit);
    }

    private Stream<String> getAuditRedisKeys() {
        return getAuditRedisKeys(null);
    }

    private Stream<String> getAuditRedisKeys(final String principal) {
        String pattern = StringUtils.isBlank(principal) ? getPatternAuditRedisKey() : getPatternAuditRedisKey("*", principal);

        ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions().match(pattern);

        if (this.scanCount != null && this.scanCount > 0) {
            builder = builder.count(this.scanCount);
        }

        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().keyCommands().scan(builder.build());
        Stream<String> resultingStream = StreamSupport
            .stream(Spliterators.spliteratorUnknownSize(cursor, Spliterator.ORDERED), false)
            .onClose(() -> {
                IOUtils.closeQuietly(cursor);
                connection.close();
            })
            .map(key -> (String) redisTemplate.getKeySerializer().deserialize(key))
            .distinct();
        if (scanCount != null && scanCount > 0) {
            resultingStream = resultingStream.limit(scanCount);
        }
        return resultingStream;
    }
}
