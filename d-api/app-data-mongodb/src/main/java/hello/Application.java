package hello;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private CustomerRepository repository;



	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private MongoTemplate mongoTemplate2;


	@Autowired
	RestTemplate restTemplate;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		// save a couple of customers
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Customer customer : repository.findAll()) {
			System.out.println(customer);
		}
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByFirstName("Alice"));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Customer customer : repository.findByLastName("Smith")) {
			System.out.println(customer);
		}


		/////多模板引擎测试

		ArrayList<DBObject> list = new ArrayList<DBObject>();
		BasicDBObject or = new BasicDBObject();
		or.put("info", new BasicDBObject("hello", 1).append("word", 2));
		list.add(or);
		mongoTemplate.getCollection("tC").insert(list);
		mongoTemplate2.getCollection("tC").insert(list);


		///Spring RestTemplate, 使用java访问URL更加优雅，更加方便。

		String url = "http://127.0.0.1:8080/customer";
//		Gson json = restTemplate.getForEntity(url, Gson.class).getBody();
		BasicDBObject json = restTemplate.getForEntity(url, BasicDBObject.class).getBody();

		System.out.println(json);
	}

}
