package org.moon.tomee.ws.jws;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CalculatorWebServiceTest {

	private static EJBContainer container;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties props = new Properties();
		props.put("openejb.embedded.remotable", "true");
		container = EJBContainer.createEJBContainer(props);
	}

	@Test
	public void testSum() throws Exception {
		URL url = new URL("http://127.0.0.1:4204/tommy-test/Calculator?wsdl");
		QName name = new QName("http://tomee.com/wsdl", "CalculatorService");
		Service service = Service.create(url, name);
		assertNotEquals("service not null", null, service);
		CalculatorWebService calculator = service.getPort(CalculatorWebService.class);
		assertEquals("sum 1 + 1", 2, calculator.sum(1, 1));
	}

	@Test
	public void testMultiply() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress("http://127.0.0.1:4204/tommy-test/Calculator?wsdl");
		factory.setServiceClass(CalculatorWebService.class);
		CalculatorWebService calculator = (CalculatorWebService) factory.create();
		assertNotEquals("service not null", null, calculator);
		assertEquals("multiply 100 * 0", 0, calculator.multiply(100, 0));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		container.close();
	}

}
