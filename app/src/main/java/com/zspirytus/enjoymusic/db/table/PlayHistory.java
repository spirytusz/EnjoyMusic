package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class PlayHistory {

    @Id
    private Long playHistoryId;

    @Generated(hash = 1714027898)
    public PlayHistory(Long playHistoryId) {
        this.playHistoryId = playHistoryId;
    }

    @Generated(hash = 2145518983)
    public PlayHistory() {
    }

    public Long getPlayHistoryId() {
        return this.playHistoryId;
    }

    public void setPlayHistoryId(Long playHistoryId) {
        this.playHistoryId = playHistoryId;
    }
}
