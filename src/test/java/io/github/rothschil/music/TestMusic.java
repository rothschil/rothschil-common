package io.github.rothschil.music;

import io.github.rothschil.AbstractBaseSimpleCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class TestMusic extends AbstractBaseSimpleCase {

    @Test
    public void md5() {
        try {
            String path = "E:\\Music\\Alan Silvestri - Forrest Gump Suite.flac";
            String md5 = DigestUtils.md5Hex(new FileInputStream(path));
            log.error("md5: {}", md5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
