package com.itsonin.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.messagebody.ReaderUtility;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
public class FullApiTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private static ObjectMapper mapper = new ObjectMapper();

	private Injector i = Guice.createInjector(new AppTestModule());
	private Dispatcher dispatcher;

	@Before
	public void setUp() {
		helper.setUp();
		dispatcher = MockDispatcherFactory.createDispatcher();
		dispatcher.getProviderFactory().getExceptionMappers()
				.put(NotFoundException.class, new NotFoundExceptionMapper());
		dispatcher
				.getProviderFactory()
				.getExceptionMappers()
				.put(UnauthorizedException.class,
						new UnauthorizedExceptionMapper());
		dispatcher.getRegistry().addSingletonResource(
				i.getInstance(DeviceApi.class));

		// @TODO You should never be able to create a SUPER device (only update
		// an existing to SUPER)
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testDoEverythignInline() throws Exception {
		// @NOTE Hi Nikolai, this is an inline test case
		// It needs to be used to simulate real world usage
		// Can you please fill in each commented piece
		// And let me know when parts are finished and I will review for you
		
		
		
		// Create 3 different devices
		// @TODO SUPER user can only be assigned via server side, all devices are set to NORMAL on create
		Device device1 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.SUPER), Long.valueOf(1L));
		Device device2 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(2L));
		Device device3 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(3L));
		Device device4 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(3L));
		Device device5 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(3L));
		
		// Authenticate Device 1 (SUPER),2,3,4
		// * Should return in response a session to be used for all
		//   future communication (if session expires, device must re-authenticate)
		//   use a filter to enforce security on all api requests
		// * You need to have a separate session/http instance for each device
		
		// Device 1 visits /api/device/list
		// * All devices currently on system should be listed
		
		// Device 1 visits /api/device/5/delete
		// * SUPER user has permission to delete a device
		
		// Device 2 creates event 1 (create a PRIVATE event)
		// * On creation of event, device 2 should be returned both the
		//   Event information and their new Guest information (and that they are a host)
		
		// Device 1 visits /api/event/list
		// * As Device1 is a SUPER user it can list all events
		
		// Device 2 visits /api/event/list
		// * Device 2 should only be able to see the event they recently created
		
		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should see themselves attending
		
		// Device 3 visits /api/event/list
		// * Device 3 should see an empty list
		
		// Device 3 visits /event/<eventId>/info
		// * The information about the event should be returned
		
		// Device 3 visits /event/<eventId>/attend
		// * On submitting attend, the api should return back the guest information record
		
		// Device 3 visits /api/event/list
		// * Device 3 should see a list containing the event that they are now attending
		
		// Device 3 visits /api/event/<eventId>/comment/create
		// * Device creates a comment with text "What should I bring"
		
		// Device 2 visits /api/event/<eventId>/comment/1/create
		// * Device creates a comment with text "Potatoes @NOTE instead of adding parentId in json
		// * This should create a new child comment from the parent question
		
		// Device 3 visits /api/event/<eventId>/comment/2/create
		// * Device creates a comment with text "TemporaryComment"
		// * This should create a response comment to Device 2's answer
		
		// Device 3 visits /api/event/<eventId>/comment/3/update
		// * Device creates a comment with text "Thanks"
		// * This should update response comment to Device 2's answer
		
		// Device 2 visits /api/event/<eventId>/comment/2/delete
		// * Device 2's original comment should be deleted (and all children comments)
		
		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should see themselves and Device 3 attending
		
		// Device 4 visits /api/event/<eventId>/comment/list
		// * Device should be able to see comments from Device 2 and Device 3
		
		// Device 4 visits /event/<eventId>/decline
		// * On submitting decline, the api should return back the guest information record 
		//   (with status declined)
		
		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should see Device 3 attending and Device 4 not attending	
		
		// Device 2 visits /api/device/2/previousGuests
		// * Device 2 should see listed Device 3 and Device 4 (without token information)
		
		// CLEAR OUT ALL COOKIES/SESSION INFO
		
		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should be forbidden from getting the guest list due to authentication
		
		// Device 2 visits /api/device/2/authenticate
		// * Re-Authenticate device 2

		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should see themselves and Device 3 attending and Device 4 not attending
		
	}

	private Device createDevice(Device device, Long expectedId)
			throws JsonProcessingException, URISyntaxException, IOException {
		String json = mapper.writeValueAsString(device);
		MockHttpRequest request = MockHttpRequest.post("api/device/create");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(new ByteArrayInputStream(json.getBytes()));

		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Device newDevice = ReaderUtility.read(Device.class,
				MediaType.APPLICATION_JSON, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(newDevice.getDeviceId(), expectedId);

		return newDevice;
	}
}
