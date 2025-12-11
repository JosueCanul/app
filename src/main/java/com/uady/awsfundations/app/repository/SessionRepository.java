package com.uady.awsfundations.app.repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.uady.awsfundations.app.model.Session;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class SessionRepository {

    private final DynamoDBMapper dynamoDBMapper;

 
    public void saveSession(Integer alumnoId) {
        Session session = new Session();
        session.setAlumnoId(alumnoId);
        dynamoDBMapper.save(session);
    }

    public void save(Session session) {
        dynamoDBMapper.save(session);
    }


    public Session getSessionBySessionString(String sessionString) {

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":ss", new AttributeValue().withS(sessionString));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("sessionString = :ss")
                .withExpressionAttributeValues(eav)
                .withLimit(1); // solo necesitamos una

        List<Session> result = dynamoDBMapper.scan(Session.class, scanExpression);

        return result.isEmpty() ? null : result.get(0);
    }


    public List<Session> getSessionsByAlumnoId(Integer alumnoId) {

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":al", new AttributeValue().withN(alumnoId.toString()));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("alumnoId = :al")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(Session.class, scanExpression);
    }


    public void closeSessionsByAlumnoId(Integer alumnoId) {

        List<Session> sessions = getSessionsByAlumnoId(alumnoId);

        sessions.forEach(s -> {
            if (Boolean.TRUE.equals(s.getActive())) {
                s.setActive(false);
                dynamoDBMapper.save(s);
            }
        });
    }


    public void closeSessionBySessionString(String sessionString) {
        Session s = getSessionBySessionString(sessionString);

        if (s != null && Boolean.TRUE.equals(s.getActive())) {
            s.setActive(false);
            dynamoDBMapper.save(s);
        }
    }
}
