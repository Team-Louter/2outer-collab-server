package com.louter.collab.domain.page.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCollaboratorId implements Serializable {
    private Long page;
    private Long user;
}
