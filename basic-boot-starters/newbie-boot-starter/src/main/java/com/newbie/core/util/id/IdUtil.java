package com.newbie.core.util.id;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class IdUtil {
	/*
	 * 返回使用ThreadLocalRandom的UUID，比默认的UUID性能更优
	 */
	public static UUID fastUUID() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return new UUID(random.nextLong(), random.nextLong());
	}
}
