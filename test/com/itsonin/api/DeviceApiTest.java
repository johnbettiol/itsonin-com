package com.itsonin.api;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.messagebody.ReaderUtility;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.itsonin.dao.DeviceDao;
import com.itsonin.entity.Device;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.DeviceType;
import com.itsonin.exception.NotFoundException;
import com.itsonin.exception.UnauthorizedException;
import com.itsonin.exception.mappers.NotFoundExceptionMapper;
import com.itsonin.exception.mappers.UnauthorizedExceptionMapper;

/**
 * @author nkislitsin
 *
 */
public class DeviceApiTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Injector i = Guice.createInjector(new AppTestModule());
    private Dispatcher dispatcher;
    private Device device;

	@Before
	public void setUp() {
		helper.setUp();
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getProviderFactory().getExceptionMappers().put(NotFoundException.class, new NotFoundExceptionMapper());
        dispatcher.getProviderFactory().getExceptionMappers().put(UnauthorizedException.class, new UnauthorizedExceptionMapper());
        dispatcher.getRegistry().addSingletonResource(i.getInstance(DeviceApi.class));

        device = new Device(DeviceType.BROWSER, DeviceLevel.SUPER);
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testCreateDevice() throws Exception {
		String json = mapper.writeValueAsString(device);
		MockHttpRequest request = MockHttpRequest.post("api/device/create");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(new ByteArrayInputStream(json.getBytes()));

		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		
		Device device = ReaderUtility.read(Device.class, MediaType.APPLICATION_JSON, response.getContentAsString());

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(device.getDeviceId(), Long.valueOf(1L));
	}
	
	@Test
	public void testAuthenticateExistingDevice() throws Exception{
		DeviceDao deviceDao = i.getInstance(DeviceDao.class);
		deviceDao.save(device);
		
		MockHttpRequest request = MockHttpRequest.get("api/device/1/authenticate");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	@Test
	public void testAuthenticateNewDevice() throws Exception{
		DeviceDao deviceDao = i.getInstance(DeviceDao.class);
		deviceDao.save(device);
		
		MockHttpRequest request = MockHttpRequest.get("api/device/100/authenticate");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
	}
	
	@Test
	public void testListDevices() throws Exception{
		DeviceDao deviceDao = i.getInstance(DeviceDao.class);
		deviceDao.save(device);
		
		MockHttpRequest request = MockHttpRequest.get("api/device/list");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testDeleteDevice() throws Exception{
		DeviceDao deviceDao = i.getInstance(DeviceDao.class);
		deviceDao.save(device);
		
		MockHttpRequest request = MockHttpRequest.delete("api/device/1/delete");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(deviceDao.list().size(), 0);
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testDeleteUnknownDevice() throws Exception{		
		MockHttpRequest request = MockHttpRequest.delete("api/device/100/delete");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
}
