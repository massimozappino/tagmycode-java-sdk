package support;


import com.tagmycode.sdk.DbService;

public class MemDbService extends DbService {
    public MemDbService(String dbName) {
        super("mem:" + dbName);
    }

    public MemDbService() {
        this("test");
    }
}
