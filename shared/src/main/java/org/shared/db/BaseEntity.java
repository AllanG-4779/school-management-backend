package org.shared.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseEntity {
    @Id
    private Long id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String created_by;
    private Boolean softDelete;
}
