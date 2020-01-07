package com.example.mymovieapp.Models.MovieModels.FromOnlineDatabase;

//https://api.themoviedb.org/3/movie/181812/recommendations?api_key=c5f034d2365ff3aaf5bc40944d249acc&language=en-US&page=1

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieRecomendations
{

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public List<Result> getResults()
    {
        return results;
    }

    public void setResults(List<Result> results)
    {
        this.results = results;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    public int getTotalResults()
    {
        return totalResults;
    }

    public void setTotalResults(int totalResults)
    {
        this.totalResults = totalResults;
    }


    public class Result
    {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("video")
        @Expose
        private boolean video;
        @SerializedName("vote_count")
        @Expose
        private int voteCount;
        @SerializedName("vote_average")
        @Expose
        private double voteAverage;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("release_date")
        @Expose
        private String releaseDate;
        @SerializedName("original_language")
        @Expose
        private String originalLanguage;
        @SerializedName("original_title")
        @Expose
        private String originalTitle;
        @SerializedName("genre_ids")
        @Expose
        private List<Integer> genreIds = null;
        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;
        @SerializedName("adult")
        @Expose
        private boolean adult;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("popularity")
        @Expose
        private double popularity;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public boolean isVideo()
        {
            return video;
        }

        public void setVideo(boolean video)
        {
            this.video = video;
        }

        public int getVoteCount()
        {
            return voteCount;
        }

        public void setVoteCount(int voteCount)
        {
            this.voteCount = voteCount;
        }

        public double getVoteAverage()
        {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage)
        {
            this.voteAverage = voteAverage;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getReleaseDate()
        {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate)
        {
            this.releaseDate = releaseDate;
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

        public String getBackdropPath()
        {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath)
        {
            this.backdropPath = backdropPath;
        }

        public boolean isAdult()
        {
            return adult;
        }

        public void setAdult(boolean adult)
        {
            this.adult = adult;
        }

        public String getOverview()
        {
            return overview;
        }

        public void setOverview(String overview)
        {
            this.overview = overview;
        }

        public String getPosterPath()
        {
            return posterPath;
        }

        public void setPosterPath(String posterPath)
        {
            this.posterPath = posterPath;
        }

        public double getPopularity()
        {
            return popularity;
        }

        public void setPopularity(double popularity)
        {
            this.popularity = popularity;
        }

    }
}
