package com.anbang.qipai.ruianmajiang.web.vo;

public class FrameInfoVo {

    private String gameId;
    private int panno;
    private int frameNo;

    public String getGameId() {
        return gameId;
    }

    public FrameInfoVo setGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public int getPanno() {
        return panno;
    }

    public FrameInfoVo setPanno(int panno) {
        this.panno = panno;
        return this;
    }

    public int getFrameNo() {
        return frameNo;
    }

    public FrameInfoVo setFrameNo(int frameNo) {
        this.frameNo = frameNo;
        return this;
    }
}
