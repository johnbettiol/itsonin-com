package com.itsonin.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.URLDataSource;

import com.google.appengine.labs.repackaged.com.google.common.io.LineReader;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.itsonin.dao.CounterDao;
import com.itsonin.dao.DeviceDao;
import com.itsonin.entity.Device;
import com.itsonin.enums.DataImportType;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.SortOrder;
import com.itsonin.exception.ForbiddenException;
import com.itsonin.exception.NotFoundException;
import com.itsonin.exception.UnauthorizedException;
import com.itsonin.security.AuthContext;
import com.itsonin.security.AuthContextService;
import com.itsonin.web.IoiRouterContext;

/**
 * @author nkislitsin
 * 
 */
public class DataImportService {

	private DeviceDao deviceDao;
	private CounterDao counterDao;
	private AuthContextService authContextService;

	@Inject
	public DataImportService(DeviceDao deviceDao, CounterDao counterDao,
			AuthContextService authContextService) {
		this.deviceDao = deviceDao;
		this.counterDao = counterDao;
		this.authContextService = authContextService;
	}

	public Device create(Device device) {
		device.setDeviceId(counterDao.nextDeviceId());
		device.setCreated(new Date());
		device.setToken(UUID.randomUUID().toString());
		Key<Device> key = deviceDao.save(device);
		return deviceDao.get(key);
	}

	public List<Device> list(String name, Date created, Date lastLogin,
			String sortField, SortOrder sortOrder, Integer offset, Integer limit) {
		if (!isSuper())
			throw new ForbiddenException("Not allowed");

		return deviceDao.list(name, created, lastLogin, sortField, sortOrder,
				offset, limit);
	}

	public void delete(Long id) {
		if (!isSuper() && !isOwner(id))
			throw new ForbiddenException("Not allowed");

		Device device = get(id);
		deviceDao.delete(device);
	}

	public Device getDeviceByToken(String token) {
		return deviceDao.getDeviceByToken(token);
	}

	public Device authenticate(Long id, String token) {
		Device device = deviceDao.get(id);
		authContextService.set(new AuthContext(device));
		String validateToken = device.getToken().toString();
		if (device == null || !token.equals(validateToken)) {
			throw new UnauthorizedException("Not Authenticated");
		} else {
			return device;
		}
	}

	public Device get(Long id) {
		Device device = deviceDao.get(id);
		if (device == null)
			throw new NotFoundException("Device with id=" + id
					+ " does not exist");
		else
			return device;
	}

	boolean isSuper() {
		Device device = authContextService.get().getDevice();
		if (DeviceLevel.SUPER.equals(device.getLevel()))
			return true;
		else
			return false;
	}

	boolean isOwner(Long deviceId) {
		Device device = authContextService.get().getDevice();
		if (device.getDeviceId().equals(deviceId)) {
			return true;
		} else {
			return false;
		}
	}

	public void updateDevice(Device device) {
		deviceDao.save(device);

	}

	public DataImportServiceResult[] fromDefaultFiles(IoiRouterContext irc) {
		String[] defaultFiles = { "seeding/locations.csv" };
		return fromFiles(irc, defaultFiles);
	}

	public DataImportServiceResult[] fromFiles(IoiRouterContext irc, String[] uploadedFiles) {
		DataImportServiceResult[] results = new DataImportServiceResult[uploadedFiles.length];
		for (int i = 0; i < uploadedFiles.length; i++) {
			DataImportType dataType = DataImportType.AUTO;
			results[i] = fromFile(irc, dataType, uploadedFiles[i]);
		}
		return results;
	}

	private DataImportServiceResult fromFile(IoiRouterContext irc, DataImportType dataType,
			String filePath) {
		InputStream is;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			return fromStream(irc, dataType, is, filePath);
		} catch (Exception e) {
			return new DataImportServiceResult(dataType, filePath, e.getMessage(), e);
		}
	}

	public DataImportServiceResult fromStream(IoiRouterContext irc, DataImportType dataType,
			InputStream fis, String source) throws Exception {
		// Should use a proper CSV parser here ;)
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(fis);
		LineReader lr = new LineReader(isr);
		int lineNo = 1;
		String headerLine = lr.readLine();
		String[] headerCols = headerLine != null ? headerLine.split("\t")
				: null;
		if (headerCols == null || headerCols.length <= 3) {
			throw new Exception("No headers found in data source!");
		}
		
		if (dataType == DataImportType.AUTO) {
			dataType = getTypeFromHeaders(headerCols);
		}
		
		if (dataType == DataImportType.UNKNOWN) {
			throw new Exception("Cannot parse data, unidentified data type!");
		}

		int[] headerColPos = findColumnPositions(headerCols);
		DataImportServiceResult result = new DataImportServiceResult(dataType, source);
		String currLine;
		while ((currLine = lr.readLine()) != null) {
			lineNo ++;
			String[] currCols = currLine.split("\t");
			if (currCols.length != headerCols.length) {
				throw new Exception(lineNo + ": column mismatch ("
						+ currCols.length + " != " + headerCols.length + ")");
			}
			processRow(headerCols, currCols);
		}
		result.setTotalProcessed(lineNo);
		return result;
	}

	private DataImportType getTypeFromHeaders(String[] headerCols) {
		// TODO Auto-generated method stub
		return null;
	}

	private String [] columnNames = {"type", "city", "area", "name", "gps_position", "gps_boundary"};
	
	private int[] findColumnPositions(String[] headerCols) {
		int [] columnPositions = new int[columnNames.length];
		for (int i = 0; i < columnNames.length; i++) {
			columnPositions[i] = -1;
			for (int j = 0; j < headerCols.length; j++) {
				if (headerCols[j].toLowerCase().equals(columnNames[i])) {
					columnPositions[i] = j;
				}
			}
		}
		return columnPositions;
	}

	private void processRow(String[] headerCols, String[] currCols) {
		// TODO Auto-generated method stub
	}

	public DataImportServiceResult fromUrl(IoiRouterContext irc, DataImportType dataType, String url) {
		try {
			URLDataSource urlds = new URLDataSource(new URL(url));
			return fromStream(irc, dataType, urlds.getInputStream(), url);
		} catch (Exception e) {
			return new DataImportServiceResult(dataType, url, e.getMessage(), e);
		}
	}

	public DataImportServiceResult[] fromUrls(IoiRouterContext irc,
			String[] urls) {
		DataImportServiceResult[] results = new DataImportServiceResult[urls.length];
		for (int i = 0; i < urls.length; i++) {
			results[i] = fromUrl(irc, DataImportType.AUTO, urls[i]);
		}
		return results;
	}

	public DataImportServiceResult[] fromStrings(IoiRouterContext irc, String [] importData) {
		DataImportServiceResult[] results = new DataImportServiceResult[importData.length];
		for (int i = 0; i < importData.length; i++) {
			results[i] = fromString(irc, importData[i]);
		}
		return results;
	}

	public DataImportServiceResult fromString(IoiRouterContext irc,
			String importData) {
		ByteArrayInputStream bais;
		DataImportType dataType = DataImportType.AUTO;
		try {
			bais = new ByteArrayInputStream(importData.getBytes("UTF-8"));
			return fromStream(irc, dataType, bais, "text");
		} catch (Exception e) {
			return new DataImportServiceResult(dataType, "inputData", e.getMessage(), e);
		}
	}

}
