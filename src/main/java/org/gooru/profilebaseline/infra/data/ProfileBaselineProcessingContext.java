package org.gooru.profilebaseline.infra.data;

import java.util.Objects;
import java.util.UUID;

/**
 * @author ashish.
 */

public class ProfileBaselineProcessingContext {

  private final UUID userId;
  private final UUID courseId;
  private final UUID classId;
  private String subject;

  private ProfileBaselineProcessingContext(UUID userId, UUID courseId, UUID classId) {
    this.userId = userId;
    this.courseId = courseId;
    this.classId = classId;
  }


  public static ProfileBaselineProcessingContext buildFromProfileBaselineQueueModel(
      ProfileBaselineQueueModel model) {
    return new ProfileBaselineProcessingContext(model.getUserId(), model.getCourseId(),
        model.getClassId());
  }

  public static ProfileBaselineProcessingContext build(UUID userId, UUID courseId, UUID classId) {
    Objects.requireNonNull(userId, "User id should not be null");
    Objects.requireNonNull(courseId, "Course id should not be null");
    return new ProfileBaselineProcessingContext(userId, courseId, classId);
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getCourseId() {
    return courseId;
  }

  public UUID getClassId() {
    return classId;
  }

  public boolean isInClassExperience() {
    return classId != null;
  }

  public boolean isILExperience() {
    return classId == null;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    if (this.subject == null) {
      this.subject = subject;
    } else {
      throw new IllegalStateException(
          "Tried to initialize the subject while it is already initialized");
    }
  }

  @Override
  public String toString() {
    return "ProfileBaselineProcessingContext{" +
        "userId=" + userId +
        ", courseId=" + courseId +
        ", classId=" + classId +
        ", subject='" + subject + '\'' +
        '}';
  }

}
