package org.gooru.profilebaseline.infra.constants;

import java.util.UUID;

/**
 * @author ashish.
 */
public final class Constants {

  private Constants() {
    throw new AssertionError();
  }

  public static final class EventBus {

    public static final String MBEP_LP_BASELINE = "org.gooru.profile.baseline.eventbus.lpbaseline";

    public static final String MBEP_LP_BASELINE_QUEUE_PROCESSOR = "org.gooru.profile.baseline.eventbus.queueprocessor";
    public static final String MBEP_LP_BASELINE_POST_PROCESSOR = "org.gooru.profile.baseline.eventbus.postprocessor";
    public static final String MBUS_TIMEOUT = "event.bus.send.timeout.seconds";

    private EventBus() {
      throw new AssertionError();
    }
  }

  public static final class Message {

    public static final String MSG_OP = "mb.operation";
    public static final String MSG_OP_STATUS = "mb.op.status";
    public static final String MSG_OP_STATUS_SUCCESS = "mb.op.status.success";
    public static final String MSG_OP_STATUS_FAIL = "mb.op.status.fail";
    public static final String MSG_OP_LP_BASELINE_DO = "mb.op.profilebaseline.do";
    public static final String MSG_USER_ANONYMOUS = "anonymous";
    public static final String MSG_USER_ID = "user_id";
    public static final String MSG_HTTP_STATUS = "http.status";
    public static final String MSG_HTTP_BODY = "http.body";
    public static final String MSG_HTTP_HEADERS = "http.headers";

    public static final String MSG_OP_LP_BASELINE = "op.lpbaseline";
    public static final String MSG_OP_POSTPROCESS_RESCOPE_R0 = "op.rescope.r0";
    public static final String MSG_MESSAGE = "message";

    private Message() {
      throw new AssertionError();
    }
  }

  public static final class Response {

    private Response() {
      throw new AssertionError();
    }
  }

  public static final class Params {

    public static final String PARAM_MEMBER_ID = "member_id";
    public static final String PARAM_CLASS_ID = "class_id";

    private Params() {
      throw new AssertionError();
    }
  }

  public static final class Route {

    public static final String API_INTERNAL_BANNER = "/api/internal/banner";
    public static final String API_INTERNAL_METRICS = "/api/internal/metrics";
    public static final String API_LPBASELINE_CALCULATE = "/api/internal/profilebaseline";

    private Route() {
      throw new AssertionError();
    }
  }

  public static final class Misc {

    public static final String COMPETENCY_PLACEHOLDER = new UUID(0, 0).toString();
    public static final String USER_PLACEHOLDER = new UUID(0, 0).toString();

    private Misc() {
      throw new AssertionError();
    }
  }

}
