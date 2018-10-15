package org.gooru.profilebaseline.processors.doprofilebaseline;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.profilebaseline.infra.data.EventBusMessage;
import org.gooru.profilebaseline.infra.jdbi.DBICreator;
import org.gooru.profilebaseline.processors.AsyncMessageProcessor;
import org.gooru.profilebaseline.responses.MessageResponse;
import org.gooru.profilebaseline.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

public class DoLearnerProfileBaselineProcessor implements AsyncMessageProcessor {


  private static final Logger LOGGER = LoggerFactory
      .getLogger(DoLearnerProfileBaselineProcessor.class);

  private final Message<JsonObject> message;
  private final Vertx vertx;
  private final Future<MessageResponse> result;
  private EventBusMessage eventBusMessage;
  private final DoLearnerProfileBaselineService service = new DoLearnerProfileBaselineService(
      DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForDsdbDS());

  public DoLearnerProfileBaselineProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    vertx.<MessageResponse>executeBlocking(future -> {
      try {
        this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
        DoLearnerProfileBaselineCommand command = DoLearnerProfileBaselineCommand
            .builder(eventBusMessage.getRequestBody());
        service.doProfileBaseline(command);
        future.complete(createResponse());
      } catch (Throwable throwable) {
        LOGGER.warn("Encountered exception", throwable);
        future.fail(throwable);
      }
    }, asyncResult -> {
      if (asyncResult.succeeded()) {
        result.complete(asyncResult.result());
      } else {
        result.fail(asyncResult.cause());
      }
    });
    return result;
  }

  private MessageResponse createResponse() {
    return MessageResponseFactory.createOkayResponse(new JsonObject());
  }
}
