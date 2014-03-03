package com.itsonin.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ExceptionMapper;

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
import com.itsonin.dao.CommentDao;
import com.itsonin.dao.CounterDao;
import com.itsonin.dao.DeviceDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.dao.GuestDeviceDao;
import com.itsonin.dto.EventWithGuest;
import com.itsonin.entity.Comment;
import com.itsonin.entity.Device;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.DeviceType;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventType;
import com.itsonin.enums.EventVisibility;
import com.itsonin.enums.GuestType;
import com.itsonin.exception.ForbiddenException;
import com.itsonin.exception.NotFoundException;
import com.itsonin.exception.UnauthorizedException;
import com.itsonin.exception.mappers.ForbiddenExceptionMapper;
import com.itsonin.exception.mappers.NotFoundExceptionMapper;
import com.itsonin.exception.mappers.UnauthorizedExceptionMapper;
import com.itsonin.mocks.MockAuthContextService;
import com.itsonin.resteasy.JacksonContextResolver;
import com.itsonin.security.AuthContextService;
import com.itsonin.service.CommentService;
import com.itsonin.service.DeviceService;
import com.itsonin.service.EventService;
import com.itsonin.service.GuestService;

/**
 * @author nkislitsin
 * 
 */
public class FullApiTest {
	
	private static final Logger log = Logger.getLogger(FullApiTest.class.getName());


	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private static ObjectMapper mapper = new ObjectMapper();
	private Dispatcher dispatcher;
	private AuthContextService authContextService = new MockAuthContextService();
	private MockAuthContextService mockAuthContextService = (MockAuthContextService) authContextService;

