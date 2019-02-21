package teguhsusanto.com.crudbook;

public class Config {
    public static final String URL = "http://test.incenplus.com:5000/users/";
    public static final String URL_LOGIN =  "http://test.incenplus.com:5000/users/login";
    public static final String URL_LOGOUT = "http://test.incenplus.com:5000/users/logout?token=";
    public static final String URL_PROFILE = "http://test.incenplus.com:5000/users/me?token=";

    public static final String TAG_CODE = "status_code";
    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "description";

    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    public final static String TAG_TOKEN ="token";

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    public static final String ERR_NO_INTERNET_CONNECTION = "No Internet Connection";
    public static final String ERR_CANNOT_EMPTY = "Kolom tidak boleh kosong";

    public static final String STATUS_LOGGING_IN = "Logging in ...";

    public static String URL_BOOK_SELECT     = "http://test.incenplus.com:5000/books?token=";
    public static String URL_BOOK_INSERT     = "http://test.incenplus.com:5000/books/insert?token=";
    public static String URL_BOOK_UPDATE     = "http://test.incenplus.com:5000/books/edit?token=";
    public static String URL_BOOK_DELETE     = "http://test.incenplus.com:5000/books/delete?token=";
    public static String URL_BOOK_DETAIL     = "http://test.incenplus.com:5000/books/detail?token=";

    public static final String TAG_NAME     = "name";
    public static final String TAG_DESCRIPTION   = "description";
    public static final String TAG_SUCCESS = "success";
}
