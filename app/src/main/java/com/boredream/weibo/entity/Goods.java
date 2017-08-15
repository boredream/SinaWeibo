package com.boredream.weibo.entity;

import com.boredream.bdcodehelper.lean.entity.LeanCloudObject;
import com.boredream.bdcodehelper.utils.CollectionUtils;

import java.util.ArrayList;

/**
 * <pre>
 *     author : lichunyang
 *     time   : 2017/08/15
 *     desc   :
 * </pre>
 */
public class Goods extends LeanCloudObject {

    private User user;
    // FIXME: 2017/8/15 合并
    private String image;
    private ArrayList<String> images;
    private String name;
    private String link;

    public User getUser() {
        if(user == null) {
            // FIXME: 2017/8/15 
            user = new User();
            user.setUsername("110");
            user.setNickname("官方");
            user.setAvatarUrl("http://img4.duitang.com/uploads/item/201408/19/20140819172216_zPzZT.jpeg");
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getImages() {
        if(CollectionUtils.isEmpty(images)) {
            images = new ArrayList<>();
            images.add(getImage());
        }
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
