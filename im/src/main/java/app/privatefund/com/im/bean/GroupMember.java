package app.privatefund.com.im.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenlong
 *
 * 群成员列表
 */
public class GroupMember implements Serializable {

    private String orgName;

    private List<GroupMemberPerson> members;

    public static class GroupMemberPerson {

        private String userId;

        private String userName;

        private String imageUrl;

        private String lastLoginTime;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }
    }

    public List<GroupMemberPerson> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberPerson> members) {
        this.members = members;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
