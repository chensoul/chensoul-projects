package com.chensoul.jpa.support;

import com.chensoul.jpa.converter.InstantLongConverter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.Instant;

/**
 * @author zhijun.chen
 * @since 0.0.1
 */
@MappedSuperclass
@Data
public abstract class BaseJpaAggregate extends AbstractAggregateRoot<BaseJpaAggregate> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.PROTECTED)
	@Column(name = "id")
	private Long id;

	@Column(name = "created_at", nullable = false, updatable = false)
	@Convert(converter = InstantLongConverter.class)
	@Setter(AccessLevel.PROTECTED)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	@Convert(converter = InstantLongConverter.class)
	@Setter(AccessLevel.PROTECTED)
	private Instant updatedAt;

	@Version
	@Column(name = "version")
	@Setter(AccessLevel.PRIVATE)
	private Integer version;

	@PrePersist
	public void prePersist() {
		this.setCreatedAt(Instant.now());
		this.setUpdatedAt(Instant.now());
	}

	@PreUpdate
	public void preUpdate() {
		this.setUpdatedAt(Instant.now());
	}
}
