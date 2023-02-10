package com.example.project_db_interface;

public class Book {
    private int book_id;
    private String title;
    private int count;
    private int weight;
    private String language;
    private int author_id;
    private int genre_id;
    private int format_id;
    private int cover_id;
    private int paper_id;
    private String author_name;
    private String genre_name;

    public Book(int id, String title, int count, int weight,
                String language, int authorId, int genreId, int formatId, int coverId, int paperId) {
        this.book_id = id;
        this.title = title;
        this.count = count;
        this.weight = weight;
        this.language = language;
        this.author_id = authorId;
        this.genre_id = genreId;
        this.format_id = formatId;
        this.cover_id = coverId;
        this.paper_id = paperId;
    }

    public Book() { }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

    public int getFormat_id() {
        return format_id;
    }

    public void setFormat_id(int format_id) {
        this.format_id = format_id;
    }

    public int getCover_id() {
        return cover_id;
    }

    public void setCover_id(int cover_id) {
        this.cover_id = cover_id;
    }

    public int getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(int paper_id) {
        this.paper_id = paper_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + book_id +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", weight=" + weight +
                ", language='" + language + '\'' +
                ", authorId=" + author_id +
                ", genreId=" + genre_id +
                ", formatId=" + format_id +
                ", coverId=" + cover_id +
                ", paperId=" + paper_id +
                '}';
    }
}
