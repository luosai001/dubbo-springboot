# dubbo-springboot
# springboot 集成dubbo项目使用
dubbo的springboot项目使用官方dubbo-spring-boot-starter 即可，前两天刚发布0.2.0版本，支持rest风格。
关于dubbo如何使用请查看官方文档：
[Dubbo用户手册（中文）](http://dubbo.apache.org/books/dubbo-user-book/)
[Dubbo开发手册（中文）](http://dubbo.apache.org/books/dubbo-dev-book/)
[Dubbo管理手册（中文）](http://dubbo.apache.org/books/dubbo-admin-book/)
下面通过一个demo来演示如何利用dubbo-spring-boot-starter 0.2.0版本暴露两个服务，一个服务一dubbo协议暴露，另一个服务以rest服务暴露。
## API
在api项目中新建两个service 接口，和一个User 类，如下图；
HelloService 代码：

```

public interface HelloService {
    String hello(String hello);
}
```

UserService 代码
```
@Path("/user")
public interface UserService {
    @Path("/register/{id:\\d+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    User getUser(@PathParam("id") Long id);
}

```

 User 类

```
public class User {
    private Long id;
    private String name;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```


## provider 服务提供者
application.yml  中只能定义单个协议，要定义多个协议需要通过api 来实现

```
pring.application.name=dubbo-provider-demo
server.port=9091
#management.server.port = 9091

demo.service.version=1.0
dubbo.application.id=dubbo-provider-demo
dubbo.application.name=dubbo-provider-demo
dubbo.scan.basePackages  = com.dubbo.springboot.provider

dubbo.registry.address=multicast://224.5.6.7:1234
dubbo.registry.id=registry

## Legacy QOS Config
dubbo.qos.port = 22222

# Dubbo Endpoint (default status is disable)
endpoints.dubbo.enabled = true

# Dubbo Health
## StatusChecker Name defaults (default : "memory", "load" )
management.health.dubbo.status.defaults = memory

# Enables Dubbo All Endpoints
#management.endpoint.dubbo.enabled = true
#management.endpoint.dubbo-shutdown.enabled = true
#management.endpoint.dubbo-configs.enabled = true
#management.endpoint.dubbo-services.enabled = true
#management.endpoint.dubbo-references.enabled = true
#management.endpoint.dubbo-properties.enabled = true
#
## Exposes all web endpoints
#management.endpoints.web.exposure.include = *
```

Config 注入多个协议

```
@Configuration
public class Config {

    @Bean("dubbo")
    public ProtocolConfig dubboProtocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(12345);
        protocolConfig.setId("dubbo");
        protocolConfig.setServer(null);
        return protocolConfig;
    }

    @Bean("rest")
    public ProtocolConfig restProtocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("rest");
        protocolConfig.setPort(9002);
        protocolConfig.setId("rest");
        protocolConfig.setServer("tomcat");
        return protocolConfig;
    }
```

HelloServiceImpl  以dubbo协议暴露服务

```
@Service(
        version = "${demo.service.version}",
        protocol ="dubbo"
)
public class HelloServiceImpl implements HelloService {
    @Override

    public String hello(String hello) {
        System.out.println(" hello - ");
        return "hello: "+hello;
    }
}
```

UserServiceImpl 以rest 协议暴露服务

```
@Service(
        version = "${demo.service.version}",
        protocol ="rest"
)
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(Long id) {
        return new User(id,"zs"+id);
    }
}
```
ProviderApplication 应用启动类

```
@SpringBootApplication()
public class ProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}
}

```

## consumer 服务消费者

application.yml

```
spring.application.name=dubbo-consumer-demo
server.port=8080
#management.server.port = 8081

demo.service.version=1.0
dubbo.application.id=dubbo-consumer-demo
dubbo.application.name=dubbo-consumer-demo

dubbo.registry.address=multicast://224.5.6.7:1234
dubbo.registry.id=registry
#dubbo.registry.protocol=rest
## Legacy QOS Config
dubbo.qos.port = 22223

# Dubbo Endpoint (default status is disable)
endpoints.dubbo.enabled = true

# Dubbo Health
## StatusChecker Name defaults (default : "memory", "load" )
management.health.dubbo.status.defaults = memory

# Enables Dubbo All Endpoints
#management.endpoint.dubbo.enabled = true
#management.endpoint.dubbo-shutdown.enabled = true
#management.endpoint.dubbo-configs.enabled = true
#management.endpoint.dubbo-services.enabled = true
#management.endpoint.dubbo-references.enabled = true
#management.endpoint.dubbo-properties.enabled = true
#
## Exposes all web endpoints
#management.endpoints.web.exposure.include = *
```
Controller 控制器 调用服务

```
@RestController
public class Controller {
    @Reference(
            version = "${demo.service.version}"
    )
    HelloService helloService;
    @Reference(
            version = "${demo.service.version}"
    )
    UserService userService;

    @RequestMapping("/")
    public String hello( String world){
        return helloService.hello(world);
    }

    @RequestMapping("/{id:\\d+}")
    public User getUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }
}

```
ConsumerApplication  应用启动类

```
@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
```
三个项目公共的pom.xml

```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>com.alibaba.boot</groupId>
			<artifactId>dubbo-spring-boot-starter</artifactId>
			<version>0.2.0</version>
		</dependency>

		<dependency>
			<groupId>org.apache.curator</groupId>
			<artifactId>curator-framework</artifactId>
			<version>2.12.0</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.jboss.resteasy/resteasy-jaxrs -->
		<dependency>
			<groupId>org.jboss.resteasy</groupId>
			<artifactId>resteasy-jaxrs</artifactId>
			<version>3.5.1.Final</version>
		</dependency>
		<dependency>
			<groupId>javax.json.bind</groupId>
			<artifactId>javax.json.bind-api</artifactId>
			<version>1.0</version>
		</dependency>
		<dependency>
			<groupId>org.eclipse</groupId>
			<artifactId>yasson</artifactId>
			<version>1.0.1</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish</groupId>
			<artifactId>javax.json</artifactId>
			<version>1.1.2</version>
		</dependency>
		<dependency>
			<groupId>org.jboss.resteasy</groupId>
			<artifactId>resteasy-jackson-provider</artifactId>
			<version>2.3.5.Final</version>
		</dependency>
		<dependency>
			<groupId>org.jboss.resteasy</groupId>
			<artifactId>resteasy-client</artifactId>
			<version>3.5.1.Final</version>
		</dependency>
```
启动provider 
启动consumer
访问consumer http://localhost:8080/11
访问consumer  http://localhost:8080/?world=HelloWorld
访问provider rest接口   http://localhost:9092/user/register/12
