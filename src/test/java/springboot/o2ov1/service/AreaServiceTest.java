package springboot.o2ov1.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.o2ov1.entity.Area;

import java.util.List;

/**
 * @author WuChangJian
 * @date 2020/3/13 10:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest{

    @Autowired
    private AreaService areaService;
    @Test
    public void testGetAreaList() {
        List<Area> areaList = areaService.getAreaList();
        Assert.assertNull(areaList);

    }
}
