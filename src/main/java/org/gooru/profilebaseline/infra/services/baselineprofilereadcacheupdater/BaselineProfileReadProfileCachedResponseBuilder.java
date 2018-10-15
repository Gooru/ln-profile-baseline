package org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater.CachedResponse.UserCompetencyMatrixCompetencyModelResponse;
import org.gooru.profilebaseline.infra.services.baselineprofilereadcacheupdater.CachedResponse.UserCompetencyMatrixDomainModelResponse;

/**
 * @author ashish
 */
final class BaselineProfileReadProfileCachedResponseBuilder {

  private static final int STATUS_COMPLETED = 4;
  private static final int STATUS_INFERRED = 2;
  private final CachedResponse response = new CachedResponse();
  private boolean statusCurrentlyInferred = false;

  public CachedResponse build(List<CachedResponseModel> models) {
    return build(models, null);
  }

  public CachedResponse build(
      List<CachedResponseModel> models, Long currentTimeMillis) {

    response.setUserCompetencyMatrix(new ArrayList<>());
    String previousDomainCode = null;

    UserCompetencyMatrixDomainModelResponse domainModelResponse = null;

    for (CachedResponseModel model : models) {
      if (domainModelResponse == null) {
        domainModelResponse = createDomainModelInResponse(response, model);
      }
      if (previousDomainCode != null) {
        if (!Objects.equals(previousDomainCode, model.getDomainCode())) {
          statusCurrentlyInferred = false;
          // In place reversal for fixing the sort order
          Collections.reverse(domainModelResponse.getCompetencies());
          domainModelResponse = createDomainModelInResponse(response, model);
        }
      }
      createCompetencyModelInResponse(domainModelResponse, model);
      previousDomainCode = model.getDomainCode();
    }

    if (currentTimeMillis != null) {
      response.setCreatedAt(currentTimeMillis);
    } else {
      response.setCreatedAt(System.currentTimeMillis());
    }
    return response;
  }

  private void createCompetencyModelInResponse(
      UserCompetencyMatrixDomainModelResponse baselineModelResponse,
      CachedResponseModel model) {
    UserCompetencyMatrixCompetencyModelResponse competencyModelResponse =
        createCompetencyModelResponseFromModel(model);
    baselineModelResponse.getCompetencies().add(competencyModelResponse);
  }

  private UserCompetencyMatrixDomainModelResponse createDomainModelInResponse(
      CachedResponse response,
      CachedResponseModel model) {
    UserCompetencyMatrixDomainModelResponse domainModelResponse;
    domainModelResponse = new UserCompetencyMatrixDomainModelResponse();
    domainModelResponse.setDomainCode(model.getDomainCode());
    domainModelResponse.setDomainName(model.getDomainName());
    domainModelResponse.setDomainSeq(model.getDomainSeq());
    domainModelResponse.setCompetencies(new ArrayList<>());
    response.getUserCompetencyMatrix().add(domainModelResponse);
    return domainModelResponse;
  }

  private UserCompetencyMatrixCompetencyModelResponse createCompetencyModelResponseFromModel(
      CachedResponseModel model) {
    UserCompetencyMatrixCompetencyModelResponse competencyModelResponse =
        new UserCompetencyMatrixCompetencyModelResponse();
    competencyModelResponse.setCompetencyCode(model.getCompetencyCode());
    competencyModelResponse.setCompetencyName(model.getCompetencyName());
    competencyModelResponse.setCompetencyDesc(model.getCompetencyDesc());
    competencyModelResponse.setCompetencyStudentDesc(model.getCompetencyStudentDesc());
    competencyModelResponse.setCompetencySeq(model.getCompetencySeq());
    if (!statusCurrentlyInferred) {
      if (model.getStatus() >= STATUS_COMPLETED) {
        statusCurrentlyInferred = true;
      }
      competencyModelResponse.setStatus(model.getStatus());
    } else {
      competencyModelResponse.setStatus(STATUS_INFERRED);
    }
    return competencyModelResponse;
  }
}
