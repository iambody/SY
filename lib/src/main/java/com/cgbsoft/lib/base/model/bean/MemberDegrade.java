package com.cgbsoft.lib.base.model.bean;

import java.util.List;

/**
 * @author chenlong
 */
public class MemberDegrade {
    private String content;
    private List<String> memberProfit;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMemberProfit() {
        return memberProfit;
    }

    public void setMemberProfit(List<String> memberProfit) {
        this.memberProfit = memberProfit;
    }
}
