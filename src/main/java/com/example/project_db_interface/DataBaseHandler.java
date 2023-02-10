package com.example.project_db_interface;

import javafx.scene.control.Alert;

import java.sql.*;

public class DataBaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName + "? " +
                "verifyServerCertificate=false" +
                "&useSSL=false" +
                "&requireSSL=false" +
                "&useLegacyDatetimeCode=false" +
                "&amp" +
                "&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void signUpUser(User user) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                Const.USERS_FIRSTNAME + "," + Const.USERS_LASTNAME + "," +
                Const.USERS_USERNAME + "," + Const.USERS_PASSWORD + "," +
                Const.USERS_LOCATION + "," + Const.USERS_GENDER + ")" +
                "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getFirstName());
            prSt.setString(2, user.getLastName());
            prSt.setString(3, user.getUserName());
            prSt.setString(4, user.getPassword());
            prSt.setString(5, user.getLocation());
            prSt.setString(6, user.getGender());
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Введите корректные значения");
            alert.showAndWait();
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USERS_USERNAME + "=? AND " + Const.USERS_PASSWORD + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getUserName());
            prSt.setString(2, user.getPassword());

            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getStaff() {
        ResultSet resSet = null;

        String select = "SELECT * FROM staff";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getGenres() {
        ResultSet resSet = null;

        String select = "SELECT * FROM genre";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getAuthors() {
        ResultSet resSet = null;

        String select = "SELECT * FROM author";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getBooks() {
        ResultSet resSet = null;

        //String select = "SELECT * FROM " + Const.BOOK_TABLE;
        String select = "SELECT * FROM book " +
                "INNER JOIN author USING(author_id) " +
                "INNER JOIN genre USING(genre_id)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getRequests() {
        ResultSet resSet = null;

        String select = "SELECT author_id, status_id, request_id AS id, author.name AS Автор, " +
                "status.name AS Статус, date AS Дата, recommend_letter AS Требование" +
                " FROM request INNER JOIN status USING(status_id) " +
                "INNER JOIN author USING(author_id)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getPublishings() {
        ResultSet resSet = null;

        String select = "SELECT staff_id, edition_id, publishing_id AS id, staff.name AS Редактор, date AS Дата, " +
                "results AS Результат, edition.edits AS Правки, request_id " +
                "FROM publishing INNER JOIN staff USING(staff_id) " +
                "INNER JOIN edition USING(edition_id);";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getPapers() {
        ResultSet resSet = null;

        String select = "SELECT * FROM paper";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getFormats() {
        ResultSet resSet = null;

        String select = "SELECT * FROM format";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet getCovers() {
        ResultSet resSet = null;

        String select = "SELECT * FROM cover";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    private int getId(String field, String callFunc) {
        try {
            CallableStatement cstmt = getDbConnection().prepareCall(callFunc);
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, field);
            cstmt.execute();
            return cstmt.getInt(1);
        } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
            return 0;
        }
    }

    public int getAuthorId(String author) {
        String callCheckAuthor = "{? = call checkAuthor(?)}";
        return getId(author, callCheckAuthor);
    }

    public int getGenreId(String genre) {
        String callCheckGenre = "{? = call checkGenre(?)}";
        return getId(genre, callCheckGenre);
    }

    public int getStatusId(String status) {
        String callCheckStatus = "{? = call checkStatus(?)}";
        return getId(status, callCheckStatus);
    }

    public int getStaffId(String staff) {
        String callCheckStaff = "{? = call checkStaff(?)}";
        return getId(staff, callCheckStaff);
    }

    public int getEditionId(String edits) {
        String callCheckEdition = "{? = call checkEdition(?)}";
        return getId(edits, callCheckEdition);
    }

    public void addRequest(Request request) {
        String insert = "INSERT INTO " + Const.REQUEST_TABLE + "(" +
                Const.REQUESTS_DATE + "," + Const.REQUESTS_RECOMMEND_LETTER + "," +
                Const.REQUESTS_AUTHOR_ID + "," + Const.REQUESTS_STATUS_ID + ") VALUES(?,?,?,?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setDate(1, request.getDate());
            prSt.setString(2, request.getRecommendLetter());
            prSt.setInt(3, request.getAuthor_id());
            prSt.setInt(4, request.getStatus_id());
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Введите корректные значения");
            alert.showAndWait();
        }
    }

    public void addPublishing(Publishing publishing) {
        String insert = "INSERT INTO " + Const.PUBLISHING_TABLE + "(" +
                Const.PUBLISHINGS_DATE + "," + Const.PUBLISHINGS_RESULTS + "," +
                Const.PUBLISHINGS_EDITION_ID + "," + Const.PUBLISHINGS_REQUEST_ID + "," +
                Const.PUBLISHINGS_STAFF_ID + ") VALUES(?,?,?,?,?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setDate(1, publishing.getDate());
            prSt.setString(2, publishing.getResults());
            prSt.setInt(3, publishing.getEdition_id());
            prSt.setInt(4, publishing.getRequest_id());
            prSt.setInt(5, publishing.getStaff_id());
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Введите корректные значения");
            alert.showAndWait();
        }
    }

    public void editRequest(Request request) {
        String update = "UPDATE " + Const.REQUEST_TABLE + " SET " +
                Const.REQUESTS_DATE + "=?," + Const.REQUESTS_RECOMMEND_LETTER + "=?," +
                Const.REQUESTS_AUTHOR_ID + "=?," + Const.REQUESTS_STATUS_ID +
                "=? WHERE " + Const.REQUESTS_ID + " = '" + request.getRequest_id() + "'";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setDate(1, request.getDate());
            prSt.setString(2, request.getRecommendLetter());
            prSt.setInt(3, request.getAuthor_id());
            prSt.setInt(4, request.getStatus_id());
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Введите корректные значения");
            alert.showAndWait();
        }
    }

    public void editPublishing(Publishing publishing) {
        String update = "UPDATE " + Const.PUBLISHING_TABLE + " SET " +
                Const.PUBLISHINGS_DATE + "=?," + Const.PUBLISHINGS_RESULTS + "=?," +
                Const.PUBLISHINGS_STAFF_ID + "=?," + Const.PUBLISHINGS_REQUEST_ID + "=?," +
                Const.PUBLISHINGS_EDITION_ID + "=? WHERE " +
                Const.PUBLISHINGS_ID + " = '" + publishing.getPublishing_id() + "'";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setDate(1, publishing.getDate());
            prSt.setString(2, publishing.getResults());
            prSt.setInt(3, publishing.getStaff_id());
            prSt.setInt(4, publishing.getRequest_id());
            prSt.setInt(5, publishing.getEdition_id());
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Введите корректные значения");
            alert.showAndWait();
        }
    }

    public void addBook(String title, String count, String weight, String lang, String author,
                        String genre, String format_id, String cover_id, String paper_id) {
        String insert = "INSERT INTO " + Const.BOOK_TABLE + "(" +
                Const.BOOKS_TITLE + "," + Const.BOOKS_COUNT + "," +
                Const.BOOKS_WEIGHT + "," + Const.BOOKS_LANGUAGE + "," +
                Const.BOOKS_AUTHOR_ID + "," + Const.BOOKS_GENRE_ID + "," +
                Const.BOOKS_FORMAT_ID + "," + Const.BOOKS_COVER_ID + "," +
                Const.BOOKS_PAPER_ID + ") VALUES(?,?,?,?,?,?,?,?,?)";

        try {
            int author_id = getAuthorId(author);
            int genre_id = getGenreId(genre);

            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, title);
            prSt.setInt(2, Integer.parseInt(count));
            prSt.setInt(3, Integer.parseInt(weight));
            prSt.setString(4, lang);
            prSt.setInt(5, author_id);
            prSt.setInt(6, genre_id);
            prSt.setInt(7, Integer.parseInt(format_id));
            prSt.setInt(8, Integer.parseInt(cover_id));
            prSt.setInt(9, Integer.parseInt(paper_id));
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Введите корректные значения");
            alert.showAndWait();
        }
    }

    public void deleteBook(Book book) {
        String delete = "DELETE FROM " + Const.BOOK_TABLE +
                " WHERE " + Const.BOOKS_ID + "="+book.getBook_id();
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteRequest(Request request) {
        String delete = "DELETE FROM " + Const.REQUEST_TABLE +
                " WHERE " + Const.REQUESTS_ID + "="+request.getRequest_id();
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deletePublishing(Publishing publishing) {
        String delete = "DELETE FROM " + Const.PUBLISHING_TABLE +
                " WHERE " + Const.PUBLISHINGS_ID + "="+publishing.getPublishing_id();
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void editBook(String title, String count, String weight, String lang, String author,
                        String genre, String format_id, String cover_id, String paper_id, int id) {
        String update = "UPDATE " + Const.BOOK_TABLE + " SET " +
                Const.BOOKS_TITLE + "=?," + Const.BOOKS_COUNT + "=?," +
                Const.BOOKS_WEIGHT + "=?," + Const.BOOKS_LANGUAGE + "=?," +
                Const.BOOKS_AUTHOR_ID + "=?," + Const.BOOKS_GENRE_ID + "=?," +
                Const.BOOKS_FORMAT_ID + "=?," + Const.BOOKS_COVER_ID + "=?," +
                Const.BOOKS_PAPER_ID + "=? WHERE " + Const.BOOKS_ID + " = '" + id + "'";

        try {
            int author_id = getAuthorId(author);
            int genre_id = getGenreId(genre);

            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.setString(1, title);
            prSt.setInt(2, Integer.parseInt(count));
            prSt.setInt(3, Integer.parseInt(weight));
            prSt.setString(4, lang);
            prSt.setInt(5, author_id);
            prSt.setInt(6, genre_id);
            prSt.setInt(7, Integer.parseInt(format_id));
            prSt.setInt(8, Integer.parseInt(cover_id));
            prSt.setInt(9, Integer.parseInt(paper_id));
            prSt.execute();
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Введите корректные значения");
            alert.showAndWait();
        }
    }

    public String getAuthorFromId(int id) {
        String get = "{? = call getAuthorFromId(?)}";
        try {
            CallableStatement cstmt = getDbConnection().prepareCall(get);
            cstmt.registerOutParameter(1, Types.VARCHAR);
            cstmt.setInt(2, id);
            cstmt.execute();
            return cstmt.getString(1);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getGenreFromId(int id) {
        String get = "{? = call getGenreFromId(?)}";
        try {
            CallableStatement cstmt = getDbConnection().prepareCall(get);
            cstmt.registerOutParameter(1, Types.VARCHAR);
            cstmt.setInt(2, id);
            cstmt.execute();
            return cstmt.getString(1);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
