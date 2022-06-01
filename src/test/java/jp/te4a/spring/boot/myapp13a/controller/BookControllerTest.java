package jp.te4a.spring.boot.myapp13a.controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import javax.activation.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

// SpringBootの起動クラスを指定
@ContextConfiguration(classes = BookApplication.class)
// クラス内の全メソッドにおいて、実行前にDIコンテナの中身を破棄する
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
// テストランナー：各テストケース（テストメソッド）を制御する：DIする場合は必須？
@ExtendWith(SpringExtension.class)
// MockおよびWebApplicationContextの自動ロード：サーブレット環境を自動作成する
@AutoConfigureMockMvc
//テスト時に起動するSprinbBootプロジェクトの使用ポート番号を設定する場合：ランダム
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// クラス単位でインスタンス生成（通常はメソッド単位）：@BeforeAllを使うため
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTest {
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
    public static final Operation INSERT_BOOK_DATA1 = Operations.insertInto("books").columns("id", "title", "writer", "publisher", "price").values(1, "タイトル１", "著者１", "出版社１", 100).build();
	public static final Operation INSERT_BOOK_DATA2 = Operations.insertInto("books").columns("id", "title", "writer", "publisher", "price").values(2, "タイトル２", "著者２", "出版社２", 200).build();
	public static final Operation INSERT_BOOK_DATA3 = Operations.insertInto("books").columns("id", "title", "writer", "publisher", "price").values(3, "タイトル３", "著者３", "出版社３", 300).build();
    @Test
    public void 書籍追加一覧ページ表示 () throws Exception {

        MvcResult result = mockMvc.perform(  get("/books")  )
        		.andExpect(  status().is2xxSuccessful()  )
        		.andExpect(  view().name("books/list")  )
        		.andReturn();
    }   

    /**
     * 書籍追加一覧ページ表示_クラスあり.
     * @throws Exception MockMVC失敗時例外
     */
    @SuppressWarnings("unchecked")
    @Test
    public void 書籍追加一覧ページ表示_書籍あり() throws Exception {

        // DB状態
        // 書籍：３冊
        Destination dest = new DataSourceDestination(dataSource);
        Operation ops = Operations.sequenceOf(INSERT_BOOK_DATA1,INSERT_BOOK_DATA2, INSERT_BOOK_DATA3);
        DbSetup dbSetup = new DbSetup(dest, ops);
        dbSetup.launch();
        BookForm form1 = new BookForm();
        form1.setId(1);
        form1.setTitle("タイトル１");
        form1.setWriter("著者１");
        form1.setPublisher("出版社１");
        form1.setPrice(100);
        BookForm form2 = new BookForm();
        form2.setId(2);
        form2.setTitle("タイトル２");
        form2.setWriter("著者２");
        form2.setPublisher("出版社２");
        form2.setPrice(200);
        BookForm form3 = new BookForm();
        form3.setId(3);
        form3.setTitle("タイトル３");
        form3.setWriter("著者３");
        form3.setPublisher("出版社３");
        form3.setPrice(300);

    	MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("books/list"))
                .andReturn();

        try {
            List<BookForm> list = (List<BookForm>) result.getModelAndView().getModel().get("books");    
            assertThat(list).contains(form1, form2, form2);
        } catch (NullPointerException e) {
            throw new Exception(e);
        }
    }


}
