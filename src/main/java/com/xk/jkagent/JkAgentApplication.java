package com.xk.jkagent;

import com.xk.jkagent.tool.WeatherServiceTool_Server;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = PgVectorStoreAutoConfiguration.class)
public class JkAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(JkAgentApplication.class, args);
    }

}
