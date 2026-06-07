package KTB4_gourmet_Week4.Assignment.store;

import KTB4_gourmet_Week4.Assignment.entity.Comment;
import KTB4_gourmet_Week4.Assignment.entity.Post;
import KTB4_gourmet_Week4.Assignment.entity.User;

import java.util.LinkedHashMap;
import java.util.Map;

public class MemoryStore {

    public static final Map<Long, User> users = new LinkedHashMap<>();
    public static final Map<Long, Post> posts = new LinkedHashMap<>();
    public static final Map<Long, Comment> comments = new LinkedHashMap<>();

    public static Long userId = 1L;
    public static Long postId = 1L;
    public static Long commentId = 1L;
}