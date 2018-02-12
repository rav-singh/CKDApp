package CKD.Android;


public class CommentClass
{
    private String userName;
    private String userUID;
    private String comment;
    private String date;
    private String threadUID;
    private String commentUID;


    public CommentClass()
    {

    }


    public CommentClass(String userName, String userUID, String comment, String date, String threadUID,String commentUID) {
        this.userName = userName;
        this.userUID = userUID;
        this.comment = comment;
        this.threadUID = threadUID;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThreadUID() {
        return threadUID;
    }

    public void setThreadUID(String threadUID) {
        this.threadUID = threadUID;
    }

    public String getCommentUID() {
        return commentUID;
    }

    public void setCommentUID(String commentUID) {
        this.commentUID = commentUID;
    }
}
