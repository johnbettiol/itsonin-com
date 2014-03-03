package com.itsonin.api;

import javax.servlet.http.HttpSession;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.itsonin.dao.CommentDao;
import com.itsonin.dao.DeviceDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.dao.GuestDeviceDao;
import com.itsonin.exception.mappers.NotFoundExceptionMapper;
import com.itsonin.mocks.MockHttpSession;
import com.itsonin.resteasy.JacksonContextResolver;
import com.itsonin.security.AuthContextService;
import com.itsonin.security.SecurityInterceptor;
import com.itsonin.service.CommentService;
import com.itsonin.service.DeviceService;
import com.itsonin.service.EventService;
import com.itsonin.service.GuestService;

/**
 * @author nkislitsin
 *
 */
public class AppTestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SecurityInterceptor.class);
		bind(AuthContextService.class).in(Singleton.class);
		bind(JacksonContextResolver.class);
		
		bind(NotFoundExceptionMapper.class);
		
		bind(CommentDao.class).in(Singleton.class);
		bind(DeviceDao.class).in(Singleton.class);
		bind(EventDao.class).in(Singleton.class);
		bind(GuestDao.class).in(Singleton.class);
		bind(GuestDeviceDao.class).in(Singleton.class);
		
		bind(CommentService.class).in(Singleton.class);
		bind(DeviceService.class).in(Singleton.class);
		bind(EventService.class).in(Singleton.class);
		bind(GuestService.class).in(Singleton.class);
		
		bind(CommentApi.class);
		bind(DeviceApi.class);
		bind(EventApi.class);
		bind(GuestApi.class);
	}
	
	@Provides 
	HttpSession getMockSession(){
		return new MockHttpSession();
    }

}