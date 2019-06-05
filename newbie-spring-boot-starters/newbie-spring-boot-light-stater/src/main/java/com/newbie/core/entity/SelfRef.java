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