	@Before
	@SuppressWarnings("rawtypes")
	public void setUp() {
		DeviceDao deviceDao = new DeviceDao();
		CommentDao commentDao = new CommentDao();
		CounterDao counterDao = new CounterDao();
		GuestDao guestDao = new GuestDao();
		EventDao eventDao = new EventDao();
		GuestDeviceDao guestDeviceDao = new GuestDeviceDao();
		CommentService commentService = new CommentService(commentDao, guestDao, guestDeviceDao,
				authContextService);
		DeviceService deviceService = new DeviceService(deviceDao, counterDao, authContextService);
		EventService eventService = new EventService
				(eventDao, guestDao, guestDeviceDao, counterDao, authContextService);
		GuestService guestService = new GuestService(guestDao, authContextService);
		CommentApi commentApi = new CommentApi(commentService);
		DeviceApi deviceApi = new DeviceApi(deviceService, guestService);
		EventApi eventApi = new EventApi(eventService);
		GuestApi guestApi = new GuestApi(guestService);
		
		helper.setUp();
		dispatcher = MockDispatcherFactory.createDispatcher();
		dispatcher.getRegistry().addSingletonResource(commentApi);
		dispatcher.getRegistry().addSingletonResource(deviceApi);
		dispatcher.getRegistry().addSingletonResource(eventApi);
		dispatcher.getRegistry().addSingletonResource(guestApi);
		Map<Class<?>, ExceptionMapper> exceptionMappers = dispatcher.getProviderFactory().getExceptionMappers();
		exceptionMappers.put(NotFoundException.class, new NotFoundExceptionMapper());
		exceptionMappers.put(UnauthorizedException.class, new UnauthorizedExceptionMapper());
		exceptionMappers.put(ForbiddenException.class, new ForbiddenExceptionMapper());
		
		dispatcher.getProviderFactory().register(JacksonContextResolver.class);	

		// @TODO You should never be able to create a SUPER device (only update
		// an existing to SUPER)
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testDoEverythingInline() throws Exception {		
		// Create 3 different devices
		// @TODO SUPER user can only be assigned via server side, all devices are set to NORMAL on create
		Device device1 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.SUPER), Long.valueOf(1L));
		Device device2 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(2L));
		Device device3 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(3L));
		Device device4 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(4L));
		Device device5 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(5L));
		Device device6 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(6L));
		Device device7 = createDevice(new Device(DeviceType.BROWSER,
				DeviceLevel.NORMAL), Long.valueOf(7L));
		// Authenticate Device 1 (SUPER),2,3,4
		// * Should return in response a session to be used for all
		//   future communication (if session expires, device must re-authenticate)
		//   use a filter to enforce security on all api requests
		// * You need to have a separate session/http instance for each device
		
		authenticate(device1, HttpServletResponse.SC_OK);
		authenticate(device2, HttpServletResponse.SC_OK);
		authenticate(device3, HttpServletResponse.SC_OK);
		authenticate(device4, HttpServletResponse.SC_OK);
		authenticate(device5, HttpServletResponse.SC_OK);
		authenticate(device6, HttpServletResponse.SC_OK);

		device7.setToken("This+should+break+it");
		authenticate(device7, HttpServletResponse.SC_UNAUTHORIZED);
		
		// Device 1 visits /api/device/list
		// * All devices currently on system should be listed
		listDevices(device1);
		
		// Device 2 visits /api/device/5/delete should be forbidden
		deleteDevice(device2, 5L, HttpServletResponse.SC_FORBIDDEN);
		
		// Device 1 visits /api/device/5/delete
		// * SUPER user has permission to delete a device
		deleteDevice(device1, 5L, HttpServletResponse.SC_OK);
		
		// On second attempt to delete it should return NOT FOUND
		// @TODO NOT WORKING 
		// deleteDevice(device1, 5L, HttpServletResponse.SC_NOT_FOUND);
		
		// @TODO NOT WORKING
		// deleteDevice(device6, 6L, HttpServletResponse.SC_OK);
		
		// Device 2 creates event 1 (create a PRIVATE event)
		// * On creation of event, device 2 should be returned both the
		//   Event information and their new Guest information (and that they are a host)
		createEvent(device2, new Event(EventType.PICNIC, EventVisibility.PRIVATE, EventStatus.ACTIVE, 
        		EventFlexibility.NEGOTIABLE, "event title", "event description", "event notes", 
        		new Date(), new Date(), 1.0d, 2.0d, "location.url", "location title", 
        		"location address", null));
		
		// Device 1 visits /api/event/list
		// * As Device1 is a SUPER user it can list all events
		listEvents(device1);
		
		// Device 2 visits /api/event/list
		// * Device 2 should only be able to see the event they recently created
		listEvents(device2);
		
		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should see themselves attending
		listGuests(device2, 1L);
		
		// Device 3 visits /api/event/list
		// * Device 3 should see an empty list
		listEvents(device3);
		
		// Device 3 visits /event/<eventId>/info
		// * The information about the event should be returned
		getEventInfo(device3, 1L);
		
		// Device 3 visits /event/<eventId>/attend
		// * On submitting attend, the api should return back the guest information record
		attendEvent(device3, 1L);
		
		// Device 3 visits /api/event/list
		// * Device 3 should see a list containing the event that they are now attending
		listEvents(device3);
		
		// Device 3 visits /api/event/<eventId>/comment/create
		// * Device creates a comment with text "What should I bring"
		createComment(device3, 1L, null, "What should I bring");
		
		// Device 2 visits /api/event/<eventId>/comment/1/create
		// * Device creates a comment with text "Potatoes @NOTE instead of adding parentId in json
		// * This should create a new child comment from the parent question
		createComment(device2, 1L, 1L, "Potatoes");
		
		// Device 3 visits /api/event/<eventId>/comment/2/create
		// * Device creates a comment with text "TemporaryComment"
		// * This should create a response comment to Device 2's answer
		createComment(device3, 1L, 2L, "TemporaryComment");
		
		// Device 3 visits /api/event/<eventId>/comment/3/update
		// * Device creates a comment with text "Thanks"
		// * This should update response comment to Device 2's answer
		updateComment(device3, 1L, 3L, "Thanks");
		
		// Device 2 visits /api/event/<eventId>/comment/2/delete
		// * Device 2's original comment should be deleted (and all children comments)
		deleteComment(device2, 1L, 2L);
		
		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should see themselves and Device 3 attending
		listGuests(device2, 1L);
		
		// Device 4 visits /api/event/<eventId>/comment/list
		// * Device should be able to see comments from Device 2 and Device 3
		listComments(device4, 1L);
		
		// Device 4 visits /event/<eventId>/decline
		// * On submitting decline, the api should return back the guest information record 
		//   (with status declined)
		declineEvent(device4, 1L);
		
		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should see Device 3 attending and Device 4 not attending	
		listGuests(device2, 1L);
		
		// Device 2 visits /api/device/2/previousGuests
		// * Device 2 should see listed Device 3 and Device 4 (without token information)
		getPreviousDevices(device2);
		
		// CLEAR OUT ALL COOKIES/SESSION INFO
		mockAuthContextService.invalidateSessions();
		
		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should be forbidden from getting the guest list due to authentication
		listGuests(device2, 1L);
		
		// Device 2 visits /api/device/2/authenticate
		// * Re-Authenticate device 2
		authenticate(device2, HttpServletResponse.SC_OK);

		// Device 2 visits /api/event/<eventId>/guest/list
		// * Device 2 should see themselves and Device 3 attending and Device 4 not attending
		listGuests(device2, 1L);
		
	}
	
	private void logRequestResponse(MockHttpRequest request, String requestJson, MockHttpResponse response, String responseJson) {
		log.log(Level.INFO, "\n" + 
				"requestUri: "+request.getUri().getAbsolutePath() + "\n" +
				"requestJson: \n" + requestJson + "\n" +
				"responseCode: " + response.getStatus() + "\n" +
				"responseJson: " + responseJson);
	}

	private Device createDevice(Device device, Long expectedId)
			throws JsonProcessingException, URISyntaxException, IOException {
		String requestJson = mapper.writeValueAsString(device);
		MockHttpRequest request = MockHttpRequest.post("api/device/create");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(new ByteArrayInputStream(requestJson.getBytes()));

		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		String responseJson = response.getContentAsString();
		
		logRequestResponse(request, requestJson, response, responseJson);
		
		Device newDevice = ReaderUtility.read(Device.class,
				MediaType.APPLICATION_JSON, responseJson);
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(newDevice.getDeviceId(), expectedId);

		return newDevice;
	}
	
	
	private void authenticate(Device device, int expectedResult) throws URISyntaxException{
		MockHttpRequest request = MockHttpRequest.get("api/device/" + device.getDeviceId() + "/authenticate/" + device.getToken());
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(expectedResult, response.getStatus());
		mockAuthContextService.createSession(device);
	}
	
	private void listDevices(Device device) throws URISyntaxException, IOException{
		mockAuthContextService.setActiveSession(device);
		
		MockHttpRequest request = MockHttpRequest.get("api/device/list");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void getPreviousDevices(Device device) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		
		MockHttpRequest request = MockHttpRequest.get("api/device/" + device.getDeviceId() + "/previousGuests");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void deleteDevice(Device device, Long deviceId, int expectedResponse) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		
		MockHttpRequest request = MockHttpRequest.delete("api/device/" + deviceId + "/delete");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(expectedResponse, response.getStatus());
			
	}

	private void createEvent(Device device, Event event) throws URISyntaxException, IOException {
		mockAuthContextService.setActiveSession(device);
		
		Guest guest = new Guest("Guest name");
		String json = mapper.writeValueAsString(new EventWithGuest(event, guest));
		MockHttpRequest request = MockHttpRequest.post("api/event/create");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(new ByteArrayInputStream(json.getBytes()));

		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, json.toString(), response, response.getContentAsString());
		EventWithGuest eventWithGuest = ReaderUtility.read(EventWithGuest.class, MediaType.APPLICATION_JSON, response.getContentAsString());
		
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(eventWithGuest.getGuest().getType(), GuestType.HOST);
	}

	private void listEvents(Device device) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		MockHttpRequest request = MockHttpRequest.get("api/event/list");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void attendEvent(Device device, Long eventId) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		MockHttpRequest request = MockHttpRequest.get("api/event/" + eventId + "/attend");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}

	private void getEventInfo(Device device, Long eventId) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		MockHttpRequest request = MockHttpRequest.get("api/event/" + eventId + "/info");
		request.accept(MediaType.APPLICATION_JSON);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void createComment(Device device, Long eventId, Long parentCommentId, String comment) 
			throws URISyntaxException, IOException {
		mockAuthContextService.setActiveSession(device);
		
		String requestJson = mapper.writeValueAsString(new Comment(eventId, 1L, parentCommentId, comment, null));
		MockHttpRequest request = MockHttpRequest.post("api/event/" + eventId + "/1/comment/create");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(new ByteArrayInputStream(requestJson.getBytes()));

		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, requestJson, response, response.getContentAsString());
		Comment savedComment = ReaderUtility.read(Comment.class, MediaType.APPLICATION_JSON, response.getContentAsString());
		
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void listComments(Device device, Long eventId) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		
		MockHttpRequest request = MockHttpRequest.get("api/event/" + eventId + "/1/comment/list");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void updateComment(Device device, Long eventId, Long commentId, String comment) 
			throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		
		MockHttpRequest request = MockHttpRequest.put("api/event/" + eventId + "/1/comment/" + commentId + "/update");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		String json = "{\"comment\":\"" + comment + "\"}";
		request.content(new ByteArrayInputStream(json.getBytes()));
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void deleteComment(Device device, Long eventId, Long commentId) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		
		MockHttpRequest request = MockHttpRequest.delete("api/event/" + eventId + "/1/comment/" + commentId + "/delete");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}

	private void declineEvent(Device device, Long eventId) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		
		MockHttpRequest request = MockHttpRequest.get("api/event/" + eventId + "/1/decline");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	private void listGuests(Device device, Long eventId) throws URISyntaxException{
		mockAuthContextService.setActiveSession(device);
		
		MockHttpRequest request = MockHttpRequest.get("/api/event/" + eventId + "/guest/list");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		logRequestResponse(request, null, response, response.getContentAsString());
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
}
