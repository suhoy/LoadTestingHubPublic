package app.front.controller;

import app.controller.front.IndexController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SmokeTest {

    //@Autowired
    private IndexController homepageController;

    //@Test
    public void contextLoads() throws Exception {
        assertThat(homepageController).isNotNull();
    }

}
