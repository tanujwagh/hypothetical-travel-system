package com.example.travel.service;

import java.io.FileNotFoundException;

public interface FileWriterService<T> {
    void write(String fileName, T t) throws FileNotFoundException;
}
