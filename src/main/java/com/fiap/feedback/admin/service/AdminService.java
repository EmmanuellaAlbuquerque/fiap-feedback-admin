package com.fiap.feedback.admin.service;

import com.fiap.feedback.admin.model.Admin;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@ApplicationScoped
public class AdminService {

    @Inject
    DynamoDbClient dynamoDbClient;

    @ConfigProperty(name = "dynamodb.admin.table.name")
    String tableName;

    private DynamoDbTable<Admin> adminTable;

    @PostConstruct
    public void init() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        
        this.adminTable = enhancedClient.table(tableName, TableSchema.fromBean(Admin.class));
    }

    public void saveAdmin(Admin admin) {
        adminTable.putItem(admin);
    }

    public boolean existsByEmail(String email) {
        Key key = Key.builder()
                .partitionValue(email)
                .build();
        
        return adminTable.getItem(key) != null;
    }
}
