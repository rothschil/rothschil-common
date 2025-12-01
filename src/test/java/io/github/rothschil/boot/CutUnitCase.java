package io.github.rothschil.boot;


import io.github.rothschil.AbstractBaseSimpleCase;
import io.github.rothschil.domain.entity.TblCdmaHlr;
import io.github.rothschil.domain.repository.TblCdmaHlrRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.List;


/**
 * @description 切片测试 - 只启动部分容器
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CutUnitCase extends AbstractBaseSimpleCase {


    @Autowired
    protected TblCdmaHlrRepository tblCdmaHlrRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindByUsername() {
        // given
        TblCdmaHlr user = new TblCdmaHlr();
        user.setPhoneprefix("1895606");

        entityManager.persistAndFlush(user);

        // when
        List<TblCdmaHlr> found = tblCdmaHlrRepository.findByField(TblCdmaHlr.class,"phoneprefix","1895606");
        log.info(found.toString());
        // then
        assertThat(found.get(0).getLocation2()).isEqualTo("0551");
    }

}
