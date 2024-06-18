package com.chensoul.jpa.support;

import java.util.List;

/**
 * 校验失败异常
 *
 * @author zhijun.chen
 * @since 0.0.1
 */
public class ValidationException extends RuntimeException {
	/**
	 * 校验结果
	 */
	private List<ValidateResult> result;

	public ValidationException(List<ValidateResult> list) {
		this.result = list;
	}

	public List<ValidateResult> getResult() {
		return this.result;
	}
}
