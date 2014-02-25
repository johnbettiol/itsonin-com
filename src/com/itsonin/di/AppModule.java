package com.itsonin.di;

import javax.servlet.http.HttpSession;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.itsonin.api.CommentApi;
import com.itsonin.api.EventApi;
import com.itsonin.api.EventScheduleApi;
import com.itsonin.api.GuestApi;
import com.itsonin.api.UserApi;
import com.itsonin.dao.CommentDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.EventScheduleDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.dao.UserDao;
import com.itsonin.exception.mappers.NotFoundExceptionMapper;
import com.itsonin.resteasy.JacksonContextResolver;
import com.itsonin.security.AuthContextService;
import com.itsonin.security.AuthFilter;
import com.itsonin.security.SecurityInterceptor;

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
		bind(EventDao.class).in(Singleton.class);
		bind(EventScheduleDao.class).in(Singleton.class);
		bind(GuestDao.class).in(Singleton.class);
		bind(UserDao.class).in(Singleton.class);
		
		bind(CommentApi.class);
		bind(EventApi.class);
		bind(EventScheduleApi.class);
		bind(GuestApi.class);
		bind(UserApi.class);
	}
	
	@Provides 
	HttpSession provideHttpSession() {
		return AuthFilter.getRequest().getSession();
	}


}