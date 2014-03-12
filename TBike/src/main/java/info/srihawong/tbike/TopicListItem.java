package info.srihawong.tbike;

/**
 * Created by Banpot.S on 10/1/2557.
 */
public class TopicListItem {
    private String username;
    private String title;
    private String createDate;
    private int topicId,forumId,userId;
    private int sticky;

    public TopicListItem(int forumId, int topicId, int userId, String username, String title, String createDate,int sticky) {
        this.username = username;
        this.title = title;
        this.createDate = createDate;
        this.topicId = topicId;
        this.forumId = forumId;
        this.userId = userId;
        this.sticky = sticky;
    }


    public int getTopicId() {
        return topicId;
    }

    public int getForumId() {
        return forumId;
    }

    public int getUserId() {
        return userId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getSticky() {
        return sticky;
    }

    public void setSticky(int sticky) {
        this.sticky = sticky;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {

        return title.replace("&amp;","&")
                .replace("&gt;",">")
                .replace("&lt;","<")
                .replace("&quot;","\"");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
