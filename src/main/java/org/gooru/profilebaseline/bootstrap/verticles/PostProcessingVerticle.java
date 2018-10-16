package org.gooru.profilebaseline.bootstrap.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import java.util.Objects;
import org.gooru.profilebaseline.infra.constants.Constants;
import org.gooru.profilebaseline.processors.ProcessorBuilder;
import org.gooru.profilebaseline.responses.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */
public class PostProcessingVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessingVerticle.class);
  private ReRouteProcessingAttributes reRouteProcessingAttributes;

  @Override
  public void start(Future<Void> startFuture) {

    EventBus eb = vertx.eventBus();
    eb.localConsumer(Constants.EventBus.MBEP_LP_BASELINE_POST_PROCESSOR, this::processMessage)
        .completionHandler(result -> {
          if (result.succeeded()) {
            LOGGER.info("LP baseline post processor point ready to listen");
            startFuture.complete();
          } else {
            LOGGER.error(
                "Error registering the LP Baseline post processor handler. Halting the machinery");
            startFuture.fail(result.cause());
            Runtime.getRuntime().halt(1);
          }
        });
    initializeHttpClient();
  }

  @Override
  public void stop(Future<Void> stopFuture) {
  }

  private void processMessage(Message<JsonObject> message) {
    String op = message.headers().get(Constants.Message.MSG_OP);
    Future<MessageResponse> future;
    switch (op) {
      case Constants.Message.MSG_OP_POSTPROCESS_RESCOPE_R0:
        future = ProcessorBuilder
            .buildReRoutePostProcessor(vertx, message, reRouteProcessingAttributes).process();
        break;
      default:
        LOGGER.warn("Invalid operation type");
        future = ProcessorBuilder.buildPlaceHolderExceptionProcessor(vertx, message).process();
    }

    future.setHandler(event -> {
      if (event.succeeded()) {
        LOGGER.info("Post processing op completed.");
      } else {
        LOGGER.warn("Post processing op failed", event.cause());
      }
    });
  }

  private void initializeHttpClient() {

    final Integer timeout = config().getInteger("http.timeout");
    final Integer poolSize = config().getInteger("http.poolSize");
    Objects.requireNonNull(timeout);
    Objects.requireNonNull(poolSize);
    HttpClient client = vertx.createHttpClient(
        new HttpClientOptions().setConnectTimeout(timeout).setMaxPoolSize(poolSize));
    String rescopeUri = config().getString("rescope.uri");
    String route0Uri = config().getString("route0.uri");
    Objects.requireNonNull(rescopeUri);
    Objects.requireNonNull(route0Uri);

    reRouteProcessingAttributes = new ReRouteProcessingAttributes(client, rescopeUri, route0Uri);
  }

  public static class ReRouteProcessingAttributes {

    private final HttpClient client;
    private final String rescopeUri;
    private final String route0Uri;


    ReRouteProcessingAttributes(HttpClient client, String rescopeUri,
        String route0Uri) {
      this.client = client;
      this.rescopeUri = rescopeUri;
      this.route0Uri = route0Uri;
    }

    public HttpClient getClient() {
      return client;
    }

    public String getRescopeUri() {
      return rescopeUri;
    }

    public String getRoute0Uri() {
      return route0Uri;
    }
  }
}
