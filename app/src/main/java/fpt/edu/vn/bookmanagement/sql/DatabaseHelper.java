package fpt.edu.vn.bookmanagement.sql;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fpt.edu.vn.bookmanagement.model.Book;
import fpt.edu.vn.bookmanagement.model.Category;
import fpt.edu.vn.bookmanagement.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    // User Table Columns names
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CATEGORY_DESCRIPTION = "category_description";
    // User Table Columns names
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_BOOK_NAME = "book_name";
    public static final String COLUMN_BOOK_CATEGORY_ID = "book_category_id";
    public static final String COLUMN_BOOK_DESCRIPTION = "book_description";
    public static final String COLUMN_BOOK_CONTENT = "book_content";
    // User Table Columns names
    public static final String COLUMN_FAVORITE_ID = "favorite_id";
    public static final String COLUMN_FAVORITE_BOOK_ID = "book_id";
    public static final String COLUMN_FAVORITE_USER_ID = "book_user_id";
    // User Table Columns names
    public static final String COLUMN_HISTORY_ID = "history_id";
    public static final String COLUMN_HISTORY_BOOK_ID = "history_book_id";
    public static final String COLUMN_HISTORY_USER_ID = "history_user_id";
    public static final String COLUMN_HISTORY_TIME = "history_time";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";
    // User table name
    //region user
    private static final String TABLE_USER = "user";
    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_FULLNAME = "full_name";
    private static final String COLUMN_USER_IS_ADMIN = "is_admin";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    ///end region user
    private static final String TABLE_CATEGORY = "category";
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String TABLE_BOOK = "book";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String TABLE_FAVORITE = "favorite";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String TABLE_HISTORY = "history";
    // create table sql query
    private final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATEGORY_NAME + " TEXT,"
            + COLUMN_CATEGORY_DESCRIPTION + " TEXT" + ")";
    private final String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_BOOK + "("
            + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_BOOK_NAME + " TEXT,"
            + COLUMN_BOOK_CATEGORY_ID + " TEXT,"
            + COLUMN_BOOK_DESCRIPTION + " TEXT,"
            + COLUMN_BOOK_CONTENT + " TEXT" + ")";
    private final String CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_FAVORITE + "("
            + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FAVORITE_BOOK_ID + " TEXT,"
            + COLUMN_FAVORITE_USER_ID + " TEXT" + ")";
    private final String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
            + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_HISTORY_BOOK_ID + " TEXT,"
            + COLUMN_HISTORY_USER_ID + " TEXT," + COLUMN_HISTORY_TIME + " TEXT" + ")";
    private final String ADD_ADMIN_USER = "INSERT INTO  user VALUES(null,'admin','admin','admin','admin123','TRUE' )";
    private final String ADD_ADMIN_USER_NORMAL = "INSERT INTO  user VALUES(null,'admin2','admin2','admin2','admin123','FALSE' )";
    // drop table sql query
    private final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    // create table sql query
    private final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_FULLNAME + " TEXT,"
            + COLUMN_USER_IS_ADMIN + " TEXT" + ")";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
      //   context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // All your code here .....
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
        // Add default value for all tables
        db.execSQL(ADD_ADMIN_USER);
        db.execSQL(ADD_ADMIN_USER_NORMAL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_FULLNAME, user.getFullName());
        values.put(COLUMN_USER_IS_ADMIN, user.isAdmin());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_FULLNAME,
                COLUMN_USER_IS_ADMIN
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FULLNAME)));
                user.setAdmin(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_USER_IS_ADMIN))));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_FULLNAME, user.getFullName());
        values.put(COLUMN_USER_IS_ADMIN, user.isAdmin());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    @SuppressLint("Range")
    public User checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_FULLNAME,
                COLUMN_USER_IS_ADMIN
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

        if (cursorCount > 0) {
            cursor.moveToFirst();
            User user = new User();
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
            user.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FULLNAME)));
            user.setAdmin(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_USER_IS_ADMIN))));
            cursor.close();
            db.close();
            return user;
        }
        cursor.close();
        db.close();
        return null;
    }
    @SuppressLint("Range")
    public User getUser(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_FULLNAME,
                COLUMN_USER_IS_ADMIN
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" ;
        // selection arguments
        String[] selectionArgs = {email};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

        if (cursorCount > 0) {
            cursor.moveToFirst();
            User user = new User();
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
            user.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FULLNAME)));
            user.setAdmin(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(COLUMN_USER_IS_ADMIN))));
            cursor.close();
            db.close();
            return user;
        }
        cursor.close();
        db.close();
        return null;
    }

    public boolean checkCategoryExist(String name) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_NAME,
                COLUMN_CATEGORY_DESCRIPTION
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_CATEGORY_NAME + " = ?";
        // selection arguments
        String[] selectionArgs = {name};
        // query user table with conditions

        Cursor cursor = db.query(TABLE_CATEGORY, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

        if (cursorCount > 0) {
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public void addCategory(String name, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_CATEGORY_DESCRIPTION, category);
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public Cursor getCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        String str = ("SELECT rowid _id, * FROM " + TABLE_CATEGORY);
        return db.rawQuery(str, null);

    }

    public void updateCategory(int id, String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_CATEGORY_DESCRIPTION, description);
        db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @SuppressLint("Range")
    public List<Category> getAllCategory() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_NAME,
                COLUMN_CATEGORY_DESCRIPTION
        };
        // sorting orders
        String sortOrder =
                COLUMN_CATEGORY_NAME + " ASC";
        List<Category> categoryList = new ArrayList<Category>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                category.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_DESCRIPTION)));
                // Adding user record to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return categoryList;
    }

    public boolean checkBookExist(String name) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_BOOK_ID,

        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_BOOK_NAME + " = ?";
        // selection arguments
        String[] selectionArgs = {name};
        // query user table with conditions
        Cursor cursor = db.query(TABLE_BOOK, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public void addBook(String name, String category, String description, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_NAME, name);
        values.put(COLUMN_BOOK_CATEGORY_ID, category);
        values.put(COLUMN_BOOK_DESCRIPTION, description);
        values.put(COLUMN_BOOK_CONTENT, content);
        db.insert(TABLE_BOOK, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public Cursor getBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String str = ("SELECT rowid _id, * FROM " + TABLE_BOOK);
        return db.rawQuery(str, null);

    }

    @SuppressLint("Range")
    public Cursor getBooksFavor(String categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String str = ("SELECT book.rowid _id,  book.book_id, book.book_name, book.book_category_id, book.book_description, book.book_content,iif(favorite.book_user_id IS NULL,'Set Favorite','Remove Favorite') as is_favorite FROM " + TABLE_BOOK + " LEFT JOIN " + TABLE_FAVORITE + " on book.book_id = favorite.book_id WHERE " + COLUMN_BOOK_CATEGORY_ID + "=" + categoryId);
        return db.rawQuery(str, null);
    }

    @SuppressLint("Range")
    public Cursor getBooksBy(String categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String str = ("SELECT rowid _id, * FROM " + TABLE_BOOK) + " WHERE " + COLUMN_BOOK_CATEGORY_ID + " =" + categoryId;
        return db.rawQuery(str, null);

    }

    public void deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOK, COLUMN_BOOK_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateBook(String id, String name, String category, String description, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_NAME, name);
        values.put(COLUMN_BOOK_CATEGORY_ID, category);
        values.put(COLUMN_BOOK_DESCRIPTION, description);
        values.put(COLUMN_BOOK_CONTENT, content);
        db.update(TABLE_BOOK, values, COLUMN_BOOK_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    @SuppressLint("Range")
    public Book getBooksById(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_BOOK_ID,
                COLUMN_BOOK_CATEGORY_ID,
                COLUMN_BOOK_DESCRIPTION,
                COLUMN_BOOK_NAME,
                COLUMN_BOOK_CONTENT
        };
        String selection = COLUMN_BOOK_ID + " = ?";
        // selection arguments
        String[] selectionArgs = {bookId};
        Cursor cursor = db.query(TABLE_BOOK, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        Book book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Book();
                book.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_ID))));
                book.setName(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_NAME)));
                book.setCategoryId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_CATEGORY_ID))));
                book.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_DESCRIPTION)));
                book.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_CONTENT)));
            } while (cursor.moveToNext());
        }
        db.close();
        return book;
    }

    @SuppressLint("Range")
    public void getAllFavorite() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_FAVORITE_USER_ID,
                COLUMN_FAVORITE_BOOK_ID,


        };
        // sorting orders
        String sortOrder =
                COLUMN_FAVORITE_USER_ID + " ASC";
        List<Category> categoryList = new ArrayList<Category>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                System.out.println("id" + cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_USER_ID)));
                System.out.println("test" + cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_BOOK_ID)));
            //    System.out.println("té" + cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_BOOK_ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
    }

    public void addFavourite(String bookId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_USER_ID, userId);
        values.put(COLUMN_FAVORITE_BOOK_ID, bookId);
        System.out.println(bookId);
        db.insert(TABLE_FAVORITE, null, values);
        db.close();
    }

    public void deleteFavorite(String bookId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, COLUMN_FAVORITE_BOOK_ID + " = ? AND " + COLUMN_FAVORITE_USER_ID + " = ?", new String[]{bookId, userId});
        db.close();

    }

    public void addHistory(String bookId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HISTORY_USER_ID, userId);
        values.put(COLUMN_HISTORY_BOOK_ID, bookId);
        Date currentTime = Calendar.getInstance().getTime();
        values.put(COLUMN_HISTORY_TIME, currentTime.toString());
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }
    @SuppressLint("Range")
    public Cursor getHistories(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String str = ("SELECT book.rowid _id,  book.book_id, book.book_name, book.book_category_id, book.book_description,history.history_time FROM " + TABLE_BOOK + " INNER JOIN " + TABLE_HISTORY + " on history.history_book_id  = book.book_id WHERE " + COLUMN_HISTORY_USER_ID + "=" + userId);
        return db.rawQuery(str, null);
    }
    @SuppressLint("Range")
    public Cursor getBookbyUserFavor(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String str = ("SELECT book.rowid _id,  book.book_id, book.book_name, book.book_category_id, book.book_description FROM " + TABLE_BOOK + " LEFT JOIN " + TABLE_FAVORITE + " on book.book_id = favorite.book_id WHERE " + COLUMN_FAVORITE_USER_ID + "=" + userId);
        return db.rawQuery(str, null);
    }

}
