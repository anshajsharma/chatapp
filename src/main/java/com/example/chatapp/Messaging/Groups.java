package com.example.chatapp.Messaging;

import java.util.Map;

public class Groups {
    String groupId,groupIcon,groupDescription,groupName,createdBy;
    long createdAt;
    Map<String,String> usersDetails;

    public Groups() {

    }

    public Groups(long createdAt, String groupId, String groupIcon, String groupDescription, String groupName, String createdBy, Map<String, String> usersDetails) {
        this.createdAt = createdAt;
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupDescription = groupDescription;
        this.groupName = groupName;
        this.createdBy = createdBy;
        this.usersDetails = usersDetails;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Groups(long createdAt, String groupId, String groupIcon, String groupDescription, String groupName, Map<String, String> usersDetails) {
        this.createdAt = createdAt;
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupDescription = groupDescription;
        this.groupName = groupName;
        this.usersDetails = usersDetails;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Groups(long createdAt, String groupId, String groupIcon, String groupDescription, Map<String, String> usersDetails) {
        this.createdAt = createdAt;
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupDescription = groupDescription;
        this.usersDetails = usersDetails;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Map<String, String> getUsersDetails() {
        return usersDetails;
    }

    public void setUsersDetails(Map<String, String> usersDetails) {
        this.usersDetails = usersDetails;
    }
}
