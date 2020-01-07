package com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieImages
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("backdrops")
    @Expose
    private List<Backdrop> backdrops = null;
    @SerializedName("posters")
    @Expose
    private List<Poster> posters = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }



    public class Backdrop
    {
        @SerializedName("aspect_ratio")
        @Expose
        private Double aspectRatio;
        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("iso_639_1")
        @Expose
        private Object iso6391;
        @SerializedName("vote_average")
        @Expose
        private Float voteAverage;
        @SerializedName("vote_count")
        @Expose
        private Integer voteCount;
        @SerializedName("width")
        @Expose
        private Integer width;

        public Double getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Object getIso6391() {
            return iso6391;
        }

        public void setIso6391(Object iso6391) {
            this.iso6391 = iso6391;
        }

        public Float getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Float voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }



    public class Poster
    {

        @SerializedName("aspect_ratio")
        @Expose
        private Double aspectRatio;
        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("iso_639_1")
        @Expose
        private String iso6391;
        @SerializedName("vote_average")
        @Expose
        private Float voteAverage;
        @SerializedName("vote_count")
        @Expose
        private Integer voteCount;
        @SerializedName("width")
        @Expose
        private Integer width;

        public Double getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public Float getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Float voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }








}


