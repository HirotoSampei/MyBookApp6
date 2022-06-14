package jp.te4a.spring.boot.myapp13a.controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import javax.activation.DataSource;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import jp.te4a.spring.boot.myapp13a.BookApplication;
import jp.te4a.spring.boot.myapp13a.form.BookForm;
import jp.te4a.spring.boot.myapp13a.form.UserForm;

// SpringBootの起動クラスを指定
@ContextConfiguration(classes = BookApplication.class)
// クラス内の全メソッドにおいて、実行前にDIコンテナの中身を破棄する
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
// テストランナー：各テストケース（テストメソッド）を制御する：DIする場合は必須？
@ExtendWith(SpringExtension.class)
// MockおよびWebApplicationContextの自動ロード：サーブレット環境を自動作成する
@AutoConfigureMockMvc
@Transactional
//テスト時に起動するSprinbBootプロジェクトの使用ポート番号を設定する場合：ランダム
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// クラス単位でインスタンス生成（通常はメソッド単位）：@BeforeAllを使うため
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//わからん
@WithUserDetails(value="testuser", userDetailsServiceBeanName="loginUserDetailsService")
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;  // SpringMVCモックオブジェクト
    @Autowired
    WebApplicationContext wac;  // Webアプリへの設定提供
    @Autowired
    private javax.sql.DataSource dataSource;
    @BeforeAll
    public void テスト前処理() {
        // Thymeleafを使用していることがテスト時に認識されない様子
        // 循環ビューが発生しないことを明示するためにViewResolverを使用
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates");
        viewResolver.setSuffix(".html");
      mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    public static final Operation INSERT_USER_DATA1 = Operations.insertInto("users").columns("id", "username", "password").values(1, "ユーザー１", "password1").build();
	public static final Operation INSERT_USER_DATA2 = Operations.insertInto("users").columns("id", "username", "password").values(2, "ユーザー２", "password2").build();

    @SuppressWarnings("unchecked")    
    @Test
    public void 書籍追加入力ページ() throws Exception {

        UserForm userForm = new UserForm();

        MvcResult result = mockMvc.perform(post("/users/create")
        	.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("username", "ユーザー")
			.flashAttr("form", userForm)
			.sessionAttr("form", userForm))

            .andExpect(status().is2xxSuccessful())            
            .andExpect(view().name("users/add"))            
            .andReturn();
    }

}