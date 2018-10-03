package org.gooru.profilebaseline.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish
 */
public enum ProfileBaselineSourceType {

  SettingChanged("setting.changed", 1),
  CourseAssignmentToClass("assign.course.to.class", 2),
  ClassJoinByMembers("join.class", 3),
  OOB("oob", 4);

  private final String name;
  private final int order;

  ProfileBaselineSourceType(String name, int order) {
    this.name = name;
    this.order = order;
  }

  public String getName() {
    return this.name;
  }

  public int getOrder() {
    return order;
  }

  private static final Map<String, ProfileBaselineSourceType> LOOKUP = new HashMap<>(
      values().length);

  static {
    for (ProfileBaselineSourceType sourceType : values()) {
      LOOKUP.put(sourceType.name, sourceType);
    }
  }

  public static ProfileBaselineSourceType builder(String type) {
    ProfileBaselineSourceType result = LOOKUP.get(type);
    if (result == null) {
      throw new IllegalArgumentException("Invalid source type: " + type);
    }
    return result;
  }

}
