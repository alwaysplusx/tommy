package org.moon.tomee.ws.jws;

import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(
		portName = "CalculatorPort",
		serviceName = "CalculatorService", 
		targetNamespace = "http://tomee.com/wsdl", 
		endpointInterface = "org.moon.tomee.ws.jws.CalculatorWebService")
public class Calculator implements CalculatorWebService {

	@Override
	public int sum(int num1, int num2) {
		return num1 + num2;
	}

	@Override
	public int multiply(int num1, int num2) {
		return num1 * num2;
	}

}
