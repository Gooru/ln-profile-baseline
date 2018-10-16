package org.gooru.profilebaseline.processors.postprocessors;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;
import org.gooru.profilebaseline.bootstrap.verticles.PostProcessingVerticle.ReRouteProcessingAttributes;
import org.gooru.profilebaseline.infra.constants.HttpConstants;
import org.gooru.profilebaseline.infra.data.ProfileBaselineQueueModel;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.gooru.profilebaseline.processors.AsyncMessageProcessor;
import org.gooru.profilebaseline.responses.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

public class ReRoutePostProcessor implements AsyncMessageProcessor {

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final ReRouteProcessingAttributes reRouteProcessingAttributes;
  private final Future<MessageResponse> result = Future.future();
  private static final Logger LOGGER = LoggerFactory.getLogger(ReRoutePostProcessor.class);

  public ReRoutePostProcessor(Vertx vertx, Message<JsonObject> message,
      ReRouteProcessingAttributes reRouteProcessingAttributes) {
    this.vertx = vertx;
    this.message = message;
    this.reRouteProcessingAttributes = reRouteProcessingAttributes;
  }

  @Override
  public Future<MessageResponse> process() {
    ProfileBaselineQueueModel model = ProfileBaselineQueueModel
        .fromJson(message.body().toString());
    vertx.<RerouteApplicabilityProvider>executeBlocking(future -> {
      RerouteApplicabilityProvider applicabilityProvider = RerouteApplicabilityProviderService
          .build(DBICreator.getDbiForDefaultDS()).getProvider(model);
      future.complete(applicabilityProvider);
    }, asyncResult -> {
      if (asyncResult.succeeded()) {
        triggerReRoutes(asyncResult.result(), model);
      } else {
        result.fail(asyncResult.cause());
      }
    });
    return result;
  }

  private void triggerReRoutes(RerouteApplicabilityProvider rerouteApplicabilityProvider,
      ProfileBaselineQueueModel model) {

    if (rerouteApplicabilityProvider.isRescopeApplicable()) {
      triggerRescope(model);
    }

    if (rerouteApplicabilityProvider.isRoute0Applicable()) {
      triggerRoute0(model);
    }

  }

  private void triggerRoute0(ProfileBaselineQueueModel model) {
    LOGGER.debug("Triggering R0");
    String requestBody = postBodyFromModel(model);
    HttpClientRequest request = reRouteProcessingAttributes.getClient()
        .postAbs(reRouteProcessingAttributes.getRoute0Uri(), response -> {
          if (response.statusCode() != HttpConstants.HttpStatus.SUCCESS.getCode()) {
            LOGGER.warn("Route0 trigger failed, status code: '{}', for model '{}'",
                response.statusCode(), requestBody);
          } else {
            LOGGER.warn("Route0 triggered successfully, status code: '{}', for model '{}'",
                response.statusCode(), requestBody);
          }
        }).exceptionHandler(ex -> {
          LOGGER.warn(
              "Error while communicating with remote server to trigger Route0 for model: '{}'",
              requestBody, ex);
        });

    request.putHeader(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON)
        .end(requestBody);
  }

  private void triggerRescope(ProfileBaselineQueueModel model) {
    LOGGER.debug("Triggering rescope");
    String requestBody = postBodyFromModel(model);
    HttpClientRequest request = reRouteProcessingAttributes.getClient()
        .postAbs(reRouteProcessingAttributes.getRescopeUri(), response -> {
          if (response.statusCode() != HttpConstants.HttpStatus.SUCCESS.getCode()) {
            LOGGER.warn("Rescope trigger failed, status code: '{}', for model '{}'",
                response.statusCode(), requestBody);
          } else {
            LOGGER.warn("Rescope triggered successfully, status code: '{}', for model '{}'",
                response.statusCode(), requestBody);
          }
        }).exceptionHandler(ex -> {
          LOGGER.warn(
              "Error while communicating with remote server to trigger Rescope for model: '{}'",
              requestBody, ex);
        });

    request.putHeader(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON)
        .end(requestBody);

  }

  private String postBodyFromModel(ProfileBaselineQueueModel model) {
    return model.toJsonSummarized();
  }


}
