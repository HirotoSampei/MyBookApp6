package jp.te4a.spring.boot.myapp13a.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.te4a.spring.boot.myapp13a.bean.UserBean;

public interface UserRepository extends JpaRepository<UserBean, String>{
}
