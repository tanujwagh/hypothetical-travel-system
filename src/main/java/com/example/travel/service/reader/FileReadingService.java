package com.example.travel.service.reader;

import java.io.IOException;

public interface FileReadingService<T> {
    T read(String filePath) throws IOException;
}
