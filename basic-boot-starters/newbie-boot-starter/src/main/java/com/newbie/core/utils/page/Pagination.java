/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.newbie.core.utils.page;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 */
@Data
@Builder
@Deprecated
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
