package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import com.dml.majiang.pan.frame.PanActionFrame;

public class PlaybackFrameDbo {

    private String gameId;
    private int frameNo;
    private int panNo;
    private PanActionFrame panActionFrame;

    public PlaybackFrameDbo() {
    }

    public PlaybackFrameDbo(String gameId, int frameNo, int panNo, PanActionFrame panActionFrame) {
        this.gameId = gameId;
        this.frameNo = frameNo;
        this.panNo = panNo;
        this.panActionFrame = panActionFrame;
    }

    public String getGameId() {
        return gameId;
    }

    public PlaybackFrameDbo setGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public int getFrameNo() {
        return frameNo;
    }

    public void setFrameNo(int frameNo) {
        this.frameNo = frameNo;
    }

    public int getPanNo() {
        return panNo;
    }

    public void setPanNo(int panNo) {
        this.panNo = panNo;
    }

    public PanActionFrame getPanActionFrame() {
        return panActionFrame;
    }

    public void setPanActionFrame(PanActionFrame panActionFrame) {
        this.panActionFrame = panActionFrame;
    }

}
