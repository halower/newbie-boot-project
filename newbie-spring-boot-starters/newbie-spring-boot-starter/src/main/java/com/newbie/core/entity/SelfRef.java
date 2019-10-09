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

package com.newbie.core.entity;

/**
 * 自引用接口，有的实体对象有自引用，从而形成树形的数据结构，由于较为特殊，故抽象出来
 */
public interface SelfRef {
	
	/**
	 * 属性访问器
	 * @return 自引用
	 */
	SelfRef getParent();
	
	/**
	 * 判断本节点是否另外一个节点的祖先
	 * @param other 另一个实例
	 * @return 测试是否另一个实例的先祖
	 */
	default boolean isAncestorOf(SelfRef other) {
		boolean b = false;
		SelfRef parent = other.getParent();
		while (parent != null) {
			if (this.equals(parent)) {
				b = true;
				break;
			}
			parent = parent.getParent();
		}
		return b;
	}
}
