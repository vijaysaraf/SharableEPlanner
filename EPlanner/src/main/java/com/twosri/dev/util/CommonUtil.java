 package com.twosri.dev.util;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonUtil {

	private ThreadLocal<String> uniqueKey;

	// Should be called only once for a process
	public synchronized void generateProcessKey() {
		uniqueKey = new ThreadLocal<>();
		String key = String.valueOf(System.currentTimeMillis());
		log.info("******* New key {} generated *******", key);
		uniqueKey.set(key);
	}

	public synchronized void releaseProcessKey() {
		log.info("******* Key {} released *******", uniqueKey.get());
		uniqueKey.set(null);
		uniqueKey = null;
	}

	public String getUniqueKey() {
		return uniqueKey != null ? uniqueKey.get() : null;
	}

}
