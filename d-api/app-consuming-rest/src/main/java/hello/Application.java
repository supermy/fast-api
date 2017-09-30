package hello;

import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootApplication
public class Application {

	@Resource
	private Environment env;


	@Autowired
	BaiduPOIService bps;

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	/**
	 * if (db != null) db.close();
	 * options.dispose();
	 *
	 * @return
	 */
	@Bean
	public RocksDB RocksDbConfig() {
		String filename = env.getRequiredProperty("db.file");


		log.debug("rocks db path:", filename);

		RocksDB.loadLibrary();
		// the Options class contains a set of configurable DB options
		// that determines the behavior of a database.
		final Options options = new Options();

		final Statistics stats = new Statistics();


		try {
			options.setCreateIfMissing(true)
					.setStatistics(stats)
					.setWriteBufferSize(8 * SizeUnit.KB)
					.setMaxWriteBufferNumber(3)
					.setMaxBackgroundCompactions(10)
					.setCompressionType(CompressionType.SNAPPY_COMPRESSION)
					.setCompactionStyle(CompactionStyle.UNIVERSAL);
		} catch (final IllegalArgumentException e) {
			assert (false);
		}

		final Filter bloomFilter = new BloomFilter(10);
		final ReadOptions readOptions = new ReadOptions()
				.setFillCache(false);
		final RateLimiter rateLimiter = new RateLimiter(10000000, 10000, 10);

		options.setMemTableConfig(
				new HashSkipListMemTableConfig()
						.setHeight(4)
						.setBranchingFactor(4)
						.setBucketCount(2000000));

		options.setMemTableConfig(
				new HashLinkedListMemTableConfig()
						.setBucketCount(100000));
		options.setMemTableConfig(
				new VectorMemTableConfig().setReservedSize(10000));

		options.setMemTableConfig(new SkipListMemTableConfig());

		options.setTableFormatConfig(new PlainTableConfig());
		// Plain-Table requires mmap read
		options.setAllowMmapReads(true);

		options.setRateLimiter(rateLimiter);

		final BlockBasedTableConfig table_options = new BlockBasedTableConfig();
		table_options.setBlockCacheSize(64 * SizeUnit.KB)
				.setFilter(bloomFilter)
				.setCacheNumShardBits(6)
				.setBlockSizeDeviation(5)
				.setBlockRestartInterval(10)
				.setCacheIndexAndFilterBlocks(true)
				.setHashIndexAllowCollision(false)
				.setBlockCacheCompressedSize(64 * SizeUnit.KB)
				.setBlockCacheCompressedNumShardBits(10);

		options.setTableFormatConfig(table_options);
		//options.setCompressionType(CompressionType.SNAPPY_COMPRESSION).setCreateIfMissing(true);

		RocksDB db = null;
		try {
			// a factory method that returns a RocksDB instance
			//String filename = "/Users/moyong/project/env-myopensource/1-spring/12-spring/rocksdb-service/src/main/resources/data";
			//db = factory.open(new File("example"), options);

			db = RocksDB.open(options, filename);
			// do something
		} catch (RocksDBException e) {
			e.printStackTrace();
		}
		return db;
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {

//			Quote quote = restTemplate.getForObject(
//					"https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//			log.info(quote.toString());

		};
	}




}