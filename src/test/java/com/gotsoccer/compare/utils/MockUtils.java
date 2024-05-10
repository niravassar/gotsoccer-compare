package com.gotsoccer.compare.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MockUtils {

    public static MockMultipartFile createMockMultipartFile(String filename) throws IOException {
        File file = FileUtils.getFile(Objects.requireNonNull(MockUtils.class.getClassLoader().getResource(filename)).getPath());
        return new MockMultipartFile("files", file.getName(), String.valueOf(MediaType.MULTIPART_FORM_DATA),
                FileUtils.readFileToByteArray(file));
    }
}
