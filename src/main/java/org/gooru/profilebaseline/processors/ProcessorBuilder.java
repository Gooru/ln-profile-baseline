package org.gooru.profilebaseline.processors;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.profilebaseline.processors.doprofilebaseline.DoLearnerProfileBaselineProcessor;
import org.gooru.profilebaseline.responses.MessageResponse;

/**
 * @author ashish
 */
public final class ProcessorBuilder {

  public static AsyncMessageProcessor buildPlaceHolderExceptionProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return () -> {
      Future<MessageResponse> future = Future.future();
      future.fail(new IllegalStateException("Illegal State for processing command"));
      return future;
    };
  }

  public static AsyncMessageProcessor buildLPBaselineProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return new DoLearnerProfileBaselineProcessor(vertx, message);
  }


  private ProcessorBuilder() {
    throw new AssertionError();
  }
}
