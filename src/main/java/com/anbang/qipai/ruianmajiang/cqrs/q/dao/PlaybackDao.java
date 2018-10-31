package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PlaybackFrameDbo;

public interface PlaybackDao {

    void save(PlaybackFrameDbo playbackFrameDbo);

    PlaybackFrameDbo find(String gameId, int panno, int frameNo);

    int lastFrameNo(String gameId, int panno);

}
