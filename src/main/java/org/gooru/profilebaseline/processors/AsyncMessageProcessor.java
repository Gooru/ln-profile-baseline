package org.gooru.profilebaseline.processors;

import io.vertx.core.Future;
import org.gooru.profilebaseline.responses.MessageResponse;

/**
 * @author ashish.
 */
public interface AsyncMessageProcessor {

  Future<MessageResponse> process();

}
