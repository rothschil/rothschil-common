package io.github.rothschil.domain.media.service;

import io.github.rothschil.domain.media.model.param.TestVideoParam;
import io.github.rothschil.domain.media.model.result.StreamUrlResult;

public interface ITestVideoService {
    StreamUrlResult createTestVideo(TestVideoParam param);

    Boolean stopTestVideo(String app,String stream);
}
