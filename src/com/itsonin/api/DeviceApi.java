package com.itsonin.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.entity.Device;
import com.itsonin.entity.Guest;
import com.itsonin.service.DeviceService;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class DeviceApi {
	
	private DeviceService deviceService;
	
	@Inject
	public DeviceApi(DeviceService deviceService){
		this.deviceService = deviceService;
	}
	
	@GET
	@Path("/device/{deviceId}/authenticate")
	@Produces("application/json")
	public Response authenticate(@QueryParam("deviceId")Long deviceId) {
		return Response.ok().build();
	}
	
	@POST
	@Path("/device/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Device create(Device device) {
		deviceService.create(device);
		return device;
	}
	
	@GET
	@Path("/device/list")
	@Produces("application/json")
	public List<Device> list() {
		return deviceService.list();
	}
	
	@DELETE
	@Path("/device/{id}/delete")
	@Produces("application/json")
	public Response delete(@PathParam("id")Long id) {
		deviceService.delete(id);
		return Response.ok().build();
	}
	
	@GET
	@Path("/device/{id}/previousGuests")
	@Produces("application/json")
	public List<Guest> previousGuests(@PathParam("id")String id) {
		return null;
	}

}
