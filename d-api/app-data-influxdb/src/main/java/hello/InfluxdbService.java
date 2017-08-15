package hello;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.Map;

/**
 * influxdb操作类 <br/>
 */
public class InfluxdbService {

    private String database;
    private String policyName;
    private InfluxDB influxDB;

    public InfluxdbService(String database, String policyName, InfluxDB influxDB) {
        this.database = database;
        this.policyName = policyName;
        this.influxDB = influxDB;
    }

    /**
     * 创建数据库
     */
    public void createDatabase() {
        influxDB.createDatabase(database);
    }

    /**
     * 创建保存策略 <br/>
     * CREATE RETENTION POLICY "default" ON "influxdb-database" DURATION 30d REPLICATION 1 DEFAULT
     *
     * @param duration       存放时间 (30d)
     * @param replicationNum 备份数量
     */
    public void createRetentionPolicy(String duration, Integer replicationNum) {

        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                policyName, database, duration, replicationNum);

        this.query(command);
    }

    /**
     * 插入数据
     *
     * @param measurement a Point in a fluent manner
     * @param tagsToAdd   the Map of tags to add
     * @param fields      the fields to add
     */
    public void insert(String measurement, Map<String, String> tagsToAdd, Map<String, Object> fields) {

        Point.Builder builder = Point.measurement(measurement).tag(tagsToAdd);
        if (fields != null && !fields.isEmpty()) {
            builder.fields(fields);
        }
        influxDB.write(database, policyName, builder.build());
    }

    /**
     * 查询数据
     *
     * @param command
     * @return QueryResult
     */
    public QueryResult query(String command) {
        return influxDB.query(new Query(command, database));
    }

    public InfluxDB getInfluxDB() {
        return influxDB;
    }

    public String getDatabase() {
        return database;
    }

    public String getPolicyName() {
        return policyName;
    }

}