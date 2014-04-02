package org.moon.tomee.jta;

public class JTAException {

	public void throwRuntimeException(){
		throw new RuntimeException("测试中自定义运行时异常");
	}
	
	public void throwException() throws Exception{
		throw new Exception("测试中自定义异常");
	}
	
}
