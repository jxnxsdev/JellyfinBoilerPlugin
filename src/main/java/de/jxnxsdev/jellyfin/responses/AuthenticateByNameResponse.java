package de.jxnxsdev.jellyfin.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AuthenticateByNameResponse {

    @JsonProperty("User")
    public User user;

    @JsonProperty("SessionInfo")
    public SessionInfo sessionInfo;

    @JsonProperty("AccessToken")
    public String accessToken;

    @JsonProperty("ServerId")
    public String serverId;

    public static class User {
        @JsonProperty("Name")
        public String name;

        @JsonProperty("ServerId")
        public String serverId;

        @JsonProperty("Id")
        public String id;

        @JsonProperty("HasPassword")
        public boolean hasPassword;

        @JsonProperty("HasConfiguredPassword")
        public boolean hasConfiguredPassword;

        @JsonProperty("HasConfiguredEasyPassword")
        public boolean hasConfiguredEasyPassword;

        @JsonProperty("EnableAutoLogin")
        public boolean enableAutoLogin;

        @JsonProperty("LastLoginDate")
        public String lastLoginDate;

        @JsonProperty("LastActivityDate")
        public String lastActivityDate;

        @JsonProperty("Configuration")
        public Configuration configuration;

        @JsonProperty("Policy")
        public Policy policy;
    }

    public static class Configuration {
        @JsonProperty("PlayDefaultAudioTrack")
        public boolean playDefaultAudioTrack;

        @JsonProperty("SubtitleLanguagePreference")
        public String subtitleLanguagePreference;

        @JsonProperty("DisplayMissingEpisodes")
        public boolean displayMissingEpisodes;

        @JsonProperty("GroupedFolders")
        public List<String> groupedFolders;

        @JsonProperty("SubtitleMode")
        public String subtitleMode;

        @JsonProperty("DisplayCollectionsView")
        public boolean displayCollectionsView;

        @JsonProperty("EnableLocalPassword")
        public boolean enableLocalPassword;

        @JsonProperty("OrderedViews")
        public List<String> orderedViews;

        @JsonProperty("LatestItemsExcludes")
        public List<String> latestItemsExcludes;

        @JsonProperty("MyMediaExcludes")
        public List<String> myMediaExcludes;

        @JsonProperty("HidePlayedInLatest")
        public boolean hidePlayedInLatest;

        @JsonProperty("RememberAudioSelections")
        public boolean rememberAudioSelections;

        @JsonProperty("RememberSubtitleSelections")
        public boolean rememberSubtitleSelections;

        @JsonProperty("EnableNextEpisodeAutoPlay")
        public boolean enableNextEpisodeAutoPlay;
    }

    public static class Policy {
        @JsonProperty("IsAdministrator")
        public boolean isAdministrator;

        @JsonProperty("IsHidden")
        public boolean isHidden;

        @JsonProperty("IsDisabled")
        public boolean isDisabled;

        @JsonProperty("BlockedTags")
        public List<String> blockedTags;

        @JsonProperty("EnableUserPreferenceAccess")
        public boolean enableUserPreferenceAccess;

        @JsonProperty("AccessSchedules")
        public List<String> accessSchedules;

        @JsonProperty("BlockUnratedItems")
        public List<String> blockUnratedItems;

        @JsonProperty("EnableRemoteControlOfOtherUsers")
        public boolean enableRemoteControlOfOtherUsers;

        @JsonProperty("EnableSharedDeviceControl")
        public boolean enableSharedDeviceControl;

        @JsonProperty("EnableRemoteAccess")
        public boolean enableRemoteAccess;

        @JsonProperty("EnableLiveTvManagement")
        public boolean enableLiveTvManagement;

        @JsonProperty("EnableLiveTvAccess")
        public boolean enableLiveTvAccess;

        @JsonProperty("EnableMediaPlayback")
        public boolean enableMediaPlayback;

        @JsonProperty("EnableAudioPlaybackTranscoding")
        public boolean enableAudioPlaybackTranscoding;

        @JsonProperty("EnableVideoPlaybackTranscoding")
        public boolean enableVideoPlaybackTranscoding;

        @JsonProperty("EnablePlaybackRemuxing")
        public boolean enablePlaybackRemuxing;

        @JsonProperty("ForceRemoteSourceTranscoding")
        public boolean forceRemoteSourceTranscoding;

        @JsonProperty("EnableContentDeletion")
        public boolean enableContentDeletion;

        @JsonProperty("EnableContentDeletionFromFolders")
        public List<String> enableContentDeletionFromFolders;

        @JsonProperty("EnableContentDownloading")
        public boolean enableContentDownloading;

        @JsonProperty("EnableSyncTranscoding")
        public boolean enableSyncTranscoding;

        @JsonProperty("EnableMediaConversion")
        public boolean enableMediaConversion;

        @JsonProperty("EnabledDevices")
        public List<String> enabledDevices;

        @JsonProperty("EnableAllDevices")
        public boolean enableAllDevices;

        @JsonProperty("EnabledChannels")
        public List<String> enabledChannels;

        @JsonProperty("EnableAllChannels")
        public boolean enableAllChannels;

        @JsonProperty("EnabledFolders")
        public List<String> enabledFolders;

        @JsonProperty("EnableAllFolders")
        public boolean enableAllFolders;

        @JsonProperty("InvalidLoginAttemptCount")
        public int invalidLoginAttemptCount;

        @JsonProperty("LoginAttemptsBeforeLockout")
        public int loginAttemptsBeforeLockout;

        @JsonProperty("MaxActiveSessions")
        public int maxActiveSessions;

        @JsonProperty("EnablePublicSharing")
        public boolean enablePublicSharing;

        @JsonProperty("BlockedMediaFolders")
        public List<String> blockedMediaFolders;

        @JsonProperty("BlockedChannels")
        public List<String> blockedChannels;

        @JsonProperty("RemoteClientBitrateLimit")
        public int remoteClientBitrateLimit;

        @JsonProperty("AuthenticationProviderId")
        public String authenticationProviderId;

        @JsonProperty("PasswordResetProviderId")
        public String passwordResetProviderId;

        @JsonProperty("SyncPlayAccess")
        public String syncPlayAccess;
    }

    public static class SessionInfo {
        @JsonProperty("PlayState")
        public PlayState playState;

        @JsonProperty("AdditionalUsers")
        public List<String> additionalUsers;

        @JsonProperty("Capabilities")
        public Capabilities capabilities;

        @JsonProperty("RemoteEndPoint")
        public String remoteEndPoint;

        @JsonProperty("PlayableMediaTypes")
        public List<String> playableMediaTypes;

        @JsonProperty("Id")
        public String id;

        @JsonProperty("UserId")
        public String userId;

        @JsonProperty("UserName")
        public String userName;

        @JsonProperty("Client")
        public String client;

        @JsonProperty("LastActivityDate")
        public String lastActivityDate;

        @JsonProperty("LastPlaybackCheckIn")
        public String lastPlaybackCheckIn;

        @JsonProperty("DeviceName")
        public String deviceName;

        @JsonProperty("DeviceId")
        public String deviceId;

        @JsonProperty("ApplicationVersion")
        public String applicationVersion;

        @JsonProperty("IsActive")
        public boolean isActive;

        @JsonProperty("SupportsMediaControl")
        public boolean supportsMediaControl;

        @JsonProperty("SupportsRemoteControl")
        public boolean supportsRemoteControl;

        @JsonProperty("NowPlayingQueue")
        public List<String> nowPlayingQueue;

        @JsonProperty("NowPlayingQueueFullItems")
        public List<String> nowPlayingQueueFullItems;

        @JsonProperty("HasCustomDeviceName")
        public boolean hasCustomDeviceName;

        @JsonProperty("ServerId")
        public String serverId;

        @JsonProperty("SupportedCommands")
        public List<String> supportedCommands;
    }

    public static class PlayState {
        @JsonProperty("CanSeek")
        public boolean canSeek;

        @JsonProperty("IsPaused")
        public boolean isPaused;

        @JsonProperty("IsMuted")
        public boolean isMuted;

        @JsonProperty("RepeatMode")
        public String repeatMode;
    }

    public static class Capabilities {
        @JsonProperty("PlayableMediaTypes")
        public List<String> playableMediaTypes;

        @JsonProperty("SupportedCommands")
        public List<String> supportedCommands;

        @JsonProperty("SupportsMediaControl")
        public boolean supportsMediaControl;

        @JsonProperty("SupportsContentUploading")
        public boolean supportsContentUploading;

        @JsonProperty("SupportsPersistentIdentifier")
        public boolean supportsPersistentIdentifier;

        @JsonProperty("SupportsSync")
        public boolean supportsSync;
    }
}
