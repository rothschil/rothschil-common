package xyz.wongs.drunkard.base;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import xyz.wongs.ImgApplication;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ImgApplication.class)
@SpringBootTest
public abstract class BaseTest {

}
