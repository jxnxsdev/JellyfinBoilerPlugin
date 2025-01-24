package de.jxnxsdev.jellyfin.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class ShowsSeasonResponse {

    @JsonProperty("Items")
    public List<Season> items;

    @JsonProperty("TotalRecordCount")
    public int totalRecordCount;

    @JsonProperty("StartIndex")
    public int startIndex;

    public static class Season {
        @JsonProperty("Name")
        public String name;

        @JsonProperty("ServerId")
        public String serverId;

        @JsonProperty("Id")
        public String id;

        @JsonProperty("PremiereDate")
        public String premiereDate;

        @JsonProperty("ChannelId")
        public String channelId;

        @JsonProperty("ProductionYear")
        public int productionYear;

        @JsonProperty("IndexNumber")
        public int indexNumber;

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

        @JsonProperty("SeriesName")
        public String seriesName;

        @JsonProperty("SeriesId")
        public String seriesId;

        @JsonProperty("PrimaryImageAspectRatio")
        public double primaryImageAspectRatio;

        @JsonProperty("SeriesPrimaryImageTag")
        public String seriesPrimaryImageTag;

        @JsonProperty("ImageTags")
        public Map<String, String> imageTags;

        @JsonProperty("BackdropImageTags")
        public List<String> backdropImageTags;

        @JsonProperty("ParentLogoImageTag")
        public String parentLogoImageTag;

        @JsonProperty("ImageBlurHashes")
        public Map<String, Map<String, String>> imageBlurHashes;

        @JsonProperty("LocationType")
        public String locationType;
    }
}
