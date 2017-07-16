package downloaddata.com.feeds.model;

import java.io.Serializable;

/**
 * News Item model
 */
public class NewsItem implements Serializable {
    private String mHeadline;
    private String mLink;
    private String mAuthor;
    private String mDate;
    private String mItemId;
    private String mCaption;
    private String mThumbImage;
    private String mPhoto;

    public String getmStory() {
        return mStory;
    }

    public void setmStory(String mStory) {
        this.mStory = mStory;
    }

    private String mStory;
    //Getters and Setters

    public String getmThumbImage() {
        return mThumbImage;
    }

    public void setmThumbImage(String mThumbImage) {
        this.mThumbImage = mThumbImage;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmCaption() {
        return mCaption;
    }

    public void setmCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getmItemId() {
        return mItemId;
    }

    public void setmItemId(String mItemId) {
        this.mItemId = mItemId;
    }


    public String getmHeadline() {
        return mHeadline;
    }

    public void setmHeadline(String mHeadline) {
        this.mHeadline = mHeadline;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public NewsItem() {
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "mHeadline='" + mHeadline + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mItemId='" + mItemId + '\'' +
                ", mCaption='" + mCaption + '\'' +
                ", mThumbImage='" + mThumbImage + '\'' +
                ", mPhoto='" + mPhoto + '\'' +
                ", mStory='" + mStory + '\'' +
                '}';
    }
}
