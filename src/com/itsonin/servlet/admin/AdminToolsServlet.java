package com.itsonin.servlet.admin;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.security.AuthContextService;
import com.itsonin.service.DataImportService;
import com.itsonin.service.DataImportServiceResult;
import com.itsonin.service.DeviceService;
import com.itsonin.service.EventService;
import com.itsonin.servlet.DefaultServlet;

@SuppressWarnings("serial")
@Singleton
public class AdminToolsServlet extends DefaultServlet {

	private EventService eventService;
	private DeviceService deviceService;
	private DataImportService dis;

	@Inject
	public AdminToolsServlet(AuthContextService authContextService,
			EventService eventService, DeviceService deviceService, DataImportService dis) {
		super(authContextService);
		this.eventService = eventService;
		this.deviceService = deviceService;
		this.dis = dis;
	}

	public void doIoiAction(HttpServletRequest req, HttpServletResponse res) {
		if (irc.getCommand() == null) {
			return;
		}
		switch (irc.getCommand()) {
		case "UpdateAccount":
			irc.getDevice().setLevel(DeviceLevel.valueOf(req.getParameter("level")));
			deviceService.updateDevice(irc.getDevice());
			req.setAttribute("message","Device Level Updated");
			break;
		case "LocationImport":
			if (req.getParameter("type") == null) {
				req.setAttribute("message","No upload type specified!");
			}
			if (req.getParameter("dataType") == null) {
				req.setAttribute("message","No data type specified!");
			}
			switch (req.getParameter("type")) {
			case "default":
				req.setAttribute("result", dis.fromDefaultFiles(irc));
				break;
			case "upload":
				String [] uploadedFiles = (String []) req.getAttribute("_rmFiles");
				if (uploadedFiles == null || uploadedFiles.length ==0) {
					req.setAttribute("message","No files uploaded!");
					break;
				}
				req.setAttribute("result", dis.fromFiles(irc, uploadedFiles));
			case "download":
				if (req.getParameter("url") == null) {
					req.setAttribute("message","No url specified!");
					break;
				}
				req.setAttribute("results", dis.fromUrls(irc, req.getParameterValues("url")));
				break;
			case "text":
				if (req.getParameter("data") == null) {
					req.setAttribute("message","No input data provided specified!");
					break;
				}
				req.setAttribute("results", dis.fromStrings(irc, req.getParameterValues("data")));
				break;
			}
			break;
		}
	}
}
