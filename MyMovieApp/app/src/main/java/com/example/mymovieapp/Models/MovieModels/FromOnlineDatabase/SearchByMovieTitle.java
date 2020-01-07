package com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchByMovieTitle
{

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getTotalResults()
    {
        return totalResults;
    }

    public void setTotalResults(int totalResults)
    {
        this.totalResults = totalResults;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    public List<Result> getResults()
    {
        return results;
    }

    public void setResults(List<Result> results)
    {
        this.results = results;
    }


    public class Result
    {

        @SerializedName("popularity")
        @Expose
        private double popularity;
        @SerializedName("vote_count")
        @Expose
        private int voteCount;
        @SerializedName("video")
        @Expose
        private boolean video;
        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("adult")
        @Expose
        private boolean adult;
        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;
        @SerializedName("original_language")
        @Expose
        private String originalLanguage;
        @SerializedName("original_title")
        @Expose
        private String originalTitle;
        @SerializedName("genre_ids")
        @Expose
        private List<Integer> genreIds = null;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("vote_average")
        @Expose
        private double voteAverage;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("release_date")
        @Expose
        private String releaseDate;

        public double getPopularity()
        {
            return popularity;
        }

        public void setPopularity(double popularity)
        {
            this.popularity = popularity;
        }

        public int getVoteCount()
        {
            return voteCount;
        }

        public void setVoteCount(int voteCount)
        {
            this.voteCount = voteCount;
        }

        public boolean isVideo()
        {
            return video;
        }

        public void setVideo(boolean video)
        {
            this.video = video;
        }

        public String getPosterPath()
        {
            return posterPath;
        }

        public void setPosterPath(String posterPath)
        {
            this.posterPath = posterPath;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public boolean isAdult()
        {
            return adult;
        }

        public void setAdult(boolean adult)
        {
            this.adult = adult;
        }

        public String getBackdropPath()
        {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath)
        {
            this.backdropPath = backdropPath;
        }

        public String getOriginalLanguage()
        {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage)
        {
            this.originalLanguage = originalLanguage;
        }

        public String getOriginalTitle()
        {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle)
        {
            this.originalTitle = originalTitle;
        }

        public List<Integer> getGenreIds()
        {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds)
        {
            this.genreIds = genreIds;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public double getVoteAverage()
        {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage)
        {
            this.voteAverage = voteAverage;
        }

        public String getOverview()
        {
            return overview;
        }

        public void setOverview(String overview)
        {
            this.overview = overview;
        }

        public String getReleaseDate()
        {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate)
        {
            this.releaseDate = releaseDate;
        }
    }
}
