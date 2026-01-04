package io.github.rothschil.domain.media.service;

import io.github.rothschil.domain.media.model.param.TranscodeParam;

public interface ITranscodeService {

    /**
     * 转码
     * @param param
     * @return
     */
    void transcode(TranscodeParam param);

    /**
     * 停止转码
     * @param stream
     */
    void stopTranscode(String stream);
}
