package cn.edu.bit.patentbackend.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public interface PatentService {
    public ArrayList<Map<String, Object>> search(String query) throws IOException;
}
