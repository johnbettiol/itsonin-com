package com.itsonin.di;

import javax.servlet.http.HttpSession;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.itsonin.api.CommentApi;
import com.itsonin.api.DeviceApi;
import com.itsonin.api.EventApi;
import com.itsonin.api.GuestApi;
import com.itsonin.api.GuestDeviceApi;
import com.itsonin.dao.CommentDao;
import com.itsonin.dao.DeviceDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.dao.GuestDeviceDao;
import com.itsonin.exception.mappers.NotFoundExceptionMapper;
import com.itsonin.resteasy.JacksonContextResolver;
import com.itsonin.security.AuthContextService;
import com.itsonin.security.AuthFilter;
import com.itsonin.security.SecurityInterceptor;
import com.itsonin.service.CommentService;
import com.itsonin.service.DeviceService;
import com.itsonin.service.EventService;
import com.itsonin.service.GuestDeviceService;
import com.itsonin.service.GuestService;

/**
 * @author nkislitsin
 *
 */
public class AppModule extends AbstractModule {

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
		bind(GuestDeviceService.class).in(Singleton.class);
		
		bind(CommentApi.class);
		bind(DeviceApi.class);
		bind(EventApi.class);
		bind(GuestApi.class);
		bind(GuestDeviceApi.class);
	}
	
	@Provides 
	HttpSession provideHttpSession() {
		return AuthFilter.getRequest().getSession();
	}

}