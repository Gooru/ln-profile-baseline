package org.gooru.profilebaseline.responses;

import io.vertx.core.json.JsonObject;
import org.gooru.profilebaseline.infra.constants.Constants;
import org.gooru.profilebaseline.infra.constants.HttpConstants;

/**
 * @author ashish.
 */
public final class MessageResponseFactory {

  private static final String API_VERSION_DEPRECATED = "API version is deprecated";

  public static MessageResponse createInvalidRequestResponse() {
    return new MessageResponse.Builder().setStatusBadRequest()
        .setResponseBody(new JsonObject().put(Constants.Message.MSG_MESSAGE, "Invalid request"))
        .build();
  }

  public static MessageResponse createForbiddenResponse() {
    return new MessageResponse.Builder().setStatusForbidden()
        .setResponseBody(new JsonObject().put(Constants.Message.MSG_MESSAGE, "Forbidden")).build();
  }

  public static MessageResponse createInternalErrorResponse() {
    return new MessageResponse.Builder().setStatusInternalError()
        .setResponseBody(new JsonObject().put(Constants.Message.MSG_MESSAGE, "Internal error"))
        .build();
  }

  public static MessageResponse createInvalidRequestResponse(String message) {
    return new MessageResponse.Builder().setStatusBadRequest()
        .setResponseBody(new JsonObject().put(Constants.Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createForbiddenResponse(String message) {
    return new MessageResponse.Builder().setStatusForbidden()
        .setResponseBody(new JsonObject().put(Constants.Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createInternalErrorResponse(String message) {
    return new MessageResponse.Builder().setStatusInternalError()
        .setResponseBody(new JsonObject().put(Constants.Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createNoContentResponse() {
    return new MessageResponse.Builder().setStatusNoContent()
        .setResponseBody(new JsonObject().put(Constants.Message.MSG_MESSAGE, "No Content")).build();
  }

  public static MessageResponse createNotFoundResponse(String message) {
    return new MessageResponse.Builder().setStatusNotFound()
        .setResponseBody(new JsonObject().put(Constants.Message.MSG_MESSAGE, message)).build();
  }

  public static MessageResponse createOkayResponse(JsonObject body) {
    return new MessageResponse.Builder().setStatusOkay().setResponseBody(body).build();
  }

  public static MessageResponse createVersionDeprecatedResponse() {
    return new MessageResponse.Builder().setStatusHttpCode(HttpConstants.HttpStatus.GONE)
        .setContentTypeJson()
        .setResponseBody(
            new JsonObject().put(Constants.Message.MSG_MESSAGE, API_VERSION_DEPRECATED)).build();
  }

  public static MessageResponse createCreatedResponse(String location) {
    return new MessageResponse.Builder().setHeader("Location", location).setStatusCreated().build();
  }

  private MessageResponseFactory() {
    throw new AssertionError();
  }

}
