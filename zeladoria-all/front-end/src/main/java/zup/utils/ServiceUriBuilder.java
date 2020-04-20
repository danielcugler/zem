package zup.utils;

import javax.ws.rs.core.UriInfo;

import zup.webservices.adm.BroadcastMessageService2;
import zup.webservices.adm.CitizenService2;
import zup.webservices.adm.EntityCategoryService2;
import zup.webservices.adm.EntityService2;
import zup.webservices.adm.MessageModelService2;
import zup.webservices.adm.SystemUserProfileService2;
import zup.webservices.adm.SystemUserService2;

import java.net.URI;

public class ServiceUriBuilder {
	
	public String edSystemUser(String id, UriInfo uriInfo) {
        return createResourceUri(SystemUserService2.class, "ed", id, uriInfo).toString();
    }
	
	public String selfSystemUser(String id, UriInfo uriInfo) {
        return createResourceUri(SystemUserService2.class, id, uriInfo).toString();
    }
	
	public String edSystemUserProfile(Integer id, UriInfo uriInfo) {
        return createResourceUri(SystemUserProfileService2.class, "ed", id, uriInfo).toString();
    }
	
	public String selfSystemUserProfile(Integer id, UriInfo uriInfo) {
        return createResourceUri(SystemUserProfileService2.class, id, uriInfo).toString();
    }
	
	public String edBroadcastMessage(Integer id, UriInfo uriInfo) {
        return createResourceUri(BroadcastMessageService2.class, "ed", id, uriInfo).toString();
    }
	
	public String selfBroadcastMessage(Integer id, UriInfo uriInfo) {
        return createResourceUri(BroadcastMessageService2.class, id, uriInfo).toString();
    }

	public String edCitizen(String id, UriInfo uriInfo) {
        return createResourceUri(CitizenService2.class, "ed", id, uriInfo).toString();
    }
	
	public String selfCitizen(String id, UriInfo uriInfo) {
        return createResourceUri(CitizenService2.class, id, uriInfo).toString();
    }
	
	public String edMessageModel(Integer id, UriInfo uriInfo) {
        return createResourceUri(MessageModelService2.class, "ed", id, uriInfo).toString();
    }
	
	public String selfMessageModel(Integer id, UriInfo uriInfo) {
        return createResourceUri(MessageModelService2.class, id, uriInfo).toString();
    }
	
	public String edEntityCategory(Integer id, UriInfo uriInfo) {
        return createResourceUri(EntityCategoryService2.class, "ed", id, uriInfo).toString();
    }
	
	public String selfEntityCategory(Integer id, UriInfo uriInfo) {
        return createResourceUri(EntityCategoryService2.class, id, uriInfo).toString();
    }
	
	
	public String edEntity(Integer id, UriInfo uriInfo) {
        return createResourceUri(EntityService2.class, "ed", id, uriInfo).toString();
    }
	
	public String selfEntity(Integer id, UriInfo uriInfo) {
        return createResourceUri(EntityService2.class, id, uriInfo).toString();
    }

    private URI createResourceUri(Class<?> resourceClass, String method, Integer id, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(resourceClass).path(method).path(Integer.toString(id)).build();
    }

    private URI createResourceUri(Class<?> resourceClass, String method, String id, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(resourceClass).path(method).path(id).build();
    }
    
    private URI createResourceUri(Class<?> resourceClass, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(resourceClass).build();
    }

    private URI createResourceUri(Class<?> resourceClass, Integer id, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(resourceClass).path(Integer.toString(id)).build();
    }
    
    private URI createResourceUri(Class<?> resourceClass, String id, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(resourceClass).path(id).build();
    }
    
}
