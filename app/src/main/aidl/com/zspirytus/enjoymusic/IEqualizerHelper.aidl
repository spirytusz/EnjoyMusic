// IEqualizerHelper.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.entity.EqualizerMetaData;

interface IEqualizerHelper {
    EqualizerMetaData addEqualizerSupport();
    void setBandLevel(int band, int level);
}
