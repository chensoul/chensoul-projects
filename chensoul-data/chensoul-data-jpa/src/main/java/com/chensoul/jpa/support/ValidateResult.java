package com.chensoul.jpa.support;

import lombok.Value;

/**
 * 校验结果
 *
 * @author zhijun.chen
 * @since 0.0.1
 */
@Value
public class ValidateResult {
	private String name;
	private String message;
}
