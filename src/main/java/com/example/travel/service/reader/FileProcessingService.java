package com.example.travel.service.reader;

import java.io.File;
import java.io.IOException;

public interface FileProcessingService<T, Q> {
    Q process(T data);
}
