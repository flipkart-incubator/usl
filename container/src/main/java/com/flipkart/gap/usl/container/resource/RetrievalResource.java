package com.flipkart.gap.usl.container.resource;

import com.google.inject.Inject;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.gap.usl.container.exceptions.ServingLayerException;
import com.flipkart.gap.usl.core.model.dimension.Dimension;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ankesh.maheshwari on 28/11/16.
 */

@Slf4j
@Path("/gamificationAndPersonalisation")
@Api("/gamificationAndPersonalisation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RetrievalResource {

    @Inject
    private RetrievalService retrievalService;

    @GET
    @Timed
    @Path("/entity/{entityId}/dimensionName/{dimensionName}")
    @ApiOperation("To fetch the response of the Dimension")
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "Dimension not exists for the given entity")})
    public Response getDimensionForEntity(@NotNull @PathParam("entityId") String entityId,
                                          @NotNull @PathParam("dimensionName") String dimensionName) {
        try {
            Optional<Dimension> dimension = retrievalService.getDimensionForEntity(dimensionName, entityId);
            return dimension.map(d -> Response.status(HttpStatus.SC_OK).entity(d).build())
                    .orElseGet(() -> Response.status(HttpStatus.SC_NOT_FOUND).entity("Dimension Not found for the user - " + entityId).build());
        } catch (ServingLayerException e) {
            return Response.status(e.getHttpStatus()).entity(e.getMessage()).build();
        }
    }

    @POST
    @Timed
    @Path("/entity/{entityId}/dimensions")
    @ApiOperation("To fetch the response of the Dimension list")
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.SC_NO_CONTENT, message = "Dimension not exists for the given entity")})
    public Response getDimensionList(@NotNull @PathParam("entityId") String entityId,
                                     @NotNull List<String> dimensionsToFetch) {
        try {
            Collection<Dimension> dimensions = retrievalService.getDimensionsListForEntity(entityId, dimensionsToFetch);
            if (CollectionUtils.isEmpty(dimensions)) {
                return Response.status(HttpStatus.SC_NOT_FOUND).entity("Dimension not found for the user - " + entityId).build();
            } else {
                return Response.status(HttpStatus.SC_OK).entity(dimensions).build();
            }
        } catch (ServingLayerException e) {
            return Response.status(e.getHttpStatus()).entity(e.getMessage()).build();
        }
    }

}
