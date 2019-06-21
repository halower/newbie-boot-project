package com.newbie.core.utils;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * 分页对象
 */
@Data
@Builder
public class Pagination<T> implements Serializable {
	private List<T> content;
	private int current;
	private int pageSize;
	private long total;

	public Pagination(List<T> content, int pageIndex, int pageSize, long total) {
		this.content = content;
		this.total = total;
		this.current = pageIndex;
		this.pageSize = pageSize;
	}
}
