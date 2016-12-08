package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by fate on 2016/12/2.
 */

public class Rank {
    private String content;
    private double rank;
    private User ranker;

    public Rank(String content, double rank, User ranker) {
        this.content = content;
        this.rank = rank;
        this.ranker = ranker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public User getRanker() {
        return ranker;
    }

    public void setRanker(User ranker) {
        this.ranker = ranker;
    }
}
