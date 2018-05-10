package com.anaconda.skein;

import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.ApplicationResourceUsageReport;
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;

public class MsgUtils {
  public static Msg.ApplicationState.Type stateToProto(YarnApplicationState state) {
    switch (state) {
      case NEW:
        return Msg.ApplicationState.Type.NEW;
      case NEW_SAVING:
        return Msg.ApplicationState.Type.NEW_SAVING;
      case SUBMITTED:
        return Msg.ApplicationState.Type.SUBMITTED;
      case ACCEPTED:
        return Msg.ApplicationState.Type.ACCEPTED;
      case RUNNING:
        return Msg.ApplicationState.Type.RUNNING;
      case FINISHED:
        return Msg.ApplicationState.Type.FINISHED;
      case FAILED:
        return Msg.ApplicationState.Type.FAILED;
      case KILLED:
        return Msg.ApplicationState.Type.KILLED;
    }
    return null; // appease the compiler, but can't get here
  }

  public static Msg.FinalStatus.Type statusToProto(FinalApplicationStatus status) {
    switch (status) {
      case UNDEFINED:
        return Msg.FinalStatus.Type.UNDEFINED;
      case SUCCEEDED:
        return Msg.FinalStatus.Type.SUCCEEDED;
      case FAILED:
        return Msg.FinalStatus.Type.FAILED;
      case KILLED:
        return Msg.FinalStatus.Type.KILLED;
    }
    return null; // appease the compiler, but can't get here
  }

  public static Msg.ApplicationReport.Builder writeApplicationReport(
      ApplicationReport r) {
    Msg.ApplicationReport.Builder builder = Msg.ApplicationReport.newBuilder()
        .setId(r.getApplicationId().toString())
        .setName(r.getName())
        .setUser(r.getUser())
        .setQueue(r.getQueue())
        .setHost(r.getHost())
        .setPort(r.getRpcPort())
        .setTrackingUrl(r.getTrackingUrl())
        .setState(stateToProto(r.getYarnApplicationState()))
        .setFinalStatus(statusToProto(r.getFinalApplicationStatus()))
        .setProgress(r.getProgress())
        .setDiagnostics(r.getDiagnostics())
        .setStartTime(r.getStartTime())
        .setFinishTime(r.getFinishTime())
        .addAllTags(r.getApplicationTags())
        .setUsage(writeUsageReport(r.getApplicationResourceUsageReport()));
    return builder;
  }

  public static Msg.ResourceUsageReport.Builder writeUsageReport(
      ApplicationResourceUsageReport r) {
    return Msg.ResourceUsageReport.newBuilder()
      .setMemorySeconds(Math.max(0, r.getMemorySeconds()))
      .setVcoreSeconds(Math.max(0, r.getVcoreSeconds()))
      .setNumUsedContainers(Math.max(0, r.getNumUsedContainers()))
      .setReservedResources(writeResources(r.getReservedResources()))
      .setNeededResources(writeResources(r.getNeededResources()))
      .setUsedResources(writeResources(r.getUsedResources()));
  }

  public static Msg.Resources.Builder writeResources(Resource r) {
    return Msg.Resources.newBuilder()
      .setMemory(Math.max(0, r.getMemory()))
      .setVcores(Math.max(0, r.getVirtualCores()));
  }
}
