package com.itsonin.di;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.itsonin.api.CommentApi;
import com.itsonin.api.DeviceApi;
import com.itsonin.api.EventApi;
import com.itsonin.api.GuestApi;
import com.itsonin.dao.CommentDao;
import com.itsonin.dao.DeviceDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.exception.mappers.ForbiddenExceptionMapper;
import com.itsonin.exception.mappers.NotFoundExceptionMapper;
import com.itsonin.exception.mappers.UnauthorizedExceptionMapper;
import com.itsonin.resteasy.JacksonContextResolver;
import com.itsonin.security.AuthAndRouteFilter;
import com.itsonin.security.AuthContextService;
import com.itsonin.security.impl.AuthContextServiceImpl;
import com.itsonin.service.CommentService;
import com.itsonin.service.DeviceService;
import com.itsonin.service.EventService;
import com.itsonin.service.GuestService;
import com.itsonin.servlet.DefaultServlet;
import com.itsonin.servlet.EventEditServlet;
import com.itsonin.servlet.EventInformationServlet;
import com.itsonin.servlet.EventInvitationServlet;
import com.itsonin.servlet.EventListServlet;
import com.itsonin.servlet.EventNewServlet;
import com.itsonin.servlet.admin.AdminToolsServlet;
import com.itsonin.tasks.AdminTasks;

public class WebModule extends ServletModule {

	@Override
	protected void configureServlets() {
		super.configureServlets();
		bind(AuthContextService.class).to(AuthContextServiceImpl.class);
		bind(JacksonContextResolver.class);
		bind(NotFoundExceptionMapper.class);
		bind(ForbiddenExceptionMapper.class);
		bind(UnauthorizedExceptionMapper.class);

		bind(CommentDao.class).in(Singleton.class);
		bind(DeviceDao.class).in(Singleton.class);
		bind(EventDao.class).in(Singleton.class);
		bind(GuestDao.class).in(Singleton.class);

		bind(CommentService.class).in(Singleton.class);
		bind(DeviceService.class).in(Singleton.class);
		bind(EventService.class).in(Singleton.class);
		bind(GuestService.class).in(Singleton.class);

		bind(CommentApi.class);
		bind(DeviceApi.class);
		bind(EventApi.class);
		bind(GuestApi.class);

		bind(AdminTasks.class);
		bind(AuthAndRouteFilter.class).in(Singleton.class);
		
		filter("/*").through(AuthAndRouteFilter.class);
		serve("/DefaultServlet").with(DefaultServlet.class);
		serve("/EventEditServlet").with(EventEditServlet.class);
		serve("/AdminToolsServlet").with(AdminToolsServlet.class);
		serve("/EventNewServlet").with(EventNewServlet.class);
		serve("/EventInvitationServlet").with(EventInvitationServlet.class);
		serve("/EventInformationServlet").with(EventInformationServlet.class);
		serve("/EventListServlet").with(EventListServlet.class);
	}
}
