package io.github.rothschil.domain.media.service.impl;

import io.github.rothschil.common.config.media.MediaServerConfig;
import io.github.rothschil.domain.media.bo.TestVideo;
import io.github.rothschil.domain.media.model.param.TestVideoParam;
import io.github.rothschil.domain.media.model.result.StreamUrlResult;
import io.github.rothschil.domain.media.service.ITestVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TestVideoService implements ITestVideoService {
    @Autowired
    private MediaServerConfig config;
    /**
     * 测试视频
     */
    private static final Map<String, TestVideo> TEST_VIDEO_MAP = new HashMap<>();

    @Override
    public StreamUrlResult createTestVideo(TestVideoParam param) {
        TestVideo testVideo = new TestVideo(param);
        testVideo.initVideo();
        testVideo.startTestVideo();
        TEST_VIDEO_MAP.put(param.getApp() + param.getStream(), testVideo);
        return new StreamUrlResult(config, param);
    }

    @Override
    public Boolean stopTestVideo(String app, String stream) {
        String key = app + stream;
        TestVideo testVideo = TEST_VIDEO_MAP.get(key);
        if (testVideo != null) {
            testVideo.closeVideo();
            TEST_VIDEO_MAP.remove(key);
            return true;
        }
        return false;
    }
}
