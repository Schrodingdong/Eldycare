package com.ensias.eldycare.userservice.repository;

import com.ensias.eldycare.userservice.model.UserModel;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends Neo4jRepository<UserModel, String> {
    @Query("MATCH (u:User {email: $userEmail}) MATCH (uc:User {email: $urgentContactEmail}) CREATE (u)-[r:HAS_URGENT_CONTACT]->(uc) RETURN r")
    void addUrgentContact(String userEmail, String urgentContactEmail);

    @Query("MATCH (u:User {email: $userEmail})-[r:HAS_URGENT_CONTACT]->(uc:User {email: $urgentContactEmail}) DELETE r")
    void deleteUrgentContact(String userEmail, String urgentContactEmail);
}
