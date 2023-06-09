package com.astamatii.endava.webchat.repositories;

import com.astamatii.endava.webchat.models.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {
}
