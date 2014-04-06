package org.moon.tomee.ws.jws;

import javax.jws.WebService;

@WebService(targetNamespace = "http://tomee.com/wsdl")
public interface CalculatorWebService {

	public int sum(int num1, int num2);

	public int multiply(int num1, int num2);

}
