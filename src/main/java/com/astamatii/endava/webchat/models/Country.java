package com.astamatii.endava.webchat.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Icon icon;
    private String countryName;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private Set<City> cities = new HashSet<>();

}
