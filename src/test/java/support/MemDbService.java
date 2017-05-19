package support;


import com.tagmycode.sdk.DbService;

import java.util.UUID;

public class MemDbService extends DbService {
    public MemDbService(String dbName) {
        super("mem:" + dbName);
    }

    public MemDbService() {
        this(UUID.randomUUID().toString());
    }
}
