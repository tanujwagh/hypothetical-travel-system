package com.example.travel.service;

import java.io.IOException;

public interface FileReadingService<T> {
    T read(String filePath) throws IOException;
}
