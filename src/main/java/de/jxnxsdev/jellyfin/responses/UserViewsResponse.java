package de.jxnxsdev.jellyfin.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class UserViewsResponse {

    @JsonProperty("Items")
    public List<Item> items;

    @JsonProperty("TotalRecordCount")
    public int totalRecordCount;

    @JsonProperty("StartIndex")
    public int startIndex;

    public static class Item {
        @JsonProperty("Name")
        public String name;

        @JsonProperty("ServerId")
        public String serverId;

        @JsonProperty("Id")
        public String id;

        @JsonProperty("Etag")
        public String etag;

        @JsonProperty("DateCreated")
        public String dateCreated;

        @JsonProperty("CanDelete")
        public boolean canDelete;

        @JsonProperty("CanDownload")
        public boolean canDownload;

        @JsonProperty("SortName")
        public String sortName;

        @JsonProperty("ExternalUrls")
        public List<String> externalUrls;

        @JsonProperty("Path")
        public String path;

        @JsonProperty("EnableMediaSourceDisplay")
        public boolean enableMediaSourceDisplay;

        @JsonProperty("ChannelId")
        public String channelId;

        @JsonProperty("Taglines")
        public List<String> taglines;

        @JsonProperty("Genres")
        public List<String> genres;

        @JsonProperty("PlayAccess")
        public String playAccess;

        @JsonProperty("RemoteTrailers")
        public List<String> remoteTrailers;

        @JsonProperty("ProviderIds")
        public Map<String, String> providerIds;

        @JsonProperty("IsFolder")
        public boolean isFolder;

        @JsonProperty("ParentId")
        public String parentId;

        @JsonProperty("Type")
        public String type;

        @JsonProperty("People")
        public List<String> people;

        @JsonProperty("Studios")
        public List<String> studios;

        @JsonProperty("GenreItems")
        public List<String> genreItems;

        @JsonProperty("LocalTrailerCount")
        public int localTrailerCount;

        @JsonProperty("UserData")
        public UserData userData;

        @JsonProperty("ChildCount")
        public int childCount;

        @JsonProperty("SpecialFeatureCount")
        public int specialFeatureCount;

        @JsonProperty("DisplayPreferencesId")
        public String displayPreferencesId;

        @JsonProperty("Tags")
        public List<String> tags;

        @JsonProperty("PrimaryImageAspectRatio")
        public double primaryImageAspectRatio;

        @JsonProperty("CollectionType")
        public String collectionType;

        @JsonProperty("ImageTags")
        public Map<String, String> imageTags;

        @JsonProperty("BackdropImageTags")
        public List<String> backdropImageTags;

        @JsonProperty("ImageBlurHashes")
        public Map<String, Map<String, String>> imageBlurHashes;

        @JsonProperty("LocationType")
        public String locationType;

        @JsonProperty("LockedFields")
        public List<String> lockedFields;

        @JsonProperty("LockData")
        public boolean lockData;
    }

    public static class UserData {
        @JsonProperty("PlaybackPositionTicks")
        public long playbackPositionTicks;

        @JsonProperty("PlayCount")
        public int playCount;

        @JsonProperty("IsFavorite")
        public boolean isFavorite;

        @JsonProperty("Played")
        public boolean played;

        @JsonProperty("Key")
        public String key;
    }
}

