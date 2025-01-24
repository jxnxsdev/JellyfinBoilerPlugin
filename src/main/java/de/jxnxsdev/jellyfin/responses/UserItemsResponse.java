package de.jxnxsdev.jellyfin.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class UserItemsResponse {

    @JsonProperty("Items")
    public List<MediaItem> items;

    @JsonProperty("TotalRecordCount")
    public int totalRecordCount;

    @JsonProperty("StartIndex")
    public int startIndex;

    public static class MediaItem {
        @JsonProperty("Name")
        public String name;

        @JsonProperty("ServerId")
        public String serverId;

        @JsonProperty("Id")
        public String id;

        @JsonProperty("Container")
        public String container;

        @JsonProperty("ChannelId")
        public String channelId;

        @JsonProperty("RunTimeTicks")
        public long runTimeTicks;

        @JsonProperty("IsFolder")
        public boolean isFolder;

        @JsonProperty("Type")
        public String type;

        @JsonProperty("UserData")
        public UserData userData;

        @JsonProperty("VideoType")
        public String videoType;

        @JsonProperty("ImageTags")
        public Map<String, String> imageTags;

        @JsonProperty("BackdropImageTags")
        public List<String> backdropImageTags;

        @JsonProperty("ImageBlurHashes")
        public Map<String, Map<String, String>> imageBlurHashes;

        @JsonProperty("LocationType")
        public String locationType;

        @JsonProperty("MediaType")
        public String mediaType;

        @JsonProperty("PremiereDate")
        public String premiereDate;

        @JsonProperty("CommunityRating")
        public Double communityRating;

        @JsonProperty("ProductionYear")
        public Integer productionYear;

        @JsonProperty("EndDate")
        public String endDate;

        @JsonProperty("OfficialRating")
        public String officialRating;

        @JsonProperty("Status")
        public String status;

        @JsonProperty("AirDays")
        public List<String> airDays;

        @JsonProperty("PrimaryImageAspectRatio")
        public Double primaryImageAspectRatio;
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

        @JsonProperty("LastPlayedDate")
        public String lastPlayedDate;

        @JsonProperty("UnplayedItemCount")
        public Integer unplayedItemCount;
    }
}

