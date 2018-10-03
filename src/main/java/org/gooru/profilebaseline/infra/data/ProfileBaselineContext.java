package org.gooru.profilebaseline.infra.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author ashish
 */
public class ProfileBaselineContext {

  public static ProfileBaselineContext buildForClassJoin(UUID classId, List<UUID> members) {
    return new ProfileBaselineContext(ProfileBaselineSourceType.ClassJoinByMembers, classId,
        members, null);
  }

  public static ProfileBaselineContext buildForOOB(UUID classId, UUID courseId, UUID memberId) {
    List<UUID> members = new ArrayList<>();
    members.add(memberId);
    return new ProfileBaselineContext(ProfileBaselineSourceType.OOB, classId, members, courseId);
  }

  public static ProfileBaselineContext buildForOOB(UUID classId, UUID courseId,
      List<UUID> members) {
    return new ProfileBaselineContext(ProfileBaselineSourceType.OOB, classId, members, courseId);
  }

  public static ProfileBaselineContext buildForSettingChanged(UUID classId) {
    return new ProfileBaselineContext(ProfileBaselineSourceType.SettingChanged, classId,
        Collections.emptyList(), null);
  }

  public static ProfileBaselineContext buildForCourseAssignedToClass(UUID classId, UUID courseId) {
    return new ProfileBaselineContext(ProfileBaselineSourceType.CourseAssignmentToClass, classId,
        Collections.emptyList(),
        courseId);
  }

  private final ProfileBaselineSourceType source;
  private final UUID classId;
  private final List<UUID> memberIds;
  private final UUID courseId;

  private ProfileBaselineContext(ProfileBaselineSourceType source, UUID classId,
      List<UUID> memberIds,
      UUID courseId) {
    this.source = source;
    this.classId = classId;
    this.memberIds = memberIds;
    this.courseId = courseId;
  }

  public ProfileBaselineSourceType getSource() {
    return source;
  }

  public UUID getClassId() {
    return classId;
  }

  public List<UUID> getMemberIds() {
    return Collections.unmodifiableList(memberIds);
  }

  public UUID getCourseId() {
    return courseId;
  }

  @Override
  public String toString() {
    String members = memberIds.stream().map(UUID::toString).collect(Collectors.joining(","));
    return "ProfileBaselineContext{" + "source=" + source.getName() + ", classId=" + classId
        + ", memberIds=" + members
        + ", courseId=" + courseId + '}';
  }

  public ProfileBaselineContext createNewContext(List<UUID> members) {
    return new ProfileBaselineContext(source, classId, members, courseId);
  }

  public ProfileBaselineContext createNewContext(List<UUID> members, UUID courseId) {
    return new ProfileBaselineContext(source, classId, members, courseId);
  }

  public boolean areUsersJoiningClass() {
    return source == ProfileBaselineSourceType.ClassJoinByMembers;
  }

  public boolean hasClassSettingChanged() {
    return source == ProfileBaselineSourceType.SettingChanged;
  }

  public boolean hasCourseBeenAssignedToClass() {
    return source == ProfileBaselineSourceType.CourseAssignmentToClass;
  }

  public boolean isOOBRequest() {
    return source == ProfileBaselineSourceType.OOB;
  }
}
