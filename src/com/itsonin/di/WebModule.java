package com.itsonin.di;

import com.google.inject.servlet.ServletModule;
import com.itsonin.security.AuthFilter;
import com.itsonin.servlet.Page2Servlet;
import com.itsonin.servlet.Page3Servlet;

public class WebModule extends ServletModule {

	  @Override
	  protected void configureServlets() { 
		  super.configureServlets();

		  serve("/page2").with(Page2Servlet.class);
		  serve("/page3").with(Page3Servlet.class);
		  
		  filter("/*").through(AuthFilter.class);
	  }
}
