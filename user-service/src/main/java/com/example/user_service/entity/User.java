// 此類別代表用戶實體，對應到資料庫中的用戶表。
package com.example.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // 用戶 ID

    @Column(unique = true)
    private String username; // 用戶名

    private String password; // 用戶密碼

    private Double balance; // 用戶餘額
}