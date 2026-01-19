package com.researchspace.figshare.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"email", "id"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

  @JsonProperty("used_quota_private")
  private long usedQuotaPrivate;

  @JsonProperty("used_quota")
  private long usedQuota;

  private long quota;

  @JsonProperty("used_quota_public")
  private long usedQuotaPublic;

  @JsonProperty("modified_date")
  private Date modifiedDate;

  @JsonProperty("created_date")
  private Date createdDate;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

	private String email;

  private long id;

  @JsonProperty("institution_id")
  private long institutionId;

  @JsonProperty("institution_user_id")
  private String institutionUserId;

  @JsonProperty("maximum_file_size")
  private long maximumFileSize;

  @JsonProperty("pending_quota_request")
  private boolean pendingQuotaRequest;

  private boolean active;

  @JsonProperty("group_id")
  private Long groupId;

}
