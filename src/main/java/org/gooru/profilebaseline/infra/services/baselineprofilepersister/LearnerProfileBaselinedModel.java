package org.gooru.profilebaseline.infra.services.baselineprofilepersister;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.gooru.profilebaseline.infra.data.ProfileBaselineProcessingContext;
import org.gooru.profilebaseline.infra.jdbi.PGArray;
import org.gooru.profilebaseline.infra.utils.CollectionUtils;

/**
 * @author ashish.
 */

class LearnerProfileBaselinedModel {

  private Long id;
  private final String subjectCode;
  private final String classId;
  private final String courseId;
  private final String userId;
  private final PGArray<String> gutCodes;
  private final String lpData;

  private LearnerProfileBaselinedModel(String subject, UUID userId, UUID courseId, UUID classId,
      PGArray<String> gutCodes, String lpData) {
    this.subjectCode = subject;
    this.classId = Objects.toString(classId, null);
    this.courseId = Objects.toString(courseId, null);
    this.userId = Objects.toString(userId, null);
    this.gutCodes = gutCodes;
    this.lpData = lpData;
  }

  public Long getId() {
    return id;
  }

  public String getSubjectCode() {
    return subjectCode;
  }

  public String getClassId() {
    return classId;
  }

  public String getCourseId() {
    return courseId;
  }

  public String getUserId() {
    return userId;
  }

  public PGArray<String> getGutCodes() {
    return gutCodes;
  }

  public String getLpData() {
    return lpData;
  }

  static LearnerProfileBaselinedModel build(ProfileBaselineProcessingContext context,
      List<String> gutCodes, String lpData) {

    Objects.requireNonNull(lpData);
    Objects.requireNonNull(context.getSubject());
    Objects.requireNonNull(context.getCourseId());
    Objects.requireNonNull(context.getUserId());

    return new LearnerProfileBaselinedModel(context.getSubject(), context.getUserId(),
        context.getCourseId(), context.getClassId(),
        CollectionUtils.convertToSqlArrayOfString(gutCodes), lpData);
  }

  public LearnerProfileBaselinedModelBean asBean() {
    LearnerProfileBaselinedModelBean bean = new LearnerProfileBaselinedModelBean();
    bean.id = id;
    bean.classId = classId;
    bean.courseId = courseId;
    bean.subjectCode = subjectCode;
    bean.userId = userId;
    bean.gutCodes = gutCodes;
    bean.lpData = lpData;
    return bean;
  }

  public static class LearnerProfileBaselinedModelBean {

    private Long id;
    private String subjectCode;
    private String classId;
    private String courseId;
    private String userId;
    private PGArray<String> gutCodes;
    private String lpData;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getSubjectCode() {
      return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
      this.subjectCode = subjectCode;
    }

    public String getClassId() {
      return classId;
    }

    public void setClassId(String classId) {
      this.classId = classId;
    }

    public String getCourseId() {
      return courseId;
    }

    public void setCourseId(String courseId) {
      this.courseId = courseId;
    }

    public String getUserId() {
      return userId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public PGArray<String> getGutCodes() {
      return gutCodes;
    }

    public void setGutCodes(PGArray<String> gutCodes) {
      this.gutCodes = gutCodes;
    }

    public String getLpData() {
      return lpData;
    }

    public void setLpData(String lpData) {
      this.lpData = lpData;
    }
  }
}
