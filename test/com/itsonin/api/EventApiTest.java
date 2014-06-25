package com.itsonin.api;

import java.io.ByteArrayInputStream;
import java.util.Date;

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
import com.itsonin.dao.EventDao;
import com.itsonin.dto.EventWithGuest;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventSharability;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventCategory;
import com.itsonin.enums.EventSubCategory;
import com.itsonin.enums.EventVisibility;
import com.itsonin.exception.NotFoundException;
import com.itsonin.exception.mappers.NotFoundExceptionMapper;

/**
 * @author nkislitsin
 *
 */
public class EventApiTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Injector i = Guice.createInjector(new AppTestModule());
    private Dispatcher dispatcher;
    private Event event;

	@Before
	public void setUp() {
		helper.setUp();
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getProviderFactory().getExceptionMappers().put(NotFoundException.class, new NotFoundExceptionMapper());
        dispatcher.getRegistry().addSingletonResource(i.getInstance(EventApi.class));
        
        event = new Event(EventCategory.GOTO, EventSubCategory.PARTY, EventSharability.NORMAL, EventVisibility.PUBLIC, EventStatus.ACTIVE, 
        		EventFlexibility.NEGOTIABLE, "event title", "event description", "event notes", 
        		new Date(), new Date(), 1.0d, 2.0d, "location.url", "location title", 
        		"location address", new Date());
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testCreateEvent() throws Exception {
		Guest guest = new Guest("Guest name");
		String json = mapper.writeValueAsString(new EventWithGuest(event, guest));
		MockHttpRequest request = MockHttpRequest.post("api/event/create");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(new ByteArrayInputStream(json.getBytes()));

		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		
		EventWithGuest eventWithGuest = ReaderUtility.read(EventWithGuest.class, MediaType.APPLICATION_JSON, response.getContentAsString());
		
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(eventWithGuest.getEvent().getEventId(), Long.valueOf(1L));
	}
	
	@Test
	public void testUpdateEvent() throws Exception{
		EventDao eventDao = i.getInstance(EventDao.class);
		eventDao.save(event);
		
		MockHttpRequest request = MockHttpRequest.put("api/event/1/update");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		String json = "{\"title\":\"newtitle\"}";
		request.content(new ByteArrayInputStream(json.getBytes()));
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(eventDao.get(1L).getTitle(), "newtitle");
	}
	
	@Test
	public void testGetEventInfo() throws Exception{
		EventDao eventDao = i.getInstance(EventDao.class);
		eventDao.save(event);
		
		MockHttpRequest request = MockHttpRequest.get("api/event/1/info");
		request.accept(MediaType.APPLICATION_JSON);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testListEvents() throws Exception{
		EventDao eventDao = i.getInstance(EventDao.class);
		eventDao.save(event);
		
		MockHttpRequest request = MockHttpRequest.get("api/event/list");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testCancelEvent() throws Exception{
		EventDao eventDao = i.getInstance(EventDao.class);
		eventDao.save(event);
		
		MockHttpRequest request = MockHttpRequest.get("api/event/1/1/cancel");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(eventDao.get(1L).getStatus(), EventStatus.CANCELLED);
	}
	
	@Test
	public void testDeclineEvent() throws Exception{
		EventDao eventDao = i.getInstance(EventDao.class);
		eventDao.save(event);
		
		MockHttpRequest request = MockHttpRequest.get("api/event/1/1/decline");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		//TODO:check
	}
}
