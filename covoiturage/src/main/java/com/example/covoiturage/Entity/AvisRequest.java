package com.example.covoiturage.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AvisRequest {
    private int note;
    private String descp;
}
