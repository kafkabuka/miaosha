package com.bnz.miaosha.seckill.service;

import java.io.IOException;
import java.util.Map;

public interface AlipayService {
	String toAlipay(Map<String, String> sourceMap) throws IOException;
}
