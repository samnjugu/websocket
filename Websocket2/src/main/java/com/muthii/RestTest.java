package com.muthii;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;



@Path("/test")
public class RestTest {
	
	public static final CacheControl NO_CACHE;
	static
	{
		NO_CACHE = new CacheControl();
		NO_CACHE.setNoCache(true);
	}
	public static final String MIME_TYPE_HTML = "text/html";
	
	@GET
	@Path("/rest")
	public Response getTest()
	{
		return Response.status(Response.Status.OK).cacheControl(NO_CACHE).type
                (MIME_TYPE_HTML).entity("Rest1 is working").build();
	}

}