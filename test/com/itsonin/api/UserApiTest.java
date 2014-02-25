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
import com.itsonin.dao.UserDao;
import com.itsonin.di.AppModule;
import com.itsonin.entity.User;
import com.itsonin.enums.UserStatus;
import com.itsonin.enums.UserType;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 *
 */
public class UserApiTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Injector i = Guice.createInjector(new AppModule());
    private Dispatcher dispatcher;

	@Before
	public void setUp() {
		helper.setUp();
		
		AuthContextService authContextService = i.getInstance(AuthContextService.class);
		UserDao userDao = i.getInstance(UserDao.class);
		UserApi userApi = new UserApi(userDao, authContextService);

        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getRegistry().addSingletonResource(userApi);
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void run() throws Exception {
		String json = mapper.writeValueAsString(new User(1L, UserType.SUPER, UserStatus.ACTIVE, 
				"msisdn", "email@email.com", "name", "salt", "hash", new Date(), new Date()));
		MockHttpRequest request = MockHttpRequest.post("api/user/create");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(new ByteArrayInputStream(json.getBytes()));

		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		
		User user = ReaderUtility.read(User.class, MediaType.APPLICATION_JSON, response.getContentAsString());

		Assert.assertEquals(user.getId(), Long.valueOf(1L));
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
}
