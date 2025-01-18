package de.jxnxsdev.jellyfin.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemInfoPublicResponse {
    @JsonProperty("LocalAddress")
    private String localAddress;

    @JsonProperty("ServerName")
    private String serverName;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("ProductName")
    private String productName;

    @JsonProperty("OperatingSystem")
    private String operatingSystem;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("StartupWizardCompleted")
    private boolean startupWizardCompleted;

    // No-argument constructor
    public SystemInfoPublicResponse() {}

    // Getters and setters
    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStartupWizardCompleted() {
        return startupWizardCompleted;
    }

    public void setStartupWizardCompleted(boolean startupWizardCompleted) {
        this.startupWizardCompleted = startupWizardCompleted;
    }
}
