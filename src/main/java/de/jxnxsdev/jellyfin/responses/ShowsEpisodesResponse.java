package de.jxnxsdev.jellyfin.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class ShowsEpisodesResponse {

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

        @JsonProperty("Container")
        public String container;

        @JsonProperty("PremiereDate")
        public String premiereDate;

        @JsonProperty("ChannelId")
        public String channelId;

        @JsonProperty("CommunityRating")
        public double communityRating;

        @JsonProperty("RunTimeTicks")
        public long runTimeTicks;

        @JsonProperty("ProductionYear")
        public int productionYear;

        @JsonProperty("IndexNumber")
        public int indexNumber;

        @JsonProperty("ParentIndexNumber")
        public int parentIndexNumber;

        @JsonProperty("IsFolder")
        public boolean isFolder;

        @JsonProperty("Type")
        public String type;

        @JsonProperty("ParentLogoItemId")
        public String parentLogoItemId;

        @JsonProperty("ParentBackdropItemId")
        public String parentBackdropItemId;

        @JsonProperty("ParentBackdropImageTags")
        public List<String> parentBackdropImageTags;

        @JsonProperty("UserData")
        public UserData userData;

        @JsonProperty("SeriesName")
        public String seriesName;

        @JsonProperty("SeriesId")
        public String seriesId;

        @JsonProperty("SeasonId")
        public String seasonId;

        @JsonProperty("SeriesPrimaryImageTag")
        public String seriesPrimaryImageTag;

        @JsonProperty("SeasonName")
        public String seasonName;

        @JsonProperty("VideoType")
        public String videoType;

        @JsonProperty("ImageTags")
        public Map<String, String> imageTags;

        @JsonProperty("BackdropImageTags")
        public List<String> backdropImageTags;

        @JsonProperty("ParentLogoImageTag")
        public String parentLogoImageTag;

        @JsonProperty("ImageBlurHashes")
        public ImageBlurHashes imageBlurHashes;

        @JsonProperty("LocationType")
        public String locationType;

        @JsonProperty("MediaType")
        public String mediaType;

        public static class UserData {

            @JsonProperty("PlayedPercentage")
            public double playedPercentage;

            @JsonProperty("PlaybackPositionTicks")
            public long playbackPositionTicks;

            @JsonProperty("PlayCount")
            public int playCount;

            @JsonProperty("IsFavorite")
            public boolean isFavorite;

            @JsonProperty("LastPlayedDate")
            public String lastPlayedDate;

            @JsonProperty("Played")
            public boolean played;

            @JsonProperty("Key")
            public String key;
        }

        public static class ImageBlurHashes {

            @JsonProperty("Primary")
            public Map<String, String> primary;

            @JsonProperty("Logo")
            public Map<String, String> logo;

            @JsonProperty("Backdrop")
            public Map<String, String> backdrop;
        }
    }
}
