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
import com.itsonin.dao.CommentDao;
import com.itsonin.entity.Comment;
import com.itsonin.exception.NotFoundException;
import com.itsonin.exception.mappers.NotFoundExceptionMapper;

/**
 * @author nkislitsin
 *
 */
public class CommentApiTest {

	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Injector i = Guice.createInjector(new AppTestModule());
    private Dispatcher dispatcher;
    private Comment comment;

	@Before
	public void setUp() {
		helper.setUp();
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getProviderFactory().getExceptionMappers().put(NotFoundException.class, new NotFoundExceptionMapper());
        dispatcher.getRegistry().addSingletonResource(i.getInstance(CommentApi.class));
        
        comment = new Comment(1L, 1L, null, "comment", new Date());
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testCreateComment() throws Exception {
		String json = mapper.writeValueAsString(comment);
		MockHttpRequest request = MockHttpRequest.post("api/event/1/1/comment/create");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		request.content(new ByteArrayInputStream(json.getBytes()));

		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		
		Comment comment = ReaderUtility.read(Comment.class, MediaType.APPLICATION_JSON, response.getContentAsString());
		
		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(comment.getCommentId(), Long.valueOf(1L));
	}
	
	@Test
	public void testUpdateComment() throws Exception{
		CommentDao commentDao = i.getInstance(CommentDao.class);
		commentDao.save(comment);
		
		MockHttpRequest request = MockHttpRequest.put("api/event/1/1/comment/1/update");
		request.accept(MediaType.APPLICATION_JSON);
		request.contentType(MediaType.APPLICATION_JSON);
		String json = "{\"comment\":\"newcomment\"}";
		request.content(new ByteArrayInputStream(json.getBytes()));
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(commentDao.get(1L).getComment(), "newcomment");
	}
	
	@Test
	public void testDeleteComment() throws Exception{
		CommentDao commentDao = i.getInstance(CommentDao.class);
		commentDao.save(comment);
		
		MockHttpRequest request = MockHttpRequest.delete("api/event/1/1/comment/1/delete");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals(commentDao.list().size(), 0);
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testListComments() throws Exception{
		CommentDao commentDao = i.getInstance(CommentDao.class);
		commentDao.save(comment);
		
		MockHttpRequest request = MockHttpRequest.get("api/event/1/1/comment/list");
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);

		Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		System.out.println(response.getContentAsString());
	}
}
