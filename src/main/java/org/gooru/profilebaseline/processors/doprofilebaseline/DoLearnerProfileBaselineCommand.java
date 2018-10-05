package org.gooru.profilebaseline.processors.doprofilebaseline;

import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.UUID;
import org.gooru.profilebaseline.infra.data.ProfileBaselineContext;
import org.gooru.profilebaseline.infra.data.ProfileBaselineSourceType;
import org.gooru.profilebaseline.infra.utils.UuidUtils;

/**
 * @author ashish.
 */

class DoLearnerProfileBaselineCommand {

  static DoLearnerProfileBaselineCommand builder(JsonObject requestBody) {
    DoLearnerProfileBaselineCommand result = new DoLearnerProfileBaselineCommand();
    result.classId = UuidUtils
        .convertStringToUuid(requestBody.getString(CommandAttributes.CLASS_ID));
    result.source = ProfileBaselineSourceType
        .builder(requestBody.getString(CommandAttributes.SOURCE));
    result.memberIds = UuidUtils
        .convertToUUIDList(requestBody.getJsonArray(CommandAttributes.MEMBER_IDS));
    result.courseId = UuidUtils
        .convertStringToUuid(requestBody.getString(CommandAttributes.COURSE_ID));
    result.override = requestBody.getBoolean(CommandAttributes.OVERRIDE, false);
    result.validate();
    return result;
  }


  private ProfileBaselineSourceType source;
  private UUID classId;
  private List<UUID> memberIds;
  private UUID courseId;
  private boolean override;

  private DoLearnerProfileBaselineCommand() {

  }

  UUID getCourseId() {
    return courseId;
  }

  ProfileBaselineSourceType getSource() {
    return source;
  }

  UUID getClassId() {
    return classId;
  }

  List<UUID> getMemberIds() {
    return memberIds;
  }

  boolean isOverride() {
    return override;
  }

  boolean hasMembershipInfo() {
    return (source == ProfileBaselineSourceType.OOB) || (source
        == ProfileBaselineSourceType.ClassJoinByMembers);
  }

  boolean applyToAllMembers() {
    return (source == ProfileBaselineSourceType.CourseAssignmentToClass) ||
        (source == ProfileBaselineSourceType.SettingChanged);
  }

  ProfileBaselineContext asRescopeContext() {
    switch (source) {
      case ClassJoinByMembers:
        return ProfileBaselineContext.buildForClassJoin(classId, memberIds);
      case SettingChanged:
        return ProfileBaselineContext.buildForSettingChanged(classId);
      case CourseAssignmentToClass:
        return ProfileBaselineContext.buildForCourseAssignedToClass(classId, courseId);
      case OOB:
        return ProfileBaselineContext.buildForOOB(classId, courseId, memberIds);
      default:
        throw new IllegalStateException("Invalid rescope source type");
    }
  }

  private void validate() {
    if (classId == null && courseId == null) {
      throw new IllegalArgumentException("Both class and course should not be absent");
    }
    if (source == null) {
      throw new IllegalArgumentException("Invalid source");
    }
    if (((memberIds == null || memberIds.isEmpty())
        && (source == ProfileBaselineSourceType.OOB
        || source == ProfileBaselineSourceType.ClassJoinByMembers))
        || (memberIds != null && !memberIds.isEmpty() && source != ProfileBaselineSourceType.OOB
        && source != ProfileBaselineSourceType.ClassJoinByMembers)) {
      throw new IllegalArgumentException(
          "Members should be provided only for OOB/class join type rescope");
    }
  }

  final class CommandAttributes {

    static final String COURSE_ID = "courseId";
    static final String OVERRIDE = "override";
    static final String SOURCE = "source";
    static final String CLASS_ID = "classId";
    static final String MEMBER_IDS = "memberIds";

    private CommandAttributes() {
      throw new AssertionError();
    }
  }
}
