package com.epam.esm.util.actuator;

import com.epam.esm.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * custom actuator endpoint for
 * entity statistics
 *
 * @author bakhridinova
 */

@Component
@Endpoint(id = "stats")
@RequiredArgsConstructor
public class StatsEndpoint {
    private final List<BaseRepository<?>> repositories;

    @Bean
    @ReadOperation
    @Transactional(readOnly = true)
    public Map<String, Long> stats() {
        Map<String, Long> result = new TreeMap<>();

        repositories.forEach(repository -> {
            String className = repository.getClass().getSimpleName().toLowerCase();
            result.put(className.substring(0, className.indexOf("repository")) + "s",
                    repository.findTotalNumber());
        });
        return result;
    }
}