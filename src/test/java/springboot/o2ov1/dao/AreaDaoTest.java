package springboot.o2ov1.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.o2ov1.entity.Area;

import java.util.List;

/**
 * @author WuChangJian
 * @date 2020/4/15 16:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {

    @Autowired
    private AreaDao areaDao;

    @Test
    public void testSelectAll() {
        List<Area> areas = areaDao.selectAll();
    }
}
