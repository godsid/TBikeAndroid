package info.srihawong.tbike;

/**
 * Created by Banpot.S on 10/1/2557.
 */
public class TopicListItem {
    private String username;
    private String title;
    private String createDate;
    private Integer topicId;

    public TopicListItem(Integer topicId, String username, String title, String createDate) {
        this.username = username;
        this.title = title;
        this.createDate = createDate;
        this.topicId = topicId;

    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
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
