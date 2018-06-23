package com.twosri.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.modelmapper.ModelMapper;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class BootApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BootApplication.class, args);
		/*
		 * new SpringApplicationBuilder(BootApplication.class).listeners(new
		 * ApplicationListener<ContextClosedEvent>() {
		 * 
		 * @Override public void onApplicationEvent(ContextClosedEvent arg0) {
		 * System.out.println("application close event!!!"); try { ((Client)
		 * arg0.getApplicationContext().getBean(Client.class)).close(); } catch
		 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * System.out.println("application closed!!!");
		 * 
		 * }
		 * 
		 * }).run(args);
		 */
	}

	/*
	 * @Bean public Client client() { try { TransportClient client = new
	 * PreBuiltTransportClient(Settings.EMPTY) .addTransportAddress(new
	 * TransportAddress(InetAddress.getByName("localhost"), 9300)); return client; }
	 * catch (UnknownHostException e) { e.printStackTrace(); return null; } }
	 */
	// Unused

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}

}