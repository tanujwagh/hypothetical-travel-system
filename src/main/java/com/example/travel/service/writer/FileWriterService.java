package com.example.travel.service.writer;

import java.io.File;
import java.io.FileNotFoundException;

public interface FileWriterService<T> {
    File write(String fileName, T t) throws FileNotFoundException;
}
