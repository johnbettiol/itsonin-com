package com.itsonin.api;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
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
import com.itsonin.enums.SortOrder;
import com.itsonin.response.SuccessResponse;
import com.itsonin.service.DeviceService;
import com.itsonin.service.GuestService;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class DeviceApi {
	
	private DeviceService deviceService;
	private GuestService guestService;
	
	@Inject
	public DeviceApi(DeviceService deviceService, GuestService guestService){
		this.deviceService = deviceService;
		this.guestService = guestService;
	}
	
	@GET
	@Path("/device/{deviceId}/authenticate/{token}")
	@Produces("application/json")
	public Device authenticate(@PathParam("deviceId")Long deviceId, @PathParam("token")String token) {
		return deviceService.authenticate(deviceId, token);
	}
	
	@POST
	@Path("/device/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Device create(Device device) {
		return deviceService.create(device);
	}
	
	@GET
	@Path("/device/list")
	@Produces("application/json")
	public List<Device> list(@QueryParam("name")String name,
							 @QueryParam("created")Date created,
							 @QueryParam("lastLogin")Date lastLogin,
							 @QueryParam("sortField")String sortField,
							 @QueryParam("sortOrder")SortOrder sortOrder,
							 @QueryParam("offset")Integer offset,
							 @QueryParam("limit")Integer limit) {
		return deviceService.list(name, created, lastLogin, sortField, sortOrder, offset, limit);
	}
	
	@POST
	@Path("/device/{id}/delete")
	@Produces("application/json")
	public Response delete(@PathParam("id")Long id) {
		deviceService.delete(id);
		return Response.ok().entity(new SuccessResponse("Device deleted successfully")).build();
	}
	
	@GET
	@Path("/device/{deviceId}/previousGuests")
	@Produces("application/json")
	public List<Guest> previousGuests(@PathParam("deviceId")Long deviceId,
									  @QueryParam("name")String name,
									  @QueryParam("sortField")String sortField,
									  @QueryParam("sortOrder")SortOrder sortOrder,
									  @QueryParam("offset")Integer offset,
									  @QueryParam("limit")Integer limit,
									  @QueryParam("numberOfLevels")Integer numberOfLevels) {
		return guestService.getPeviousGuests(deviceId, name, sortField, sortOrder, offset, limit, numberOfLevels);
	}

}
